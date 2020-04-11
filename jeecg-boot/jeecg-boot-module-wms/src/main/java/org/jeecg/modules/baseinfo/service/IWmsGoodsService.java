package org.jeecg.modules.baseinfo.service;

import org.jeecg.modules.baseinfo.entity.WmsGoods;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @Description: 货物管理
 * @Author: jeecg-boot
 * @Date:   2020-02-26
 * @Version: V1.0
 */
public interface IWmsGoodsService extends IService<WmsGoods> {

    /**
     * 查询启用的货物信息,并返回
     * @return
     */
    Map<String, List> queryList();

    /**
     * 根据货物ID 查询货物
     * @param gId
     * @return
     */
    WmsGoods queryByGoodsId(String gId);

    WmsGoods queryByGoodsCode(String gCode);
}
