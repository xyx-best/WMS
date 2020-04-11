package org.jeecg.modules.stockout.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.stock.entity.WmsStock;
import org.jeecg.modules.stock.service.IWmsStockService;
import org.jeecg.modules.stockout.entity.WmsPicking;
import org.jeecg.modules.stockout.entity.WmsStockout;
import org.jeecg.modules.stockout.entity.WmsStockoutdtl;
import org.jeecg.modules.stockout.service.IWmsPickingService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.stockout.service.IWmsStockoutService;
import org.jeecg.modules.stockout.service.IWmsStockoutdtlService;
import org.jeecg.modules.transaction.entity.WmsTransaction;
import org.jeecg.modules.transaction.entity.WmsTransactionHis;
import org.jeecg.modules.transaction.service.IWmsTransactionHisService;
import org.jeecg.modules.transaction.service.IWmsTransactionService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;

 /**
 * @Description: 拣选表
 * @Author: jeecg-boot
 * @Date:   2020-03-24
 * @Version: V1.0
 */
@RestController
@RequestMapping("/wmspicking/wmsPicking")
@Slf4j
public class WmsPickingController extends JeecgController<WmsPicking, IWmsPickingService> {
	@Autowired
	private IWmsPickingService wmsPickingService;

	@Autowired
	private IWmsStockoutService wmsStockoutService;
	@Autowired
	private IWmsStockoutdtlService wmsStockoutdtlService;
	@Autowired
	private IWmsTransactionService wmsTransactionService;
	@Autowired
	private IWmsTransactionHisService wmsTransactionHisService;
	@Autowired
	private IWmsStockService wmsStockService;


	/**
	 * 分页列表查询
	 *
	 * @param wmsPicking
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(WmsPicking wmsPicking,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<WmsPicking> queryWrapper = QueryGenerator.initQueryWrapper(wmsPicking, req.getParameterMap());
		Page<WmsPicking> page = new Page<WmsPicking>(pageNo, pageSize);
		IPage<WmsPicking> pageList = wmsPickingService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param wmsPicking
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody WmsPicking wmsPicking) {
		wmsPickingService.save(wmsPicking);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param wmsPicking
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody WmsPicking wmsPicking) {
		wmsPickingService.updateById(wmsPicking);
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
		wmsPickingService.removeById(id);
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
		this.wmsPickingService.removeByIds(Arrays.asList(ids.split(",")));
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
		WmsPicking wmsPicking = wmsPickingService.getById(id);
		if(wmsPicking==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(wmsPicking);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param wmsPicking
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, WmsPicking wmsPicking) {
        return super.exportXls(request, wmsPicking, WmsPicking.class, "拣选表");
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
        return super.importExcel(request, response, WmsPicking.class);
    }

	 /**
	  * 拣选确认
	  *
	  * @param wmsPicking
	  * @return
	  */
	 @PostMapping(value = "/confirmPicking")
	 public Result<?> confirmPicking(@RequestBody WmsPicking wmsPicking){

	 	wmsPickingService.delet("1");

		 //查找同一细单ID下的拣选记录列表，如果列表中有 没有出库的记录，则不更新明细表和出库表
		 QueryWrapper<WmsPicking> qWRs = new QueryWrapper<WmsPicking>();
		 qWRs.eq("sourcedtl_id", wmsPicking.getSourcedtlId());
		 qWRs.ne("picking_id", wmsPicking.getPickingId());
		 List<WmsPicking> wmsPkList = wmsPickingService.list(qWRs);
		 //用于判断是否更新出库明细 出库总表
		 boolean isUpdate = true;
		 if (wmsPkList.isEmpty()) {
			 isUpdate = true;
		 } else {    // 分配了多个货位
			 for (WmsPicking wp : wmsPkList) {
				 if (!wp.getPickingState().equals("2")){
					 isUpdate = false;
				 }
			 }
		 }
		 //更新出库明细 出库总表
		 if (isUpdate) {
			 //根据 细单ID 找到对应的出库明细记录
			 QueryWrapper<WmsStockoutdtl> qWDtl = new QueryWrapper<WmsStockoutdtl>();
			 qWDtl.eq("stockoutdtl_id", wmsPicking.getSourcedtlId());
			 WmsStockoutdtl wmsStockoutdtl = wmsStockoutdtlService.getOne(qWDtl);

			 wmsStockoutdtl.setStockoutState("3");
			 wmsStockoutdtlService.updateById(wmsStockoutdtl);

			 //根据出库明细记录的 出库总表ID 找到对应的 出库总表记录
			 QueryWrapper<WmsStockout> qWS = new QueryWrapper<WmsStockout>();
			 qWS.eq("stockout_id", wmsStockoutdtl.getStockoutId());
			 WmsStockout wmsStockout = wmsStockoutService.getOne(qWS);

			 wmsStockout.setStockoutState("3");
			 wmsStockoutService.updateById(wmsStockout);
		 }

		 //根据 拣选ID 找到对应的 交易记录
		 QueryWrapper<WmsTransaction> queryWrapper = new QueryWrapper<WmsTransaction>();
		 queryWrapper.eq("move_id", wmsPicking.getPickingId());
		 WmsTransaction wmsTransaction = wmsTransactionService.getOne(queryWrapper);

		 wmsTransaction.setTransactionState("1");
		 wmsTransactionService.updateById(wmsTransaction);

		 //添加交易历史记录
		 WmsTransactionHis wmsTransactionHis = wmsTransactionService.copyToHis(wmsTransaction);
		 wmsTransactionHisService.save(wmsTransactionHis);

		 //根据交易记录的 库存ID 找到对应的 库存记录
		 QueryWrapper<WmsStock> qWT = new QueryWrapper<>();
		 qWT.eq("stock_id", wmsTransaction.getStockId());
		 List<WmsStock> wmsStockList = wmsStockService.list(qWT);

		 //删除库存记录
		 for (WmsStock ws : wmsStockList) {
			 if (ws.getStockState().equals("2")) {
				 wmsStockService.removeById(ws.getId());
			 }
		 }

		 wmsPicking.setPickingState("2");
		 wmsPicking.setStockoutTime(new Date());
		 wmsPickingService.updateById(wmsPicking);


		 return Result.ok("确认成功，已更新上架表、库存表、出库表、出库明细表、交易表!");
	 }

}
