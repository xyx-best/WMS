package org.jeecg.modules.baseinfo.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.modules.baseinfo.entity.WmsArea;
import org.jeecg.modules.baseinfo.entity.WmsGoods;
import org.jeecg.modules.baseinfo.entity.WmsLoc;
import org.jeecg.modules.baseinfo.mapper.WmsAreaMapper;
import org.jeecg.modules.baseinfo.mapper.WmsGoodsCategoryMapper;
import org.jeecg.modules.baseinfo.service.IWmsAreaService;
import org.jeecg.modules.baseinfo.service.IWmsGoodsCategoryService;
import org.jeecg.modules.stock.mapper.WmsStockMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.*;

/**
 * @Description: 区域管理
 * @Author: jeecg-boot
 * @Date: 2020-02-26
 * @Version: V1.0
 */
@Service
public class WmsAreaServiceImpl extends ServiceImpl<WmsAreaMapper, WmsArea> implements IWmsAreaService {

    @Autowired
    private IWmsGoodsCategoryService wmsGoodsCategoryService;
    @Autowired
    private WmsAreaMapper wmsAreaMapper;
    @Autowired
    private WmsStockMapper wmsStockMapper;

    @Override
    public Map<String, List> queryList() {
        LambdaQueryWrapper<WmsArea> query = new LambdaQueryWrapper<WmsArea>();
        query.eq(WmsArea::getAreaState, CommonConstant.IS_USE_YES);
        query.orderByAsc(WmsArea::getAreaCode);
        List<WmsArea> list = this.list(query);
        // 调用wrapTreeDataToTreeList方法生成树状数据
//        List<WmsWarehouse> listResult = FindsDepartsChildrenUtil.wrapTreeDataToTreeList(list);
        Map<String, List> m = new HashMap<>();
        m.put("list", list);
        return m;
    }

    /**
     * 根据 areaId查询区域
     *
     * @return
     */
    @Override
    public List<WmsArea> queryByAreaId(String areaId) {
        LambdaQueryWrapper<WmsArea> query = new LambdaQueryWrapper<WmsArea>();
        query.eq(WmsArea::getAreaId, areaId);
        List<WmsArea> list = this.list(query);
        // 调用wrapTreeDataToTreeList方法生成树状数据
        //List<WmsWarehouse> listResult = FindsDepartsChildrenUtil.wrapTreeDataToTreeList(list);
        return list;
    }

    @Override
    public List<WmsArea> queryByIdList(List<WmsLoc> wmsLocs) {
        LambdaQueryWrapper<WmsArea> query = new LambdaQueryWrapper<WmsArea>();
        List<String> idList = new ArrayList<>();
        for (WmsLoc l : wmsLocs) {
            idList.add(l.getAreaId());
        }
        query.in(WmsArea::getAreaId, idList);
        List<WmsArea> list = this.list(query);
        return list;
    }

    @Override
    public List<WmsArea> queryByStrategy(WmsGoods wmsGoods) {
        LambdaQueryWrapper<WmsArea> query = Wrappers.lambdaQuery(new WmsArea());
        //查询条件： 启用状态的区域
        query.eq(WmsArea::getAreaState, "1");

        //上架策略 不包含类别 / 包含类别且（类别为空或类别同一系列）
        query.and(l -> l.notLike(WmsArea::getRackingStrategy, "0")
                .or(i -> i.like(WmsArea::getRackingStrategy, "0")
                        .and(k -> k.in(WmsArea::getAreaKind, wmsGoodsCategoryService.queryParentByChild(wmsGoods.getGoodsType()))
                                .or(j -> j.isNull(WmsArea::getAreaKind)))));

        //上架策略 不包含规格 / 包含规格且（规格为空或规格相同）
        query.and(l -> l.notLike(WmsArea::getRackingStrategy, "1")
                .or(i -> i.like(WmsArea::getRackingStrategy, "1")
                        .and(k -> k.in(WmsArea::getRkstraSize, wmsGoods.getGoodsSize(),"")
                                .or(j -> j.isNull(WmsArea::getRkstraSize)))));

        //上架策略 不包含花色 / 包含花色且（花色为空或花色相同）
        query.and(l -> l.notLike(WmsArea::getRackingStrategy, "2")
                .or(i -> i.like(WmsArea::getRackingStrategy, "2")
                        .and(k -> k.in(WmsArea::getRkstraColor, wmsGoods.getGoodsColor(),"")
                                .or(j -> j.isNull(WmsArea::getRkstraColor)))));

        query.orderByAsc(WmsArea::getAreaId);
        List<WmsArea> list = this.list(query);
        //没有上架策略的区域
        LambdaQueryWrapper<WmsArea> query2 = new LambdaQueryWrapper<WmsArea>();
        query2.isNull(WmsArea::getRackingStrategy);
        query2.eq(WmsArea::getAreaState, "1");
        List<WmsArea> list2 = this.list(query2);
        list.addAll(list2);
        return list;
    }

    @Override
    public boolean isEnoughSize(List<WmsArea> wmsAreaList, Integer allQuantity){
        List<String> s = new ArrayList<>();
        for (WmsArea a : wmsAreaList) {
            s.add(a.getAreaId());
        }
        //获取区域列表的大小总和
        Integer sumSize = wmsAreaMapper.selectSumAreaSize(s);
        //获取区域列表的 库存总和
        Integer sumStock = wmsStockMapper.selectSumStock(s);
        //全部区域 的可用空闲量=区域总大小 - 已用的库存总和
        Integer sumRest = (sumSize==null?0:sumSize) - (sumStock==null?0:sumStock);

        return sumRest >= allQuantity;
    }

}
