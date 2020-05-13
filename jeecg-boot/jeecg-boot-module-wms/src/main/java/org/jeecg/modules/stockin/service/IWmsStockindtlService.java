package org.jeecg.modules.stockin.service;

import org.jeecg.modules.stockin.entity.WmsStockindtl;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: 入库明细表
 * @Author: jeecg-boot
 * @Date:   2020-03-21
 * @Version: V1.0
 */
public interface IWmsStockindtlService extends IService<WmsStockindtl> {

	public List<WmsStockindtl> selectByMainId(String mainId);

	/**
	 * 根据入库ID和入库编码 查询入库明细表
	 * @return
	 */
	List<WmsStockindtl> getByStockin(String id, String code);

	/**
	 * 根据明细Id查询记录
	 * @param stockindtlId
	 * @return
	 */
	WmsStockindtl getByStockindtlId(String stockindtlId);

	/**
	 * 查询最近7天的入库量
	 * @return
	 */
	List<Map<String, Object>> queryLastDays();
}
