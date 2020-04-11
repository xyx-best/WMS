package org.jeecg.modules.sysmanage.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.jeecg.modules.sysmanage.service.IIdManageService;
import javax.annotation.PostConstruct;

@Component
public class IdManageUtil {

    @Autowired
    protected IIdManageService idManageService;

    private static IdManageUtil idManageUtil ;

    //通过注解@PostConstruct ，在初始化的时候初始化静态对象和它的静态成员变量healthDataService，
    // 原理是拿到service层bean对象，静态存储下来，防止被释放。
    @PostConstruct //通过@PostConstruct实现初始化bean之前进行的操作
    public void init() {
        idManageUtil = this;
        idManageUtil.idManageService = this.idManageService;
        // 初使化时将已静态化的testService实例化
    }

    //获取下一个id
    public static String getId(String tbName){
        return idManageUtil.idManageService .getCurIdByTableName(tbName);
    }

    //获取下一个编码
    public static String getCode(String tbName){
        return idManageUtil.idManageService .getCodeByTableName(tbName);
    }
}
