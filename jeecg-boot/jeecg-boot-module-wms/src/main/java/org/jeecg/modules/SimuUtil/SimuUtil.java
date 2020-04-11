package org.jeecg.modules.SimuUtil;

import com.baomidou.dynamic.datasource.annotation.DS;

@DS("multi-datasource1")
public class SimuUtil {



//    //获取入库信息 分配货位
//    @DS("multi-datasource1")
//    public List<Map<String, Object>> getWmsStockin() {
//        return  jdbcTemplate1.queryForList("select * from wms_stockin");
//    }
//
//    //获取出库信息 分配货位
//    public static List<Map<String, Object>> getWmsStockout() {
//        return  jdbcTemplate.queryForList("select * from wms_stockout");
//    }
//
//    //添加入库历史
//    public static void saveWmsStockinHis() {
//        jdbcTemplate.execute("insert into wms_stockin_his values('a','b','11')");
//    }
//
//    //添加出库历史
//    public static void saveWmsStockoutHis() {
//        jdbcTemplate.execute("insert into wms_stockout_his values('a','b','11')");
//    }
//
//    //返回仿真入库货位信息
//    @DS("multi-datasource1")
//    public static void saveSimuStockin(String areaCode, WmsLoc wmsLoc) {
//        String[] site = wmsLoc.getLocLoc().split("-");
//        Integer rackLane = Integer.parseInt(areaCode);   //巷道
//        Integer side = 1;   //左边
//        if (site[0].equals("R") || site[0] == "R") {
//            side = 2;      //右边
//        }
//        Integer column = Integer.parseInt(site[1]);    //列数
//        Integer row = Integer.parseInt(site[2]);       //行数
//        jdbcTemplate.execute("insert into simu_stockin values(0, " + rackLane + ", " + side +", "+column+", "+row +")");
//    }
//
//    //返回仿真出库货位信息
//    public static void saveSimuStockinout() {
//        jdbcTemplate.execute("insert into simu_stockout values('a','b','11')");
//    }
}
