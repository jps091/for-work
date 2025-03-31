"use strict";

import { parse } from "querystring";

const CONFIG = {
    defaultDimention: { width: 750 },
    defaultQuality: 100,
    defaultFit: "cover",
    webpExtension: "webp",
    allowedFits: ["cover", "contain", "fill", "inside", "outside"],
};

const getFormat = (headers) => {
    const acceptHeader = headers["accept"]?.[0]?.value || "";
    return acceptHeader.includes(CONFIG.webpExtension)
        ? CONFIG.webpExtension
        : "";
};

export const handler = async (event, context, callback) => {
    const request = event.Records[0].cf.request;

    const params = parse(request.querystring);

    const [rawWidth, rawHeight] = (params.s || "").split("x").map(Number);
    const width = rawWidth || CONFIG.defaultDimention.width;
    const height = rawHeight || null;
    const quality = params.q ? parseInt(params.q, 10) : CONFIG.defaultQuality;
    const fit = CONFIG.allowedFits.includes(params.t)
        ? params.t
        : CONFIG.defaultFit;

    const match = request.uri.match(/(.*)\/(.*)\.(.*)/);
    if (!match) {
        console.error("잘못된 URI", request.uri);
        return callback(null, request);
    }

    const [_, prefix, imageName, extension] = match;
    let format = getFormat(request.headers);
    format = format ? format : extension === "jpg" ? "jpeg" : extension;

    const folderName = [
        `s${width}x${height || ""}`,
        `q${quality}`,
        `t${fit}`,
        format ? `f${format}` : "f",
    ].join("_");

    request.uri = `${prefix}/${imageName}/${folderName}/${imageName}.${extension}`;
    console.log("변경된 URI:", request.uri);

    return callback(null, request);
};
