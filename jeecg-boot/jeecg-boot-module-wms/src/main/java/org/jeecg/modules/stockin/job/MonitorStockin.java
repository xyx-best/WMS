package org.jeecg.modules.stockin.job;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.baseinfo.entity.WmsGoods;
import org.jeecg.modules.baseinfo.service.IWmsGoodsService;
import org.jeecg.modules.sqlutil.sqlutil;
import org.jeecg.modules.stock.service.IWmsStockService;
import org.jeecg.modules.stockin.entity.WmsStockin;
import org.jeecg.modules.stockin.entity.WmsStockindtl;
import org.jeecg.modules.stockin.service.IWmsStockinService;
import org.jeecg.modules.stockin.service.IWmsStockindtlService;
import org.jeecg.modules.sysmanage.service.IIdManageService;
import org.jeecg.modules.sysmanage.util.IdManageUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 监听跟仿真系统对接的 SQL server 数据库，来分配货位
 *
 * @DisallowConcurrentExecution 不允许并发执行, 只有上一次任务完成后才能启用下一个定时任务
 */
@Slf4j
@DisallowConcurrentExecution
@PersistJobDataAfterExecution
public class MonitorStockin implements Job {

    @Autowired
    private IWmsStockinService wmsStockinService;
    @Autowired
    private IWmsStockindtlService wmsStockindtlService;
    @Autowired
    private IIdManageService idManageService;
    @Autowired
    private IWmsGoodsService wmsGoodsService;
    @Autowired
    private sqlutil sqlutil;
    @Autowired
    private IWmsStockService wmsStockService;

    @Override
    @DS("master")
    @GetMapping(value = "/list")
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //获取入库信息 分配货位
        List<Map<String, Object>> wmsin = sqlutil.getInfoBySqlWithOrderId("select * from wms_stockin", "wms_stockin");
        Date as = new Date();
        if (!wmsin.isEmpty()) {
            for (Map<String, Object> m : wmsin) {

                if (Integer.parseInt(m.get("has_read").toString()) == 1) {
                    WmsStockin wmsStockin = wmsStockinService.getByStockinCode((String) m.get("stockin_code"));
                    WmsStockindtl wmsStockindtl = wmsStockindtlService.selectByMainId(wmsStockin.getStockinId()).get(0);
                    startStockIn(m, wmsStockin, wmsStockindtl);

                } else {
                    //组装 入库总表 明细 记录
                    WmsStockin wmsStockin = getWmsStockin(m);
                    sqlutil.execute("update wms_stockin set stockin_code = '" + wmsStockin.getStockinCode() + "' , has_read = 1 where order_id =  " + m.get("order_id"));
                    WmsStockindtl wmsStockindtl = getWmsStockindtl(m, wmsStockin);
                    startStockIn(m, wmsStockin, wmsStockindtl);
                }

                Date a = new Date();
                String aadf = "";

                //更新上架单
//                QueryWrapper<WmsRacking> qWRs = new QueryWrapper<WmsRacking>();
//                qWRs.eq("sourcedtl_id", wSD.getStockindtlId());
//                List<WmsRacking> wmsRkList = wmsRackingService.list(qWRs);
//                for (WmsRacking wr : wmsRkList) {
//                    wr.setRackingState("2");
//                    wr.setStockinTime(new Date());
//
//                    //更新交易（历史）表
//                    QueryWrapper<WmsTransaction> queryWrapper = new QueryWrapper<WmsTransaction>();
//                    queryWrapper.eq("move_id", wr.getRackingId());
//                    WmsTransaction wmsTransaction = wmsTransactionService.getOne(queryWrapper);
//
//                    wmsTransaction.setTransactionState("1");
//                    wmsTransactionService.updateById(wmsTransaction);
//
//                    //添加交易历史记录
//                    WmsTransactionHis wmsTransactionHis = wmsTransactionService.copyToHis(wmsTransaction);
//                    wmsTransactionHisService.save(wmsTransactionHis);
//                }
//                wmsRackingService.updateBatchById(wmsRkList);
            }
        }

    }

    private void startStockIn(Map<String, Object> m, WmsStockin wmsStockin, WmsStockindtl wSD) {
        //执行入库
        Map<String, Object> map = wmsStockinService.execStockin(wSD, m);

        if (!(boolean) map.get("success")) {
//            if (m.get("stockin_code") == null) {
//                sqlutil.execute("update wms_stockin set stockin_code = '" + wmsStockin.getStockinCode() + "' where order_id =  " + m.get("order_id"));
//            }
            return;
        }

        //删除记录
        sqlutil.delAndInsWms("wms_stockin", m);

        //更新入库总表
        wmsStockin.setStockinState("2");
        wmsStockinService.updateById(wmsStockin);

        //入库明细记录 并更新状态
        wSD.setStockinState("2");
        wmsStockindtlService.updateById(wSD);
    }

    private WmsStockindtl getWmsStockindtl(Map<String, Object> m, WmsStockin wmsStockin) {
        //保存入库明细记录
        WmsStockindtl wSD = new WmsStockindtl();
        wSD.setStockindtlId(IdManageUtil.getId("wms_stockindtl"));
        wSD.setStockindtlCode(IdManageUtil.getCode("wms_stockindtl"));
        wSD.setGoodsBatchnumber(IdManageUtil.getCode("wms_stockindtl"));
        wSD.setGoodsCode((String) m.get("goods_code"));
        wSD.setGoodsQuantity((Integer) m.get("goods_quantity"));
        wSD.setStockinState("0");
        wSD.setStockinId(wmsStockin.getStockinId());
        wSD.setStockinCode(wmsStockin.getStockinCode());

        WmsGoods wmsGoods = wmsGoodsService.queryByGoodsCode((String) m.get("goods_code"));
        wSD.setGoodsId(wmsGoods.getGoodsId());
        wSD.setGoodsColor(wmsGoods.getGoodsColor());
        wSD.setGoodsSize(wmsGoods.getGoodsSize());
        wSD.setGoodsLevel((String) m.get("goods_level"));
        wSD.setGoodsName(wmsGoods.getGoodsName());
        wSD.setGoodsType(wmsGoods.getGoodsType());
        wSD.setGoodsUnit(wmsGoods.getGoodsUnit());
        wmsStockindtlService.save(wSD);
        return wSD;
    }

    private WmsStockin getWmsStockin(Map<String, Object> m) {
        //保存入库总表记录
        WmsStockin wmsStockin = new WmsStockin();
        wmsStockin.setStockinId(idManageService.getCurIdByTableName("wms_stockin"));
        wmsStockin.setStockinCode(idManageService.getCodeByTableName("wms_stockin"));
        wmsStockin.setStockinType((String) m.get("stockin_type"));
        wmsStockin.setStockinSource(m.containsKey("stockin_source") ? (String) m.get("stockin_source") : null);
        wmsStockin.setStockinState("0");
        wmsStockinService.save(wmsStockin);
        return wmsStockin;
    }

}
