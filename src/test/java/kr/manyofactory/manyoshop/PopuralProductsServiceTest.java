package kr.manyofactory.manyoshop;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import kr.manyofactory.manyoshop.models.PopularProduct;
import kr.manyofactory.manyoshop.services.PopularProductService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class PopuralProductsServiceTest {

    @Autowired
    private PopularProductService popularProductService;

    @Test
    void testGeneratePopularProducts() {
        try {
            popularProductService.generatePopularProducts();
            log.debug("generatePopularProducts() 호출 성공");
        } catch (Exception e) {
            log.error("generatePopularProducts() 호출 실패", e);
            fail("예외 발생: " + e.getMessage());
        }
    }

    @Test
    void testGetYesterdayTopProducts() {
        List<PopularProduct> results = popularProductService.getYesterdayTopProducts();

        assertNotNull(results, "결과는 null이 아니어야 합니다.");
        assertTrue(results.size() >= 0, "결과 리스트는 0개 이상이어야 합니다.");

        log.debug("getYesterdayTopProducts() 결과 수: {}", results.size());
        results.forEach(product -> log.debug(product.toString()));
    }

    @Test
    void testGetTopProductsByWishlist() {
        List<PopularProduct> results = popularProductService.getTopProductsByWishlist();

        assertNotNull(results, "결과는 null이 아니어야 합니다.");
        assertTrue(results.size() >= 0, "결과 리스트는 0개 이상이어야 합니다.");

        log.debug("getTopProductsByWishlist() 결과 수: {}", results.size());
        results.forEach(product -> log.debug(product.toString()));
    }
}
