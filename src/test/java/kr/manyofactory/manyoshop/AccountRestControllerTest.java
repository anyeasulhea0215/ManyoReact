package kr.manyofactory.manyoshop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.web.context.WebApplicationContext;
import kr.manyofactory.manyoshop.helpers.RestApiTestHelper;
import lombok.extern.slf4j.Slf4j;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class AccountRestControllerTest {

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc;

    private RestApiTestHelper restApiTestHelper;

    @BeforeEach
    public void setUp() {
        this.restApiTestHelper = new RestApiTestHelper(mockMvc);
    }



    /** 아이디 중복 체크 api테스트 */
    @Test
    void idUniqueCheck() throws Exception {

        MockHttpServletRequestBuilder requestBuilder = get("/api/account/id_unique_check");
        requestBuilder.accept(MediaType.APPLICATION_JSON);
        requestBuilder.header("User-Agent",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36");
        requestBuilder.param("user_id", "testuser123");

        ResultActions action = mockMvc.perform(requestBuilder);

        // 3.결과출력
        // action.andDo(print());

        // 4. 상태검증
        action.andExpect(status().isOk());

        // 5.결과추출
        MvcResult result = action.andReturn();
        MockHttpServletResponse response = result.getResponse();
        int status = response.getStatus();
        String responseJson = response.getContentAsString();

        log.debug("응답 상태 코드:{ }", status);
        log.debug("응답 json전문: {}", responseJson);


    }


    /** email 중복 체크 api테스트 */
    @Test
    void emailUniqueCheck() throws Exception {

        MockHttpServletRequestBuilder requestBuilder = get("/api/account/email_unique_check");
        requestBuilder.accept(MediaType.APPLICATION_JSON);
        requestBuilder.header("User-Agent",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36");
        requestBuilder.param("email", "testuser@gmail.com");

        ResultActions action = mockMvc.perform(requestBuilder);

        // 3.결과출력
        action.andDo(print());

        // 4. 상태검증
        action.andExpect(status().isOk());

        // 5.결과추출
        MvcResult result = action.andReturn();
        MockHttpServletResponse response = result.getResponse();
        int status = response.getStatus();
        String responseJson = response.getContentAsString();

        log.debug("응답 상태 코드:{ }", status);
        log.debug("응답 json전문: {}", responseJson);

    }

    /** 회원가입 api 테스트 */
    @Test
    void join() throws Exception {
        // 업로드할 피알 구성
        String inputFieldName = "photo";
        String fileType = "image/jpeg";
        String filePath = "C:\\Users\\anyeasulhea\\study-springboot\\upload\\simple.png";
        File file = new File(filePath);
        FileInputStream fileInputStream = new FileInputStream(file);

        MockMultipartFile image1 =
                new MockMultipartFile(inputFieldName, file.getName(), fileType, fileInputStream);

        // when
        MockMultipartHttpServletRequestBuilder requestBuilder = multipart("/api/account/join");
        requestBuilder.accept(MediaType.APPLICATION_JSON);
        requestBuilder.header("User-Agent",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36");
        requestBuilder.param("user_id", "hellotest");
        requestBuilder.param("user_pw", "password123");
        requestBuilder.param("user_pw_re", "password123");

        requestBuilder.param("user_name", "테스트");
        requestBuilder.param("email", "hello@example.com");

        requestBuilder.param("phone", "01012345678");
        requestBuilder.param("birthday", "1990-01-01");

        requestBuilder.param("gender", "M");
        requestBuilder.param("postcode", "12345");
        requestBuilder.param("addr1", "서울시 강남구");
        requestBuilder.param("addr2", "역삼동");

        // 파일 업로드 파라미터 추가
        requestBuilder.file(image1);

        // 2)요청 실행
        ResultActions action = mockMvc.perform(requestBuilder);

        // 상태검증
        action.andExpect(status().isOk());

        // 결과 추출
        MvcResult result = action.andReturn();
        MockHttpServletResponse response = result.getResponse();
        int status = response.getStatus();
        String responseJson = response.getContentAsString();

        log.debug("응답 상태 코드: {}", status);
        log.debug("응답 json전문: {}", responseJson);
    }


    @Test
    void login() throws Exception {

        MockHttpServletRequestBuilder requestBuilder = post("/api/account/login");

        requestBuilder.accept(MediaType.APPLICATION_JSON);
        requestBuilder.header("User-Agent",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36");
        requestBuilder.param("user_id", "test1sdsdsdaasdad");
        requestBuilder.param("user_pw", "1234");

        ResultActions action = mockMvc.perform(requestBuilder);

        // 3.결과출력
        action.andDo(print());

        // 4. 상태검증
        action.andExpect(status().isOk());

        // 5.결과추출
        MvcResult result = action.andReturn();
        MockHttpServletResponse response = result.getResponse();
        int status = response.getStatus();
        String responseJson = response.getContentAsString();

        log.debug("응답 상태 코드:{ }", status);
        log.debug("응답 json전문: {}", responseJson);

    }

    /* 비밀번호 재발급 테스트 */
    @Test
    void resetPw() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", "test1");
        params.put("email", "anyeasulhea@naver.com");

        restApiTestHelper.test("PUT", "/api/account/reset_pw", params, null);
    }


}
