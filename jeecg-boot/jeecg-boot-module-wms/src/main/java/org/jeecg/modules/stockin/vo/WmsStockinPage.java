package org.jeecg.modules.stockin.vo;

import java.util.List;
import org.jeecg.modules.stockin.entity.WmsStockin;
import org.jeecg.modules.stockin.entity.WmsStockindtl;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelEntity;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;

/**
 * @Description: 入库表
 * @Author: jeecg-boot
 * @Date:   2020-03-21
 * @Version: V1.0
 */
@Data
public class WmsStockinPage {
	
	/**主键*/
	private String id;
	/**入库id*/
	@Excel(name = "入库id", width = 15)
	private String stockinId;
	/**入库编码*/
	@Excel(name = "入库编码", width = 15)
	private String stockinCode;
	/**入库类型*/
	@Excel(name = "入库类型", width = 15)
	private String stockinType;
	/**入库来源*/
	@Excel(name = "入库来源", width = 15)
	private String stockinSource;
	/**表单状态*/
	@Excel(name = "表单状态", width = 15)
	private String stockinState;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
	private String createBy;
	/**创建日期*/
	@Excel(name = "创建日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	private Date createTime;
	/**更新人*/
	@Excel(name = "更新人", width = 15)
	private String updateBy;
	/**更新日期*/
	@Excel(name = "更新日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
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
	
	@ExcelCollection(name="入库明细表")
	private List<WmsStockindtl> wmsStockindtlList;	
	
}
