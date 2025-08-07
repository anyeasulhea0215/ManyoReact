package kr.manyofactory.manyoshop;

import kr.manyofactory.manyoshop.models.Members;
import kr.manyofactory.manyoshop.services.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@Slf4j
@SpringBootTest
class MemberServiceTests {

    @Autowired
    private MemberService memberService;

    @Test
    void testIsUniqueUserId() {
        // 중복 아이디 검사
        Members input = new Members();
        input.setUserId("testuser");

        try {
            memberService.isUniqueUserId(input);
        } catch (Exception e) {
            log.debug("사용할 수 없는 아이디 입니다.");
            return;
        }

        log.debug("사용 가능한 아이디 입니다.");
    }

    @Test
    void testIsUniqueEmail() {
        Members input = new Members();
        input.setEmail("test@example.com");

        try {
            memberService.isUniqueEmail(input);
        } catch (Exception e) {
            log.debug("사용할 수 없는 이메일 입니다.");
            return;
        }

        log.debug("사용 가능한 이메일 입니다.");
    }

    @Test
    void testJoin() {
        // 테스트용 회원 정보 생성
        Members input = new Members();
        input.setUserId("testuser");
        input.setUserPw("testpw123");
        input.setUserName("테스트유저");
        input.setEmail("testuser@example.com");
        input.setPhone("010-1111-2222");
        input.setBirthday("1999-02-23");
        input.setGender("M");
        input.setPostcode("12345");
        input.setAddr1("서울시 강남구");
        input.setAddr2("테스트동 101호");
        input.setIsEmailAgree("Y");
        input.setIsSmsAgree("N");

        Members output = null;

        try {
            output = memberService.join(input);
        } catch (Exception e) {
            log.error("회원 가입에 실패했습니다.", e);
            return;
        }

        log.debug("가입된 회원 정보: {}", output);
    }


    @Test
    void testJoinSuccess() {
        Members input = new Members();
        // To make the test repeatable, use a unique value for userId and email
        String uniqueId = "user_" + System.currentTimeMillis();
        input.setUserId(uniqueId);
        input.setUserPw("testpassword");
        input.setUserName("반복테스트");
        input.setEmail(uniqueId + "@test.com");
        input.setPhone("01022223333");
        input.setBirthday("2000-01-01");
        input.setGender("M");
        input.setPostcode("12345");
        input.setAddr1("서울시 강남구");
        input.setAddr2("역삼동");
        input.setPhoto(null);

        Members output = null;

        try {
            output = memberService.join(input);
            log.debug("회원가입 성공 >> {}", output.toString());
        } catch (Exception e) {
            log.error("회원가입 실패", e);
        }
    }


    @Test
    void testLogin() {
        Members input = new Members();
        input.setUserId("hellotest");
        input.setUserPw("1234");

        Members output = null;
        try {
            output = memberService.login(input);
        } catch (Exception e) {
            log.error("로그인에 실패", e);
            return;
        }

        log.debug("로그인 성공, 회우너 정보: {}", output);
    }



}
