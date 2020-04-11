package org.jeecg.modules.stockout.vo;

import java.util.List;
import org.jeecg.modules.stockout.entity.WmsStockout;
import org.jeecg.modules.stockout.entity.WmsStockoutdtl;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelEntity;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;

/**
 * @Description: 出库总表
 * @Author: jeecg-boot
 * @Date:   2020-03-24
 * @Version: V1.0
 */
@Data
public class WmsStockoutPage {
	
	/**主键*/
	private String id;
	/**出库ID*/
	@Excel(name = "出库ID", width = 15)
	private String stockoutId;
	/**出库编码*/
	@Excel(name = "出库编码", width = 15)
	private String stockoutCode;
	/**出库类型*/
	@Excel(name = "出库类型", width = 15)
	private String stockoutType;
	/**出库状态*/
	@Excel(name = "出库状态", width = 15)
	private String stockoutState;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
	private String createBy;
	/**创建日期*/
	@Excel(name = "创建日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
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
	
	@ExcelCollection(name="出库明细表")
	private List<WmsStockoutdtl> wmsStockoutdtlList;	
	
}
