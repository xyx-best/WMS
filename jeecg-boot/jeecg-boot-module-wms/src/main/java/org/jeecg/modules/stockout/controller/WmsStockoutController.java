package org.jeecg.modules.stockout.controller;

import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.modules.baseinfo.entity.WmsArea;
import org.jeecg.modules.baseinfo.entity.WmsLoc;
import org.jeecg.modules.baseinfo.service.IWmsAreaService;
import org.jeecg.modules.baseinfo.service.IWmsLocService;
import org.jeecg.modules.stock.entity.WmsStock;
import org.jeecg.modules.stock.service.IWmsStockService;
import org.jeecg.modules.stockout.entity.WmsPicking;
import org.jeecg.modules.stockout.service.IWmsPickingService;
import org.jeecg.modules.sysmanage.util.IdManageUtil;
import org.jeecg.modules.transaction.entity.WmsTransaction;
import org.jeecg.modules.transaction.service.IWmsTransactionService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.vo.LoginUser;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.stockout.entity.WmsStockoutdtl;
import org.jeecg.modules.stockout.entity.WmsStockout;
import org.jeecg.modules.stockout.vo.WmsStockoutPage;
import org.jeecg.modules.stockout.service.IWmsStockoutService;
import org.jeecg.modules.stockout.service.IWmsStockoutdtlService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import com.alibaba.fastjson.JSON;

 /**
 * @Description: 出库总表
 * @Author: jeecg-boot
 * @Date:   2020-03-24
 * @Version: V1.0
 */
@RestController
@RequestMapping("/wmsstockout/wmsStockout")
@Slf4j
public class WmsStockoutController {
	@Autowired
	private IWmsStockoutService wmsStockoutService;
	@Autowired
	private IWmsStockoutdtlService wmsStockoutdtlService;


	 @Autowired
	 private IWmsLocService wmsLocService;
	 @Autowired
	 private IWmsStockService wmsStockService;
	 @Autowired
	 private IWmsAreaService wmsAreaService;
	 @Autowired
	 private IWmsPickingService wmsPickingService;
	 @Autowired
	 private IWmsTransactionService wmsTransactionService;


	 /**
	 * 分页列表查询
	 *
	 * @param wmsStockout
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(WmsStockout wmsStockout,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<WmsStockout> queryWrapper = QueryGenerator.initQueryWrapper(wmsStockout, req.getParameterMap());
		Page<WmsStockout> page = new Page<WmsStockout>(pageNo, pageSize);
		IPage<WmsStockout> pageList = wmsStockoutService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param wmsStockoutPage
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody WmsStockoutPage wmsStockoutPage) {
		WmsStockout wmsStockout = new WmsStockout();
		wmsStockoutPage.setStockoutId(IdManageUtil.getId("wms_stockout"));
		wmsStockoutPage.setStockoutCode(IdManageUtil.getCode("wms_stockout"));
		BeanUtils.copyProperties(wmsStockoutPage, wmsStockout);
		wmsStockoutService.saveMain(wmsStockout, wmsStockoutPage.getWmsStockoutdtlList());
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param wmsStockoutPage
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody WmsStockoutPage wmsStockoutPage) {
		WmsStockout wmsStockout = new WmsStockout();
		BeanUtils.copyProperties(wmsStockoutPage, wmsStockout);
		WmsStockout wmsStockoutEntity = wmsStockoutService.getById(wmsStockout.getId());
		if(wmsStockoutEntity==null) {
			return Result.error("未找到对应数据");
		}
		wmsStockoutService.updateMain(wmsStockout, wmsStockoutPage.getWmsStockoutdtlList());
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
		wmsStockoutService.delMain(id);
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
		this.wmsStockoutService.delBatchMain(Arrays.asList(ids.split(",")));
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
		WmsStockout wmsStockout = wmsStockoutService.getById(id);
		if(wmsStockout==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(wmsStockout);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryWmsStockoutdtlByMainId")
	public Result<?> queryWmsStockoutdtlListByMainId(@RequestParam(name="id",required=true) String id) {
		List<WmsStockoutdtl> wmsStockoutdtlList = wmsStockoutdtlService.selectByMainId(id);
		return Result.ok(wmsStockoutdtlList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param wmsStockout
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, WmsStockout wmsStockout) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<WmsStockout> queryWrapper = QueryGenerator.initQueryWrapper(wmsStockout, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //Step.2 获取导出数据
      List<WmsStockout> queryList = wmsStockoutService.list(queryWrapper);
      // 过滤选中数据
      String selections = request.getParameter("selections");
      List<WmsStockout> wmsStockoutList = new ArrayList<WmsStockout>();
      if(oConvertUtils.isEmpty(selections)) {
          wmsStockoutList = queryList;
      }else {
          List<String> selectionList = Arrays.asList(selections.split(","));
          wmsStockoutList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
      }

      // Step.3 组装pageList
      List<WmsStockoutPage> pageList = new ArrayList<WmsStockoutPage>();
      for (WmsStockout main : wmsStockoutList) {
          WmsStockoutPage vo = new WmsStockoutPage();
          BeanUtils.copyProperties(main, vo);
          List<WmsStockoutdtl> wmsStockoutdtlList = wmsStockoutdtlService.selectByMainId(main.getId());
          vo.setWmsStockoutdtlList(wmsStockoutdtlList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "出库总表列表");
      mv.addObject(NormalExcelConstants.CLASS, WmsStockoutPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("出库总表数据", "导出人:"+sysUser.getRealname(), "出库总表"));
      mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
      return mv;
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
      MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
      Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
      for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
          MultipartFile file = entity.getValue();// 获取上传文件对象
          ImportParams params = new ImportParams();
          params.setTitleRows(2);
          params.setHeadRows(1);
          params.setNeedSave(true);
          try {
              List<WmsStockoutPage> list = ExcelImportUtil.importExcel(file.getInputStream(), WmsStockoutPage.class, params);
              for (WmsStockoutPage page : list) {
                  WmsStockout po = new WmsStockout();
                  BeanUtils.copyProperties(page, po);
                  wmsStockoutService.saveMain(po, page.getWmsStockoutdtlList());
              }
              return Result.ok("文件导入成功！数据行数:" + list.size());
          } catch (Exception e) {
              log.error(e.getMessage(),e);
              return Result.error("文件导入失败:"+e.getMessage());
          } finally {
              try {
                  file.getInputStream().close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
      }
      return Result.ok("文件导入失败！");
    }

	 /**
	  * 生成拣选和库存记录
	  *
	  * @param wmsStockout
	  * @return
	  */
	 @PostMapping(value = "/createRecord")
	 public Result<?> createRecord(@RequestBody WmsStockout wmsStockout) {

		 if (wmsStockout.getStockoutState().equals("2") || wmsStockout.getStockoutState().equals("3")) {
			 return Result.error("该出库记录已生成拣选和库存记录，请勿重复生成!!!");
		 }

		 String stockoutId = wmsStockout.getStockoutId();
		 String stockoutCode = wmsStockout.getStockoutCode();
		 //根据出库ID、编码查询出库明细记录
		 Map<String, List> m = wmsStockoutdtlService.queryByStockout(stockoutId, stockoutCode);
		 WmsStockoutdtl wmsStockoutdtl = null;
		 if (m.get("list").size() != 0) {
			 //如果有出库明细记录 ，则暂时存放
			 wmsStockoutdtl = (WmsStockoutdtl) m.get("list").get(0);
		 } else {
			 return Result.error("未找到对应的出库明细记录");
		 }
		 //根据出库明细的货物类型查询库存
		 List<WmsStock> wlList = wmsStockService.queryByGoods(wmsStockoutdtl.getGoodsId(), wmsStockoutdtl.getGoodsCode(), wmsStockoutdtl.getGoodsQuantity());
		 if (wmsStockService.getStockOut(wmsStockoutdtl, wlList, null)) return Result.error("没有足够的货物库存!!!");

		 //修改 出库记录的 状态 为分配完成
		 wmsStockout.setStockoutState("2");
		 wmsStockoutService.updateById(wmsStockout);

		 wmsStockoutdtl.setStockoutState("2");
		 wmsStockoutdtlService.updateById(wmsStockoutdtl);

		 return Result.ok("生成记录成功！");
	 }

 }
