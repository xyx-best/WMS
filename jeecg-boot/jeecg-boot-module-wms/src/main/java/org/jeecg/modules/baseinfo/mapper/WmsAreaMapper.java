package org.jeecg.modules.baseinfo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.baseinfo.entity.WmsArea;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 区域管理
 * @Author: jeecg-boot
 * @Date:   2020-02-26
 * @Version: V1.0
 */
public interface WmsAreaMapper extends BaseMapper<WmsArea> {

    /**
     * 求 指定区域列表的 总大小
     * @param areaIds
     * @return
     */
//    @Select("SELECT SUM(AREA_SIZE) FROM WMS_AREA WHERE AREA_ID IN (#{areaIds})")
    Integer selectSumAreaSize(@Param("areaIds")List<String> areaIds);
}
