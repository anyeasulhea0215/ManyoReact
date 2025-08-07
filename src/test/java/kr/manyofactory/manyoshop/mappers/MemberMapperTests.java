package kr.manyofactory.manyoshop.mappers;

import kr.manyofactory.manyoshop.models.Members;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class MemberMapperTests {


    @Autowired
    private MemberMapper memberMapper;

    @Test
    public void testInsert() {
        Members input = new Members();

        input.setUserId("testuser55");


        input.setUserPw("testpw123");
        input.setUserName("테스트유저");
        input.setEmail("testuser@example.com");
        input.setIsEmailAgree("Y");
        input.setPhone("010-1234-5678");
        input.setIsSmsAgree("N");
        input.setPostcode("12345");
        input.setAddr1("서울시 강남구");
        input.setAddr2("테스트동 101호");
        input.setGender("M");
        input.setBirthday("1000-23-23");
        input.setIsOut("N");

        int result = memberMapper.insert(input);
        log.debug("insert 결과: {}", result);
        log.debug("생성된 ID: {}", input.getId());
    }

    @Test
    public void selectCount() {
        Members input = new Members();
        input.setUserId("testuser");
        int count = memberMapper.selectCount(input);
        log.debug("회원 수: {}", count);
    }

    @Test
    public void selectItem() {
        Members input = new Members();
        input.setId(100); // 실제 DB에 존재하는 ID로 변경 필요

        Members result = memberMapper.selectItem(input);
        log.debug("조회 결과: {}", result);
    }


    @Test
    void login() {
        Members input = new Members();
        input.setUserId("test1");
        input.setUserPw("1234");

        Members output = memberMapper.login(input);
        log.debug("output: {}", output);

    }

    @Test
    void updateLoginDate() {

        Members input = new Members();
        input.setId(2);

        int output = memberMapper.updateLoginDate(input);
        log.debug("output: {}", output);
    }

    @Test
    void editMember() {

        Members input = new Members();
        input.setId(5);
        input.setUserName("테스트유저");
        input.setEmail("test@example.com");
        input.setPhone("010-1111-2222");
        input.setBirthday("1999-02-11");
        input.setGender("M");
        input.setPostcode("12345");
        input.setAddr1("서울시 강남구");
        input.setAddr2("테스트동 101호");
        input.setPhoto(null);
        input.setIsSmsAgree("Y");

        input.setUserPw("1234");
        input.setNewUserPw("newuserpassw");

        // Insert실행
        int insertId = memberMapper.update(input);
        log.debug("insertId:{}", insertId);

    }

}
