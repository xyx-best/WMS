package org.jeecg.modules.baseinfo.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.baseinfo.entity.WmsGoodsCategory;
import org.jeecg.modules.baseinfo.service.IWmsGoodsCategoryService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.sysmanage.service.IIdManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Description: 货物类别表
 * @Author: jeecg-boot
 * @Date:   2020-03-03
 * @Version: V1.0
 */
@RestController
@RequestMapping("/goodsCategory/wmsGoodsCategory")
@Slf4j
public class WmsGoodsCategoryController extends JeecgController<WmsGoodsCategory, IWmsGoodsCategoryService>{
	@Autowired
	private IWmsGoodsCategoryService wmsGoodsCategoryService;
	@Autowired
	private IIdManageService idManageService;
	
	/**
	 * 分页列表查询
	 *
	 * @param wmsGoodsCategory
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/rootList")
	public Result<?> queryPageList(WmsGoodsCategory wmsGoodsCategory,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		if(oConvertUtils.isEmpty(wmsGoodsCategory.getParentCategoryId()) && oConvertUtils.isEmpty(wmsGoodsCategory.getCategoryName())){
			wmsGoodsCategory.setParentCategoryId("0");
		}
		QueryWrapper<WmsGoodsCategory> queryWrapper = QueryGenerator.initQueryWrapper(wmsGoodsCategory, req.getParameterMap());
		Page<WmsGoodsCategory> page = new Page<WmsGoodsCategory>(pageNo, pageSize);
		IPage<WmsGoodsCategory> pageList = wmsGoodsCategoryService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	 /**
      * 获取子数据
      * @param wmsGoodsCategory
      * @param req
      * @return
      */
	@GetMapping(value = "/childList")
	public Result<?> queryPageList(WmsGoodsCategory wmsGoodsCategory,HttpServletRequest req) {
		QueryWrapper<WmsGoodsCategory> queryWrapper = QueryGenerator.initQueryWrapper(wmsGoodsCategory, req.getParameterMap());
		List<WmsGoodsCategory> list = wmsGoodsCategoryService.list(queryWrapper);
		return Result.ok(list);
	}
	
	
	/**
	 *   添加
	 *
	 * @param wmsGoodsCategory
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody WmsGoodsCategory wmsGoodsCategory) {
		String wId = idManageService.getCurIdByTableName("wms_goods_category");
		wmsGoodsCategory.setCategoryId(wId);
		WmsGoodsCategory parent = wmsGoodsCategoryService.queryByCategoryId(wmsGoodsCategory.getParentCategoryId());
		wmsGoodsCategory.setParentCategoryId(parent.getCategoryId());
		wmsGoodsCategoryService.addWmsGoodsCategory(wmsGoodsCategory);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param wmsGoodsCategory
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody WmsGoodsCategory wmsGoodsCategory) {
		wmsGoodsCategoryService.updateWmsGoodsCategory(wmsGoodsCategory);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		wmsGoodsCategoryService.deleteWmsGoodsCategory(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.wmsGoodsCategoryService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		WmsGoodsCategory wmsGoodsCategory = wmsGoodsCategoryService.getById(id);
		if(wmsGoodsCategory==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(wmsGoodsCategory);
	}

	 /**
	  * 通过categoryName查询
	  *
	  * @param categoryName
	  * @return
	  */
	 @GetMapping(value = "/queryByName")
	 public Result<?> queryByName(@RequestParam(name="categoryName",required=true) String categoryName) {
	 	Map<String, Object> name = new HashMap<>();
	 	name.put("category_name", categoryName);
		 List<WmsGoodsCategory> wmsGoodsCategory = (List<WmsGoodsCategory>) wmsGoodsCategoryService.listByMap(name);
		 if(wmsGoodsCategory.size()==0) {
			 return Result.error("未找到对应数据");
		 }
		 return Result.ok(wmsGoodsCategory);
	 }

    /**
    * 导出excel
    *
    * @param request
    * @param wmsGoodsCategory
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, WmsGoodsCategory wmsGoodsCategory) {
		return super.exportXls(request, wmsGoodsCategory, WmsGoodsCategory.class, "货物类别表");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
		return super.importExcel(request, response, WmsGoodsCategory.class);
    }

}
