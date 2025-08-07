package kr.manyofactory.manyoshop.mappers;

import kr.manyofactory.manyoshop.models.Product;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface BestProductMapper {

    @Select("""
            <script>
            SELECT p.*
            FROM best_products bp
            JOIN products p ON bp.product_id = p.product_id
            <where>
                <if test="keyword != null and keyword != ''">
                    AND p.product_name LIKE CONCAT('%', #{keyword}, '%')
                </if>
            </where>
            ORDER BY bp.reg_date DESC
            </script>
            """)
    List<Product> selectBestProducts(String keyword);

    @Insert("""
            INSERT INTO best_products (product_id, reg_date, edit_time)
            VALUES (#{productId}, NOW(), NOW())
            """)
    int insertBestProduct(int productId);


    @Delete("DELETE FROM best_products WHERE product_id = #{productId}")
    int deleteBestProduct(int productId);

    @Select("""
            <script>
            SELECT COUNT(*)
            FROM best_products bp
            JOIN products p ON bp.product_id = p.product_id
            <where>
                <if test="keyword != null and keyword != ''">
                    AND p.product_name LIKE CONCAT('%', #{keyword}, '%')
                </if>
            </where>
            </script>
            """)
    int countBestProducts(String keyword);

    @Select("""
            <script>
            SELECT p.*
            FROM best_products bp
            JOIN products p ON bp.product_id = p.product_id
            <where>
                <if test='keyword != null and keyword != ""'>
                    AND p.product_name LIKE CONCAT('%', #{keyword}, '%')
                </if>
            </where>
            ORDER BY bp.reg_date DESC
            LIMIT #{limit} OFFSET #{offset}
            </script>
            """)
    List<Product> selectBestProductsPaging(String keyword, int offset, int limit);

    @Select("""
                SELECT *
                FROM products
                WHERE product_id = #{productId}
            """)
    Product selectProductById(int productId);

}
