package org.jeecg.modules.stockin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.modules.stockin.entity.WmsRacking;
import org.jeecg.modules.stockin.mapper.WmsRackingMapper;
import org.jeecg.modules.stockin.service.IWmsRackingService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 上架表
 * @Author: jeecg-boot
 * @Date:   2020-03-10
 * @Version: V1.0
 */
@Service
public class WmsRackingServiceImpl extends ServiceImpl<WmsRackingMapper, WmsRacking> implements IWmsRackingService {

    @Override
    public List<WmsRacking> getByStockindtlId(String stockindtlId) {
        QueryWrapper<WmsRacking> qWR = new QueryWrapper<WmsRacking>();
        qWR.eq("sourcedtl_id", stockindtlId);
        List<WmsRacking> wmsRkList = this.list(qWR);
        return wmsRkList;
    }

    @Override
    public WmsRacking getByRackingId(String rackingId) {
        LambdaQueryWrapper<WmsRacking> qWR = new LambdaQueryWrapper<WmsRacking>();
        qWR.eq(WmsRacking::getRackingId, rackingId);
        WmsRacking wmsRk = this.list(qWR).get(0);
        return wmsRk;
    }
}
