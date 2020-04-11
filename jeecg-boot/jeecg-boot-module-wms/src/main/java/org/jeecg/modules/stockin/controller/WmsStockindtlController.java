package org.jeecg.modules.stockin.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.stockin.entity.WmsStockindtl;
import org.jeecg.modules.stockin.service.IWmsStockindtlService;

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
 * @Description: 入库明细表
 * @Author: jeecg-boot
 * @Date:   2020-03-10
 * @Version: V1.0
 */
@RestController
@RequestMapping("/wmsstockindtl/wmsStockindtl")
@Slf4j
public class WmsStockindtlController extends JeecgController<WmsStockindtl, IWmsStockindtlService> {
	@Autowired
	private IWmsStockindtlService wmsStockindtlService;
	@Autowired
	private IIdManageService idManageService;
	
	/**
	 * 分页列表查询
	 *
	 * @param wmsStockindtl
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(WmsStockindtl wmsStockindtl,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<WmsStockindtl> queryWrapper = QueryGenerator.initQueryWrapper(wmsStockindtl, req.getParameterMap());
		Page<WmsStockindtl> page = new Page<WmsStockindtl>(pageNo, pageSize);
		IPage<WmsStockindtl> pageList = wmsStockindtlService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param wmsStockindtl
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody WmsStockindtl wmsStockindtl) {
		String wId = idManageService.getCurIdByTableName("wms_stockindtl"); //获取自增id  十位数
		String code = idManageService.getCodeByTableName("wms_stockindtl");   //获取编码 （年月日+自增的4位数）
		wmsStockindtl.setStockindtlCode(code);
		wmsStockindtl.setStockindtlId(wId);
		wmsStockindtlService.save(wmsStockindtl);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param wmsStockindtl
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody WmsStockindtl wmsStockindtl) {
		wmsStockindtlService.updateById(wmsStockindtl);
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
		wmsStockindtlService.removeById(id);
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
		this.wmsStockindtlService.removeByIds(Arrays.asList(ids.split(",")));
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
		WmsStockindtl wmsStockindtl = wmsStockindtlService.getById(id);
		if(wmsStockindtl==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(wmsStockindtl);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param wmsStockindtl
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, WmsStockindtl wmsStockindtl) {
        return super.exportXls(request, wmsStockindtl, WmsStockindtl.class, "入库明细表");
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
        return super.importExcel(request, response, WmsStockindtl.class);
    }

}
