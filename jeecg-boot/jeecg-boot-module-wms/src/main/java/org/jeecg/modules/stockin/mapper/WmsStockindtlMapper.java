package org.jeecg.modules.stockin.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.jeecg.modules.stockin.entity.WmsStockindtl;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 入库明细表
 * @Author: jeecg-boot
 * @Date:   2020-03-21
 * @Version: V1.0
 */
public interface WmsStockindtlMapper extends BaseMapper<WmsStockindtl> {

//	public boolean deleteByMainId(@Param("mainId") String mainId);

	/**
	 *  通过主表外键批量删除客户
	 * @param mainId
	 * @return
	 */
	@Delete("DELETE FROM WMS_STOCKINDTL WHERE STOCKIN_ID = #{mainId}")
	public boolean deleteByMainId(@Param("mainId") String mainId);

	/**
	 * 根据入库总表ID 查询对应的 明细记录
	 * @param mainId
	 * @return
	 */
	public List<WmsStockindtl> selectByMainId(@Param("mainId") String mainId);

	/**
	 * 查询最近7天的每天的入库量
	 */
	public List<Map<String, Object>> selectSumLastDays();
}
