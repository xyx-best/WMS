package org.jeecg.modules.baseinfo.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.baseinfo.entity.WmsGoodsCategory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 货物类别表
 * @Author: jeecg-boot
 * @Date:   2020-03-03
 * @Version: V1.0
 */
public interface WmsGoodsCategoryMapper extends BaseMapper<WmsGoodsCategory> {

	/**
	 * 编辑节点状态
	 * @param id
	 * @param status
	 */
	void updateTreeNodeStatus(@Param("id") String id, @Param("status") String status);
}
