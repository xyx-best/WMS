package org.jeecg.modules.stockout.job;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.sqlutil.sqlutil;
import org.jeecg.modules.stock.entity.WmsStock;
import org.jeecg.modules.stock.service.IWmsStockService;
import org.jeecg.modules.stockout.entity.WmsPicking;
import org.jeecg.modules.stockout.entity.WmsStockout;
import org.jeecg.modules.stockout.entity.WmsStockoutdtl;
import org.jeecg.modules.stockout.service.IWmsPickingService;
import org.jeecg.modules.stockout.service.IWmsStockoutService;
import org.jeecg.modules.stockout.service.IWmsStockoutdtlService;
import org.jeecg.modules.transaction.entity.WmsTransaction;
import org.jeecg.modules.transaction.entity.WmsTransactionHis;
import org.jeecg.modules.transaction.service.IWmsTransactionHisService;
import org.jeecg.modules.transaction.service.IWmsTransactionService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 监听跟仿真系统对接的 SQL server 数据库，将已完成出库动作的记录状态修改为已出库
 */
@Slf4j
public class FinishStockout implements Job {

    @Autowired
    private IWmsStockService wmsStockService;
    @Autowired
    private IWmsStockoutService wmsStockoutService;
    @Autowired
    private IWmsStockoutdtlService wmsStockoutdtlService;
    @Autowired
    private IWmsPickingService wmsPickingService;
    @Autowired
    private IWmsTransactionService wmsTransactionService;
    @Autowired
    private IWmsTransactionHisService wmsTransactionHisService;
    @Autowired
    private sqlutil sqlutil;


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //获取已完成出库操作的记录
        List<Map<String, Object>> finishedStockout = sqlutil.getInfoBySql("select * from stockout_finish");
        if (!finishedStockout.isEmpty()) {
            for (Map<String, Object> m : finishedStockout) {
                if (m.get("tray_number") != null){
                    //根据托盘号获取库存，修改库存状态为 已出库
                    WmsStock wmsStock = wmsStockService.getByTrayNumber((String)m.get("tray_number"));
                    wmsStockService.removeById(wmsStock.getId());

                    //根据库存找到交易记录 修改交易状态为 历史
                    WmsTransaction wmsTransaction = wmsTransactionService.getByStockId(wmsStock.getStockId());
                    wmsTransaction.setTransactionState("1");

                    //添加交易历史记录
                    WmsTransactionHis wmsTransactionHis = wmsTransactionService.copyToHis(wmsTransaction);
                    wmsTransactionHisService.save(wmsTransactionHis);

                    //根据交易记录里的移动单号（即拣选单ID），获取拣选记录，并修改状态为 已完成
                    WmsPicking wmsPicking = wmsPickingService.getByPickingId(wmsTransaction.getMoveId());
                    wmsPicking.setStockoutTime((Date) m.get("finish_date"));
                    wmsPicking.setPickingState("2");
                    wmsPickingService.updateById(wmsPicking);

                    //查找同一出库明细ID的拣选记录列表
                    List<WmsPicking> wmsPickingList = wmsPickingService.getByStockoutdtlId(wmsPicking.getSourcedtlId());
                    //是否同一拣选明细记录下的全部拣选单都已完成
                    boolean isAllFinish = false;
                    for (WmsPicking wp : wmsPickingList) {
                        if (wp.getPickingState().equals("1")) {
                            isAllFinish = false;
                            break;
                        }
                        isAllFinish = true;
                    }
                    if (isAllFinish) {
                        //同一拣选明细记录下的全部拣选单都已完成，则更新出库总表、出库明细，状态修改为出库完成
                        WmsStockoutdtl wmsStockoutdtl = wmsStockoutdtlService.getByStockoutdtlId(wmsPicking.getSourcedtlId()).get(0);
                        wmsStockoutdtl.setStockoutState("3");
                        wmsStockoutdtlService.updateById(wmsStockoutdtl);

                        WmsStockout wmsStockout = wmsStockoutService.getByStockoutId(wmsStockoutdtl.getStockoutId());
                        wmsStockout.setStockoutState("3");
                        wmsStockoutService.updateById(wmsStockout);
                    }

                    //在SQL server删除该条记录,并 插入到历史表中
                    sqlutil.delAndInsFinished("stockout_finish", (String)m.get("tray_number"), (Date) m.get("finish_date"));

                } else {
                    //删除托盘号为空的记录
                    sqlutil.delNullTNFinished("stockout_finish");
                }

            }

        }
    }
}
