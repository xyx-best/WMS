package org.jeecg.modules.stock.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jeecg.modules.baseinfo.entity.WmsArea;
import org.jeecg.modules.baseinfo.entity.WmsGoods;
import org.jeecg.modules.baseinfo.entity.WmsLoc;
import org.jeecg.modules.baseinfo.service.IWmsAreaService;
import org.jeecg.modules.baseinfo.service.IWmsLocService;
import org.jeecg.modules.sqlutil.sqlutil;
import org.jeecg.modules.stock.entity.WmsStock;
import org.jeecg.modules.stock.mapper.WmsStockMapper;
import org.jeecg.modules.stock.service.IWmsStockService;
import org.jeecg.modules.stockout.entity.WmsPicking;
import org.jeecg.modules.stockout.entity.WmsStockoutdtl;
import org.jeecg.modules.stockout.service.IWmsPickingService;
import org.jeecg.modules.sysmanage.util.IdManageUtil;
import org.jeecg.modules.transaction.entity.WmsTransaction;
import org.jeecg.modules.transaction.service.IWmsTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.*;

/**
 * @Description: 库存表
 * @Author: jeecg-boot
 * @Date:   2020-03-09
 * @Version: V1.0
 */
@Service
public class WmsStockServiceImpl extends ServiceImpl<WmsStockMapper, WmsStock> implements IWmsStockService {

    @Autowired
    private IWmsTransactionService wmsTransactionService;
    @Autowired
    private IWmsPickingService wmsPickingService;
    @Autowired
    private IWmsLocService wmsLocService;
    @Autowired
    private IWmsAreaService wmsAreaService;
    @Autowired
    private sqlutil sqlutil;

    /**
     * 根据货位ID 批量查询库存
     * @param idList
     * @return
     */
    @Override
    public Map<String,List<WmsStock>> queryByIdList(List<String> idList, WmsGoods wmsGoods) {
        LambdaQueryWrapper<WmsStock> query = new LambdaQueryWrapper<WmsStock>();
        Map<String,List<WmsStock>> map = new HashMap<>();
        query.in(WmsStock::getLocId, idList);
        //判断货物 的 上架策略是否包含等级 ， 若包含，则要找到等级相同的 库存
        if (wmsGoods.getRackingStrategy().contains("3")) {
            query.eq(WmsStock::getGoodsLevel, wmsGoods.getGoodsLevel());
            query.orderByAsc(WmsStock::getLocId);
            List<WmsStock> list = this.list(query);
            map.put("same", list);
            LambdaQueryWrapper<WmsStock> query2 = new LambdaQueryWrapper<WmsStock>();
            query2.in(WmsStock::getLocId, idList);
            query2.ne(WmsStock::getGoodsLevel, wmsGoods.getGoodsLevel());
            List<WmsStock> list2 = this.list(query2);
            map.put("dif", list2);
        } else {
            query.orderByAsc(WmsStock::getLocId);
            List<WmsStock> list = this.list(query);
            map.put("same", list);
        }
        return map;
    }

    @Override
    public List<WmsStock> queryByGoods(String id, String code, Integer quantity) {
        LambdaQueryWrapper<WmsStock> query = new LambdaQueryWrapper<WmsStock>();
        query.eq(WmsStock::getGoodsId, id);
        query.eq(WmsStock::getGoodsCode, code);
        query.ge(WmsStock::getGoodsQuantity, quantity);
        query.eq(WmsStock::getStockState, "1");
        query.orderByAsc(WmsStock::getGoodsQuantity,WmsStock::getLocId);
        List<WmsStock> list = this.list(query);
        // 调用wrapTreeDataToTreeList方法生成树状数据
        //List<WmsWarehouse> listResult = FindsDepartsChildrenUtil.wrapTreeDataToTreeList(list);
        if (list.isEmpty()) {
            LambdaQueryWrapper<WmsStock> query2 = new LambdaQueryWrapper<WmsStock>();
            query2.eq(WmsStock::getGoodsId, id);
            query2.eq(WmsStock::getGoodsCode, code);
            query2.eq(WmsStock::getStockState, "1");
            query2.ge(WmsStock::getGoodsQuantity, 0);
            query2.orderByDesc(WmsStock::getGoodsQuantity);
            query2.orderByAsc(WmsStock::getLocId);
            list = this.list(query2);
        }
        return list;
    }

    @Override
    public void copyNewNormalStock(WmsStock wmsStock, Integer rest) {
        WmsStock copyStock = new WmsStock();
        copyStock.setStockId(IdManageUtil.getId("wms_stock"));
        copyStock.setStockState("1");
        copyStock.setAreaId(wmsStock.getAreaId());
        copyStock.setAreaCode(wmsStock.getAreaCode());
        copyStock.setAreaName(wmsStock.getAreaName());
        copyStock.setLocId(wmsStock.getLocId());
        copyStock.setLocCode(wmsStock.getLocCode());
        copyStock.setLocName(wmsStock.getLocName());
        copyStock.setGoodsId(wmsStock.getGoodsId());
        copyStock.setGoodsCode(wmsStock.getGoodsCode());
        copyStock.setGoodsName(wmsStock.getGoodsName());
        copyStock.setGoodsSize(wmsStock.getGoodsSize());
        copyStock.setGoodsUnit(wmsStock.getGoodsUnit());
        copyStock.setGoodsType(wmsStock.getGoodsType());
        copyStock.setGoodsColor(wmsStock.getGoodsColor());
        copyStock.setGoodsBatchnumber(wmsStock.getGoodsBatchnumber());
        copyStock.setGoodsQuantity(rest);
        copyStock.setGoodsLevel(wmsStock.getGoodsLevel());
        copyStock.setStockinTime(wmsStock.getStockinTime());
        save(copyStock);
    }

    //执行出库操作
    @Override
    public boolean execStockout(WmsStockoutdtl wmsStockoutdtl, Map<String, Object> m) {
        LambdaQueryWrapper<WmsStock> query = new LambdaQueryWrapper<WmsStock>();
        //根据传入的 出库明细 筛选 查询库存
        if (!wmsStockoutdtl.getGoodsCode().isEmpty()) {
            query.eq(WmsStock::getGoodsCode, wmsStockoutdtl.getGoodsCode());
        }
        if (wmsStockoutdtl.getGoodsName()!=null) {
            query.eq(WmsStock::getGoodsName, wmsStockoutdtl.getGoodsName());
        }
        if (wmsStockoutdtl.getGoodsSize()!=null) {
            query.eq(WmsStock::getGoodsSize, wmsStockoutdtl.getGoodsSize());
        }
        if (wmsStockoutdtl.getGoodsUnit()!=null) {
            query.eq(WmsStock::getGoodsUnit, wmsStockoutdtl.getGoodsUnit());
        }
        if (wmsStockoutdtl.getGoodsType()!=null) {
            query.eq(WmsStock::getGoodsType, wmsStockoutdtl.getGoodsType());
        }
        if (wmsStockoutdtl.getGoodsColor()!=null) {
            query.eq(WmsStock::getGoodsColor, wmsStockoutdtl.getGoodsColor());
        }
        if (wmsStockoutdtl.getGoodsBatchnumber()!=null) {
            query.eq(WmsStock::getGoodsBatchnumber, wmsStockoutdtl.getGoodsBatchnumber());
        }
        if (wmsStockoutdtl.getGoodsLevel()!=null) {
            query.eq(WmsStock::getGoodsLevel, wmsStockoutdtl.getGoodsLevel());
        }
        query.eq(WmsStock::getStockState, "1");

        LambdaQueryWrapper<WmsStock> query2=query.clone();
        //查询单个库存量大于等于 出库量
        query.ge(WmsStock::getGoodsQuantity, wmsStockoutdtl.getGoodsQuantity());
        query.orderByAsc(WmsStock::getGoodsQuantity);
        query.orderByDesc(WmsStock::getLocId);
        List<WmsStock> list = this.list(query);
        //如果没有单个库存量足够大，则将全部符合条件的库存取出，不管库存量
        if (list.size() <= 0) {
            query2.orderByDesc(WmsStock::getGoodsQuantity,WmsStock::getLocId);
            list = this.list(query2);
        }
        return getStockOut(wmsStockoutdtl, list, (Integer)m.get("order_id"));
    }

    //获取足够的库存 来出库
    @Override
    public boolean getStockOut(WmsStockoutdtl wmsStockoutdtl, List<WmsStock> list, Integer orderId) {
        // 要出库的 库存记录 列表
        List<WmsStock> wmsStocksList = new ArrayList<>();
        //需要出库的总量
        Integer needQuantity = wmsStockoutdtl.getGoodsQuantity();
        //如果最后一个库存需要拆分，rest是用来保存拆分后不用出库的量
        Integer rest = 0;
        //库存相加量
        Integer sum = 0;
        for (WmsStock w : list) {
            sum += w.getGoodsQuantity();
            if ((sum - needQuantity) ==0) {
                wmsStocksList.add(w);
                break;
            } else if ((sum - needQuantity) > 0) {
                rest = sum - needQuantity;
                w.setGoodsQuantity(w.getGoodsQuantity() - rest);
                wmsStocksList.add(w);
                //如果要拆分的库存， 则新增一条 剩余库存量的 库存记录
                copyNewNormalStock(w, rest);
                break;
            }
            wmsStocksList.add(w);
        }
        //如果库存相加量  <   出库总量，则返回存库不足
        if (sum - needQuantity < 0 ) {
            return true;
        }

        for (WmsStock ws : wmsStocksList) {
            //生成拣选单和交易单  并且修改 库存记录的 状态 为待出库
            copyValueToNew(ws, wmsStockoutdtl);
            WmsLoc wmsLoc = wmsLocService.queryByLocId(ws.getLocId());
            if (getCountByLocId(ws.getLocId()) <= 0) {
                //该货位上没有货物，重置上架策略具体内容
                if (wmsLoc.getRackingStrategy().contains("2"))
                    wmsLoc.setRkstraColor("");
                if (wmsLoc.getRackingStrategy().contains("1"))
                    wmsLoc.setRkstraSize("");
                wmsLocService.updateById(wmsLoc);
                if (getCountByAreaId(ws.getAreaId()) <= 0) {
                    //该区域内没有货物，重置上架策略具体内容
                    WmsArea wa = wmsAreaService.queryByAreaId(wmsLoc.getAreaId()).get(0);
                    if (wa.getRackingStrategy().contains("1"))
                        wa.setRkstraSize("");
                    if (wa.getRackingStrategy().contains("2"))
                        wa.setRkstraColor("");
                    wmsAreaService.updateById(wa);
                }
            }

            if (orderId != null)
                sqlutil.saveSimuStockinout("simu_stockout",ws.getAreaCode(), wmsLoc, orderId, ws.getTrayNumber());
//            this.removeById(ws.getId()); //将出库的库存删除
        }
        return false;
    }

    //该货位是否还有其他库存
    @Override
    public Integer getCountByLocId(String locId) {
        LambdaQueryWrapper<WmsStock> query = new LambdaQueryWrapper<WmsStock>();
        query.eq(WmsStock::getLocId, locId);
        query.in(WmsStock::getStockState, "0","1");
        int c = this.count(query);
        return c;
    }

    //该区域是否还有其他库存
    @Override
    public Integer getCountByAreaId(String areaId) {
        LambdaQueryWrapper<WmsStock> query = new LambdaQueryWrapper<WmsStock>();
        query.eq(WmsStock::getAreaId, areaId);
        query.in(WmsStock::getStockState, "0","1");
        int c = this.count(query);
        return c;
    }

    /**
     * 赋值生成拣选记录和库存记录
     *
     * @param wmsStockoutdtl
     * @param wmsStock
     * @param wmsStockoutdtl
     */
    public void copyValueToNew(WmsStock wmsStock,WmsStockoutdtl wmsStockoutdtl) {
        //生成拣选记录
        WmsPicking wmsPicking = new WmsPicking();
        wmsPicking.setPickingId(IdManageUtil.getId("wms_picking"));
        wmsPicking.setPickingCode(IdManageUtil.getCode("wms_picking"));
        wmsPicking.setSourcedtlId(wmsStockoutdtl.getStockoutdtlId());
        wmsPicking.setPickingState("0");
        wmsPicking.setAreaId(wmsStock.getAreaId());
        wmsPicking.setAreaCode(wmsStock.getAreaCode());
        wmsPicking.setAreaName(wmsStock.getAreaName());
        wmsPicking.setLocId(wmsStock.getLocId());
        wmsPicking.setLocCode(wmsStock.getLocCode());
        wmsPicking.setLocName(wmsStock.getLocName());
        wmsPicking.setGoodsId(wmsStock.getGoodsId());
        wmsPicking.setGoodsCode(wmsStock.getGoodsCode());
        wmsPicking.setGoodsName(wmsStock.getGoodsName());
        wmsPicking.setGoodsSize(wmsStock.getGoodsSize());
        wmsPicking.setGoodsUnit(wmsStock.getGoodsUnit());
        wmsPicking.setGoodsType(wmsStock.getGoodsType());
        wmsPicking.setGoodsColor(wmsStock.getGoodsColor());
        wmsPicking.setGoodsBatchnumber(wmsStock.getGoodsBatchnumber());
        wmsPicking.setGoodsQuantity(wmsStock.getGoodsQuantity());
        wmsPicking.setGoodsLevel(wmsStock.getGoodsLevel());
        wmsPickingService.save(wmsPicking);  //  保存拣选记录

        //生成交易记录
        WmsTransaction wmsTransaction = new WmsTransaction();
        wmsTransaction.setTransactionId(IdManageUtil.getId("wms_transaction"));
        wmsTransaction.setTransactionCode(IdManageUtil.getCode("wms_transaction"));
        wmsTransaction.setMoveId(wmsPicking.getPickingId());
        wmsTransaction.setSourceType("1");
        wmsTransaction.setStockId(wmsStock.getStockId());
        wmsTransaction.setAreaId(wmsStock.getAreaId());
        wmsTransaction.setAreaName(wmsStock.getAreaName());
        wmsTransaction.setAreaCode(wmsStock.getAreaCode());
        wmsTransaction.setLocId(wmsStock.getLocId());
        wmsTransaction.setLocName(wmsStock.getLocName());
        wmsTransaction.setLocCode(wmsStock.getLocCode());
        wmsTransaction.setTransactionState("0");
        wmsTransaction.setGoodsId(wmsStock.getGoodsId());
        wmsTransaction.setGoodsCode(wmsStock.getGoodsCode());
        wmsTransaction.setGoodsName(wmsStock.getGoodsName());
        wmsTransaction.setGoodsSize(wmsStock.getGoodsSize());
        wmsTransaction.setGoodsUnit(wmsStock.getGoodsUnit());
        wmsTransaction.setGoodsType(wmsStock.getGoodsType());
        wmsTransaction.setGoodsColor(wmsStock.getGoodsColor());
        wmsTransaction.setGoodsBatchnumber(wmsStock.getGoodsBatchnumber());
        wmsTransaction.setGoodsQuantity(wmsStock.getGoodsQuantity());
        wmsTransaction.setGoodsLevel(wmsStock.getGoodsLevel());
        wmsTransaction.setCreateTime(new Date());
        wmsTransaction.setCreateBy(wmsStock.getCreateBy());
        wmsTransactionService.save(wmsTransaction);

        wmsStock.setStockState("2");
        updateById(wmsStock);
    }

    @Override
    public WmsStock getByTrayNumber(String trayNumber) {
        LambdaQueryWrapper<WmsStock> query = new LambdaQueryWrapper<WmsStock>();
        query.eq(WmsStock::getTrayNumber, trayNumber);
        WmsStock wmsStock = this.list(query).get(0);
        return wmsStock;
    }
}
