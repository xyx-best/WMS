package org.jeecg.modules.stockin.entity;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description: 入库表
 * @Author: jeecg-boot
 * @Date:   2020-03-21
 * @Version: V1.0
 */
@Data
@TableName("wms_stockin")
public class WmsStockin implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    private String id;
	/**入库id*/
    private String stockinId;
	/**入库编码*/
    private String stockinCode;
	/**入库类型*/
    private String stockinType;
	/**入库来源*/
    private String stockinSource;
	/**表单状态*/
    private String stockinState;
	/**创建人*/
    private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date createTime;
	/**更新人*/
    private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
	/**所属部门*/
    private String sysOrgCode;
	/**预留字段1*/
    private String udf1;
	/**预留字段2*/
    private String udf2;
	/**预留字段3*/
    private String udf3;
}
