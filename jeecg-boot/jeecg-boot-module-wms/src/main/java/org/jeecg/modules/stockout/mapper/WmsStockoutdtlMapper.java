package org.jeecg.modules.stockout.mapper;

import java.util.List;
import org.jeecg.modules.stockout.entity.WmsStockoutdtl;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 出库明细表
 * @Author: jeecg-boot
 * @Date:   2020-03-24
 * @Version: V1.0
 */
public interface WmsStockoutdtlMapper extends BaseMapper<WmsStockoutdtl> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<WmsStockoutdtl> selectByMainId(@Param("mainId") String mainId);
}
