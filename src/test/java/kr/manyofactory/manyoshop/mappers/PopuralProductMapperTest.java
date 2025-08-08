package kr.manyofactory.manyoshop.mappers;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import kr.manyofactory.manyoshop.models.PopularProduct;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class PopuralProductMapperTest {
    @Autowired
    private PopularProductMapper popularProductMapper;

    /*
     * @Test void testInsertPopularProductsFromOrders() { try {
     * popularProductMapper.insertPopularProductsFromOrders();
     * log.debug("insertPopularProductsFromOrders() 호출 성공"); } catch (Exception e) {
     * log.error("insertPopularProductsFromOrders() 호출 실패", e); fail("예외가 발생했습니다: " +
     * e.getMessage()); } }
     */

    /*
     * @Test void testFindYesterdayTopProducts() { List<PopularProduct> results =
     * popularProductMapper.findYesterdayTopProducts();
     * 
     * 
     * log.debug("어제 인기 상품 개수: {}", results.size()); results.forEach(product ->
     * log.debug(product.toString())); }
     */

    @Test
    void testFindTopProductsByWishlist() {
        List<PopularProduct> results = popularProductMapper.findTopProductsByWishlist();

        log.debug("위시리스트 인기 상품 개수: {}", results.size());
        results.forEach(product -> log.debug(product.toString()));
    }
}
