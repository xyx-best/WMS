package org.jeecg.modules.baseinfo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.modules.baseinfo.entity.WmsWarehouse;
import org.jeecg.modules.baseinfo.mapper.WmsWarehouseMapper;
import org.jeecg.modules.baseinfo.service.IWmsWarehouseService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 仓库管理
 * @Author: jeecg-boot
 * @Date:   2020-02-26
 * @Version: V1.0
 */
@Service
public class WmsWarehouseServiceImpl extends ServiceImpl<WmsWarehouseMapper, WmsWarehouse> implements IWmsWarehouseService {

    @Override
    public Map<String, List> queryList() {
        LambdaQueryWrapper<WmsWarehouse> query = new LambdaQueryWrapper<WmsWarehouse>();
        query.eq(WmsWarehouse::getIsUse, CommonConstant.IS_USE_YES);
        query.orderByAsc(WmsWarehouse::getWarehouseCode);
        List<WmsWarehouse> list = this.list(query);
        // 调用wrapTreeDataToTreeList方法生成树状数据
//        List<WmsWarehouse> listResult = FindsDepartsChildrenUtil.wrapTreeDataToTreeList(list);
        Map<String, List> m = new HashMap<>();
        m.put("list",list);
        return m;
    }
}
