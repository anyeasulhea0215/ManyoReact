package kr.manyofactory.manyoshop.services;

import kr.manyofactory.manyoshop.models.Product;
import kr.manyofactory.manyoshop.models.ProductInquiry;
import java.util.List;

public interface ProductInquiryService {
    int add(ProductInquiry input) throws Exception;
    ProductInquiry getItem(ProductInquiry input) throws Exception;
    List<ProductInquiry> getList(ProductInquiry input) throws Exception;
    int getCount(ProductInquiry input) throws Exception;
    int editAnswer(ProductInquiry input) throws Exception;
    int delete(ProductInquiry input) throws Exception;

    List<Product> getPurchasedProductsByMemberId(int memberId);
    List<ProductInquiry> getMyInquiries(int memberId);
    List<ProductInquiry> getInquiriesByProductIdAndMemberId(int productId, int memberId);

    ProductInquiry getInquiryById(int inquiryId) throws Exception;
    void update(ProductInquiry inquiry);





}
