package org.jeecg.modules.baseinfo.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;

/**
 * @Description: 仓库管理
 * @Author: jeecg-boot
 * @Date:   2020-02-26
 * @Version: V1.0
 */
@Data
@TableName("wms_warehouse")
public class WmsWarehouse implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    private java.lang.String id;
	/**仓库Id*/
	@Excel(name = "仓库Id", width = 15)
	private java.lang.String warehouseId;
	/**仓库名称*/
	@Excel(name = "仓库名称", width = 15)
    private java.lang.String warehouseName;
	/**仓库地点*/
	@Excel(name = "仓库地点", width = 15)
    private java.lang.String warehouseLoc;
	/**仓库容量*/
	@Excel(name = "仓库容量", width = 15)
    private java.lang.Integer warehouseVolume;
	/**仓库类型*/
	@Excel(name = "仓库类型", width = 15)
    private java.lang.String warehouseType;
	/**仓库启用状态*/
	@Excel(name = "仓库启用状态", width = 15)
    private java.lang.String isUse;
	/**仓库管理者*/
	@Excel(name = "仓库管理者", width = 15)
    private java.lang.String warehouseManager;
	/**仓库编码*/
	@Excel(name = "仓库编码", width = 15)
    private java.lang.String warehouseCode;
	/**备注*/
	@Excel(name = "备注", width = 15)
	private java.lang.String memo;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    private java.lang.String createBy;
	/**创建日期*/
	@Excel(name = "创建日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private java.util.Date createTime;
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
	/**预留字段1*/
	@Excel(name = "预留字段1", width = 15)
	private java.lang.String udf1;
	/**预留字段2*/
	@Excel(name = "预留字段2", width = 15)
	private java.lang.String udf2;
	/**预留字段3*/
	@Excel(name = "预留字段3", width = 15)
	private java.lang.String udf3;
}
