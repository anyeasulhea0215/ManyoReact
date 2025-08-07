package kr.manyofactory.manyoshop.mappers;

import kr.manyofactory.manyoshop.models.PopularProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PopularProductMapper {

    @Select("""
        SELECT product_name, total_count, stat_date
        FROM popular_products
        WHERE stat_date = CURDATE() - INTERVAL 1 DAY
        ORDER BY total_count DESC
        LIMIT 5
    """)
    List<PopularProduct> findYesterdayTopProducts();
}
