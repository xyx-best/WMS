package org.jeecg.modules.transaction.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jeecg.modules.transaction.entity.WmsTransaction;
import org.jeecg.modules.transaction.entity.WmsTransactionHis;
import org.jeecg.modules.transaction.mapper.WmsTransactionMapper;
import org.jeecg.modules.transaction.service.IWmsTransactionService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Date;

/**
 * @Description: 交易表
 * @Author: jeecg-boot
 * @Date:   2020-03-16
 * @Version: V1.0
 */
@Service
public class WmsTransactionServiceImpl extends ServiceImpl<WmsTransactionMapper, WmsTransaction> implements IWmsTransactionService {

    /**
     * 根据 交易总表的记录 复制一份 历史记录
     * @param wmsTransaction
     * @return
     */
    @Override
    public WmsTransactionHis copyToHis(WmsTransaction wmsTransaction) {
        WmsTransactionHis wmsTransactionHis = new WmsTransactionHis();
        wmsTransactionHis.setTransactionId(wmsTransaction.getTransactionId());
        wmsTransactionHis.setTransactionCode(wmsTransaction.getTransactionCode());
        wmsTransactionHis.setMoveId(wmsTransaction.getMoveId());
        wmsTransactionHis.setStockId(wmsTransaction.getStockId());
        wmsTransactionHis.setSourceType(wmsTransaction.getSourceType());
        wmsTransactionHis.setTransactionState(wmsTransaction.getTransactionState());
        wmsTransactionHis.setAreaId(wmsTransaction.getAreaId());
        wmsTransactionHis.setAreaCode(wmsTransaction.getAreaCode());
        wmsTransactionHis.setAreaName(wmsTransaction.getAreaName());
        wmsTransactionHis.setLocId(wmsTransaction.getLocId());
        wmsTransactionHis.setLocCode(wmsTransaction.getLocCode());
        wmsTransactionHis.setLocName(wmsTransaction.getLocName());
        wmsTransactionHis.setGoodsId(wmsTransaction.getGoodsId());
        wmsTransactionHis.setGoodsCode(wmsTransaction.getGoodsCode());
        wmsTransactionHis.setGoodsName(wmsTransaction.getGoodsName());
        wmsTransactionHis.setGoodsSize(wmsTransaction.getGoodsSize());
        wmsTransactionHis.setGoodsUnit(wmsTransaction.getGoodsUnit());
        wmsTransactionHis.setGoodsType(wmsTransaction.getGoodsType());
        wmsTransactionHis.setGoodsColor(wmsTransaction.getGoodsColor());
        wmsTransactionHis.setGoodsBatchnumber(wmsTransaction.getGoodsBatchnumber());
        wmsTransactionHis.setGoodsQuantity(wmsTransaction.getGoodsQuantity());
        wmsTransactionHis.setGoodsLevel(wmsTransaction.getGoodsLevel());
        wmsTransactionHis.setCreateTime(wmsTransaction.getCreateTime());
        wmsTransactionHis.setCreateBy(wmsTransaction.getCreateBy());
        return wmsTransactionHis;
    }

    @Override
    public WmsTransaction getByStockId(String stockId) {
        LambdaQueryWrapper<WmsTransaction> query = new LambdaQueryWrapper<WmsTransaction>();
        query.eq(WmsTransaction::getStockId, stockId);
        WmsTransaction wmsTransaction = this.list(query).get(0);
        return wmsTransaction;
    }
}
