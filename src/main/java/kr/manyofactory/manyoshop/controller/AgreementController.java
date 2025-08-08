package kr.manyofactory.manyoshop.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;
import kr.manyofactory.manyoshop.models.MemberAgreement;
import kr.manyofactory.manyoshop.services.MemberAgreementService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/agreement")
@RequiredArgsConstructor
public class AgreementController {

    private final MemberAgreementService memberAgreementService;

    private static final String TEMP_AGREEMENTS_SESSION_KEY = "tempAgreements";

    /** 약관 동의 처리 */
    @PostMapping("/login/agreement")
    public String submitAgreement(@RequestParam("termTypes") List<String> termTypes,
            @RequestParam("agreements") List<String> agreements, HttpSession session) {

        List<MemberAgreement> agreementList = new ArrayList<>();

        for (int i = 0; i < termTypes.size(); i++) {
            MemberAgreement ag = new MemberAgreement();

            ag.setTermType(termTypes.get(i));
            ag.setIsAgreed(agreements.get(i));

            agreementList.add(ag);
        }


        session.setAttribute(TEMP_AGREEMENTS_SESSION_KEY, agreementList);

        return "redirect:/account/join";
    }

}
