package project.forwork.api.common.config.swageer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@Component
public class MultipartJackson2HttpMessageConverter extends AbstractJackson2HttpMessageConverter {
    /** TODO 주석 제거
     * "Content-Type: multipart/form-data" 헤더를 지원하는 HTTP 요청 변환기
     * Content type 'application/octet-stream' not supported
     * 스프링은 기본적으로 application/octet-stream 미디어 타입을 지원X
     * but 스웨거와 같은 도구에서 멀티 파일 전송을 다룰 때 application/octet-stream 타입을 요구하는 문제 발생
     *
     * 스프링이 JSON 형식의 데이터를 직렬화할 때 MediaType.APPLICATION_OCTET_STREAM으로 처리하도록 설정하며,
     * 이는 스웨거가 해당 형식을 인식하고 올바르게 멀티파일 요청을 처리할 수 있게 합니다.
     *
     * 즉, 스프링이 기본적으로 처리하지 않는 미디어 타입인 application/octet-stream을 지원하기 위해
     * 커스터마이징한 메시지 컨버터를 등록함으로써 스웨거와의 호환성을 높인 것입니다.
     */
    public MultipartJackson2HttpMessageConverter(ObjectMapper objectMapper) {
        super(objectMapper, MediaType.APPLICATION_OCTET_STREAM);
    }

    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return false;
    }

    @Override
    public boolean canWrite(Type type, Class<?> clazz, MediaType mediaType) {
        return false;
    }

    @Override
    protected boolean canWrite(MediaType mediaType) {
        return false;
    }
}
