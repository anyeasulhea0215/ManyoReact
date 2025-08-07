package kr.manyofactory.manyoshop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
// 인터셉터를 등록하는데 사용
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import kr.manyofactory.manyoshop.interceptors.MyInterceptor;
// spring MVC의 설정을 커스터마이징하기 위해 사용하는 인터페이스이다

@Configuration // spring의 설정 클래스이다
@SuppressWarnings("null") // null경고를 무시한다
public class MyWebConfig implements WebMvcConfigurer {
    @Autowired
    public MyInterceptor myInterceptor;

    @Value("${upload.dir}")
    private String uploadDir;

    @Value("${upload.url}")
    private String uploadUrl;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 직접 정의한 MyInterceptor를 Spring에 등록
        InterceptorRegistration ir = registry.addInterceptor(myInterceptor);

        // 해당 경로는 인터셉터가 가로채지 않는다.
        ir.excludePathPatterns("/hello", "/world", "/error", "/robots.txt", "/favicon.ico",
                "/assets/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(String.format("%s/**", uploadUrl))
                .addResourceLocations(String.format("file://%s/", uploadDir));
    }
}
