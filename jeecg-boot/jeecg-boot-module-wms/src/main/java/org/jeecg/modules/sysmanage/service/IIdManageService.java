package org.jeecg.modules.sysmanage.service;

import org.jeecg.modules.sysmanage.entity.IdManage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @Description: 流水号id管理
 * @Author: jeecg-boot
 * @Date:   2020-03-06
 * @Version: V1.0
 */
public interface IIdManageService extends IService<IdManage> {

    /**
     * 组合下一个ID
     * @param idManage
     */
    boolean combineNextId(@RequestBody IdManage idManage, int count);

    /**
     * 根据表名获取当前id
     * 即将nextID赋给currentID并返回字段 ，然后重新计算nextID保存
     * @param tbName
     * @return
     */
    String getCurIdByTableName(String tbName);

    /**
     * 根据不同表的今天计数来算出编码并返回
     * @param tbName
     * @return
     */
    String getCodeByTableName(String tbName);
}
