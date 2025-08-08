package kr.manyofactory.manyoshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;
import kr.manyofactory.manyoshop.helpers.SessionCheckHelper;
import kr.manyofactory.manyoshop.models.Members;

@Controller
public class AccountController {

    @GetMapping({"/", "/login"})
    public String index() {
        return "login/login";
    }

    /*
     * 계정 관련 페이지로 이동
     *
     * @return-계정 관련 페이지의 뷰 이름
     *
     * @GetMapping({"/", "/account"}) public String index() { return "account/index"; }
     */

    /**
     * 로그인,회원가입 -->인덱스 페이지
     *
     * @return
     */
    @GetMapping("/account")
    public String accountRedirect() {
        return "redirect:/login/login";
    }

    @GetMapping("/login/join")
    @SessionCheckHelper(enable = false) // 회원가입 페이지는 로그인하지 않아도 접근 가능
    public String join() {
        return "login/join";
    }

    /**
     * 회원가입 결과 페이지로 이동
     *
     * @return -회원가입 결과 페이지의 뷰 이름
     */
    @GetMapping("/login/join_result")
    @SessionCheckHelper(enable = false) // 회원가입 후 결과 페이지는 로그인하지 않아도 접근 가능
    public String joinResult() {
        return "login/join_result";
    }

    @GetMapping("/login/find_id")
    @SessionCheckHelper(enable = false) // 아이디 찾기 페이지는 로그인하지 않아도 접근 가능
    public String findId() {
        return "login/find_id";
    }

    @GetMapping("/login/reset_pw")
    @SessionCheckHelper(enable = false) // 비밀번호 재설정 페이지는 로그인하지 않아도 접근 가능
    public String resetPw() {
        return "login/reset_pw";
    }


    @GetMapping("/login/out")
    @SessionCheckHelper(enable = true) // 회원 탈퇴 페이지는 로그인 상태에서 접근 가능
    public String out() {
        return "login/out";
    }

    @GetMapping("/login/terms")
    @SessionCheckHelper(enable = false) // 약관 동의 페이지는 로그인하지 않아도 접근 가능
    public String getterms() {
        return "login/terms"; // index(login.html) ->terms.html --> 일반회원가입하기(join)
    }

    @GetMapping("/mypage/member-edit")
    @SessionCheckHelper(enable = true) // 회원 정보 수정 페이지는 로그인 상태에서 접근 가능
    public String showEditForm(HttpSession session, Model model) {
        Members member = (Members) session.getAttribute("memberInfo");

        if (member == null) {
            return "redirect:/login/login"; // 로그인 안 돼있으면 로그인 페이지로
        }

        model.addAttribute("member", member);
        return "mypage/member-edit";
    }
}
