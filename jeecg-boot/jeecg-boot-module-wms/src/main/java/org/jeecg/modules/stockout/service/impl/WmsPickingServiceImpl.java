package org.jeecg.modules.stockout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jeecg.modules.stockout.entity.WmsPicking;
import org.jeecg.modules.stockout.mapper.WmsPickingMapper;
import org.jeecg.modules.stockout.service.IWmsPickingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 拣选表
 * @Author: jeecg-boot
 * @Date:   2020-03-24
 * @Version: V1.0
 */
@Service
public class WmsPickingServiceImpl extends ServiceImpl<WmsPickingMapper, WmsPicking> implements IWmsPickingService {
@Autowired
private WmsPickingMapper wmsPickingMapper;
    public void delet(String id) {
//        wmsPickingMapper.deleteByMainId(id);
    }

    @Override
    public WmsPicking getByPickingId(String pickingId) {
        LambdaQueryWrapper<WmsPicking> query = new LambdaQueryWrapper<WmsPicking>();
        query.eq(WmsPicking::getPickingId, pickingId);
        WmsPicking w = this.list(query).get(0);
        return w;
    }

    @Override
    public List<WmsPicking> getByStockoutdtlId(String stockoutdtlId) {
        LambdaQueryWrapper<WmsPicking> query = new LambdaQueryWrapper<WmsPicking>();
        query.eq(WmsPicking::getSourcedtlId, stockoutdtlId);
        List<WmsPicking> wList = this.list(query);
        return wList;
    }
}
