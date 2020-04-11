package org.jeecg.modules.sysmanage.job;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.sysmanage.entity.IdManage;
import org.jeecg.modules.sysmanage.service.IIdManageService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
public class RefreshAutoCode implements Job {

    @Autowired
    private IIdManageService idManageService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        List<IdManage> idManageList = idManageService.list();
        for (IdManage im : idManageList) {
            im.setTodayCount(0);
            idManageService.updateById(im);
        }
//        idManageService.updateBatchById(idManageList);
    }
}
