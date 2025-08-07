package kr.manyofactory.manyoshop.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import kr.manyofactory.manyoshop.mappers.MemberStatisticsMapper;
import kr.manyofactory.manyoshop.models.MemberStatics;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberStatisticsService {

    private final MemberStatisticsMapper memberStatisticsMapper;

    public List<MemberStatics> saveAndGetAll() {

        LocalDate yesterday = LocalDate.now().minusDays(1);
        int count = memberStatisticsMapper.countByJoinDate(yesterday);

        memberStatisticsMapper.insertDailyCount(yesterday, count);

        return memberStatisticsMapper.selectAllDailyCounts();
    }
}
