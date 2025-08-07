package kr.manyofactory.manyoshop.services;

import kr.manyofactory.manyoshop.mappers.SpecialProductMapper;
import kr.manyofactory.manyoshop.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecialProductService {

    @Autowired
    private SpecialProductMapper specialProductMapper;

    // 특가 상품 총 개수 조회
    public int getSpecialProductCount(String keyword) {
        return specialProductMapper.countSpecialProducts(keyword);
    }

    // 특가 상품 목록 조회
    public List<Product> getSpecialProducts(String keyword, int offset, int listCount) {
        return specialProductMapper.selectSpecialProducts(keyword, offset, listCount);
    }
}
