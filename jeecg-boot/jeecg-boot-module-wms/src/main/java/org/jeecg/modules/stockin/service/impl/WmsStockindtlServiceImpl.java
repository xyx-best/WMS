package org.jeecg.modules.stockin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.collections.map.HashedMap;
import org.jeecg.modules.stockin.entity.WmsStockindtl;
import org.jeecg.modules.stockin.mapper.WmsStockindtlMapper;
import org.jeecg.modules.stockin.service.IWmsStockindtlService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 入库明细表
 * @Author: jeecg-boot
 * @Date:   2020-03-21
 * @Version: V1.0
 */
@Service
public class WmsStockindtlServiceImpl extends ServiceImpl<WmsStockindtlMapper, WmsStockindtl> implements IWmsStockindtlService {
	
	@Autowired
	private WmsStockindtlMapper wmsStockindtlMapper;
	
	@Override
	public List<WmsStockindtl> selectByMainId(String mainId) {
		return wmsStockindtlMapper.selectByMainId(mainId);
	}

	@Override
	public List<WmsStockindtl> getByStockin(String id, String code) {
		LambdaQueryWrapper<WmsStockindtl> query = new LambdaQueryWrapper<WmsStockindtl>();
		query.eq(WmsStockindtl::getStockinId, id);
		query.eq(WmsStockindtl::getStockinCode, code);
		List<WmsStockindtl> list = this.list(query);
		// 调用wrapTreeDataToTreeList方法生成树状数据
		//List<WmsWarehouse> listResult = FindsDepartsChildrenUtil.wrapTreeDataToTreeList(list);
		return list;
	}

	@Override
	public WmsStockindtl getByStockindtlId(String stockindtlId) {
		LambdaQueryWrapper<WmsStockindtl> query = new LambdaQueryWrapper<WmsStockindtl>();
		query.eq(WmsStockindtl::getStockindtlId, stockindtlId);
		WmsStockindtl w = this.list(query).get(0);
		return w;
	}

	@Override
	public List<Map<String, Object>> queryLastDays() {
		List<Map<String, Object>> mList = wmsStockindtlMapper.selectSumLastDays();
//		int sum = 0;
//		Map<String, Integer> result = new HashedMap();
//		for (Map<String, Object> m : mList) {
//			result.put((String) m.get("date"), Integer.parseInt(m.get("quantity").toString()));
//		}
		return mList;
	}
}
