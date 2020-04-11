package org.jeecg.modules.baseinfo.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.baseinfo.entity.WmsWarehouse;
import org.jeecg.modules.baseinfo.service.IWmsWarehouseService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.sysmanage.service.IIdManageService;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Description: 仓库管理
 * @Author: jeecg-boot
 * @Date:   2020-02-26
 * @Version: V1.0
 */
@RestController
@RequestMapping("/warehouse/wmsWarehouse")
@Slf4j
public class WmsWarehouseController extends JeecgController<WmsWarehouse, IWmsWarehouseService> {
	@Autowired
	private IWmsWarehouseService wmsWarehouseService;

	@Autowired
	private IIdManageService idManageService;

	/**
	 * 分页列表查询
	 *
	 * @param wmsWarehouse
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(WmsWarehouse wmsWarehouse,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<WmsWarehouse> queryWrapper = QueryGenerator.initQueryWrapper(wmsWarehouse, req.getParameterMap());
		Page<WmsWarehouse> page = new Page<WmsWarehouse>(pageNo, pageSize);
		IPage<WmsWarehouse> pageList = wmsWarehouseService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param wmsWarehouse
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody WmsWarehouse wmsWarehouse) {
		String wId = idManageService.getCurIdByTableName("wms_warehouse");
		wmsWarehouse.setWarehouseId(wId);
		wmsWarehouseService.save(wmsWarehouse);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param wmsWarehouse
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody WmsWarehouse wmsWarehouse) {
		wmsWarehouseService.updateById(wmsWarehouse);
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
		wmsWarehouseService.removeById(id);
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
		this.wmsWarehouseService.removeByIds(Arrays.asList(ids.split(",")));
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
		WmsWarehouse wmsWarehouse = wmsWarehouseService.getById(id);
		if(wmsWarehouse==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(wmsWarehouse);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param wmsWarehouse
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, WmsWarehouse wmsWarehouse) {
        return super.exportXls(request, wmsWarehouse, WmsWarehouse.class, "仓库管理");
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
        return super.importExcel(request, response, WmsWarehouse.class);
    }

}
