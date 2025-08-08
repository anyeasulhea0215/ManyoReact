package kr.manyofactory.manyoshop.services.impl;

import kr.manyofactory.manyoshop.mappers.ProductInquiryMapper;
import kr.manyofactory.manyoshop.models.Product;
import kr.manyofactory.manyoshop.models.ProductInquiry;
import kr.manyofactory.manyoshop.services.ProductInquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductInquiryServiceImpl implements ProductInquiryService {

    private final ProductInquiryMapper productInquiryMapper;

    @Override
    public int add(ProductInquiry input) throws Exception {
        return productInquiryMapper.insert(input);
    }

    @Override
    public ProductInquiry getItem(ProductInquiry input) throws Exception {
        return productInquiryMapper.selectItem(input);
    }


    @Override
    public int getCount(ProductInquiry input) throws Exception {
        return productInquiryMapper.selectCount(input);
    }

    @Override
    public int editAnswer(ProductInquiry input) throws Exception {
        return productInquiryMapper.updateAnswer(input);
    }

    @Override
    public int delete(ProductInquiry input) throws Exception {
        return productInquiryMapper.delete(input);
    }


    @Override
    public List<Product> getPurchasedProductsByMemberId(int memberId) {
        return productInquiryMapper.findPurchasedProducts(memberId);
    }

    @Override
    public List<ProductInquiry> getList(ProductInquiry input) throws Exception {
        return productInquiryMapper.selectListByMemberId(input.getMemberId());
    }

    @Override
    public List<ProductInquiry> getMyInquiries(int memberId) {
        return productInquiryMapper.selectListByMemberId(memberId);
    }
    @Override
    public List<ProductInquiry> getInquiriesByProductIdAndMemberId(int productId, int memberId) {
        return productInquiryMapper.selectMyInquiriesByProduct(memberId, productId);
    }
    @Override
    public ProductInquiry getInquiryById(int inquiryId) throws Exception {
        ProductInquiry input = new ProductInquiry();
        input.setInquiryId(inquiryId);
        return productInquiryMapper.selectItem(input);
    }


    @Override
    public void update(ProductInquiry inquiry) {
        productInquiryMapper.updateAnswer(inquiry);
    }
}




