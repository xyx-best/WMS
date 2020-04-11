package org.jeecg.modules.transaction.entity;

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
 * @Description: 交易历史表
 * @Author: jeecg-boot
 * @Date:   2020-03-17
 * @Version: V1.0
 */
@Data
@TableName("wms_transaction_his")
public class WmsTransactionHis implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    private String id;
	/**交易ID*/
	@Excel(name = "交易ID", width = 15)
    private String transactionId;
	/**交易编码*/
	@Excel(name = "交易编码", width = 15)
    private String transactionCode;
	/**上架/拣选单ID*/
	@Excel(name = "上架/拣选单ID", width = 15)
    private String moveId;
	/**来源总单ID*/
	@Excel(name = "来源总单ID", width = 15)
    private String sourceId;
	/**来源总单编码*/
	@Excel(name = "来源总单编码", width = 15)
    private String sourceCode;
	/**来源总单状态*/
	@Excel(name = "来源总单状态", width = 15)
    private String sourceState;
	/**来源总单创建时间*/
	@Excel(name = "来源总单创建时间", width = 15)
    private String sourceCreateTime;
	/**来源总单创建人*/
	@Excel(name = "来源总单创建人", width = 15)
    private String sourceCreateBy;
	/**来源细单ID*/
	@Excel(name = "来源细单ID", width = 15)
    private String sourcedtlId;
	/**来源细单编码*/
	@Excel(name = "来源细单编码", width = 15)
    private String sourcedtlCode;
	/**来源细单状态*/
	@Excel(name = "来源细单状态", width = 15)
    private String sourcedtlState;
	/**来源细单创建时间*/
	@Excel(name = "来源细单创建时间", width = 15)
    private String sourcedtlCreateTime;
	/**来源细单创建人*/
	@Excel(name = "来源细单创建人", width = 15)
    private String sourcedtlCreateBy;
	/**库存ID*/
	@Excel(name = "库存ID", width = 15)
    private String stockId;
	/**来源类型*/
	@Excel(name = "来源类型", width = 15)
    private String sourceType;
	/**交易状态*/
	@Excel(name = "交易状态", width = 15)
    private String transactionState;
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
	/**创建时间*/
	@Excel(name = "创建时间", width = 15)
    private Date createTime;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    private String createBy;
	/**更新人*/
	@Excel(name = "更新人", width = 15)
    private String updateBy;
	/**更新时间*/
	@Excel(name = "更新时间", width = 15)
    private Date updateTime;
	/**所属部门*/
	@Excel(name = "所属部门", width = 15)
    private String sysOrgCode;
	/**预留1*/
	@Excel(name = "预留1", width = 15)
    private String udf1;
	/**预留2*/
	@Excel(name = "预留2", width = 15)
    private String udf2;
	/**预留3*/
	@Excel(name = "预留3", width = 15)
    private String udf3;
}
