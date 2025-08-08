package kr.manyofactory.manyoshop.mappers;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import kr.manyofactory.manyoshop.models.MemberStatics;
import java.time.LocalDate;
import java.util.List;

@Mapper
public interface MemberStatisticsMapper {

    /**
     * 주어진 날짜(어제)의 회원 가입 수 조회
     */
    @Select("""
                SELECT COUNT(*)
                FROM members
                WHERE DATE(join_date) = #{date}
            """)
    int countByJoinDate(@Param("date") LocalDate date);

    /**
     * 일별 회원 가입 통계 저장
     */
    @Insert("""
                INSERT INTO daily_member_count (date, count)
                VALUES (#{date}, #{count})
                ON DUPLICATE KEY UPDATE count = #{count}
            """)
    int insertDailyCount(@Param("date") LocalDate date, @Param("count") int count);

    /**
     * 전체 회원 가입 통계 조회
     */
    @Select("""
                SELECT id, date, count
                FROM daily_member_count
                ORDER BY date DESC
            """)
    List<MemberStatics> selectAllDailyCounts();
}
