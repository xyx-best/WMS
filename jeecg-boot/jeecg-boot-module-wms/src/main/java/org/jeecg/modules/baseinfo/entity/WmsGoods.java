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
 * @Description: 货物管理
 * @Author: jeecg-boot
 * @Date:   2020-02-26
 * @Version: V1.0
 */
@Data
@TableName("wms_goods")
public class WmsGoods implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    private java.lang.String id;
	/**货物Id*/
	@Excel(name = "货物Id", width = 15)
	private java.lang.String goodsId;
	/**货物名称*/
	@Excel(name = "货物名称", width = 15)
    private java.lang.String goodsName;
	/**货物规格*/
	@Excel(name = "货物规格", width = 15)
    private java.lang.String goodsSize;
	/**货物品种*/
	@Excel(name = "货物类别", width = 15)
    private java.lang.String goodsType;
	/**货物单位*/
	@Excel(name = "货物单位", width = 15)
	private java.lang.String goodsUnit;
	/**货物启停状态*/
	@Excel(name = "货物启停状态", width = 15)
    private java.lang.String isUse;
	/**货物花色*/
	@Excel(name = "货物花色", width = 15)
    private java.lang.String goodsColor;
	/**货物编码*/
	@Excel(name = "货物编码", width = 15)
    private java.lang.String goodsCode;
	/**货物等级*/
	@Excel(name = "货物等级", width = 15)
	private java.lang.String goodsLevel;
	/**上架策略*/
	@Excel(name = "上架策略", width = 15)
	private java.lang.String rackingStrategy;
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
