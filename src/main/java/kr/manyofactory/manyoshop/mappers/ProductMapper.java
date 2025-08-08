package kr.manyofactory.manyoshop.mappers;

import kr.manyofactory.manyoshop.models.Product;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface ProductMapper {

    /**
     * 상품 등록
     */
    @Insert("""

                INSERT INTO products (
                    product_id, category_id, best_id, product_name, product_price,
                    sale_price, discount, reg_date, edit_date, product_img
                ) VALUES (
                    #{productId}, #{categoryId}, #{bestId}, #{productName}, #{productPrice},
                    #{salePrice}, #{discount}, #{regDate}, #{editDate}, #{productImg}
                )
            """)
    int insert(Product input);

    /**
     *
     * INSERT INTO products ( product_id, category_id, best_id, product_name, product_price,
     * sale_price, discount, reg_date, edit_date, product_img ) VALUES ( #{productId},
     * #{categoryId}, #{bestId}, #{productName}, #{productPrice}, #{salePrice}, #{discount},
     * #{regDate}, #{editDate}, #{productImg} ) """) int insert(Product input);
     *
     * /*
     *
     * 상품 상세 조회
     */
    @Select("SELECT * FROM products WHERE product_id = #{productId}")
    @Results(id = "productResultMap",
            value = {@Result(column = "product_id", property = "productId"),
                    @Result(column = "category_id", property = "categoryId"),
                    @Result(column = "best_id", property = "bestId"),
                    @Result(column = "product_name", property = "productName"),
                    @Result(column = "product_price", property = "productPrice"),
                    @Result(column = "sale_price", property = "salePrice"),
                    @Result(column = "discount", property = "discount"),
                    @Result(column = "reg_date", property = "regDate"),
                    @Result(column = "edit_date", property = "editDate"),
                    @Result(column = "product_img", property = "productImg")})
    Product selectItem(int productId);

    /**
     * 상품 수 조회 (검색 조건 포함)
     */
    @Select("""
    <script>
    SELECT COUNT(*) FROM products
    <where>
        <if test='categoryId != null'> category_id = #{categoryId} </if>
        <if test='productName != null and productName != ""'> AND product_name LIKE CONCAT('%', #{productName}, '%') </if>
    </where>
    </script>
""")
    int selectCount(Product input);



    /*
     * 상품 목록 조회 (검색 + 페이징)
     */
    @Select("""
    <script>
    SELECT * FROM products
    <where>
        <if test='categoryId != null'> category_id = #{categoryId} </if>
        <if test='productName != null and productName != ""'> AND product_name LIKE CONCAT('%', #{productName}, '%') </if>
    </where>
    ORDER BY product_id DESC
    LIMIT #{offset}, #{listCount}
    </script>
""")
    @ResultMap("productResultMap")
    List<Product> selectList(Product input);


    // 전체 상품 수 조회 (검색어 포함)
    @Select("""
                <script>
                SELECT COUNT(*) FROM products
                <where>
                    <if test="keyword != null and keyword != ''">
                        AND product_name LIKE CONCAT('%', #{keyword}, '%')
                    </if>
                </where>
                </script>
            """)

    int countProducts(String keyword);

    /** 전체 상품 목록 조회 (검색어 포함) */
    @Select("""
            <script>
            SELECT * FROM products
            <where>
                <if test="keyword != null and keyword != ''">
                    AND product_name LIKE CONCAT('%', #{keyword}, '%')
                </if>
            </where>
            ORDER BY product_id DESC
            LIMIT #{offset}, #{listCount}
            </script>
            """)
    @ResultMap("productResultMap")
    List<Product> selectProducts(String keyword, int offset, int listCount);


    /**
     * 상품 목록 조회 (카테고리 + 이름 + 페이징)
     */
    @Select("""
                <script>
                SELECT * FROM products
                <where>
                  <if test='categoryId != null'> category_id = #{categoryId} </if>
                  <if test='productName != null and productName != ""'> AND product_name LIKE CONCAT('%', #{productName}, '%') </if>
                </where>
            
                ORDER BY product_id DESC
                LIMIT #{limit} OFFSET #{offset}
                </script>
            """)
    @ResultMap("productResultMap")
    List<Product> selectProductsPaging(int categoryId, String productName, int offset, int limit);

    /**
     * 상품 ID로 조회
     */
    @Select("SELECT * FROM products WHERE product_id = #{productId}")
    @ResultMap("productResultMap")
    Product selectProductById(@Param("productId") int productId);

    /**
     *
     * @param productName
     * @return
     */
    @Select("""
    <script>
    SELECT * FROM products
    WHERE product_name LIKE CONCAT('%', #{productName}, '%')
    LIMIT 1
    </script>
""")
    Product selectProductByNameLike(String productName);

    @Select("SELECT * FROM products ORDER BY product_id DESC")
    @ResultMap("productResultMap")
    List<Product> selectAll();


}
