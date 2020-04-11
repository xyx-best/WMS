package org.jeecg.modules.sysmanage.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 流水号id管理
 * @Author: jeecg-boot
 * @Date:   2020-03-06
 * @Version: V1.0
 */
@Data
@TableName("id_manage")
public class IdManage implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    private java.lang.String id;
	/**表名*/
	@Excel(name = "表名", width = 15)
    private java.lang.String tableName;
	/**当前id*/
	@Excel(name = "当前id", width = 15)
    private java.lang.String currentId;
	/**下一个id*/
	@Excel(name = "下一个id", width = 15)
    private java.lang.String nextId;
	/**长度*/
	@Excel(name = "长度", width = 15)
    private java.lang.Integer length;
	/**前缀*/
	@Excel(name = "前缀", width = 15)
    private java.lang.String prefix;
	/**前缀文本*/
	@Excel(name = "前缀文本", width = 15)
	private java.lang.String prefixText;
	/**后缀*/
	@Excel(name = "后缀", width = 15)
    private java.lang.String suffix;
	/**后缀文本*/
	@Excel(name = "后缀文本", width = 15)
	private java.lang.String suffixText;
	/**计数*/
	@Excel(name = "计数", width = 15)
    private java.lang.Integer count;
	/**今天计数*/
	@Excel(name = "今天计数", width = 15)
	private java.lang.Integer todayCount;
	/**备注*/
	@Excel(name = "备注", width = 15)
	private java.lang.String memo;
	/**创建日期*/
	@Excel(name = "创建日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date createTime;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    private java.lang.String createBy;
	/**更新人*/
	@Excel(name = "更新人", width = 15)
    private java.lang.String updateBy;
	/**更新日期*/
	@Excel(name = "更新日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private java.util.Date updateTime;
	/**所属部门*/
	@Excel(name = "所属部门", width = 15)
    private java.lang.String sysOrgCode;
}
