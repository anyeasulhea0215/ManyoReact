package kr.manyofactory.manyoshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;
import kr.manyofactory.manyoshop.models.Members;

@Controller
public class LoginController {


    @GetMapping("/join")
    public String join() {
        return "login/join";
    }


    @GetMapping("/login/login")
    public String login() {
        return "login/login";
    }



}
