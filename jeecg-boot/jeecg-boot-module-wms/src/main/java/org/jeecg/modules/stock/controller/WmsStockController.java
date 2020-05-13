package org.jeecg.modules.stock.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.baseinfo.service.IWmsAreaService;
import org.jeecg.modules.stock.entity.WmsStock;
import org.jeecg.modules.stock.service.IWmsStockService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.jeecg.modules.sysmanage.service.IIdManageService;


 /**
 * @Description: 库存表
 * @Author: jeecg-boot
 * @Date:   2020-03-09
 * @Version: V1.0
 */
@RestController
@RequestMapping("/stock/wmsStock")
@Slf4j
public class WmsStockController extends JeecgController<WmsStock, IWmsStockService> {
	@Autowired
	private IWmsStockService wmsStockService;
	@Autowired
	private IIdManageService idManageService;
	@Autowired
	private IWmsAreaService wmsAreaService;
	
	/**
	 * 分页列表查询
	 *
	 * @param wmsStock
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(WmsStock wmsStock,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<WmsStock> queryWrapper = QueryGenerator.initQueryWrapper(wmsStock, req.getParameterMap());
		Page<WmsStock> page = new Page<WmsStock>(pageNo, pageSize);
		IPage<WmsStock> pageList = wmsStockService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param wmsStock
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody WmsStock wmsStock) {
		String wId = idManageService.getCurIdByTableName("wms_stock");
		wmsStock.setStockId(wId);
		wmsStockService.save(wmsStock);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param wmsStock
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody WmsStock wmsStock) {
		wmsStockService.updateById(wmsStock);
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
		wmsStockService.removeById(id);
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
		this.wmsStockService.removeByIds(Arrays.asList(ids.split(",")));
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
		WmsStock wmsStock = wmsStockService.getById(id);
		if(wmsStock==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(wmsStock);
	}

	 /**
	  * 通过id查询
	  *
	  * @param id
	  * @return
	  */
	 @GetMapping(value = "/queryAreaList")
	 public Result<?> queryAreaList(@RequestParam(name="id",required=false) String id) {
		 Map<String, List> m = wmsAreaService.queryList();
		 if(m==null) {
			 return Result.error("未找到对应数据");
		 }
		 return Result.ok(m);
	 }

    /**
    * 导出excel
    *
    * @param request
    * @param wmsStock
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, WmsStock wmsStock) {
        return super.exportXls(request, wmsStock, WmsStock.class, "库存表");
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
        return super.importExcel(request, response, WmsStock.class);
    }

	 /**
	  * 根据时间范围 获取库存量
	  * @param req
	  * @return
	  */
	 @GetMapping(value = "/getStockInfoByTime")
	 public Result<?> getStockInfoByTime(HttpServletRequest req){
		 List<Map<String, Integer>> list = null;
		 if (req.getParameterMap().containsKey("dateRange")) {
			 String stime = req.getParameterMap().get("start")[0];
			 String etime = req.getParameterMap().get("end")[0];
			 list = wmsStockService.queryByRange(stime, etime);
		 }
		 if (req.getParameterMap().containsKey("type")) {
			 list = wmsStockService.queryByTime(req.getParameterMap().get("type")[0]);
		 }
		 return Result.ok(list);
	 }

}
