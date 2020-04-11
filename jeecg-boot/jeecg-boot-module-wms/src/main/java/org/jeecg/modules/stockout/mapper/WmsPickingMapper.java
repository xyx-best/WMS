package org.jeecg.modules.stockout.mapper;

import java.util.List;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.stockout.entity.WmsPicking;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 拣选表
 * @Author: jeecg-boot
 * @Date:   2020-03-24
 * @Version: V1.0
 */
public interface WmsPickingMapper extends BaseMapper<WmsPicking> {
//
//    /**
//     *  通过主表外键批量删除客户
//     * @param mainId
//     * @return
//     */
//    @Delete("DELETE FROM TEST WHERE ID = #{mainId}")
//    @DS("multi-datasource1")
//    public boolean deleteByMainId(@Param("mainId") String mainId);
}
