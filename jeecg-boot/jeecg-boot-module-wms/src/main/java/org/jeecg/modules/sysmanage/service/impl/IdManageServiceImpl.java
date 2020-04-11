package org.jeecg.modules.sysmanage.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.jeecg.modules.sysmanage.entity.IdManage;
import org.jeecg.modules.sysmanage.mapper.IdManageMapper;
import org.jeecg.modules.sysmanage.service.IIdManageService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 流水号id管理
 * @Author: jeecg-boot
 * @Date:   2020-03-06
 * @Version: V1.0
 */
@Service
public class IdManageServiceImpl extends ServiceImpl<IdManageMapper, IdManage> implements IIdManageService {

    /**
     * 组合下一个ID
     * @param idManage
     */
    public boolean combineNextId(@RequestBody IdManage idManage, int count) {
        String nId = "";
        String dateP = "";
        String dateS = "";
        if (idManage.getPrefix()!=null && !idManage.getPrefix().equals("0")){
            String p=idManage.getPrefixText().split("\\（")[0];
            SimpleDateFormat sdfP = new SimpleDateFormat(p);
            dateP = sdfP.format(new Date());
            nId+=dateP;
        }
        if (idManage.getSuffix()!=null && !idManage.getSuffix().equals("0")){
            String s=idManage.getSuffixText().split("\\（")[0];
            SimpleDateFormat sdfS = new SimpleDateFormat(s);
            dateS = sdfS.format(new Date());
        }
        String c = count+"";
        int zeroNum = (idManage.getLength()==null ? 10 : idManage.getLength()) - dateP.length() - dateS.length() - c.length();
        if (zeroNum>=0){
            for (int i=0;i<zeroNum;i++){
                nId+="0";
            }
            nId+=""+ c +dateS;
            idManage.setNextId(nId);
            return true;
        }else {
            return false;
        }
    }

    /**
     * 根据表名获取当前id
     * 即将nextID赋给currentID并返回字段 ，然后重新计算nextID保存
     * @param tbName
     * @return
     */
    @Override
    @Transactional
    @DS("master")
    public String getCurIdByTableName(String tbName){
        Map<String, Object> name = new HashMap<>();
        name.put("table_name", tbName);
        List<IdManage> idManageList = (List<IdManage>) listByMap(name);
        if (idManageList.size()==0){
            return "没有该表的流水号ID管理";
        }
        IdManage idManage = idManageList.get(0);
        String tempNextId = idManage.getNextId();  //临时保存nextid
        int cc = idManage.getCount()+1;
        if (idManage.getCurrentId()==null){
            combineNextId(idManage, cc);
        } else {
            idManage.setCount(cc);
            combineNextId(idManage, cc+1);

        }
        idManage.setCurrentId(tempNextId);   //设置当前id为下一个id
        updateById(idManage);
        return idManage.getCurrentId();
    }

    @Override
    public String getCodeByTableName(String tbName) {
        Map<String, Object> name = new HashMap<>();
        name.put("table_name", tbName);
        List<IdManage> idManageList = (List<IdManage>) listByMap(name);
        if (idManageList.size()==0){
            return "没有该表的流水号ID管理";
        }
        IdManage idManage = idManageList.get(0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String date = sdf.format(new Date());
        int c = (idManage.getTodayCount() == null ? 0 : idManage.getTodayCount()) + 1;
        String todayCount = c + "";
        idManage.setTodayCount(c);   //今天计数加一
        updateById(idManage);
        String code = "";
        code+=date;
        for (int i = 0;i < 4-todayCount.length();i++){
            code+="0";
        }
        code+=todayCount;
        return code;
    }
}
