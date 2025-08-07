package kr.manyofactory.manyoshop.exceptions;

import org.springframework.http.HttpStatus;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MyException extends Exception {
    private HttpStatus status;

    /**
     * 상위 클래스의 생성자를 재정의하는 생성자
     *
     * HTTP상태코드를 기본값으로 하는 INTERNAL_SERVER_ERROR(500)으로 지정한다
     *
     * @param message - 예외 메세지
     */
    public MyException(String message) {
        super(message);
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    /**
     * 직접 추가한 생성자
     *
     * 상황에 따른 HTTP코드를 지정할 수 있다
     *
     * @param status
     * @param message
     */
    public MyException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }
}
