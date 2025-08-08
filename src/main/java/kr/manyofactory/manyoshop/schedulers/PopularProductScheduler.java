package kr.manyofactory.manyoshop.schedulers;

import kr.manyofactory.manyoshop.services.PopularProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class PopularProductScheduler {
private final PopularProductService popularProductService;
public void insertDailyPopularProducts() {
popularProductService.generatePopularProducts();
}
}