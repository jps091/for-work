"use strict";

import AWS from "aws-sdk";
import Sharp from "sharp";

const S3 = new AWS.S3({ signatureVersion: "v4" });

export const handler = async (event, context, callback) => {
    const response = event.Records[0].cf.response;
    const request = event.Records[0].cf.request;
    const headers = request.headers;

    console.log(`Response status code: ${response.status}`);
    console.log("Request Headers:", headers);

    // 요청이 서버에서 왔는지 확인
    const isServerRequest =
        headers["user-agent"] && headers["user-agent"][0].value.includes("axios");
    console.log("Is Server Request:", isServerRequest);

    if (isServerRequest) {
        console.log("🚨 서버에서 요청됨 → CloudFront 503 방지 처리");
        request.headers["origin"] = [
            { key: "Origin", value: "https://your-backend.com" },
        ];
    }

    if (response.status === "404" || response.status === "403") {
        try {
            const { BUCKET, originalKey, resizedKey, resizeOptions } =
                parseRequest(request);

            console.log("S3 Bucket:", BUCKET);
            console.log("Original Key:", originalKey);
            console.log("Resized Key:", resizedKey);

            const originalImage = await fetchOriginalImage(BUCKET, originalKey);
            if (!originalImage) {
                console.error("S3에서 원본 이미지를 찾을 수 없음:", originalKey);
                return callback(null, response);
            }

            const resizedImage = await resizeImage(originalImage, resizeOptions);
            await uploadResizedImage(
                BUCKET,
                resizedKey,
                resizedImage,
                resizeOptions.format
            );

            return buildSuccessResponse(
                callback,
                response,
                resizedImage,
                resizeOptions.format
            );
        } catch (err) {
            console.error("이미지 처리 중 오류 발생:", err);
            return callback(null, response);
        }
    }

    return callback(null, response);
};

const parseRequest = (request) => {
    const path = request.uri;
    console.log("Original URI:", path);

    const originDomainName = request.origin?.s3?.domainName || "";
    const BUCKET = originDomainName.split(".s3.")[0];
    const originPath = request.origin?.s3?.path || "";

    const match = path.match(
        /(.*\/)([^/]+)\/(s\d+x\d*?_q\d+_t.*?_f.*?)\/([^/]+)$/
    );
    if (!match) {
        throw new Error(`잘못된 URI: ${path}`);
    }

    const [_, dynamicPrefix, folderName, sizeFolder, imageName] = match;

    const sizeMatch = sizeFolder.match(/s(\d+x\d*?)_q(\d+)_t(.*?)_f(.*)/);
    if (!sizeMatch) {
        throw new Error(`잘못된 리사이즈 폴더 형식: ${sizeFolder}`);
    }

    const dimensions = sizeMatch[1].split("x");
    const width = dimensions[0] ? parseInt(dimensions[0], 10) : null;
    const height = dimensions[1] ? parseInt(dimensions[1], 10) : null;

    const quality = parseInt(sizeMatch[2], 10);
    const fit = sizeMatch[3];
    const format = sizeMatch[4] || "jpeg";

    const prefix = `${originPath.replace(/\/$/, "")}/${dynamicPrefix.replace(
        /^\//,
        ""
    )}`;
    const originalKey = `${prefix}${imageName}`.replace(/^\//, "");
    const resizedKey = `resized/${path.replace(/^\//, "")}`;

    console.log("Parsed originalKey:", originalKey);
    console.log("Parsed resizedKey:", resizedKey);

    return {
        BUCKET,
        originalKey,
        resizedKey,
        resizeOptions: {
            width: width || 720,
            height: height || null,
            quality,
            fit,
            format,
        },
    };
};

const fetchOriginalImage = async (bucket, key) => {
    try {
        const data = await S3.getObject({ Bucket: bucket, Key: key }).promise();
        return data.Body;
    } catch (err) {
        console.error(`S3 원본에서 이미지 가져오기 실패: ${key}`, err);
        return null;
    }
};

const resizeImage = async (imageBuffer, options) => {
    try {
        const sharpInstance = Sharp(imageBuffer).resize({
            width: options.width,
            height: options.height,
            fit: options.fit,
        });
        return await sharpInstance
            .toFormat(options.format, { quality: options.quality })
            .toBuffer();
    } catch (err) {
        throw new Error(`리사이즈 실패: ${err.message}`);
    }
};

const uploadResizedImage = async (bucket, key, imageBuffer, format) => {
    try {
        await S3.putObject({
            Bucket: bucket,
            Key: key,
            Body: imageBuffer,
            ContentType: `image/${format}`,
            CacheControl: "max-age=3600",
            StorageClass: "STANDARD",
        }).promise();
        console.log(`S3에 리사이즈된 이미지 업로드 완료: ${key}`);
    } catch (err) {
        throw new Error(`S3에 리사이즈 이미지 업로드 실패: ${err.message}`);
    }
};

const buildSuccessResponse = (callback, response, imageBuffer, format) => {
    response.status = "200";
    response.body = imageBuffer.toString("base64");
    response.bodyEncoding = "base64";
    response.headers["content-type"] = [
        { key: "Content-type", value: `image/${format}` },
    ];
    return callback(null, response);
};
