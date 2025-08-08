package kr.manyofactory.manyoshop.controller.apis;

import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.List;
import kr.manyofactory.manyoshop.models.MemberStatics;
import kr.manyofactory.manyoshop.services.MemberStatisticsService;
import kr.manyofactory.manyoshop.services.PopularProductService;

@RestController
@RequestMapping("/api/statistics/members")
@RequiredArgsConstructor
public class MemberStatisticsRestController {

    private final MemberStatisticsService memberStatisticsService;

    @GetMapping("/daily")
    public List<MemberStatics> getMemberStaticsCount() {
        return memberStatisticsService.saveAndGetAll();
    }
}
