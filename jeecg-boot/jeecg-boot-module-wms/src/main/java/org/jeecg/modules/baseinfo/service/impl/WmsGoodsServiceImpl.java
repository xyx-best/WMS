package org.jeecg.modules.baseinfo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.modules.baseinfo.entity.WmsGoods;
import org.jeecg.modules.baseinfo.mapper.WmsGoodsMapper;
import org.jeecg.modules.baseinfo.service.IWmsGoodsService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 货物管理
 * @Author: jeecg-boot
 * @Date:   2020-02-26
 * @Version: V1.0
 */
@Service
public class WmsGoodsServiceImpl extends ServiceImpl<WmsGoodsMapper, WmsGoods> implements IWmsGoodsService {

    @Override
    public Map<String, List> queryList() {
        LambdaQueryWrapper<WmsGoods> query = new LambdaQueryWrapper<WmsGoods>();
        query.eq(WmsGoods::getIsUse, CommonConstant.IS_USE_YES);
        query.orderByAsc(WmsGoods::getGoodsId);
        List<WmsGoods> list = this.list(query);
        // 调用wrapTreeDataToTreeList方法生成树状数据
//        List<WmsWarehouse> listResult = FindsDepartsChildrenUtil.wrapTreeDataToTreeList(list);
        Map<String, List> m = new HashMap<>();
        m.put("list",list);
        return m;
    }

    @Override
    public WmsGoods queryByGoodsId(String gId) {
        QueryWrapper<WmsGoods> qWG = new QueryWrapper<WmsGoods>();
        qWG.eq("goods_id", gId);
        WmsGoods wmsGoods = getOne(qWG);
        if (wmsGoods != null) {
            return wmsGoods;
        }
        return null;
    }

    @Override
    public WmsGoods queryByGoodsCode(String gCode) {
        QueryWrapper<WmsGoods> qWG = new QueryWrapper<WmsGoods>();
        qWG.eq("goods_code", gCode);
        WmsGoods wmsGoods = getOne(qWG);
        if (wmsGoods != null) {
            return wmsGoods;
        }
        return null;
    }
}
