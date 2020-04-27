package org.jeecg.modules.stock.service;

import org.jeecg.modules.baseinfo.entity.WmsGoods;
import org.jeecg.modules.stock.entity.WmsStock;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.stockin.entity.WmsStockin;
import org.jeecg.modules.stockout.entity.WmsStockoutdtl;

import java.util.List;
import java.util.Map;

/**
 * @Description: 库存表
 * @Author: jeecg-boot
 * @Date:   2020-03-09
 * @Version: V1.0
 */
public interface IWmsStockService extends IService<WmsStock> {

    /**
     * 根据货位ID 批量查询库存
     * @param idList
     * @return
     */
    Map<String,List<WmsStock>> queryByIdList(List<String> idList, WmsGoods wmsGoods);

    /**
     * 根据货物ID、编码和货物数量 查询库存记录
     * @return
     */
    List<WmsStock> queryByGoods(String id, String code, Integer quantity);

    /**
     * 复制新的 已入库存库记录
     * @param wmsStock
     * @return
     */
    void copyNewNormalStock(WmsStock wmsStock, Integer rest);

    /**
     * 根据出库细单 查询 库存
     * @param wmsStockoutdtl
     * @return
     */
    boolean execStockout(WmsStockoutdtl wmsStockoutdtl, Map<String, Object> m);

    /**
     * 赋值生成拣选记录和库存记录
     * @param wmsStock
     * @param wmsStockoutdtl
     */
    void copyValueToNew(WmsStock wmsStock,WmsStockoutdtl wmsStockoutdtl);

    /**
     * 根据 条件 获取要出库的  库存
     * @param wmsStockoutdtl
     * @param list
     * @return
     */
    boolean getStockOut(WmsStockoutdtl wmsStockoutdtl, List<WmsStock> list, Integer orderId);

    Integer getCountByLocId(String locId);// 根据货位Id 查库存数

    Integer getCountByAreaId(String areaId);// 根据区域Id 查库存数

    WmsStock getByTrayNumber(String trayNumber);

}
