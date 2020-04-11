package org.jeecg.modules.stockout.service;

import org.jeecg.modules.stockout.entity.WmsPicking;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 拣选表
 * @Author: jeecg-boot
 * @Date:   2020-03-24
 * @Version: V1.0
 */
public interface IWmsPickingService extends IService<WmsPicking> {
    public void delet(String id);
}
