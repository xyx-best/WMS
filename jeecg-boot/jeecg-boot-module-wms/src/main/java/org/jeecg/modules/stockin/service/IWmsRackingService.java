package org.jeecg.modules.stockin.service;

import org.jeecg.modules.stockin.entity.WmsRacking;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 上架表
 * @Author: jeecg-boot
 * @Date:   2020-03-10
 * @Version: V1.0
 */
public interface IWmsRackingService extends IService<WmsRacking> {

    public List<WmsRacking> getByStockindtlId(String stockindtlId);

    /**
     * 根据上架ID 获取 上架记录
     * @param rackingId
     * @return
     */
    public WmsRacking getByRackingId(String rackingId);
}
