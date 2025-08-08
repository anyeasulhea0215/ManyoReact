package kr.manyofactory.manyoshop.mappers;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import kr.manyofactory.manyoshop.models.MemberStatics;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class MemberStaticsMapperTest {


    @Autowired
    private MemberStatisticsMapper memberStatisticsMapper;

    @Test
    void testCountByJoinDate() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        int count = memberStatisticsMapper.countByJoinDate(yesterday);

        log.debug("어제({}) 가입자 수: {}", yesterday, count);
    }

    @Test
    void testInsertDailyCount() {
        LocalDate yesterday = LocalDate.now().minusDays(1);

        // 어제 가입자 수를 실제로 조회
        int count = memberStatisticsMapper.countByJoinDate(yesterday);
        log.debug("어제({}) 가입자 수: {}", yesterday, count);

        // 조회한 가입자 수를 통계 테이블에 저장
        int result = memberStatisticsMapper.insertDailyCount(yesterday, count);
        log.debug("insertDailyCount 결과: {}", result);

    }


    @Test
    void testSelectAllDailyCounts() {
        List<MemberStatics> results = memberStatisticsMapper.selectAllDailyCounts();

        log.debug("전체 통계 개수: {}", results.size());
        results.forEach(stat -> log.debug(stat.toString()));


    }
}
