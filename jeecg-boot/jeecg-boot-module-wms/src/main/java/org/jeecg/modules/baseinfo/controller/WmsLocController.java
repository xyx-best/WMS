package org.jeecg.modules.baseinfo.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.baseinfo.entity.WmsArea;
import org.jeecg.modules.baseinfo.service.IWmsAreaService;
import org.jeecg.modules.baseinfo.entity.WmsLoc;
import org.jeecg.modules.baseinfo.service.IWmsLocService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.sysmanage.service.IIdManageService;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.sysmanage.util.IdManageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Description: 货位管理
 * @Author: jeecg-boot
 * @Date:   2020-02-26
 * @Version: V1.0
 */
@RestController
@RequestMapping("/loc/wmsLoc")
@Slf4j
public class WmsLocController extends JeecgController<WmsLoc, IWmsLocService> {
	@Autowired
	private IWmsLocService wmsLocService;
	@Autowired
	private IWmsAreaService wmsAreaService;
	@Autowired
	private IIdManageService idManageService;

	/**
	 * 分页列表查询
	 *
	 * @param wmsLoc
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(WmsLoc wmsLoc,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<WmsLoc> queryWrapper = QueryGenerator.initQueryWrapper(wmsLoc, req.getParameterMap());
		queryWrapper.orderByDesc("loc_id");
		Page<WmsLoc> page = new Page<WmsLoc>(pageNo, pageSize);
		IPage<WmsLoc> pageList = wmsLocService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param wmsLoc
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody WmsLoc wmsLoc) {
		String wId = idManageService.getCurIdByTableName("wms_loc");
		wmsLoc.setLocId(wId);
		wmsLocService.save(wmsLoc);
		return Result.ok("添加成功！");
	}

	/**
	 *   批量添加
	 *
	 * @param wmsLoc
	 * @return
	 */
	@PostMapping(value = "/addBatch")
	public Result<?> addBatch(@RequestBody WmsLoc wmsLoc) {
		List<WmsLoc> wmsLocs = wmsLocService.getAddBatchWmsLocs(wmsLoc);
		wmsLocService.saveBatch(wmsLocs);
		return Result.ok("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param wmsLoc
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody WmsLoc wmsLoc) {
		wmsLocService.updateById(wmsLoc);
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
		wmsLocService.removeById(id);
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
		this.wmsLocService.removeByIds(Arrays.asList(ids.split(",")));
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
		WmsLoc wmsLoc = wmsLocService.getById(id);
		if(wmsLoc==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(wmsLoc);
	}

	/**
	 * 通过所有启用的货位
	 *
	 * @return
	 */
	@GetMapping(value = "/queryList")
	public Result<?> queryList() {
		Map<String, List> m = wmsLocService.queryList();
		if(m==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(m);
	}

	 /**
	  * 通过所有启用的区域
	  *
	  * @return
	  */
	 @GetMapping(value = "/queryAreaList")
	 public Result<?> queryAreaList() {
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
    * @param wmsLoc
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, WmsLoc wmsLoc) {
        return super.exportXls(request, wmsLoc, WmsLoc.class, "货位管理");
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
        return super.importExcel(request, response, WmsLoc.class);
    }

}
