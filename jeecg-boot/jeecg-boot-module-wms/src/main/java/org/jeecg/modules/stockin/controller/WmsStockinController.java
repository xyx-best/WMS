package org.jeecg.modules.stockin.controller;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.modules.baseinfo.service.IWmsAreaService;
import org.jeecg.modules.baseinfo.service.IWmsGoodsService;
import org.jeecg.modules.baseinfo.service.IWmsLocService;
import org.jeecg.modules.stock.service.IWmsStockService;
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
import org.jeecg.modules.stockin.entity.WmsStockindtl;
import org.jeecg.modules.stockin.entity.WmsStockin;
import org.jeecg.modules.stockin.vo.WmsStockinPage;
import org.jeecg.modules.stockin.service.IWmsStockinService;
import org.jeecg.modules.stockin.service.IWmsStockindtlService;
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
import org.jeecg.modules.sysmanage.service.IIdManageService;

/**
 * @Description: 入库表
 * @Author: jeecg-boot
 * @Date: 2020-03-21
 * @Version: V1.0
 */
@RestController
@RequestMapping("/wmsStockin/wmsStockin")
@Slf4j
public class WmsStockinController {
    @Autowired
    private IWmsStockinService wmsStockinService;
    @Autowired
    private IWmsStockindtlService wmsStockindtlService;
    @Autowired
    private IIdManageService idManageService;

    @Autowired
    private IWmsLocService wmsLocService;
    @Autowired
    private IWmsStockService wmsStockService;
    @Autowired
    private IWmsAreaService wmsAreaService;
    @Autowired
    private IWmsGoodsService wmsGoodsService;

    /**
     * 分页列表查询
     *
     * @param wmsStockin
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/list")
    public Result<?> queryPageList(WmsStockin wmsStockin,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<WmsStockin> queryWrapper = QueryGenerator.initQueryWrapper(wmsStockin, req.getParameterMap());
        Page<WmsStockin> page = new Page<WmsStockin>(pageNo, pageSize);
        IPage<WmsStockin> pageList = wmsStockinService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param wmsStockinPage
     * @return
     */
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody WmsStockinPage wmsStockinPage) {
        WmsStockin wmsStockin = new WmsStockin();
        wmsStockinPage.setStockinId(idManageService.getCurIdByTableName("wms_stockin"));
        wmsStockinPage.setStockinCode(idManageService.getCodeByTableName("wms_stockin"));
        BeanUtils.copyProperties(wmsStockinPage, wmsStockin);
        wmsStockinService.saveMain(wmsStockin, wmsStockinPage.getWmsStockindtlList());
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param wmsStockinPage
     * @return
     */
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody WmsStockinPage wmsStockinPage) {
        WmsStockin wmsStockin = new WmsStockin();
        BeanUtils.copyProperties(wmsStockinPage, wmsStockin);
        WmsStockin wmsStockinEntity = wmsStockinService.getById(wmsStockin.getId());
        if (wmsStockinEntity == null) {
            return Result.error("未找到对应数据");
        }
        wmsStockinService.updateMain(wmsStockin, wmsStockinPage.getWmsStockindtlList());
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        wmsStockinService.delMain(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.wmsStockinService.delBatchMain(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        WmsStockin wmsStockin = wmsStockinService.getById(id);
        if (wmsStockin == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(wmsStockin);

    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/queryWmsStockindtlByMainId")
    public Result<?> queryWmsStockindtlListByMainId(@RequestParam(name = "id", required = true) String id) {
        List<WmsStockindtl> wmsStockindtlList = wmsStockindtlService.selectByMainId(id);
        return Result.ok(wmsStockindtlList);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param wmsStockin
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, WmsStockin wmsStockin) {
        // Step.1 组装查询条件查询数据
        QueryWrapper<WmsStockin> queryWrapper = QueryGenerator.initQueryWrapper(wmsStockin, request.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        //Step.2 获取导出数据
        List<WmsStockin> queryList = wmsStockinService.list(queryWrapper);
        // 过滤选中数据
        String selections = request.getParameter("selections");
        List<WmsStockin> wmsStockinList = new ArrayList<WmsStockin>();
        if (oConvertUtils.isEmpty(selections)) {
            wmsStockinList = queryList;
        } else {
            List<String> selectionList = Arrays.asList(selections.split(","));
            wmsStockinList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
        }

        // Step.3 组装pageList
        List<WmsStockinPage> pageList = new ArrayList<WmsStockinPage>();
        for (WmsStockin main : wmsStockinList) {
            WmsStockinPage vo = new WmsStockinPage();
            BeanUtils.copyProperties(main, vo);
            List<WmsStockindtl> wmsStockindtlList = wmsStockindtlService.selectByMainId(main.getId());
            vo.setWmsStockindtlList(wmsStockindtlList);
            pageList.add(vo);
        }

        // Step.4 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "入库表列表");
        mv.addObject(NormalExcelConstants.CLASS, WmsStockinPage.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("入库表数据", "导出人:" + sysUser.getRealname(), "入库表"));
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
                List<WmsStockinPage> list = ExcelImportUtil.importExcel(file.getInputStream(), WmsStockinPage.class, params);
                for (WmsStockinPage page : list) {
                    WmsStockin po = new WmsStockin();
                    BeanUtils.copyProperties(page, po);
                    wmsStockinService.saveMain(po, page.getWmsStockindtlList());
                }
                return Result.ok("文件导入成功！数据行数:" + list.size());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return Result.error("文件导入失败:" + e.getMessage());
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
     * 生成上架和库存记录
     *
     * @param wmsStockin
     * @return
     */
    @PostMapping(value = "/createRecord")
    public Result<?> createRecord(@RequestBody WmsStockin wmsStockin) {

        if (wmsStockin.getStockinState().equals("2") || wmsStockin.getStockinState().equals("3")) {
            return Result.error("该入库记录已生成上架和库存记录，请勿重复生成!!!");
        }

        String stockinId = wmsStockin.getStockinId();
        String stockinCode = wmsStockin.getStockinCode();
        //根据入库ID、编码查询入库明细记录
        Map<String, List> m = wmsStockindtlService.queryByStockin(stockinId, stockinCode);
        WmsStockindtl wmsStockindtl = null;
        if (m.get("list").size() != 0) {
            //如果有入库明细记录 ，则暂时存放
            wmsStockindtl = (WmsStockindtl) m.get("list").get(0);
        } else {
            return Result.error("未找到对应入库明细记录");
        }

        Result r = wmsStockinService.execStockin(wmsStockindtl, null);

        wmsStockin.setStockinState("2");
        wmsStockinService.updateById(wmsStockin);

        //找到对应的 入库明细记录 并更新状态
        WmsStockindtl wmsStockindtl1 = (WmsStockindtl) wmsStockindtlService.queryByStockin(wmsStockin.getStockinId(), wmsStockin.getStockinCode()).get("list").get(0);
        wmsStockindtl.setStockinState("2");
        wmsStockindtlService.updateById(wmsStockindtl);

        return r;
    }
}
