package org.jeecg.modules.stockout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jeecg.modules.stockout.entity.WmsStockout;
import org.jeecg.modules.stockout.entity.WmsStockoutdtl;
import org.jeecg.modules.stockout.mapper.WmsStockoutdtlMapper;
import org.jeecg.modules.stockout.mapper.WmsStockoutMapper;
import org.jeecg.modules.stockout.service.IWmsStockoutService;
import org.jeecg.modules.sysmanage.util.IdManageUtil;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 出库总表
 * @Author: jeecg-boot
 * @Date:   2020-03-24
 * @Version: V1.0
 */
@Service
public class WmsStockoutServiceImpl extends ServiceImpl<WmsStockoutMapper, WmsStockout> implements IWmsStockoutService {

	@Autowired
	private WmsStockoutMapper wmsStockoutMapper;
	@Autowired
	private WmsStockoutdtlMapper wmsStockoutdtlMapper;
	
	@Override
	@Transactional
	public void saveMain(WmsStockout wmsStockout, List<WmsStockoutdtl> wmsStockoutdtlList) {
		wmsStockoutMapper.insert(wmsStockout);
		if(wmsStockoutdtlList!=null && wmsStockoutdtlList.size()>0) {
			for(WmsStockoutdtl entity:wmsStockoutdtlList) {
				//外键设置
				entity.setStockoutId(wmsStockout.getStockoutId());
				wmsStockoutdtlMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(WmsStockout wmsStockout,List<WmsStockoutdtl> wmsStockoutdtlList) {
		wmsStockoutMapper.updateById(wmsStockout);
		
		//1.先删除子表数据
		wmsStockoutdtlMapper.deleteByMainId(wmsStockout.getStockoutId());
		
		//2.子表数据重新插入
		if(wmsStockoutdtlList!=null && wmsStockoutdtlList.size()>0) {
			for(WmsStockoutdtl entity:wmsStockoutdtlList) {
				//外键设置
				entity.setStockoutdtlId(IdManageUtil.getId("wms_stockoutdtl"));
				entity.setStockoutdtlCode(IdManageUtil.getCode("wms_stockoutdtl"));
				entity.setStockoutId(wmsStockout.getStockoutId());
				entity.setStockoutCode(wmsStockout.getStockoutCode());
				wmsStockoutdtlMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		WmsStockout wmsStockout = getById(id);
		wmsStockoutdtlMapper.deleteByMainId(wmsStockout.getStockoutId());
		wmsStockoutMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			WmsStockout wmsStockout = getById(id);
			wmsStockoutdtlMapper.deleteByMainId(wmsStockout.getStockoutId());
			wmsStockoutMapper.deleteById(id);
		}
	}

	@Override
	public WmsStockout getByStockoutId(String stockoutId) {
		LambdaQueryWrapper<WmsStockout> query = new LambdaQueryWrapper<WmsStockout>();
		query.eq(WmsStockout::getStockoutId, stockoutId);
		WmsStockout w = this.list(query).get(0);
		return w;
	}

}
