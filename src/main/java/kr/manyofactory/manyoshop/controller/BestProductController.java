package kr.manyofactory.manyoshop.controller;

import kr.manyofactory.manyoshop.helpers.PageHelper;
import kr.manyofactory.manyoshop.models.Members;
import kr.manyofactory.manyoshop.models.Product;
import kr.manyofactory.manyoshop.services.BestProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Controller
public class BestProductController {

    @Autowired
    private BestProductService bestProductService;

    @GetMapping("/best")
    public String showBestProducts(HttpSession session,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "1") int page, Model model) {

        Members loginMember = (Members) session.getAttribute("memberInfo");
        if (loginMember == null) {
            return "redirect:/login";
        }

        int memberId = loginMember.getId();

        /* ============================= */

        int totalCount = bestProductService.getBestProductCount(keyword);


        int listCount = 12;
        int groupCount = 5;


        PageHelper pageHelper = new PageHelper(page, totalCount, listCount, groupCount);


        List<Product> bestProducts = bestProductService.getBestProducts(keyword,
                pageHelper.getOffset(), pageHelper.getListCount());

        model.addAttribute("bestProducts", bestProducts);
        model.addAttribute("keyword", keyword);
        model.addAttribute("pageHelper", pageHelper);

        model.addAttribute("memberInfo", loginMember);


        LocalDate tomorrow = LocalDate.now().plusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d(EEE)", Locale.KOREAN);
        model.addAttribute("tomorrowDate", tomorrow.format(formatter));

        return "manyo/best";
    }



}
