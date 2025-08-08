package kr.manyofactory.manyoshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpSession;
import kr.manyofactory.manyoshop.models.Members;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;


@Controller
@RequestMapping("/manyo")
public class MainController {
    @GetMapping("/main")
    public String showMainPage(Model model, HttpSession session) {

        Members loginMember = (Members) session.getAttribute("memberInfo");
        if (loginMember == null) {
            return "redirect:/login";
        }

        int memberId = loginMember.getId();

        LocalDate tomorrow = LocalDate.now().plusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d(EEE)", Locale.KOREAN);
        model.addAttribute("tomorrowDate", tomorrow.format(formatter));

        model.addAttribute("memberInfo", loginMember);
        return "manyo/main";
    }

    @GetMapping("/event")
    public String eventPage() {
        return "manyo/event";
    }

    @GetMapping("/brand-story")
    public String brandStory() {
        return "manyo/brand-story";
    }

    @GetMapping("/global_partners")
    public String globalPartners() {
        return "manyo/global_partners";
    }

    @GetMapping("/special-price")
    public String specialPrice() {

        return "manyo/special-price";
    }

    @GetMapping("/best")
    public String bestPage() {
        return "manyo/best";
    }
}
