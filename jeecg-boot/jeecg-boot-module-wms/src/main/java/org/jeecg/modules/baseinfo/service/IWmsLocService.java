package org.jeecg.modules.baseinfo.service;

import org.jeecg.modules.baseinfo.entity.WmsArea;
import org.jeecg.modules.baseinfo.entity.WmsGoods;
import org.jeecg.modules.baseinfo.entity.WmsLoc;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @Description: 货位管理
 * @Author: jeecg-boot
 * @Date:   2020-02-26
 * @Version: V1.0
 */
public interface IWmsLocService extends IService<WmsLoc> {

    /**
     * 查询启用的货位信息,并进行显示
     * @return
     */
    Map<String, List> queryList();

    /**
     * 根据货位类型查询
     * @return
     */
    Map<String, List> queryByTypeVol(String type);

    /**
     *  组装批量添加 的 货位数组
     * @param wmsLoc
     * @return
     */
    List<WmsLoc> getAddBatchWmsLocs(WmsLoc wmsLoc);

    List<WmsLoc> queryByStrategy(WmsArea wmsArea, WmsGoods wmsGoods);

    WmsLoc queryByLocId(String LocId);
}
