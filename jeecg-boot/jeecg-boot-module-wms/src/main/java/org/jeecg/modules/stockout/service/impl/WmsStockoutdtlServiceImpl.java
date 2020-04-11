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
	public Map<String, List> queryByStockout(String id, String code) {
		LambdaQueryWrapper<WmsStockoutdtl> query = new LambdaQueryWrapper<WmsStockoutdtl>();
		query.eq(WmsStockoutdtl::getStockoutId, id);
		query.eq(WmsStockoutdtl::getStockoutCode, code);
		List<WmsStockoutdtl> list = this.list(query);
		// 调用wrapTreeDataToTreeList方法生成树状数据
		//List<WmsWarehouse> listResult = FindsDepartsChildrenUtil.wrapTreeDataToTreeList(list);
		Map<String, List> m = new HashMap<>();
		m.put("list",list);
		return m;
	}
}
