package org.jeecg.modules.transaction.service;

import org.jeecg.modules.transaction.entity.WmsTransaction;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.transaction.entity.WmsTransactionHis;

/**
 * @Description: 交易表
 * @Author: jeecg-boot
 * @Date:   2020-03-16
 * @Version: V1.0
 */
public interface IWmsTransactionService extends IService<WmsTransaction> {

    /**
     * 根据 交易总表的记录 复制一份 历史记录
     * @param wmsTransaction
     * @return
     */
    WmsTransactionHis copyToHis(WmsTransaction wmsTransaction);

}
