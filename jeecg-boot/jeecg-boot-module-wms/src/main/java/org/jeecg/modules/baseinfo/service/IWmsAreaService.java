package org.jeecg.modules.baseinfo.service;

import org.jeecg.modules.baseinfo.entity.WmsArea;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.baseinfo.entity.WmsGoods;
import org.jeecg.modules.baseinfo.entity.WmsLoc;

import java.util.List;
import java.util.Map;

/**
 * @Description: 区域管理
 * @Author: jeecg-boot
 * @Date:   2020-02-26
 * @Version: V1.0
 */
public interface IWmsAreaService extends IService<WmsArea> {

    /**
     * 查询启用的区域信息,并进行显示
     * @return
     */
    Map<String, List> queryList();


    /**
     * 根据 areaId查询区域
     *
     * @return
     */
    List<WmsArea> queryByAreaId(String areaId);

    /**
     * 根据 areaId查询区域
     *
     * @return
     */
    List<WmsArea> queryByIdList(List<WmsLoc> wmsLocs);

    /**
     * 根据 货物上架策略查找对应的区域
     * @param wmsGoods
     * @return
     */
    List<WmsArea> queryByStrategy(WmsGoods wmsGoods);

    /**
     * 根据区域列表 和 要 比较的数量， 返回区域列表里的
     * @param wmsAreaList
     * @param allQuantity
     * @return
     */
    boolean isEnoughSize(List<WmsArea> wmsAreaList, Integer allQuantity);
}
