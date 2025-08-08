package kr.manyofactory.manyoshop.mappers;

import kr.manyofactory.manyoshop.models.PopularProduct;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PopularProductMapper {

    @Select("""
        SELECT product_id, product_name, total_count, stat_date
        FROM popular_products
        WHERE stat_date = CURDATE() - INTERVAL 1 DAY
        ORDER BY total_count DESC
        LIMIT 5
    """)
    List<PopularProduct> findYesterdayTopProducts();

    @Insert("""
        INSERT INTO popular_products (product_id, product_name, total_count, stat_date)
        SELECT
            p.product_id,
            p.product_name,
            SUM(oi.order_count),
            CURDATE() - INTERVAL 1 DAY
        FROM
            order_items oi
            JOIN products p ON oi.product_id = p.product_id
        WHERE
            DATE(oi.reg_date) = CURDATE() - INTERVAL 1 DAY
            AND oi.order_status = '결제완료'
        GROUP BY
            p.product_id
        ORDER BY
            SUM(oi.order_count) DESC
        LIMIT 5
    """)
    void insertPopularProductsFromOrders();
}
