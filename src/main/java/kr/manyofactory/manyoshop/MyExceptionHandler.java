package kr.manyofactory.manyoshop;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import kr.manyofactory.manyoshop.exceptions.MyException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class MyExceptionHandler {

    /**
     * 모든 예외를 처리하는 핸들러
     *
     * @param e -발생한 예외
     * @return ResponseEntity 객체
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> myExceptionHandler(Exception e) {
        log.error(e.getMessage(), e);

        int status = HttpStatus.INTERNAL_SERVER_ERROR.value();

        if (e instanceof MyException) {
            MyException myException = (MyException) e;
            status = myException.getStatus().value();
        }

        Map<String, Object> result = new LinkedHashMap<String, Object>();
        result.put("status", status);
        result.put("message",
                e.getMessage() != null ? e.getMessage() : HttpStatus.INTERNAL_SERVER_ERROR);
        result.put("error", e.getClass().getSimpleName());
        result.put("timestamp", LocalDateTime.now().toString());

        return ResponseEntity.status(status).body(result);

    }

}
