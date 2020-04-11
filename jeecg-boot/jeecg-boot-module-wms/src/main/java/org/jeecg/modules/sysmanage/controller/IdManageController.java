package org.jeecg.modules.sysmanage.controller;

import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.sysmanage.entity.IdManage;
import org.jeecg.modules.sysmanage.service.IIdManageService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Description: 流水号id管理
 * @Author: jeecg-boot
 * @Date: 2020-03-06
 * @Version: V1.0
 */
@RestController
@RequestMapping("/sysmanage/idManage")
@Slf4j
public class IdManageController extends JeecgController<IdManage, IIdManageService> {
    @Autowired
    private IIdManageService idManageService;

    /**
     * 分页列表查询
     *
     * @param idManage
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/list")
    public Result<?> queryPageList(IdManage idManage,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<IdManage> queryWrapper = QueryGenerator.initQueryWrapper(idManage, req.getParameterMap());
        Page<IdManage> page = new Page<IdManage>(pageNo, pageSize);
        IPage<IdManage> pageList = idManageService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param idManage
     * @return
     */
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody IdManage idManage) {
        boolean flag = false; //判断长度是否足够长
        int count = idManage.getCount()==null ? 0 : idManage.getCount();
        flag = idManageService.combineNextId(idManage,count);  //组装下一个ID放进去
        if (flag){
            idManageService.save(idManage);
            return Result.ok("添加成功！");
        } else {
            return Result.error("长度不够，请加大长度！");
        }
    }




    /**
     * 编辑
     *
     * @param idManage
     * @return
     */
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody IdManage idManage) {
        boolean flag = false; //判断长度是否足够长
        flag = idManageService.combineNextId(idManage, idManage.getCount());  //组装下一个ID放进去
        if (flag){
            idManageService.updateById(idManage);
            return Result.ok("编辑成功！");
        } else {
            return Result.error("长度不够，请加大长度！");
        }
//        idManageService.updateById(idManage);
////        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        idManageService.removeById(id);
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
        this.idManageService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        IdManage idManage = idManageService.getById(id);
        if (idManage == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(idManage);
    }

    /**
     * 根据表名获取当前id
     * 即将nextID赋给currentID并返回字段 ，然后重新计算nextID保存
     * @param tableName
     * @return
     */
    @GetMapping(value = "/getCurrentId")
    public Result<?> getCurrentId(@RequestParam(name = "tableName", required = true) String tableName){
        String cid = idManageService.getCurIdByTableName(tableName);
        if (cid == null || cid.equals("")) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(cid);
    }


    /**
     * 导出excel
     *
     * @param request
     * @param idManage
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, IdManage idManage) {
        return super.exportXls(request, idManage, IdManage.class, "流水号id管理");
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
        return super.importExcel(request, response, IdManage.class);
    }

}
