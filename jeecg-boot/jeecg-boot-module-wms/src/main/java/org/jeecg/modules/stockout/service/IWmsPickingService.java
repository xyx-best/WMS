package org.jeecg.modules.stockout.service;

import org.jeecg.modules.stockout.entity.WmsPicking;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 拣选表
 * @Author: jeecg-boot
 * @Date:   2020-03-24
 * @Version: V1.0
 */
public interface IWmsPickingService extends IService<WmsPicking> {

    public void delet(String id);

    /**
     * 根据picking Id 获取拣选记录
     * @param pickingId
     * @return
     */
    public WmsPicking getByPickingId(String pickingId);

    public List<WmsPicking> getByStockoutdtlId(String stockoutdtlId);
}
