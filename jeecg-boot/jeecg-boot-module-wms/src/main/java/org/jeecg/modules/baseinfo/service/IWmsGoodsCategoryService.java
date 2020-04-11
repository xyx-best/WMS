package org.jeecg.modules.baseinfo.service;

import org.jeecg.modules.baseinfo.entity.WmsGoodsCategory;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.exception.JeecgBootException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Description: 货物类别表
 * @Author: jeecg-boot
 * @Date:   2020-03-03
 * @Version: V1.0
 */
public interface IWmsGoodsCategoryService extends IService<WmsGoodsCategory> {

	/**根节点父ID的值*/
	public static final String ROOT_PID_VALUE = "0";
	
	/**树节点有子节点状态值*/
	public static final String HASCHILD = "1";
	
	/**树节点无子节点状态值*/
	public static final String NOCHILD = "0";

	/**新增节点*/
	void addWmsGoodsCategory(WmsGoodsCategory wmsGoodsCategory);
	
	/**修改节点*/
	void updateWmsGoodsCategory(WmsGoodsCategory wmsGoodsCategory) throws JeecgBootException;
	
	/**删除节点*/
	void deleteWmsGoodsCategory(String id) throws JeecgBootException;

	/**
	 * 查询叶子类别或者父类别信息,并进行显示
	 * @Parm findLeaves
	 * @return
	 */
	Map<String, List> queryList(int findLeaves);


	/**
	 * 根据子节点 获取 父节点
	 * @param childCategoryId
	 * @return
	 */
	String[] queryParentByChild(String childCategoryId);

	WmsGoodsCategory queryByCategoryId(String CategoryId);

}
