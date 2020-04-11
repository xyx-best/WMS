package org.jeecg.modules.baseinfo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.baseinfo.entity.WmsGoodsCategory;
import org.jeecg.modules.baseinfo.mapper.WmsGoodsCategoryMapper;
import org.jeecg.modules.baseinfo.service.IWmsGoodsCategoryService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 货物类别表
 * @Author: jeecg-boot
 * @Date:   2020-03-03
 * @Version: V1.0
 */
@Service
public class WmsGoodsCategoryServiceImpl extends ServiceImpl<WmsGoodsCategoryMapper, WmsGoodsCategory> implements IWmsGoodsCategoryService {

	@Override
	public void addWmsGoodsCategory(WmsGoodsCategory wmsGoodsCategory) {
		if(oConvertUtils.isEmpty(wmsGoodsCategory.getParentCategoryId())){
			wmsGoodsCategory.setParentCategoryId(IWmsGoodsCategoryService.ROOT_PID_VALUE);
		}else{
			//如果当前节点父ID不为空 则设置父节点的hasChildren 为1
			WmsGoodsCategory parent = queryByCategoryId(wmsGoodsCategory.getParentCategoryId());
			if(parent!=null && !"1".equals(parent.getHasChild())){
				parent.setHasChild("1");
				baseMapper.updateById(parent);
			}
		}
		baseMapper.insert(wmsGoodsCategory);
	}
	
	@Override
	public void updateWmsGoodsCategory(WmsGoodsCategory wmsGoodsCategory) {
		WmsGoodsCategory entity = this.getById(wmsGoodsCategory.getId());
		if(entity==null) {
			throw new JeecgBootException("未找到对应实体");
		}
		String old_pid = entity.getParentCategoryId();
		String new_pid = wmsGoodsCategory.getParentCategoryId();
		if(!old_pid.equals(new_pid)) {
			updateOldParentNode(old_pid);
			if(oConvertUtils.isEmpty(new_pid)){
				wmsGoodsCategory.setParentCategoryId(IWmsGoodsCategoryService.ROOT_PID_VALUE);
			}
			if(!IWmsGoodsCategoryService.ROOT_PID_VALUE.equals(wmsGoodsCategory.getParentCategoryId())) {
				baseMapper.updateTreeNodeStatus(wmsGoodsCategory.getParentCategoryId(), IWmsGoodsCategoryService.HASCHILD);
			}
		}
		baseMapper.updateById(wmsGoodsCategory);
	}
	
	@Override
	public void deleteWmsGoodsCategory(String id) throws JeecgBootException {
		WmsGoodsCategory wmsGoodsCategory = this.getById(id);
		if(wmsGoodsCategory==null) {
			throw new JeecgBootException("未找到对应实体");
		}
		updateOldParentNode(wmsGoodsCategory.getParentCategoryId());
		baseMapper.deleteById(id);
	}
	
	
	
	/**
	 * 根据所传pid查询旧的父级节点的子节点并修改相应状态值
	 * @param pid
	 */
	private void updateOldParentNode(String pid) {
		if(!IWmsGoodsCategoryService.ROOT_PID_VALUE.equals(pid)) {
			Integer count = baseMapper.selectCount(new QueryWrapper<WmsGoodsCategory>().eq("parent_category_id", pid));
			if(count==null || count<=1) {
				baseMapper.updateTreeNodeStatus(pid, IWmsGoodsCategoryService.NOCHILD);
			}
		}
	}

	@Override
	public Map<String, List> queryList(int findLeaves) {
		LambdaQueryWrapper<WmsGoodsCategory> query = new LambdaQueryWrapper<WmsGoodsCategory>();
		//判断是否查找叶子节点
		if (findLeaves==1){
			query.eq(WmsGoodsCategory::getHasChild,0);
		} else if (findLeaves==0){
			query.eq(WmsGoodsCategory::getParentCategoryId,0);
		} else if (findLeaves==2){
			//不做限制，查询所有类别
		}
		List<WmsGoodsCategory> list = this.list(query);
		// 调用wrapTreeDataToTreeList方法生成树状数据
//        List<WmsWarehouse> listResult = FindsDepartsChildrenUtil.wrapTreeDataToTreeList(list);
		Map<String, List> m = new HashMap<>();
		m.put("list",list);
		return m;
	}

	/**
	 * 根据子节点 获取 父节点
	 * @param childCategoryId
	 * @return
	 */
	@Override
	public String[] queryParentByChild(String childCategoryId) {
		List<String> parentCid = new ArrayList<>();
		QueryWrapper<WmsGoodsCategory> query = new QueryWrapper<>();
		query.eq("category_id", childCategoryId);
		WmsGoodsCategory wC = getOne(query);
		do{
			parentCid.add(wC.getParentCategoryId());
			wC = getById(wC.getParentCategoryId());
		}while (wC!=null);
		parentCid.add("");
		parentCid.add(childCategoryId);
		String[] tem = new String[parentCid.size()];
		return parentCid.toArray(tem);
	}

	@Override
	public WmsGoodsCategory queryByCategoryId(String CategoryId) {
		QueryWrapper<WmsGoodsCategory> query = new QueryWrapper<>();
		query.eq("category_id", CategoryId);
		WmsGoodsCategory wC = getOne(query);
		return  wC;
	}

}
