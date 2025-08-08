package kr.manyofactory.manyoshop.schedulers;

import kr.manyofactory.manyoshop.services.PopularProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PopularProductScheduler {

    private final PopularProductService popularProductService;

    @Scheduled(cron = "0 0 2 * * *") // 매일 새벽 2시 실행
    public void insertDailyPopularProducts() {
        log.info("인기상품 집계 시작");
        popularProductService.generatePopularProducts();
        log.info("인기상품 집계 완료");
    }
}