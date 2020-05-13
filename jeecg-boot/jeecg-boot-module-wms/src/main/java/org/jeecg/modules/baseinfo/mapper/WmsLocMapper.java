package org.jeecg.modules.baseinfo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.baseinfo.entity.WmsLoc;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 货位管理
 * @Author: jeecg-boot
 * @Date:   2020-02-26
 * @Version: V1.0
 */
public interface WmsLocMapper extends BaseMapper<WmsLoc> {

    Integer selectSizeByIds(@Param("difIds")List<String> difIds);
}
