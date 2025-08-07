package kr.manyofactory.manyoshop.schedulers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import kr.manyofactory.manyoshop.helpers.FileHelper;
import kr.manyofactory.manyoshop.models.Members;
import kr.manyofactory.manyoshop.services.MemberService;
import kr.manyofactory.manyoshop.services.MemberStatisticsService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@EnableAsync
public class AccountSchedulers {


    @Value("${upload.dir}")
    private String uploadDir;

    @Value("${thumbnail.width}")
    private String thumbnailWidth;

    @Value("${thumbnail.height}")
    private String thumbnailHeight;


    @Autowired
    private FileHelper fileHelper;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberStatisticsService memberStatisticsService;

    // @Scheduled(cron = "0 0 4 * * ?")
    @Scheduled(cron = "15 * * * * ?") // 매 분마다 오전 15초에 실행
    // @Scheduled(cron = "0 0/30 * * * ?")
    public void processOutMembers() throws Exception {
        log.debug("탈퇴 회원 정리 시작");


        List<Members> outMembers = null;

        try {
            log.debug("탈퇴회원 조회 및 삭제");
            outMembers = memberService.processOutMembers();
        } catch (Exception e) {
            log.error("탈퇴 회원 조회 및 삭제 실패", e);
            return;
        }

        for (int i = 0; i < outMembers.size(); i++) {
            Members m = outMembers.get(i);
            fileHelper.deleteFile(m.getPhoto());
        }

    }


    // 매일 오전 1시 실행
    @Scheduled(cron = "0 0 1 * * *")
    public void runDailyStatistics() {
        memberStatisticsService.saveYesterdayMemberCount();
    }
}
