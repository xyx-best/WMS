package org.jeecg.modules.baseinfo.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.baseinfo.entity.WmsGoods;
import org.jeecg.modules.baseinfo.service.IWmsGoodsService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.baseinfo.service.IWmsGoodsCategoryService;
import org.jeecg.modules.sysmanage.service.IIdManageService;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Description: 货物管理
 * @Author: jeecg-boot
 * @Date:   2020-02-26
 * @Version: V1.0
 */
@RestController
@RequestMapping("/goods/wmsGoods")
@Slf4j
public class WmsGoodsController extends JeecgController<WmsGoods, IWmsGoodsService> {
	@Autowired
	private IWmsGoodsService wmsGoodsService;
	@Autowired
	private IWmsGoodsCategoryService wmsGoodsCategoryService;
	@Autowired
	private IIdManageService idManageService;
	
	/**
	 * 分页列表查询
	 *
	 * @param wmsGoods
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(WmsGoods wmsGoods,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<WmsGoods> queryWrapper = QueryGenerator.initQueryWrapper(wmsGoods, req.getParameterMap());
		Page<WmsGoods> page = new Page<WmsGoods>(pageNo, pageSize);
		IPage<WmsGoods> pageList = wmsGoodsService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param wmsGoods
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody WmsGoods wmsGoods) {
		String wId = idManageService.getCurIdByTableName("wms_goods");
		wmsGoods.setGoodsId(wId);
		wmsGoodsService.save(wmsGoods);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param wmsGoods
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody WmsGoods wmsGoods) {
		wmsGoodsService.updateById(wmsGoods);
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
		wmsGoodsService.removeById(id);
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
		this.wmsGoodsService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		WmsGoods wmsGoods = wmsGoodsService.getById(id);
		if(wmsGoods==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(wmsGoods);
	}

	 /**
	  * 通过是否含有字节点查询
	  *
	  * @return
	  */
	 @GetMapping(value = "/queryLastCategoryList")
	 public Result<?> queryLastCategoryList() {
		 Map<String, List> m = wmsGoodsCategoryService.queryList(1);
		 if(m==null) {
			 return Result.error("未找到对应数据");
		 }
		 return Result.ok(m);
	 }

	/**
	 * 通过所有启用的货物
	 *
	 * @return
	 */
	@GetMapping(value = "/queryList")
	public Result<?> queryList() {
		Map<String, List> m = wmsGoodsService.queryList();
		if(m==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(m);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param wmsGoods
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, WmsGoods wmsGoods) {
        return super.exportXls(request, wmsGoods, WmsGoods.class, "货物管理");
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
        return super.importExcel(request, response, WmsGoods.class);
    }

}
