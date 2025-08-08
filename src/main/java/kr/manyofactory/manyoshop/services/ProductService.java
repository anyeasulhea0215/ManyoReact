package kr.manyofactory.manyoshop.services;


import kr.manyofactory.manyoshop.mappers.ProductInquiryMapper;
import kr.manyofactory.manyoshop.mappers.ProductMapper;
import kr.manyofactory.manyoshop.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;



@Service
public class ProductService {

    @Autowired
    private ProductInquiryMapper productInquiryMapper;

    @Autowired
    private ProductMapper productMapper;

    public int getProductCount(String keyword) {
        return productMapper.countProducts(keyword);
    }

    public List<Product> getProducts(String keyword, int offset, int listCount) {
        return productMapper.selectProducts(keyword, offset, listCount);
    }

    public Product getProductByNameLike(String productName) {
        return productMapper.selectProductByNameLike(productName);
    }
     public List<Product> getAll() {
        return productMapper.selectAll();
    }

    public Product getProductById(int productId) {
        return productMapper.selectProductById(productId);
    }

    public int getProductCount(String keyword, Integer categoryId) {
        Product input = new Product();
        input.setProductName(keyword);
        input.setCategoryId(categoryId);
        return productMapper.selectCount(input);
    }

    public List<Product> getProducts(String keyword, Integer categoryId, int offset, int listCount) {
        Product input = new Product();
        input.setProductName(keyword);
        input.setCategoryId(categoryId);
        input.setOffset(offset);
        input.setListCount(listCount);
        return productMapper.selectList(input);
    }

    public List<Product> getPurchasedProductsByMemberId(int memberId) {
    return productInquiryMapper.findPurchasedProducts(memberId);
}



}



