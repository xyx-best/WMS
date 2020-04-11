package org.jeecg.modules.baseinfo.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.modules.baseinfo.entity.WmsArea;
import org.jeecg.modules.baseinfo.entity.WmsGoods;
import org.jeecg.modules.baseinfo.entity.WmsLoc;
import org.jeecg.modules.baseinfo.mapper.WmsLocMapper;
import org.jeecg.modules.baseinfo.service.IWmsGoodsCategoryService;
import org.jeecg.modules.baseinfo.service.IWmsLocService;
import org.jeecg.modules.sysmanage.util.IdManageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.*;

/**
 * @Description: 货位管理
 * @Author: jeecg-boot
 * @Date:   2020-02-26
 * @Version: V1.0
 */
@Service
public class WmsLocServiceImpl extends ServiceImpl<WmsLocMapper, WmsLoc> implements IWmsLocService {

    @Autowired
    private IWmsGoodsCategoryService wmsGoodsCategoryService;

    @Override
    public Map<String, List> queryList() {
        LambdaQueryWrapper<WmsLoc> query = new LambdaQueryWrapper<WmsLoc>();
        query.eq(WmsLoc::getIsUse, CommonConstant.IS_USE_YES);
        query.orderByAsc(WmsLoc::getLocId);
        List<WmsLoc> list = this.list(query);
        // 调用wrapTreeDataToTreeList方法生成树状数据
//        List<WmsWarehouse> listResult = FindsDepartsChildrenUtil.wrapTreeDataToTreeList(list);
        Map<String, List> m = new HashMap<>();
        m.put("list",list);
        return m;
    }

    /**
     * 根据货位类型查询
     * @return
     */
    @Override
    public Map<String, List> queryByTypeVol(String type) {
        LambdaQueryWrapper<WmsLoc> query = new LambdaQueryWrapper<WmsLoc>();
        query.eq(WmsLoc::getIsUse, CommonConstant.IS_USE_YES);
        query.eq(WmsLoc::getLocState, "1");
//        query.ge(WmsLoc::getLocVolume, volume);
//        query.eq(WmsLoc::getLocType, type).or(i -> i.eq(WmsLoc::getIsMix, "1").eq(WmsLoc::getLocType, "0000000000"));
        query.eq(WmsLoc::getLocType, type).or(i -> i.eq(WmsLoc::getLocType, "0000000000"));
        query.orderByAsc(WmsLoc::getLocId);
        List<WmsLoc> list = this.list(query);
        // 调用wrapTreeDataToTreeList方法生成树状数据
        //List<WmsWarehouse> listResult = FindsDepartsChildrenUtil.wrapTreeDataToTreeList(list);
        Map m = new HashMap();
        m.put("list",list);
        return m;
    }


    public List<WmsLoc> getAddBatchWmsLocs(WmsLoc wmsLoc) {
        //获取区域ID后两位
        String areaId = wmsLoc.getAreaId().substring(8,10);
        String[] locs = wmsLoc.getLocLoc().split("-");
        String side = locs[0];  //L/R 左右
        Integer row = Integer.valueOf(locs[1]);   //行号
        Integer col = Integer.valueOf(locs[2]);  //列号
        List<WmsLoc> wmsLocs = new ArrayList<>();
        for (int r = 1; r <= row; r++) {
            for (int c = 1; c <= col; c++) {
                WmsLoc l = new WmsLoc();
                l.setLocId(IdManageUtil.getId("wms_loc"));
                l.setLocName(areaId + l.getLocId().substring(5) + "号货位");
                l.setLocCode(areaId + l.getLocId().substring(5)+"");
                l.setLocLoc(side+"-"+r+"-"+c);
                l.setLocVolume(wmsLoc.getLocVolume() == null ? 0 : wmsLoc.getLocVolume());
                l.setAreaId(wmsLoc.getAreaId());
                l.setIsMix(wmsLoc.getIsMix() == null ? "0" : wmsLoc.getIsMix());
                l.setLocType(wmsLoc.getLocType());
                l.setLocState(wmsLoc.getLocState());
                l.setIsUse(wmsLoc.getIsUse());
                l.setMemo(wmsLoc.getMemo() == null?"":wmsLoc.getMemo());
                l.setRackingStrategy(wmsLoc.getRackingStrategy());
                wmsLocs.add(l);
            }
        }
        return wmsLocs;
    }

    @Override
    public List<WmsLoc> queryByStrategy(WmsArea wmsArea, WmsGoods wmsGoods) {
        LambdaQueryWrapper<WmsLoc> query = new LambdaQueryWrapper<WmsLoc>();
        // 条件  指定区域
        query.eq(WmsLoc::getAreaId, wmsArea.getAreaId());
        //条件 类别的上架策略
        query.and(l -> l.notLike(WmsLoc::getRackingStrategy, "0")
                .or(i -> i.like(WmsLoc::getRackingStrategy, "0")
                        .and(k -> k.in(WmsLoc::getLocType, wmsGoodsCategoryService.queryParentByChild(wmsGoods.getGoodsType()))
                                .or(j -> j.isNull(WmsLoc::getLocType)))));
        //条件 规格的上架策略
        query.and(l -> l.notLike(WmsLoc::getRackingStrategy, "1")
                .or(i -> i.like(WmsLoc::getRackingStrategy, "1")
                        .and(k -> k.in(WmsLoc::getRkstraSize, wmsGoods.getGoodsSize(),"")
                                .or(j -> j.isNull(WmsLoc::getRkstraSize)))));
        //条件 花色的上架策略
        query.and(l -> l.notLike(WmsLoc::getRackingStrategy, "2")
                .or(i -> i.like(WmsLoc::getRackingStrategy, "2")
                        .and(k -> k.in(WmsLoc::getRkstraColor, wmsGoods.getGoodsColor(),"")
                                .or(j -> j.isNull(WmsLoc::getRkstraColor)))));

        query.orderByDesc(WmsLoc::getLocVolume);
        List<WmsLoc> list = this.list(query);
        return list;
    }

    @Override
    public WmsLoc queryByLocId(String LocId) {
        LambdaQueryWrapper<WmsLoc> qWG = new LambdaQueryWrapper<WmsLoc>();
        qWG.eq(WmsLoc::getLocId, LocId);
        WmsLoc wmsLoc = getOne(qWG);
        return wmsLoc;
    }
}
