package kr.manyofactory.manyoshop.helpers;


import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletResponse;

@Component
public class RestHelper {
    @Autowired
    private HttpServletResponse response;

    /**
     * JSON 형식의 응답을 출력하기 위한 메서드
     *
     * @param status - HTTP 상태 코드
     * @param message - 결과 메시지
     * @param data - JSON으로 변환할 데이터 객체
     * @param error - 에러 메시지
     * @return Map
     */
    public Map<String, Object> sendJson(int status, String message, Object data, Exception error) {
        /** 1) JSON 형식 수정을 위한 HTTP 헤더 설정 */
        // JSON 형식으로 인식됨
        response.setContentType("application/json; charset=UTF-8");

        // HTTP 상태 코드 설정 (200, 404, 500 등)
        response.setStatus(status);

        // CORS 보안 문제 방지 --> CrossDomain에 의한 접근 허용
        // 인증이 필요한 경우에는 자격 증명 설정 필요함 --> 인증키 등을 보안검사가 필요함
        // 여기서는 단순히 테스트용으로만 설정함
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
        response.setHeader("Access-Control-Allow-Origin", "*");

        /** 2) JSON으로 변환할 Map 객체의 구성 */
        Map<String, Object> result = new LinkedHashMap<String, Object>();

        // HTTP 상태 코드와 결과 메시지 추가
        result.put("status", status);
        result.put("message", message);

        // data가 전달되었다면 result에 병합한다.
        if (data != null) {
            result.put("item", data);
        }

        // error가 전달되었다면 result에 포함한다.
        if (error != null) {
            result.put("error", error.getClass().getName());
            result.put("message", error.getMessage());

            // printStackTrace()의 출력 내용을 문자열로 받음
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(bos);
            error.printStackTrace(ps);

            String trace = bos.toString();
            result.put("trace", trace);
        }

        // 데이터 생성 시각을 포함한다 (브라우저 캐시 방지)
        result.put("timestamp", LocalDateTime.now().toString());

        return result;
    }

    /**
     * JSON 형식의 응답을 출력하기 위한 메서드
     *
     * @param data - JSON으로 변환할 데이터 객체
     * @return Map
     */
    public Map<String, Object> sendJson(Object data) {
        return this.sendJson(200, "OK", data, null);
    }

    /**
     * JSON 형식의 응답을 출력하기 위한 메서드 복잡한 결과값이 요청에 대해 성공 여부만을 알려주기 위해 사용한다.
     *
     * @return Map
     */
    public Map<String, Object> sendJson() {
        return this.sendJson(200, "OK", null, null);
    }

    /**
     * 에러 상황을 JSON 형식으로 출력하기 위한 메서드<br />
     * <br />
     * <strong>주의</strong>: 이 메서드는 HTTP 상태 코드와 메시지를 파라미터로 받아 JSON 형식으로 에러 메시지를 리턴한다.
     *
     * @param status - HTTP 상태 코드
     * @param message - 결과 메시지
     * @return Map&lt;String, Object&gt;
     */
    public Map<String, Object> sendError(int status, String message) {
        Exception error = new Exception(message);
        return this.sendJson(status, null, null, error);
    }

    /**
     * JSON 형식으로 에러 메시지를 리턴한다. HTTP 상태코드로 400을 설정하고, 결과 메시지는 파라미터로 전달되는 message 값을 설정한다. 파라미터의 유효성
     * 검사 실패 등의 경우에 사용한다.
     *
     * @param message - 에러 메시지
     * @return Map
     */
    public Map<String, Object> badRequest(String message) {
        return this.sendError(400, message);
    }

    /**
     * JSON 형식으로 에러 메시지를 리턴한다. HTTP 상태코드로 400을 설정하고, 결과 메시지는 파라미터로 전달되는 error 객체를 사용한다. 파라미터의 유효성 검사
     * 실패 등의 경우에 사용한다.
     *
     * @param error - 에러 객체
     * @return Map
     */
    public Map<String, Object> badRequest(Exception error) {
        return this.sendJson(400, null, null, error);
    }

    /**
     * JSON 형식으로 에러 메시지를 리턴한다. HTTP 상태코드로 500을 설정하고, 결과 메시지는 파라미터로 전달되는 값을 설정한다. 500 에러는 서버의 모든 종류의
     * 예외, 주로 DB연동 등의 처리에서 발생한 예외를 지칭한다.
     *
     * @param message - 에러 메시지
     * @return Map
     */
    public Map<String, Object> serverError(String message) {
        return this.sendError(500, message);
    }

    /**
     * JSON 형식으로 에러 메시지를 리턴한다. HTTP 상태코드로 500을 설정하고, 결과 메시지는 파라미터로 전달되는 error 객체를 사용한다. 500 에러는 서버의
     * 모든 종류의 예외, 주로 DB연동 등의 처리에서 발생한 예외를 처리한다.
     *
     * @param error - 에러 객체
     * @return Map
     */
    public Map<String, Object> serverError(Exception error) {
        return this.sendJson(500, null, null, error);
    }



}
