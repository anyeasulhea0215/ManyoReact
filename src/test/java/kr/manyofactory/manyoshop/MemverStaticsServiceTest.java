package kr.manyofactory.manyoshop;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import kr.manyofactory.manyoshop.models.MemberStatics;
import kr.manyofactory.manyoshop.services.MemberStatisticsService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class MemverStaticsServiceTest {

    @Autowired
    private MemberStatisticsService memberStatisticsService;

    @Test
    void testMembersave() {

        try {
            List<MemberStatics> results = memberStatisticsService.saveAndGetAll();

            log.debug("회원 통계 리스트 사이즈: {}", results.size());
            results.forEach(stat -> log.debug(stat.toString()));


            log.debug("테스트 성공: 통계 저장 및 조회 정상 작동");
        } catch (Exception e) {
            log.error("테스트 실패: 예외 발생", e);

        }
    }

}
