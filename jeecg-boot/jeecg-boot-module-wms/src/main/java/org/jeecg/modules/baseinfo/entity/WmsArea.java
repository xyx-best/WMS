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
 * @Description: 区域管理
 * @Author: jeecg-boot
 * @Date:   2020-02-26
 * @Version: V1.0
 */
@Data
@TableName("wms_area")
public class WmsArea implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    private java.lang.String id;
	/**区域Id*/
	@Excel(name = "区域Id", width = 15)
	private java.lang.String areaId;
	/**区域名称*/
	@Excel(name = "区域名称", width = 15)
    private java.lang.String areaName;
	/**区域地点*/
	@Excel(name = "区域地点", width = 15)
    private java.lang.String areaLoc;
	/**所属的仓库ID*/
	@Excel(name = "所属的仓库ID", width = 15)
    private java.lang.String warehouseId;
	/**区域大小*/
	@Excel(name = "区域大小", width = 15)
    private java.lang.Integer areaSize;
	/**区域用途*/
	@Excel(name = "区域用途", width = 15)
    private java.lang.String areaType;
	/**区域存放类别*/
	@Excel(name = "区域存放类别", width = 15)
    private java.lang.String areaKind;
	/**区域启停状态*/
	@Excel(name = "区域启停状态", width = 15)
    private java.lang.String areaState;
	/**区域管理者*/
	@Excel(name = "区域管理者", width = 15)
    private java.lang.String areaManager;
	/**区域编码*/
	@Excel(name = "区域编码", width = 15)
    private java.lang.String areaCode;
	/**上架策略*/
	@Excel(name = "上架策略", width = 15)
	private java.lang.String rackingStrategy;
	/**上架策略-规格*/
	@Excel(name = "策略规格", width = 15)
	private java.lang.String rkstraSize;
	/**上架策略-花色*/
	@Excel(name = "策略花色", width = 15)
	private java.lang.String rkstraColor;
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
