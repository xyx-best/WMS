package org.jeecg.modules.stockout.job;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.sqlutil.sqlutil;
import org.jeecg.modules.stock.service.IWmsStockService;
import org.jeecg.modules.stockout.entity.WmsPicking;
import org.jeecg.modules.stockout.entity.WmsStockout;
import org.jeecg.modules.stockout.entity.WmsStockoutdtl;
import org.jeecg.modules.stockout.service.IWmsPickingService;
import org.jeecg.modules.stockout.service.IWmsStockoutService;
import org.jeecg.modules.stockout.service.IWmsStockoutdtlService;
import org.jeecg.modules.sysmanage.util.IdManageUtil;
import org.jeecg.modules.transaction.entity.WmsTransaction;
import org.jeecg.modules.transaction.entity.WmsTransactionHis;
import org.jeecg.modules.transaction.service.IWmsTransactionHisService;
import org.jeecg.modules.transaction.service.IWmsTransactionService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 监听跟仿真系统对接的 SQL server 数据库，来分配 出库的货位
 */
@Slf4j
public class MonitorStockout implements Job {

    @Autowired
    private IWmsStockoutdtlService wmsStockoutdtlService;
    @Autowired
    private IWmsStockoutService wmsStockoutService;
    @Autowired
    private IWmsStockService wmsStockService;
    @Autowired
    private IWmsPickingService wmsPickingService;
    @Autowired
    private IWmsTransactionService wmsTransactionService;
    @Autowired
    private IWmsTransactionHisService wmsTransactionHisService;
    @Autowired
    private sqlutil sqlutil;


    @Override
    @DS("master")
    @GetMapping(value = "/list")
    public void execute(JobExecutionContext context) throws JobExecutionException {

        //获取出库信息 分配货位
        List<Map<String, Object>> wmsout = sqlutil.getInfoBySqlWithOrderId("select * from wms_stockout", "wms_stockout");
        if (!wmsout.isEmpty()) {
            for (Map<String, Object> m : wmsout) {
                if (Integer.parseInt(m.get("has_read").toString()) == 1) {
                    //组装 总表记录
                    WmsStockout wmsStockout = wmsStockoutService.getByStockoutCode((String) m.get("stockout_code"));
                    //组装明细记录
                    WmsStockoutdtl wmsStockoutdtl = wmsStockoutdtlService.selectByMainId(wmsStockout.getStockoutId()).get(0);
                    startStockOut(m, wmsStockout, wmsStockoutdtl);

                } else {
                    //组装 总表记录
                    WmsStockout wmsStockout = getWmsStockout(m);
                    sqlutil.execute("update wms_stockout set stockout_code = '" + wmsStockout.getStockoutCode() + "' , has_read = 1 where order_id =  " + m.get("order_id"));

                    //组装明细记录
                    WmsStockoutdtl wmsStockoutdtl = getWmsStockoutdtl(m, wmsStockout);
                    startStockOut(m, wmsStockout, wmsStockoutdtl);
                }
            }
        }
    }

    private void startStockOut(Map<String, Object> m, WmsStockout wmsStockout, WmsStockoutdtl wmsStockoutdtl) {
        //执行出库
        if (wmsStockService.execStockout(wmsStockoutdtl, m)) {
//            if (m.get("stockout_code") == null) {
//                sqlutil.execute("update wms_stockout set stockout_code = '" + wmsStockout.getStockoutCode() + "' where order_id =  " + m.get("order_id"));
//            }
            return;
        }

        //删除simu_stockout的记录
        sqlutil.delAndInsWms("wms_stockout", m);

        //更新出库总表状态
        wmsStockout.setStockoutState("2");
        wmsStockoutService.updateById(wmsStockout);

        //更新出库细表状态
        wmsStockoutdtl.setStockoutState("2");
        wmsStockoutdtlService.updateById(wmsStockoutdtl);
    }

    /**
     * 组装总表记录 出库的
     *
     * @param m
     * @return
     */
    private WmsStockout getWmsStockout(Map<String, Object> m) {
        //保存出库总表记录
        WmsStockout wmsStockout = new WmsStockout();
        wmsStockout.setStockoutId(IdManageUtil.getId("wms_stockout"));
        wmsStockout.setStockoutCode(IdManageUtil.getCode("wms_stockout"));
        if (m.containsKey("stockout_type")) {
            wmsStockout.setStockoutType((String) m.get("stockout_type"));
        }
        wmsStockout.setStockoutState("0");
        wmsStockoutService.save(wmsStockout);
        return wmsStockout;
    }

    /**
     * 组装 出库明细 记录
     *
     * @param m
     * @param wmsStockout
     * @return
     */
    private WmsStockoutdtl getWmsStockoutdtl(Map<String, Object> m, WmsStockout wmsStockout) {
        //保存出库明细记录
        WmsStockoutdtl wmsStockoutdtl = new WmsStockoutdtl();
        wmsStockoutdtl.setStockoutdtlId(IdManageUtil.getId("wms_stockoutdtl"));
        wmsStockoutdtl.setStockoutdtlCode(IdManageUtil.getCode("wms_stockoutdtl"));
        wmsStockoutdtl.setStockoutId(wmsStockout.getStockoutId());
        wmsStockoutdtl.setStockoutCode(wmsStockout.getStockoutCode());
        wmsStockoutdtl.setStockoutState("0");
        if (m.containsKey("goods_code")) {
            wmsStockoutdtl.setGoodsCode(m.get("goods_code").toString());
        }
        if (m.containsKey("goods_name")) {
            wmsStockoutdtl.setGoodsName((String) m.get("goods_name"));
        }
        if (m.containsKey("goods_size")) {
            wmsStockoutdtl.setGoodsSize((String) m.get("goods_size"));
        }
        if (m.containsKey("goods_unit")) {
            wmsStockoutdtl.setGoodsUnit((String) m.get("goods_unit"));
        }
        if (m.containsKey("goods_type")) {
            wmsStockoutdtl.setGoodsType((String) m.get("goods_type"));
        }
        if (m.containsKey("goods_color")) {
            wmsStockoutdtl.setGoodsColor((String) m.get("goods_color"));
        }
        if (m.containsKey("goods_batchnumber")) {
            wmsStockoutdtl.setGoodsBatchnumber((String) m.get("goods_batchnumber"));
        }
        if (m.containsKey("goods_quantity")) {
            wmsStockoutdtl.setGoodsQuantity((Integer) m.get("goods_quantity"));
        }
        if (m.containsKey("goods_level")) {
            wmsStockoutdtl.setGoodsLevel((String) m.get("goods_level"));
        }

        //保存
        wmsStockoutdtlService.save(wmsStockoutdtl);
        return wmsStockoutdtl;
    }


}
