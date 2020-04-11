package org.jeecg.modules.baseinfo.entity;

import java.io.Serializable;
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
 * @Description: 货物类别表
 * @Author: jeecg-boot
 * @Date:   2020-03-03
 * @Version: V1.0
 */
@Data
@TableName("wms_goods_category")
public class WmsGoodsCategory implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
	private String id;
	/**类别Id*/
	@Excel(name = "类别Id", width = 15)
	private String categoryId;
	/**类别父级节点*/
	@Excel(name = "父级类别节点", width = 15)
	private String parentCategoryId;
	/**类别名字*/
	@Excel(name = "类别名字", width = 15)
	private String categoryName;
	/**类别是否有子节点*/
	@Excel(name = "类别是否有子节点", width = 15)
	private String hasChild;
	/**预留字段1*/
	@Excel(name = "预留字段1", width = 15)
	private String udf1;
	/**预留字段2*/
	@Excel(name = "预留字段2", width = 15)
	private String udf2;
	/**预留字段3*/
	@Excel(name = "预留字段3", width = 15)
	private String udf3;
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
}
