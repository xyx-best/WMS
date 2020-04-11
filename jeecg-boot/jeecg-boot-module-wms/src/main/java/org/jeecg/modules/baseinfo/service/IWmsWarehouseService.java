package org.jeecg.modules.baseinfo.service;

import org.jeecg.modules.baseinfo.entity.WmsWarehouse;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @Description: 仓库管理
 * @Author: jeecg-boot
 * @Date:   2020-02-26
 * @Version: V1.0
 */
public interface IWmsWarehouseService extends IService<WmsWarehouse> {

    /**
     * 查询所有仓库信息,并进行显示
     * @return
     */
    Map<String, List> queryList();
}
