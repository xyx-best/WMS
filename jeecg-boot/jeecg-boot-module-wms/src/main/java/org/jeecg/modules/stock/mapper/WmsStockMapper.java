package org.jeecg.modules.stock.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.stock.entity.WmsStock;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 库存表
 * @Author: jeecg-boot
 * @Date:   2020-03-09
 * @Version: V1.0
 */
public interface WmsStockMapper extends BaseMapper<WmsStock> {

    /**
     * 求 指定区域列表的 库存 货物数量
     * @param areaIds
     * @return
     */
//    @Select("SELECT SUM(GOODS_QUANTITY) FROM WMS_STOCK WHERE AREA_ID IN (#{areaIds})")
    Integer selectSumStock(@Param("areaIds")List<String> areaIds);

    Integer selectSumStockWithLevel(@Param("areaIds")List<String> areaIds, @Param("level")String level);

    List<Map<String, Integer>> selectByTime(String date, String format1, String format2);

    List<String> selectLocIdWithLevelAndAreas(@Param("goodsLevel")String goodsLevel, @Param("areaIds")List<String> areaIds);

    List<Map<String, Integer>> selectByRangeTime(@Param("start")String start, @Param("end")String end);
}
