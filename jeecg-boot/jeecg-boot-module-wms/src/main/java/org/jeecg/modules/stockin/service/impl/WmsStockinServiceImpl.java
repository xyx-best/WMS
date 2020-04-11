package org.jeecg.modules.stockin.service.impl;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.baseinfo.entity.WmsArea;
import org.jeecg.modules.baseinfo.entity.WmsGoods;
import org.jeecg.modules.baseinfo.entity.WmsLoc;
import org.jeecg.modules.baseinfo.service.IWmsAreaService;
import org.jeecg.modules.baseinfo.service.IWmsGoodsService;
import org.jeecg.modules.baseinfo.service.IWmsLocService;
import org.jeecg.modules.sqlutil.sqlutil;
import org.jeecg.modules.stock.entity.WmsStock;
import org.jeecg.modules.stock.service.IWmsStockService;
import org.jeecg.modules.stockin.entity.WmsRacking;
import org.jeecg.modules.stockin.entity.WmsStockin;
import org.jeecg.modules.stockin.entity.WmsStockindtl;
import org.jeecg.modules.stockin.mapper.WmsStockindtlMapper;
import org.jeecg.modules.stockin.mapper.WmsStockinMapper;
import org.jeecg.modules.stockin.service.IWmsRackingService;
import org.jeecg.modules.stockin.service.IWmsStockinService;
import org.jeecg.modules.sysmanage.util.IdManageUtil;
import org.jeecg.modules.transaction.entity.WmsTransaction;
import org.jeecg.modules.transaction.service.IWmsTransactionService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.*;
import org.jeecg.modules.sysmanage.service.IIdManageService;

/**
 * @Description: 入库表
 * @Author: jeecg-boot
 * @Date:   2020-03-21
 * @Version: V1.0
 */
@Service
public class WmsStockinServiceImpl extends ServiceImpl<WmsStockinMapper, WmsStockin> implements IWmsStockinService {

	@Autowired
	private WmsStockinMapper wmsStockinMapper;
	@Autowired
	private WmsStockindtlMapper wmsStockindtlMapper;
	@Autowired
	private IIdManageService idManageService;
	@Autowired
	private IWmsLocService wmsLocService;
	@Autowired
	private IWmsRackingService wmsRackingService;
	@Autowired
	private IWmsStockService wmsStockService;
	@Autowired
	private IWmsTransactionService wmsTransactionService;
	@Autowired
	private IWmsAreaService wmsAreaService;
	@Autowired
	private IWmsGoodsService wmsGoodsService;
	@Autowired
	private sqlutil sqlutil;
	
	@Override
	@Transactional
	public void saveMain(WmsStockin wmsStockin, List<WmsStockindtl> wmsStockindtlList) {
		wmsStockinMapper.insert(wmsStockin);
		if(wmsStockindtlList!=null && wmsStockindtlList.size()>0) {
			for(WmsStockindtl entity:wmsStockindtlList) {
				//外键设置
				entity.setStockinId(wmsStockin.getStockinId());
				wmsStockindtlMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(WmsStockin wmsStockin,List<WmsStockindtl> wmsStockindtlList) {
		wmsStockinMapper.updateById(wmsStockin);

		//1.先删除子表数据
		wmsStockindtlMapper.deleteByMainId(wmsStockin.getStockinId());
		
		//2.子表数据重新插入
		if(wmsStockindtlList!=null && wmsStockindtlList.size()>0) {
			for(WmsStockindtl entity:wmsStockindtlList) {
				//外键设置
				entity.setStockindtlId(IdManageUtil.getId("wms_stockindtl"));
				entity.setStockindtlCode(IdManageUtil.getCode("wms_stockindtl"));
				entity.setStockinId(wmsStockin.getStockinId());
				entity.setStockinCode(wmsStockin.getStockinCode());
				wmsStockindtlMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		WmsStockin wmsStockin = getById(id);
		wmsStockindtlMapper.deleteByMainId(wmsStockin.getStockinId());
		wmsStockinMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			WmsStockin wmsStockin = getById(id);
			wmsStockindtlMapper.deleteByMainId(wmsStockin.getStockinId());
			wmsStockinMapper.deleteById(id);
		}
	}

	//执行入库
	@Override
	public Result<?> execStockin(WmsStockindtl wmsStockindtl, Map<String, Object> m){

		//获取对应的货物
		WmsGoods wG = wmsGoodsService.queryByGoodsCode(wmsStockindtl.getGoodsCode());
		//根据上架策略 查询 合适的区域
		List<WmsArea> waList = wmsAreaService.queryByStrategy(wG);
		if (waList.size() == 0) {
			return Result.error("没有合适的区域能够存放货物!");
		} else if (!wmsAreaService.isEnoughSize(waList, wmsStockindtl.getGoodsQuantity())){  // 判断是否能否又足够空间
			return Result.error("没有合适的区域能够存放货物!");
		}

		//剩余需要入库的量
		Integer restGoodsQuantity = wmsStockindtl.getGoodsQuantity();

		//分配一个区域的货位，如果一个区域放不完，则放到第二个区域 ，第三个。。。
		while (restGoodsQuantity >= 0 && waList.size()!=0) {

			//在可用的区域内随机取一个
			Random rand = new Random();
			int index = rand.nextInt(waList.size());
			WmsArea randArea = waList.get(index);
			//如果区域的 上架策略不为空 ， 每次入库都要维护上架策略
			if (!randArea.getRackingStrategy().isEmpty()){
				String[] rs = randArea.getRackingStrategy().split(",");
				for (String a : rs) {
					switch (a) {
						case "1": randArea.setRkstraSize(wG.getGoodsSize());break;
						case "2": randArea.setRkstraColor(wG.getGoodsColor());break;
					}
				}
				wmsAreaService.updateById(randArea);
			}
			//在选定的区域中， 根据上架策略查询货位
			List<WmsLoc> wmsLocList = wmsLocService.queryByStrategy(randArea, wG);

			//提取货位id
			List<String> locIdList = new ArrayList<>();  //货位ID列表
			if (!wmsLocList.isEmpty()) {
				for (WmsLoc w : wmsLocList) {
					if (!locIdList.contains(w.getLocId()))
						locIdList.add(w.getLocId());
				}
			} else {
				return Result.error("没有适合的空闲货位可分配!!!");
			}
			//根据货位ID  查询库存，并计算出货位的空余存放量
			Map<String,List<WmsStock>> wStockMap = wmsStockService.queryByIdList(locIdList, wG);
			//等级相同 或者  没有等级过滤的 库存列表
			List<WmsStock> wmsStockList = wStockMap.get("same");
			//等级不相同的 库存列表
			if (wStockMap.containsKey("dif")) {
				List<String> difStockIdList = new ArrayList<>();
				for (WmsStock s : wStockMap.get("dif")) {
					difStockIdList.add(s.getLocId());   //等级不一样的 LocId 数组
				}
				//将不符合条件的货位剔除掉
				for (WmsLoc w : wmsLocList) {
					if (difStockIdList.contains(w.getLocId())){
						wmsLocList.remove(w);
					}
				}
				if (wmsLocList.isEmpty()) {
					return Result.error("没有适合的空闲货位可分配!!!");
				}
			}
			Map<String, Integer> locVolUsedMap = new HashMap<>();    //已用的库存量
			if (!wmsStockList.isEmpty()) {
				//计算已用的库存量
				for (WmsStock ws : wmsStockList) {
					boolean flag = locVolUsedMap.containsKey(ws.getLocId());
					locVolUsedMap.put(ws.getLocId(), flag ? (locVolUsedMap.get(ws.getLocId()) + ws.getGoodsQuantity()) : ws.getGoodsQuantity());
				}
			} else {
				for (WmsLoc w : wmsLocList) {
					locVolUsedMap.put(w.getLocId(), 0);
				}
			}
			//分配货位
			restGoodsQuantity = distributeLoc(restGoodsQuantity, wmsStockindtl, randArea, wmsLocList, locVolUsedMap, m);
			waList.remove(randArea);
		}
		return Result.ok("生成记录成功！");
	}

	/**
	 * 分配1个/多个货位
	 *
	 * @param wmsStockindtl
	 * @param wLL
	 * @param locVolUsedMap
	 * @return
	 */
	private Integer distributeLoc(Integer allGoodsQuantity, WmsStockindtl wmsStockindtl, WmsArea wmsArea, List<WmsLoc> wLL, Map<String, Integer> locVolUsedMap, Map<String, Object> m) {
		//装载要分配的多个货位
		List<WmsLoc> locs = new ArrayList<>();
		Integer sumVol = 0;
		//总的入库量  allGoodsQuantity
		//用于保存 这次每个货位的入库量
		Map<String, Integer> locRestVol = new HashMap<>();
		//剩余的入库量（即还没有入库的货物量）
		Integer rest = allGoodsQuantity;
		List<WmsLoc> wmsLocList = wLL;
		while(sumVol<allGoodsQuantity&&wmsLocList.size()>=0){
			Random random = new Random();
			WmsLoc wl = wmsLocList.get(random.nextInt(wmsLocList.size()));
			//只要多个货位空闲的大于要入库的货物量，则视为能入库
			Integer free = wl.getLocVolume() - (locVolUsedMap.containsKey(wl.getLocId()) ? locVolUsedMap.get(wl.getLocId()) : 0);
			if (free == 0) continue;
			if (free >= rest) {
				free = rest;
			}
			sumVol += free;
			locRestVol.put(wl.getLocId(), free);
			locs.add(wl);
			if (sumVol >= allGoodsQuantity) {
				break;
			} else {
				rest = allGoodsQuantity - sumVol;
			}
			wmsLocList.remove(wl);
		}

		//赋值 添加多条上架记录和库存记录
		for (WmsLoc wmsLoc : locs) {
			copyValueToNew(wmsStockindtl, wmsLoc, wmsArea, locRestVol.get(wmsLoc.getLocId()), m);
			if (!wmsLoc.getRackingStrategy().isEmpty()){
				String[] rs = wmsLoc.getRackingStrategy().split(",");
				for (String a : rs) {
					switch (a) {
						case "1": wmsLoc.setRkstraSize(wmsStockindtl.getGoodsSize());break;
						case "2": wmsLoc.setRkstraColor(wmsStockindtl.getGoodsColor());break;
					}
				}
				wmsLocService.updateById(wmsLoc);
			}
		}

		if (sumVol < allGoodsQuantity) {
			return rest;
		}
		return -1;
	}

	/**
	 * 赋值生成上架记录和库存记录
	 *
	 * @param wmsStockindtl
	 * @param wmsLoc
	 * @param wmsArea
	 * @param locRestVol
	 */
	private void copyValueToNew(WmsStockindtl wmsStockindtl, WmsLoc wmsLoc, WmsArea wmsArea, Integer locRestVol, Map<String, Object> m) {
		//生成上架记录
		WmsRacking wmsRacking = new WmsRacking();
		wmsRacking.setRackingId(idManageService.getCurIdByTableName("wms_racking"));
		wmsRacking.setRackingCode(idManageService.getCodeByTableName("wms_racking"));
		wmsRacking.setSourcedtlId(wmsStockindtl.getStockindtlId());
		wmsRacking.setRackingState("0");
		wmsRacking.setAreaId(wmsArea.getAreaId());
		wmsRacking.setAreaCode(wmsArea.getAreaCode());
		wmsRacking.setAreaName(wmsArea.getAreaName());
		wmsRacking.setLocId(wmsLoc.getLocId());
		wmsRacking.setLocCode(wmsLoc.getLocCode());
		wmsRacking.setLocName(wmsLoc.getLocName());
		wmsRacking.setGoodsId(wmsStockindtl.getGoodsId());
		wmsRacking.setGoodsCode(wmsStockindtl.getGoodsCode());
		wmsRacking.setGoodsName(wmsStockindtl.getGoodsName());
		wmsRacking.setGoodsSize(wmsStockindtl.getGoodsSize());
		wmsRacking.setGoodsUnit(wmsStockindtl.getGoodsUnit());
		wmsRacking.setGoodsType(wmsStockindtl.getGoodsType());
		wmsRacking.setGoodsColor(wmsStockindtl.getGoodsColor());
		wmsRacking.setGoodsBatchnumber(wmsStockindtl.getGoodsBatchnumber());
		wmsRacking.setGoodsQuantity(locRestVol);
		wmsRacking.setGoodsLevel(wmsStockindtl.getGoodsLevel());
		wmsRackingService.save(wmsRacking);  //  保存上架记录
		WmsStock wmsStock = createWmsStock(wmsStockindtl, wmsLoc, wmsArea, locRestVol, (String)m.get("tray_number"));

		if (m.containsKey("order_id")){
			sqlutil.saveSimuStockinout("simu_stockin", wmsArea.getAreaCode(), wmsLoc, (Integer) m.get("order_id"), (String)m.get("tray_number"));
			wmsStock.setStockState("1");   //改变库存状态 为 已入库
			wmsStockService.updateById(wmsStock);
		}

		//生成交易记录
		createTransaction(wmsStockindtl, wmsRacking, wmsStock);
	}

	private WmsStock createWmsStock(WmsStockindtl wmsStockindtl, WmsLoc wmsLoc, WmsArea wmsArea, Integer locRestVol, String trayNumber) {
		//生成库存记录
		WmsStock wmsStock = new WmsStock();
		wmsStock.setStockId(idManageService.getCurIdByTableName("wms_stock"));
		wmsStock.setStockState("0");
		wmsStock.setAreaId(wmsArea.getAreaId());
		wmsStock.setAreaCode(wmsArea.getAreaCode());
		wmsStock.setAreaName(wmsArea.getAreaName());
		wmsStock.setLocId(wmsLoc.getLocId());
		wmsStock.setLocCode(wmsLoc.getLocCode());
		wmsStock.setLocName(wmsLoc.getLocName());
		wmsStock.setGoodsId(wmsStockindtl.getGoodsId());
		wmsStock.setGoodsCode(wmsStockindtl.getGoodsCode());
		wmsStock.setGoodsName(wmsStockindtl.getGoodsName());
		wmsStock.setGoodsSize(wmsStockindtl.getGoodsSize());
		wmsStock.setGoodsUnit(wmsStockindtl.getGoodsUnit());
		wmsStock.setGoodsType(wmsStockindtl.getGoodsType());
		wmsStock.setGoodsColor(wmsStockindtl.getGoodsColor());
		wmsStock.setGoodsBatchnumber(wmsStockindtl.getGoodsBatchnumber());
		wmsStock.setGoodsQuantity(locRestVol);
		wmsStock.setGoodsLevel(wmsStockindtl.getGoodsLevel());
		wmsStock.setTrayNumber(trayNumber);
		wmsStockService.save(wmsStock);
		return wmsStock;
	}

	public void createTransaction(WmsStockindtl wmsStockindtl, WmsRacking wmsRacking, WmsStock wmsStock) {
		//生成交易记录
		WmsTransaction wmsTransaction = new WmsTransaction();
		wmsTransaction.setTransactionId(idManageService.getCurIdByTableName("wms_transaction"));
		wmsTransaction.setTransactionCode(idManageService.getCodeByTableName("wms_transaction"));
		wmsTransaction.setMoveId(wmsRacking.getRackingId());
		wmsTransaction.setSourceType("0");
		wmsTransaction.setStockId(wmsStock.getStockId());
		wmsTransaction.setAreaId(wmsRacking.getAreaId());
		wmsTransaction.setAreaName(wmsRacking.getAreaName());
		wmsTransaction.setAreaCode(wmsRacking.getAreaCode());
		wmsTransaction.setLocId(wmsRacking.getLocId());
		wmsTransaction.setLocName(wmsRacking.getLocName());
		wmsTransaction.setLocCode(wmsRacking.getLocCode());
		wmsTransaction.setTransactionState("0");
		wmsTransaction.setGoodsId(wmsStockindtl.getGoodsId());
		wmsTransaction.setGoodsCode(wmsStockindtl.getGoodsCode());
		wmsTransaction.setGoodsName(wmsStockindtl.getGoodsName());
		wmsTransaction.setGoodsSize(wmsStockindtl.getGoodsSize());
		wmsTransaction.setGoodsUnit(wmsStockindtl.getGoodsUnit());
		wmsTransaction.setGoodsType(wmsStockindtl.getGoodsType());
		wmsTransaction.setGoodsColor(wmsStockindtl.getGoodsColor());
		wmsTransaction.setGoodsBatchnumber(wmsStockindtl.getGoodsBatchnumber());
		wmsTransaction.setGoodsQuantity(wmsRacking.getGoodsQuantity());
		wmsTransaction.setGoodsLevel(wmsStockindtl.getGoodsLevel());
		wmsTransaction.setCreateTime(new Date());
		wmsTransaction.setCreateBy(wmsStockindtl.getCreateBy());
		wmsTransactionService.save(wmsTransaction);
	}

}
