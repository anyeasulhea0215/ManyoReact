package kr.manyofactory.manyoshop.services;

import kr.manyofactory.manyoshop.mappers.BestProductMapper;
import kr.manyofactory.manyoshop.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BestProductService {

    @Autowired
    private BestProductMapper bestProductMapper;


    public List<Product> getBestProducts(String keyword, int offset, int limit) {
        return bestProductMapper.selectBestProductsPaging(keyword, offset, limit);
    }
    public Product getProductById(int productId) {
        return bestProductMapper.selectProductById(productId);
    }


    public int getBestProductCount(String keyword) {
        return bestProductMapper.countBestProducts(keyword);
    }


}
