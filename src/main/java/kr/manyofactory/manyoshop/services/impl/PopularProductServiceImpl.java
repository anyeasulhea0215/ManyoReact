
package kr.manyofactory.manyoshop.services.impl;

import kr.manyofactory.manyoshop.models.PopularProduct;
import kr.manyofactory.manyoshop.services.PopularProductService;
import kr.manyofactory.manyoshop.mappers.PopularProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PopularProductServiceImpl implements PopularProductService {

    private final PopularProductMapper popularProductMapper;

    @Override
    public List<PopularProduct> getTopProductsByWishlist() {
        return popularProductMapper.findTopProductsByWishlist();
    }

    @ Override
    public void generatePopularProducts() {
    List<PopularProduct> products = popularProductMapper.findTopProductsByWishlist();
    for (PopularProduct product : products) {
        System.out.println("위시기반 인기상품: " + product.getProductName());
    }
}

}