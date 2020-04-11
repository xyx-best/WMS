package org.jeecg.modules.stockout.service;

import org.jeecg.modules.stockout.entity.WmsStockoutdtl;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import java.util.Map;

/**
 * @Description: 出库明细表
 * @Author: jeecg-boot
 * @Date:   2020-03-24
 * @Version: V1.0
 */
public interface IWmsStockoutdtlService extends IService<WmsStockoutdtl> {

	public List<WmsStockoutdtl> selectByMainId(String mainId);

	/**
	 * 根据入库ID和入库编码 查询入库明细表
	 * @return
	 */
	Map<String, List> queryByStockout(String id, String code);
}
