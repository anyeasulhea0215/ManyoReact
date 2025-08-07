package kr.manyofactory.manyoshop.interceptors;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.manyofactory.manyoshop.helpers.RestHelper;
import kr.manyofactory.manyoshop.helpers.SessionCheckHelper;
import kr.manyofactory.manyoshop.helpers.WebHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua_parser.Client;
import ua_parser.Parser;


@Slf4j // 객체를 생성하여 로깅하는 기능을 제공함
@Component // 이 클래스는 Bean으로 등록한다
@SuppressWarnings("null") // 컴파일러의 null경고를 무시한다
@RequiredArgsConstructor
public class MyInterceptor implements HandlerInterceptor {
    /** 페이지의 실행 시작 시각을 저장할 변수 */
    long startTime = 0;

    /** 페이지의 실행 완료 시각을 저장할 변수 */
    long endTime = 0;

    private final WebHelper webHelper;

    private final RestHelper restHelper;


    /**
     * Controller 실행 전에 수행되는 메서드 클라이언트(웹브라우저) 요청을 컨트롤러에 전달하기 전에 호출된다. return 값으로 boolean 값을 반환하는데
     * false인 경우 Controller를 실행시키지 않고 요청을 종료한다. 보통 이곳에서 각종 체크작업과 로그를 기록하는 작업을 진행한다.
     */
    @Override // 컨트롤러가 실행되기전에 호출한다
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) throws Exception {
        // log.debug("MyInterceptor.preHandle 실행됨");

        log.info("---------- new client connect ----------");


        // 로그인 여부에 따른 페이지 접근 제어
        // SessionCheckHelper 어노테이션이 붙은 메서드에 대해서만 세션 체크를 수행한다
        if (handler instanceof HandlerMethod handlerMethod) {
            // 세션 검사용 어노테이션을 가져온다
            SessionCheckHelper annotation =
                    handlerMethod.getMethodAnnotation(SessionCheckHelper.class);

            // 어노테이션이 존재한다면 세션 체크를 수행한다
            if (annotation != null) {

                Class<?> beanType = handlerMethod.getBeanType(); // 컨트롤러 유형을 가져옴

                boolean isRestful = beanType.isAnnotationPresent(RestController.class); // Restful
                                                                                        // 방식의
                                                                                        // 컨트롤러인지 검사
                boolean enable = annotation.enable();

                HttpSession session = request.getSession(); // 로그인 여부를 체크한다.
                boolean isLoggedIn = session != null && session.getAttribute("memberInfo") != null;

                if (enable) { // 로그인 중에만 접근 가능한 페이지 검사
                    if (!isLoggedIn) { // 로그인을 하지 않은 상태일때
                        if (isRestful) {
                            restHelper.badRequest("로그인이 필요함");
                        } else {
                            response.sendRedirect("/login/login"); // restcontroller가 아닌 controller들
                                                                   // 처리
                            return false;
                            // webHelper.badRequest("로그인이 필요함");
                        }

                        return false;
                    }
                } else {
                    if (isLoggedIn) {
                        if (isRestful) {
                            restHelper.badRequest("로그인이 필요함");
                        } else {
                            response.sendRedirect("/login/login");
                            return false;
                            // webHelper.badRequest("로그인이 필요함");
                        }

                        return false;
                    }
                }
            }

        }


        /** 1) 페이지의 실행 시작 시각을 구한다. */
        startTime = System.currentTimeMillis();

        /** 2) 접속한 클라이언트 정보 알아내기 */
        // user-agent헤더를 가져와 클라이언트 정보를 파싱한다
        // ua-parser라이브러리를 사용하여 클라이언트의 운영체제, 브라우저정보를 분석한다
        String ua = request.getHeader("user-agent");
        Parser uaParser = new Parser();
        Client c = uaParser.parse(ua);

        // 클라언트 정보 로깅
        String fmt = "[Client] %s, %s, %s, %s, %s, %s";

        String ipAddr = webHelper.getClientIp();

        // HttpServletRequest객체를 사용하여 클라이언트 IP주소를 가져온다

        String osVersion = c.os.major + (c.os.minor != null ? "." + c.os.minor : "");
        String uaVersion =
                c.userAgent.major + (c.userAgent.minor != null ? "." + c.userAgent.minor : "");
        String clientInfo = String.format(fmt, ipAddr, c.device.family, c.os.family, osVersion,
                c.userAgent.family, uaVersion);

        log.info(clientInfo);
        // 클라이언트의 IP주소, 운영체제, 브라우저 정보를 포맷팅하여 로그로 출력한다

        /** 3) 클라이언트의 요청 정보(URL) 확인하기 */
        // 전체 URL 획득
        String url = request.getRequestURL().toString();

        // GET방식인지, POST방식인지 조회한다.
        String methodName = request.getMethod();

        // URL에 "?"이후에 전달되는 GET파라미터 문자열을 모두 가져온다.
        String queryString = request.getQueryString();

        // 가져온 값이 있다면 URL과 결합하여 완전한 URL을 구성한다.
        if (queryString != null) {
            url = url + "?" + queryString;
        }

        // 획득한 정보를 로그로 표시한다.
        log.info(String.format("[%s] %s", methodName, url));

        /** 3) 클라이언트가 전달한 모든 파라미터 확인하기 */
        Map<String, String[]> params = request.getParameterMap();

        for (String key : params.keySet()) {
            String[] value = params.get(key);
            log.info(String.format("[Param] %s <- %s", key, String.join(",", value)));
        }

        /** 4) 클라이언트가 머물렀던 이전 페이지 확인하기 */
        String referer = request.getHeader("referer");

        // 이전에 머물렀던 페이지가 존재하는가?
        // --> 직전 종료시각과 이번 요청의 시작시간과 차이로 이전 페이지에 머문 시간을 의미한다.
        if (referer != null && endTime > 0) {
            log.info(String.format("REFERER : time=%d, url=%s", startTime - endTime, referer));
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    /**
     * view 단으로 forward 되기 전에 수행. 컨트롤러 로직이 실행된 후 호출된다. 컨트롤러 단에서 예외 발생 시 실행 메서드는 수행되지 않는다. request를
     * 넘겨줄 데이터 가공 시 많이 사용된다.
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            @Nullable ModelAndView modelAndView) throws Exception {
        // log.debug("MyInterceptor.postHandle 실행됨");

        /** 1) 라우트의 실행 종료 시각을 가져온다. */
        endTime = System.currentTimeMillis();

        /** 2) 라우트별로 수행하는데 걸린 시간을 구한다. */
        log.info(String.format("running time: %d(ms)", endTime - startTime));

        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    /**
     * 컨트롤러 종료 후 view가 정상적으로 랜더링 된 후 제일 마지막에 실행이 되는 메서드. (잘 사용 안함)
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
            Object handler, @Nullable Exception ex) throws Exception {
        // log.debug("AppInterceptor.afterCompletion 실행됨");
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
