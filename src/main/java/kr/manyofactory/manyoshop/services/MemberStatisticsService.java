package kr.manyofactory.manyoshop.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import kr.manyofactory.manyoshop.mappers.MemberStatisticsMapper;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class MemberStatisticsService {

    private final MemberStatisticsMapper memberStatisticsMapper;

    public void saveYesterdayMemberCount() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        int count = memberStatisticsMapper.countByJoinDate(yesterday); // 가입날짜 저장->count

        memberStatisticsMapper.insertDailyCount(yesterday, count); // 가입한 회원 수 저장
    }
}
