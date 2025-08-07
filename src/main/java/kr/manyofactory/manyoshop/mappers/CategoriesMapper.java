package kr.manyofactory.manyoshop.mappers;

import kr.manyofactory.manyoshop.models.Categories;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CategoriesMapper {

    /** 카테고리 등록 */
    @Insert("""
        INSERT INTO categories (category_name, reg_date, edit_date)
        VALUES (#{categoryName}, NOW(), NOW())
    """)
    @Options(useGeneratedKeys = true, keyProperty = "categoryId", keyColumn = "category_id")
    public int insert(Categories input);

    /** 카테고리 1건 조회 */
    @Select("""
        SELECT category_id, category_name, reg_date, edit_date
        FROM categories
        WHERE category_id = #{categoryId}
    """)
    public Categories selectItem(int categoryId);

    /** 카테고리 전체 목록 조회 */
    @Select("""
        SELECT category_id, category_name, reg_date, edit_date
        FROM categories
        ORDER BY category_id ASC
    """)
    public List<Categories> selectList();

}
