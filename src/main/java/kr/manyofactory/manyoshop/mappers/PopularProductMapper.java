package kr.manyofactory.manyoshop.mappers;

import kr.manyofactory.manyoshop.models.PopularProduct;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PopularProductMapper {



    @Select("""
    SELECT
        p.product_id,
        p.product_name,
        COUNT(w.wishlist_id) AS total_count,
        CURDATE() AS stat_date
    FROM
        wishlist w
        JOIN products p ON w.product_id = p.product_id
    GROUP BY
        p.product_id
    ORDER BY
        total_count DESC
    LIMIT 5
""")
List<PopularProduct> findTopProductsByWishlist();

}
