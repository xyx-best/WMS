package org.jeecg.modules.stockin.entity;

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
 * @Description: 入库明细表
 * @Author: jeecg-boot
 * @Date:   2020-03-10
 * @Version: V1.0
 */
@Data
@TableName("wms_stockindtl")
public class WmsStockindtl implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    private String id;
	/**入库明细ID*/
	@Excel(name = "入库明细ID", width = 15)
    private String stockindtlId;
	/**入库明细编码*/
	@Excel(name = "入库明细编码", width = 15)
    private String stockindtlCode;
	/**入库ID*/
	@Excel(name = "入库ID", width = 15)
    private String stockinId;
	/**入库编码*/
	@Excel(name = "入库编码", width = 15)
    private String stockinCode;
	/**入库状态*/
	@Excel(name = "入库状态", width = 15)
    private String stockinState;
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
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    private String createBy;
	/**创建时间*/
	@Excel(name = "创建时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date createTime;
	/**更新人*/
	@Excel(name = "更新人", width = 15)
    private String updateBy;
	/**更新时间*/
	@Excel(name = "更新时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date updateTime;
	/**所属部门*/
	@Excel(name = "所属部门", width = 15)
    private String sysOrgCode;
	/**预留字段1*/
	@Excel(name = "预留字段1", width = 15)
    private String udf1;
	/**预留字段2*/
	@Excel(name = "预留字段2", width = 15)
    private String udf2;
	/**预留字段3*/
	@Excel(name = "预留字段3", width = 15)
    private String udf3;
}
