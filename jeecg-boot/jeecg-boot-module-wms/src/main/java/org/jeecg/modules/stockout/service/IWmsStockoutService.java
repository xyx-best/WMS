package org.jeecg.modules.stockout.service;

import org.jeecg.modules.stockout.entity.WmsStockoutdtl;
import org.jeecg.modules.stockout.entity.WmsStockout;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @Description: 出库总表
 * @Author: jeecg-boot
 * @Date:   2020-03-24
 * @Version: V1.0
 */
public interface IWmsStockoutService extends IService<WmsStockout> {

	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(WmsStockout wmsStockout, List<WmsStockoutdtl> wmsStockoutdtlList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(WmsStockout wmsStockout, List<WmsStockoutdtl> wmsStockoutdtlList);
	
	/**
	 * 删除一对多
	 */
	public void delMain(String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain(Collection<? extends Serializable> idList);

	public WmsStockout getByStockoutId(String stockoutId);

    WmsStockout getByStockoutCode(String stockout_code);
}

