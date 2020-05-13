package org.jeecg.modules.stockout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jeecg.modules.stockout.entity.WmsStockoutdtl;
import org.jeecg.modules.stockout.mapper.WmsStockoutdtlMapper;
import org.jeecg.modules.stockout.service.IWmsStockoutdtlService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 出库明细表
 * @Author: jeecg-boot
 * @Date:   2020-03-24
 * @Version: V1.0
 */
@Service
public class WmsStockoutdtlServiceImpl extends ServiceImpl<WmsStockoutdtlMapper, WmsStockoutdtl> implements IWmsStockoutdtlService {
	
	@Autowired
	private WmsStockoutdtlMapper wmsStockoutdtlMapper;
	
	@Override
	public List<WmsStockoutdtl> selectByMainId(String mainId) {
		return wmsStockoutdtlMapper.selectByMainId(mainId);
	}

	@Override
	public List<WmsStockoutdtl> getByStockoutdtlId(String stockoutdtlId) {
		LambdaQueryWrapper<WmsStockoutdtl> query = new LambdaQueryWrapper<WmsStockoutdtl>();
		query.eq(WmsStockoutdtl::getStockoutdtlId, stockoutdtlId);
		List<WmsStockoutdtl> w = this.list(query);
		return w;
	}

	@Override
	public List<Map<String, Object>> queryLastDays() {
		List<Map<String, Object>> mList = wmsStockoutdtlMapper.selectSumLastDays();
		return mList;
	}
}
