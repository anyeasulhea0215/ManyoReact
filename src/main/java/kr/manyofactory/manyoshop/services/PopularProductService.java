package kr.manyofactory.manyoshop.services;

import kr.manyofactory.manyoshop.models.PopularProduct;

import java.util.List;

public interface PopularProductService {
    List<PopularProduct> getYesterdayTopProducts();
     void generatePopularProducts();

     List<PopularProduct> getTopProductsByWishlist();


}
