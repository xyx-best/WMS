package org.jeecg.modules.baseinfo.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.baseinfo.entity.WmsArea;
import org.jeecg.modules.baseinfo.service.IWmsAreaService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.baseinfo.service.IWmsGoodsCategoryService;
import org.jeecg.modules.baseinfo.service.IWmsWarehouseService;
import org.jeecg.modules.sysmanage.service.IIdManageService;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Description: 区域管理
 * @Author: jeecg-boot
 * @Date:   2020-02-26
 * @Version: V1.0
 */
@RestController
@RequestMapping("/area/wmsArea")
@Slf4j
public class WmsAreaController extends JeecgController<WmsArea, IWmsAreaService> {
	@Autowired
	private IWmsAreaService wmsAreaService;

	@Autowired
	private IWmsWarehouseService wmsWarehouseService;

	@Autowired
	private IWmsGoodsCategoryService wmsGoodsCategoryService;

	@Autowired
	private IIdManageService idManageService;

	/**
	 * 分页列表查询
	 *
	 * @param wmsArea
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(WmsArea wmsArea,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<WmsArea> queryWrapper = QueryGenerator.initQueryWrapper(wmsArea, req.getParameterMap());
		Page<WmsArea> page = new Page<WmsArea>(pageNo, pageSize);
		IPage<WmsArea> pageList = wmsAreaService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	/**
	 *   添加
	 *
	 * @param wmsArea
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody WmsArea wmsArea) {
		String wId = idManageService.getCurIdByTableName("wms_area");
		wmsArea.setAreaId(wId);
		wmsAreaService.save(wmsArea);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param wmsArea
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody WmsArea wmsArea) {
		if (!wmsArea.getRackingStrategy().contains("1")) {
			wmsArea.setRkstraSize("");
		}
		if (!wmsArea.getRackingStrategy().contains("2")) {
			wmsArea.setRkstraColor("");
		}
		wmsAreaService.updateById(wmsArea);
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
		wmsAreaService.removeById(id);
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
		this.wmsAreaService.removeByIds(Arrays.asList(ids.split(",")));
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
		WmsArea wmsArea = wmsAreaService.getById(id);
		if(wmsArea==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(wmsArea);
	}

	 /**
	  * 查询所有启用的仓库
	  *
	  * @return
	  */
	 @GetMapping(value = "/queryList")
	 public Result<?> queryList() {
		 Map<String, List> m = wmsWarehouseService.queryList();
		 if(m==null) {
			 return Result.error("未找到对应数据");
		 }
		 return Result.ok(m);
	 }

	 /**
	  * 查询货物种类
	  *
	  * @return
	  */
	 @GetMapping(value = "/queryCategoryList")
	 public Result<?> queryCategoryList() {
		 Map<String, List> m = wmsGoodsCategoryService.queryList(2);
		 if(m==null) {
			 return Result.error("未找到对应数据");
		 }
		 return Result.ok(m);
	 }

    /**
    * 导出excel
    *
    * @param request
    * @param wmsArea
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, WmsArea wmsArea) {
        return super.exportXls(request, wmsArea, WmsArea.class, "区域管理");
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
        return super.importExcel(request, response, WmsArea.class);
    }

}
