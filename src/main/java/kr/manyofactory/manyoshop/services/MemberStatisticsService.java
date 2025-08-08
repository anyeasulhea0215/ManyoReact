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

        int count = memberStatisticsMapper.countByJoinDate(yesterday); // 1일전 회원가입수 조회

        memberStatisticsMapper.insertDailyCount(yesterday, count);
        // 디비에 회원가입 수, 날짜 삽입

        return memberStatisticsMapper.selectAllDailyCounts();
    }
}
