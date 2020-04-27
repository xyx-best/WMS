package org.jeecg.modules.stockin.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.stock.entity.WmsStock;
import org.jeecg.modules.stock.service.IWmsStockService;
import org.jeecg.modules.stockin.entity.WmsRacking;
import org.jeecg.modules.stockin.entity.WmsStockin;
import org.jeecg.modules.stockin.entity.WmsStockindtl;
import org.jeecg.modules.stockin.service.IWmsRackingService;
import org.jeecg.modules.stockin.service.IWmsStockinService;
import org.jeecg.modules.stockin.service.IWmsStockindtlService;
import org.jeecg.modules.transaction.entity.WmsTransaction;
import org.jeecg.modules.transaction.entity.WmsTransactionHis;
import org.jeecg.modules.transaction.service.IWmsTransactionHisService;
import org.jeecg.modules.transaction.service.IWmsTransactionService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.jeecg.modules.sqlutil.sqlutil;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 监听跟仿真系统对接的 SQL server 数据库，将已完成入库动作的记录状态修改为已入库
 */
@Slf4j
public class FinishStockin implements Job {

    @Autowired
    private IWmsStockService wmsStockService;
    @Autowired
    private IWmsStockinService wmsStockinService;
    @Autowired
    private IWmsStockindtlService wmsStockindtlService;
    @Autowired
    private IWmsRackingService wmsRackingService;
    @Autowired
    private IWmsTransactionService wmsTransactionService;
    @Autowired
    private IWmsTransactionHisService wmsTransactionHisService;
    @Autowired
    private sqlutil sqlutil;


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //获取已完成入库操作的记录
        List<Map<String, Object>> finishedStockin = sqlutil.getInfoBySql("select * from stockin_finish");
        if (!finishedStockin.isEmpty()) {
            for (Map<String, Object> m : finishedStockin) {
                if (m.get("tray_number") != null){
                    //根据托盘号获取库存，修改库存状态为 已入库
                    WmsStock wmsStock = wmsStockService.getByTrayNumber((String)m.get("tray_number"));
                    wmsStock.setStockState("1");
                    wmsStockService.updateById(wmsStock);

                    //根据库存找到交易记录 修改交易状态为 历史
                    WmsTransaction wmsTransaction = wmsTransactionService.getByStockId(wmsStock.getStockId());
                    wmsTransaction.setTransactionState("1");

                    //添加交易历史记录
                    WmsTransactionHis wmsTransactionHis = wmsTransactionService.copyToHis(wmsTransaction);
                    wmsTransactionHisService.save(wmsTransactionHis);

                    //根据交易记录里的移动单号（即上架单ID），获取上架记录，并修改状态为 已完成
                    WmsRacking wmsRacking = wmsRackingService.getByRackingId(wmsTransaction.getMoveId());
                    wmsRacking.setStockinTime((Date) m.get("finish_date"));
                    wmsRacking.setRackingState("2");
                    wmsRackingService.updateById(wmsRacking);

                    //查找同一入库明细ID的上架记录列表
                    List<WmsRacking> wmsRackingList = wmsRackingService.getByStockindtlId(wmsRacking.getSourcedtlId());
                    //是否同一上架明细记录下的全部上架单都已完成
                    boolean isAllFinish = false;
                    for (WmsRacking wr : wmsRackingList) {
                        if (wr.getRackingState().equals("1")) {
                            isAllFinish = false;
                            break;
                        }
                        isAllFinish = true;
                    }
                    if (isAllFinish) {
                        //同一上架明细记录下的全部上架单都已完成，则更新入库总表、入库明细，状态修改为入库完成
                        WmsStockindtl wmsStockindtl = wmsStockindtlService.getByStockindtlId(wmsRacking.getSourcedtlId());
                        wmsStockindtl.setStockinState("3");
                        wmsStockindtlService.updateById(wmsStockindtl);

                        WmsStockin wmsStockin = wmsStockinService.getByStockinId(wmsStockindtl.getStockinId());
                        wmsStockin.setStockinState("3");
                        wmsStockinService.updateById(wmsStockin);
                    }

                    //在SQL server删除该条记录,并 插入到历史表中
                    sqlutil.delAndInsFinished("stockin_finish", (String)m.get("tray_number"), (Date) m.get("finish_date"));

                } else {
                    //删除托盘号为空的记录
                    sqlutil.delNullTNFinished("stockin_finish");
                }

            }

        }
    }
}
