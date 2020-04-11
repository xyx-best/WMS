package org.jeecg.modules.stockout.entity;

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
 * @Description: 拣选表
 * @Author: jeecg-boot
 * @Date:   2020-03-24
 * @Version: V1.0
 */
@Data
@TableName("wms_picking")
public class WmsPicking implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    private String id;
	/**拣选ID*/
	@Excel(name = "拣选ID", width = 15)
    private String pickingId;
	/**拣选编码*/
	@Excel(name = "拣选编码", width = 15)
    private String pickingCode;
	/**来源细单ID*/
	@Excel(name = "来源细单ID", width = 15)
    private String sourcedtlId;
	/**拣选状态*/
	@Excel(name = "拣选状态", width = 15)
    private String pickingState;
	/**区域ID*/
	@Excel(name = "区域ID", width = 15)
    private String areaId;
	/**区域编码*/
	@Excel(name = "区域编码", width = 15)
    private String areaCode;
	/**区域名称*/
	@Excel(name = "区域名称", width = 15)
    private String areaName;
	/**货位ID*/
	@Excel(name = "货位ID", width = 15)
    private String locId;
	/**货位编码*/
	@Excel(name = "货位编码", width = 15)
    private String locCode;
	/**货位名称*/
	@Excel(name = "货位名称", width = 15)
    private String locName;
	/**货物ID*/
	@Excel(name = "货物ID", width = 15)
    private String goodsId;
	/**货物编码*/
	@Excel(name = "货物编码", width = 15)
    private String goodsCode;
	/**货物名称*/
	@Excel(name = "货物名称", width = 15)
    private String goodsName;
	/**货物规格*/
	@Excel(name = "货物规格", width = 15)
    private String goodsSize;
	/**货物单位*/
	@Excel(name = "货物单位", width = 15)
    private String goodsUnit;
	/**货物类别*/
	@Excel(name = "货物类别", width = 15)
    private String goodsType;
	/**货物花色*/
	@Excel(name = "货物花色", width = 15)
    private String goodsColor;
	/**货物批号*/
	@Excel(name = "货物批号", width = 15)
    private String goodsBatchnumber;
	/**货物数量*/
	@Excel(name = "货物数量", width = 15)
    private Integer goodsQuantity;
	/**货物等级*/
	@Excel(name = "货物等级", width = 15)
    private String goodsLevel;
	/**出库时间*/
	@Excel(name = "出库时间", width = 15)
    private Date stockoutTime;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    private String createBy;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
	/**更新人*/
	@Excel(name = "更新人", width = 15)
    private String updateBy;
	/**更新时间*/
	@Excel(name = "更新时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
	/**所属部门*/
	@Excel(name = "所属部门", width = 15)
    private String sysOrgCode;
}
