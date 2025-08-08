package kr.manyofactory.manyoshop.controller.apis;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


import jakarta.servlet.http.Cookie;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import kr.manyofactory.manyoshop.exceptions.StringFormatException;
import kr.manyofactory.manyoshop.helpers.FileHelper;
import kr.manyofactory.manyoshop.helpers.MailHelper;
import kr.manyofactory.manyoshop.helpers.RegexHelper;
import kr.manyofactory.manyoshop.helpers.RestHelper;
import kr.manyofactory.manyoshop.helpers.SessionCheckHelper;
import kr.manyofactory.manyoshop.helpers.UtilHelper;
import kr.manyofactory.manyoshop.models.MemberAgreement;
import kr.manyofactory.manyoshop.models.Members;
import kr.manyofactory.manyoshop.models.UploadItem;
import kr.manyofactory.manyoshop.services.MemberAgreementService;
import kr.manyofactory.manyoshop.services.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AccountRestController {

    private final MemberService memberService;
    private final MemberAgreementService memberAgreementService;

    private final RestHelper restHelper;

    private final RegexHelper regexHelper;

    // FileHelper객체
    private final FileHelper fileHelper;

    private final UtilHelper utilHelper;

    private final MailHelper mailHelper;


    /**
     * 회원 아이디 중복검사
     *
     * @param userId -검사할 회원 아이디
     * @param memberInfo -현재 로그인중인 회원정보(세션에서 가져옴)
     * @return 중복 여부 결과에 대한 json응답
     * @throws Exception
     */
    @GetMapping("/api/account/id_unique_check")
    @SessionCheckHelper(enable = false) // 회원 아이디 중복검사는 로그인하지 않아도 접근 가능
    public Map<String, Object> idUniqueCheck(@RequestParam("user_id") String userId,
            @SessionAttribute(value = "memberInfo", required = false) Members memberInfo)
            throws Exception {

        // 입력값 유효성 검사
        regexHelper.isValue(userId, "아이디를 입력하세요");
        regexHelper.isEngNum(userId, "아이디는 영문자와 숫자만 입력가능");

        // 중복 검사에 사용할 멤버 객체 생성
        Members input = new Members();
        input.setUserId(userId);

        // 로그인 중이라면 현재 회원의 일련번호를 함께 전달한다
        if (memberInfo != null) {
            input.setId(memberInfo.getId());
        }

        // 중복 검사 수행
        memberService.isUniqueUserId(input);

        // 결과를 json형태로 반환accountController
        // RestHelper의 sendJson()메서드는 기본적으로 성공 응답을 반환한다
        return restHelper.sendJson();
    }


    @GetMapping("/api/account/email_unique_check") // 회원 이메일 중복검사는 로그인하지 않아도 접근 가능
    public Map<String, Object> EmailUniqueCheck(@RequestParam("email") String email,
            @SessionAttribute(value = "memberInfo", required = false) Members memberInfo)
            throws Exception {

        // 입력값 유효성 검사
        regexHelper.isValue(email, "email를 입력하세요");
        regexHelper.isEmail(email, "email 형식이 맞지 않습니다");


        // 중복 검사에 사용할 멤버 객체 생성
        Members input = new Members();
        input.setEmail(email);

        // 로그인 중이라면 현재 회원의 일련번호를 함께 전달한다
        if (memberInfo != null) {
            input.setId(memberInfo.getId());
        }

        // 중복 검사 수행
        memberService.isUniqueEmail(input);


        // 결과를 json형태로 반환accountController
        // RestHelper의 sendJson()메서드는 기본적으로 성공 응답을 반환한다
        return restHelper.sendJson();

    }


    /**
     *
     * @param userId -회원 아이디
     * @param userPw -회원비밀번호
     * @param userPwRe -회ㅐ원비밀번호 확인
     * @param userName -회원 이름
     * @param email -회원 이메일
     * @param phone -회원 전화번호
     * @param birthday -회원 생년월일
     * @param gender -회원 성별
     * @param postcode -회원 우편번호
     * @param addr1 -회원주소 1
     * @param addr2 -회원 주소 2
     * @param photo -회원 프로필 사진
     * @return 회원 가입 결과에 대한 json응답
     * @throws Exception 입력값 유효성 검사 실패 또는 회원 가입 처리중 예외 발생 시
     */
    @PostMapping("/api/account/join")
    @SessionCheckHelper(enable = false) // 회원 가입은 로그인하지 않아도 접근 가능
    public Map<String, Object> join(HttpServletRequest request,
            @RequestParam(value = "agreements", required = false) List<String> agreements,
            @RequestParam("user_id") String userId, @RequestParam("user_pw") String userPw,
            @RequestParam("user_pw_re") String userPwRe, @RequestParam("user_name") String userName,
            @RequestParam("email") String email, @RequestParam("phone") String phone,
            @RequestParam("birthday") String birthday, @RequestParam("gender") String gender,
            @RequestParam("postcode") String postcode, @RequestParam("addr1") String addr1,
            @RequestParam("addr2") String addr2,
            @RequestParam(value = "is_email_agree", required = false,
                    defaultValue = "N") String isEmailAgree,
            @RequestParam(value = "is_sms_agree", required = false,
                    defaultValue = "N") String isSmsAgree,
            @RequestParam(value = "photo", required = false) MultipartFile photo) throws Exception {

        // 1)입력값 유효성검사
        regexHelper.isValue(userId, "아이디를 입력하세요");
        regexHelper.isEngNum(userId, "아이디는 영문자와 숫자만 입력할 수 있습니다");
        regexHelper.isValue(userPw, "비밀번호를 입력하세요");

        if (!userPw.equals(userPwRe)) {
            throw new StringFormatException("비밀번호가 잘못됨");
        }
        regexHelper.isValue(userName, "이름을 입력");
        regexHelper.isKor(userName, "이름은 한글만 가능");
        regexHelper.isValue(email, "이메일을 입력하세요");
        regexHelper.isEmail(email, "이메일 형식이 잘못됨");
        regexHelper.isValue(phone, "전화번호를 입력하세요");
        regexHelper.isPhone(phone, "전화번호 형식이 잘못됨");
        regexHelper.isValue(birthday, "생년월일을 입력하세요");
        regexHelper.isValue(gender, "성별을 입력하세요");

        if (!gender.equals("M") && !gender.equals("F")) {
            throw new StringFormatException("성별은 M또는 F만 입력할수 잇음");
        }

        regexHelper.isValue(postcode, "우편번호를 입력하세요");
        regexHelper.isValue(addr1, "주소를 입력하세요");
        regexHelper.isValue(addr2, "상세주소를 입력하세요");



        if (agreements == null || agreements.isEmpty()) {
            throw new StringFormatException("필수 약관에 동의해야 회원가입이 가능합니다.");
        }

        /* 2)업로드 받기 */
        UploadItem uploadItem = fileHelper.saveMultipartFile(photo);

        /* 3)정보를 service에 전달하기 위한 객체 구성 */
        Members member = new Members();
        member.setUserId(userId);
        member.setUserPw(userPw);
        member.setUserName(userName);
        member.setEmail(email);
        member.setPhone(phone);
        member.setBirthday(birthday);
        member.setGender(gender);
        member.setPostcode(postcode);
        member.setAddr1(addr1);
        member.setAddr2(addr2);

        member.setIsSmsAgree(isSmsAgree);
        member.setIsEmailAgree(isEmailAgree);

        member.setIsOut("N"); // 사용자 입력 없이 직접 입력

        // 업로드 된 이미지의 경로만 db에 저장하면 됨
        if (uploadItem != null) {
            member.setPhoto(uploadItem.getFilePath());
        }

        // 4)DB에 저장
        memberService.join(member);

        /**
         * 5) 약관 동의 내용 Insert -> join후에 임시저장된 정보를 삽입
         */
        for (String termType : List.of("TERM01", "TERM02")) {
            String isAgreed = (agreements.contains(termType)) ? "Y" : "N";
            memberAgreementService.saveAgreemenet(member.getId(), termType, isAgreed);
        }

        // 6)결과 반환
        return restHelper.sendJson();

    }



    /* 로그인 */
    @PostMapping("/api/account/login")
    @SessionCheckHelper(enable = false) // 로그인하지 되지 않은 상태에서만 접근 가능
    public Map<String, Object> login(HttpServletRequest request, HttpServletResponse response,
            @RequestParam("user_id") String userId, @RequestParam("user_pw") String userPw,
            @RequestParam(value = "rememberId", required = false) String rememberId) // 아이디 저장기능 사용
                                                                                     // 변수
            throws Exception {

        // 입력값 유효성 검사
        regexHelper.isValue(userId, "아이디를 입력하세요");
        regexHelper.isValue(userPw, "비밀번호를 입력하세요");

        // 로그인 정보생성
        Members input = new Members();
        input.setUserId(userId);
        input.setUserPw(userPw);
        Members output = memberService.login(input);
        request.getSession().setAttribute("memberInfo", output);

        // 쿠키처리
        if (rememberId != null) {

            Cookie cookie = new Cookie("rememberId", userId);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24 * 30); // 30일동안 유지
            response.addCookie(cookie);

        } else {

            Cookie cookie = new Cookie("rememberId", userId);
            cookie.setPath("/");
            cookie.setMaxAge(0); // 30일동안 유지
            response.addCookie(cookie);
        }


        // 세션에서 memberInfo 꺼내서 photo 출력
        System.out.println("memberInfo.photo = "
                + ((Members) request.getSession().getAttribute("memberInfo")).getPhoto());

        // RestHelper의 sendJson()메서드는 기본적으로 성공 응답을 반환한다
        return restHelper.sendJson();
    }


    /* 로그아웃 */
    @GetMapping("/api/account/logout")
    @SessionCheckHelper(enable = true) // 로그인 상태에서만 접근 가능
    public Map<String, Object> logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate(); // logout
        return restHelper.sendJson();
    }


    /**
     * 회원 아이디 찾기
     *
     * @param userName -회원이름
     * @param email -회원 이메일
     * @return -찾은 회원 아이디 대한 json응답
     * @throws Exception 입력값 유효성 검사 실패 또는 아이디 찾기 처리중 예외 발생 시
     */
    @PostMapping("/api/account/find_id")
    @SessionCheckHelper(enable = false) // 로그인하지 않은 상태에서만 접근 가능
    public Map<String, Object> findId(@RequestParam("user_name") String userName,
            @RequestParam("email") String email) throws Exception {

        // 입력값 유효성 검사
        regexHelper.isValue(userName, "이름을 입력하세요");
        regexHelper.isValue(email, "email를 입력하세요");
        regexHelper.isEmail(email, "email 형식이 맞지 않습니다");

        // 중복 검사에 사용할 멤버 객체 생성
        Members input = new Members();
        input.setUserName(userName);
        input.setEmail(email);

        Members output = memberService.findId(input);

        // 결과를 json형태로 반환
        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("user_id", output.getUserId());

        // RestHelper의 sendJson()메서드는 기본적으로 성공 응답을 반환한다
        return restHelper.sendJson(data);
    }


    /* 비밀번호 재설정 */
    @PutMapping("/api/account/reset_pw")
    @SessionCheckHelper(enable = false) // 로그인하지 않은 상태에서만 접근 가능
    public Map<String, Object> resetPw(@RequestParam("user_id") String userId,
            @RequestParam("email") String email) throws Exception {

        /* 1)임시 비밀번호를 디비에 갱신하기 */
        String newPassword = utilHelper.getRandomString(8);
        Members input = new Members();
        input.setUserId(userId);
        input.setEmail(email);
        input.setUserPw(newPassword);
        memberService.resetPw(input);

        /* 2)이메일 발송을 위한 템플릿 처리 */
        // 메일 템플릿 파일 경로

        ClassPathResource resource =
        new ClassPathResource("templates/mail_templates/reset_pw.html");
        String mailTemplatePath = resource.getFile().getAbsolutePath();
        String template = fileHelper.readString(mailTemplatePath);
        template = template.replace("{{userId}}", userId);
        template = template.replace("{{password}}", newPassword);

        /* 3)메일 발송 */
        String subject = userId + "님의 비밀번호가 재설정 되었습니다";
        mailHelper.sendMail(email, subject, template);
        return restHelper.sendJson();
    }

    @DeleteMapping("/api/account/out")
    @SessionCheckHelper(enable = true) // 회원 탈퇴 페이지는 로그인 상태에서 접근 가능
    public Map<String, Object> out(HttpServletRequest request,
            @SessionAttribute("memberInfo") Members memberInfo,
            @RequestParam("password") String password) throws Exception {

        // 세션으로부터 추출한 member객체에 입력받은 비밀번호를 넣어준다
        memberInfo.setUserPw(password);

        // 탈퇴수행
        memberService.out(memberInfo);

        // 로그아웃을 위해 세션을 삭제한다
        HttpSession session = request.getSession();
        session.invalidate();

        // RestHelper의 sendJson()메서드는 기본적으로 성공 응답을 반환한다
        return restHelper.sendJson();
    }


    /**
     * 회원정보 수정 컨트롤러
     *
     * @param request -세션 갱신용
     * @param memberInfo -현재 세션 정보 확인용
     * @param userPw -현재 비밀번호
     * @param newUserPw -신규 비밀번호
     * @param newUserPwConfirm -신규 비밀번호 확인용
     * @param userName
     * @param email
     * @param phone
     * @param birthday
     * @param gender
     * @param postcode
     * @param addr1
     * @param addr2
     * @param isEmailAgree
     * @param isSmsAgreeParam
     * @param deletePhoto
     * @param photo
     * @return
     * @throws Exception
     */
    @PutMapping("/api/account/edit")
    @SessionCheckHelper(enable = true) // 회원 정보 수정 페이지는 로그인 상태에서 접근 가능
    public Map<String, Object> edit(HttpServletRequest request, // 세션 생신용
            @SessionAttribute("memberInfo") Members memberInfo, // 현재 세션 정보 확인용
            @RequestParam("user_pw") String userPw, // 현재 비밀번호 (정보 확인용)
            @RequestParam("new_user_pw") String newUserPw, // 신규 비밀번호
            @RequestParam("new_user_pw_confirm") String newUserPwConfirm,
            @RequestParam("user_name") String userName, @RequestParam("email") String email,
            @RequestParam("phone") String phone, @RequestParam("birthday") String birthday,
            @RequestParam("gender") String gender, @RequestParam("postcode") String postcode,
            @RequestParam("addr1") String addr1, @RequestParam("addr2") String addr2,
            @RequestParam(value = "is_email_agree", required = false,
                    defaultValue = "N") String isEmailAgree,
            @RequestParam(value = "isSmsAgree", required = false) String isSmsAgreeParam,
            @RequestParam(value = "delete_photo", defaultValue = "N") String deletePhoto,
            @RequestParam(value = "photo", required = false) MultipartFile photo) throws Exception {

        // 1)입력값 유효성검사
        regexHelper.isValue(userPw, "비밀번호를 입력하세요");

        if ((newUserPw != null && !newUserPw.isEmpty())
                // 신규비밀번호가 입력된경우
                && !newUserPw.equals(newUserPwConfirm)) {
            regexHelper.isValue(newUserPw, "비밀번호 확인이 잘못되었습니다");
        }

        regexHelper.isValue(userName, "이름을 입력");
        regexHelper.isKor(userName, "이름은 한글만 가능");
        regexHelper.isValue(email, "이메일을 입력하세요");
        regexHelper.isEmail(email, "이메일 형식이 잘못됨");
        regexHelper.isValue(phone, "전화번호를 입력하세요");
        regexHelper.isPhone(phone, "전화번호 형식이 잘못됨");
        regexHelper.isValue(birthday, "생년월일을 입력하세요");
        regexHelper.isValue(gender, "성별을 입력하세요");

        if (!gender.equals("M") && !gender.equals("F")) {
            throw new StringFormatException("성별은 M또는 F만 입력할수 잇음");
        }

        regexHelper.isValue(postcode, "우편번호를 입력하세요");
        regexHelper.isValue(addr1, "주소를 입력하세요");
        regexHelper.isValue(addr2, "상세주소를 입력하세요");

        String isSmsAgree = "N";

        if ("Y".equals(isSmsAgreeParam)) {
            isSmsAgree = "Y";
        }

        // 이메일 중복검사
        Members input = new Members();
        input.setEmail(email);
        input.setId(memberInfo.getId());
        memberService.isUniqueEmail(input);


        /* 2)업로드 받기 */
        UploadItem uploadItem = null;

        try {
            uploadItem = fileHelper.saveMultipartFile(photo);
        } catch (Exception e) {
            /**
             * FileHelper는 upload field자체가 없는 경우 예외를 발생시킴 하지만 html에서 사진을 삭제하지 않응 경우는 upload필드가
             * disabled되므로 컨트롤러 입장에서는 업로드 필드 자체가 없는 경우로 판단되기 때문에 에러가 발생함
             */
        }

        /*
         * 3)정보를 service에 전달하기 위한 객체 구성 아이디는 수정필요 없으므로 비워돔.
         */
        Members member = new Members();
        member.setId(memberInfo.getId());
        member.setUserPw(userPw);
        member.setNewUserPw(newUserPw);
        member.setUserName(userName);
        member.setEmail(email);
        member.setPhone(phone);
        member.setBirthday(birthday);
        member.setGender(gender);
        member.setPostcode(postcode);
        member.setAddr1(addr1);
        member.setAddr2(addr2);
        member.setIsSmsAgree(isSmsAgree);
        member.setIsEmailAgree(isEmailAgree);

        // 현재 프로필 사진을 가져옴
        String currentPhoto = memberInfo.getPhoto();

        // 현재 프로필 사진이 있는 경우
        if (currentPhoto != null && !currentPhoto.equals("")) {

            // 기존 사진의 삭제가 요청되었다면?
            if (deletePhoto.equals("Y")) {

                try {
                    fileHelper.deleteFile(currentPhoto);
                } catch (Exception e) {

                }
                if (uploadItem != null) {
                    member.setPhoto(uploadItem.getFilePath());
                } else {
                    member.setPhoto(null);
                }
            } else {
                // 삭제 요청이 없는 경우는 세션의 사진 경로를 그대로 적용하여 기존 사진을 유지하도록 한다
                member.setPhoto(currentPhoto);
            }
        } else {
            // 업로드된 사진이 있다면 빈즈에 포함한다
            if (uploadItem != null) {
                member.setPhoto(uploadItem.getFilePath());
            }
        }

        // 5)DB에저장
        Members output = memberService.update(member);

        // 6)변경된 정보로 세션 갱신
        request.getSession().setAttribute("memberInfo", output);

        log.debug("세션 갱신 후 photo: {}", output.getPhoto());


        // 6)결과 반환
        return restHelper.sendJson();

    }



}
