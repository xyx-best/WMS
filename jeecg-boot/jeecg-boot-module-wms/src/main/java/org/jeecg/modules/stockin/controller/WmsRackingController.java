package org.jeecg.modules.stockin.controller;

import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.stock.entity.WmsStock;
import org.jeecg.modules.stock.service.IWmsStockService;
import org.jeecg.modules.stockin.entity.WmsRacking;
import org.jeecg.modules.stockin.entity.WmsStockin;
import org.jeecg.modules.stockin.entity.WmsStockindtl;
import org.jeecg.modules.stockin.service.IWmsRackingService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.stockin.service.IWmsStockinService;
import org.jeecg.modules.stockin.service.IWmsStockindtlService;
import org.jeecg.modules.transaction.entity.WmsTransaction;
import org.jeecg.modules.transaction.entity.WmsTransactionHis;
import org.jeecg.modules.transaction.service.IWmsTransactionHisService;
import org.jeecg.modules.transaction.service.IWmsTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.jeecg.modules.sysmanage.service.IIdManageService;

 /**
 * @Description: 上架表
 * @Author: jeecg-boot
 * @Date:   2020-03-10
 * @Version: V1.0
 */
@RestController
@RequestMapping("/wmsracking/wmsRacking")
@Slf4j
public class WmsRackingController extends JeecgController<WmsRacking, IWmsRackingService> {
	@Autowired
	private IWmsRackingService wmsRackingService;
	@Autowired
	private IIdManageService idManageService;
	@Autowired
	private IWmsStockService wmsStockService;
	@Autowired
	private IWmsStockindtlService wmsStockindtlService;
	@Autowired
	private IWmsTransactionService wmsTransactionService;
	@Autowired
	private IWmsStockinService wmsStockinService;
	@Autowired
	private IWmsTransactionHisService wmsTransactionHisService;
	
	/**
	 * 分页列表查询
	 *
	 * @param wmsRacking
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(WmsRacking wmsRacking,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<WmsRacking> queryWrapper = QueryGenerator.initQueryWrapper(wmsRacking, req.getParameterMap());
		Page<WmsRacking> page = new Page<WmsRacking>(pageNo, pageSize);
		IPage<WmsRacking> pageList = wmsRackingService.page(page, queryWrapper);
		return Result.ok(pageList);
	}


	/**
	 *   添加
	 *
	 * @param wmsRacking
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody WmsRacking wmsRacking) {
		String wId = idManageService.getCurIdByTableName("wms_racking");
		String code = idManageService.getCodeByTableName("wms_racking");
		wmsRacking.setRackingCode(code);
		wmsRacking.setRackingId(wId);
		wmsRackingService.save(wmsRacking);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param wmsRacking
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody WmsRacking wmsRacking) {
		wmsRackingService.updateById(wmsRacking);
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
		wmsRackingService.removeById(id);
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
		this.wmsRackingService.removeByIds(Arrays.asList(ids.split(",")));
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
		WmsRacking wmsRacking = wmsRackingService.getById(id);
		if(wmsRacking==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(wmsRacking);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param wmsRacking
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, WmsRacking wmsRacking) {
        return super.exportXls(request, wmsRacking, WmsRacking.class, "上架表");
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
        return super.importExcel(request, response, WmsRacking.class);
    }

	 /**
	  * 上架确认
	  *
	  * @param wmsRacking
	  * @return
	  */
    @PostMapping(value = "/confirmRacking")
    public Result<?> confirmRacking(@RequestBody WmsRacking wmsRacking){

    	//查找同一细单ID下的上架记录列表，如果列表中有没有入库的记录，则不更新明细表和入库表
		QueryWrapper<WmsRacking> qWRs = new QueryWrapper<WmsRacking>();
		qWRs.eq("sourcedtl_id", wmsRacking.getSourcedtlId());
		qWRs.ne("racking_id", wmsRacking.getRackingId());
		List<WmsRacking> wmsRkList = wmsRackingService.list(qWRs);
		//用于判断是否更新入库明细 入库总表
		boolean isUpdate = true;
		if (wmsRkList.isEmpty()) {     //只分配了一个货位
			isUpdate = true;
		} else {    // 分配了多个货位
			for (WmsRacking wr : wmsRkList) {
				if (!wr.getRackingState().equals("2")){
					isUpdate = false;
				}
			}
		}
		//更新入库明细 入库总表
		if (isUpdate) {
			//根据 细单ID 找到对应的入库明细记录
			QueryWrapper<WmsStockindtl> qWDtl = new QueryWrapper<WmsStockindtl>();
			qWDtl.eq("stockindtl_id", wmsRacking.getSourcedtlId());
			WmsStockindtl wmsStockindtl = wmsStockindtlService.getOne(qWDtl);

			wmsStockindtl.setStockinState("3");
			wmsStockindtlService.updateById(wmsStockindtl);

			//根据入库明细记录的 入库总表ID 找到对应的 入库总表记录
			QueryWrapper<WmsStockin> qWS = new QueryWrapper<WmsStockin>();
			qWS.eq("stockin_id", wmsStockindtl.getStockinId());
			WmsStockin wmsStockin = wmsStockinService.getOne(qWS);

			wmsStockin.setStockinState("3");
			wmsStockinService.updateById(wmsStockin);
		}

		//根据 上架ID 找到对应的 交易记录
		QueryWrapper<WmsTransaction> queryWrapper = new QueryWrapper<WmsTransaction>();
		queryWrapper.eq("move_id", wmsRacking.getRackingId());
		WmsTransaction wmsTransaction = wmsTransactionService.getOne(queryWrapper);

		wmsTransaction.setTransactionState("1");
		wmsTransactionService.updateById(wmsTransaction);

		//添加交易历史记录
		WmsTransactionHis wmsTransactionHis = wmsTransactionService.copyToHis(wmsTransaction);
		wmsTransactionHisService.save(wmsTransactionHis);

		//根据交易记录的 库存ID 找到对应的 库存记录
		QueryWrapper<WmsStock> qWT = new QueryWrapper<>();
		qWT.eq("stock_id", wmsTransaction.getStockId());
		WmsStock wmsStock = wmsStockService.getOne(qWT);

		wmsStock.setStockState("1");
		wmsStock.setStockinTime(new Date());
		wmsStockService.updateById(wmsStock);


		wmsRacking.setRackingState("2");
		wmsRacking.setStockinTime(new Date());
		wmsRackingService.updateById(wmsRacking);



		return Result.ok("确认成功，已更新上架表、库存表、入库表、入库明细表、交易表!");
	}
}
