package kr.manyofactory.manyoshop.mappers;
import kr.manyofactory.manyoshop.models.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SpecialProductMapper {

    // 특가상품 리스트 조회
    @Select("""
        SELECT 
            p.product_id,
            p.category_id,
            p.product_name,
            p.product_price,
            p.sale_price,
            p.discount,
            p.reg_date,
            p.edit_date,
            p.product_img
        FROM special_product sp
        JOIN products p ON sp.product_id = p.product_id
        WHERE p.product_name LIKE CONCAT('%', #{keyword}, '%')
        ORDER BY sp.reg_date DESC
        LIMIT #{offset}, #{listCount}
    """)
    @Results(id = "productResultMap", value = {
            @Result(column = "product_id", property = "productId"),
            @Result(column = "category_id", property = "categoryId"),
            @Result(column = "product_name", property = "productName"),
            @Result(column = "product_price", property = "productPrice"),
            @Result(column = "sale_price", property = "salePrice"),
            @Result(column = "discount", property = "discount"),
            @Result(column = "reg_date", property = "regDate"),
            @Result(column = "edit_date", property = "editDate"),
            @Result(column = "product_img", property = "productImg")
    })
    List<Product> selectSpecialProducts(String keyword, int offset, int listCount);


    // 특가상품 총 개수
    @Select("""
        SELECT COUNT(*)
        FROM special_product sp
        JOIN products p ON sp.product_id = p.product_id
        WHERE p.product_name LIKE CONCAT('%', #{keyword}, '%')
    """)
    int countSpecialProducts(String keyword);
}
