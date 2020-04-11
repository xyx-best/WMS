package org.jeecg.modules.stockin.service;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.baseinfo.entity.WmsArea;
import org.jeecg.modules.baseinfo.entity.WmsLoc;
import org.jeecg.modules.stockin.entity.WmsStockindtl;
import org.jeecg.modules.stockin.entity.WmsStockin;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @Description: 入库表
 * @Author: jeecg-boot
 * @Date:   2020-03-21
 * @Version: V1.0
 */
public interface IWmsStockinService extends IService<WmsStockin> {

	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(WmsStockin wmsStockin, List<WmsStockindtl> wmsStockindtlList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(WmsStockin wmsStockin, List<WmsStockindtl> wmsStockindtlList);
	
	/**
	 * 删除一对多
	 */
	public void delMain(String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain(Collection<? extends Serializable> idList);

	/**
	 * 执行入库
	 * @param wmsStockindtl
	 * @param m
	 * @return
	 */
	Result<?> execStockin(WmsStockindtl wmsStockindtl, Map<String, Object> m);

}
