package org.jeecg.modules.stockout.service.impl;

import org.jeecg.modules.stockout.entity.WmsPicking;
import org.jeecg.modules.stockout.mapper.WmsPickingMapper;
import org.jeecg.modules.stockout.service.IWmsPickingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

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
}
