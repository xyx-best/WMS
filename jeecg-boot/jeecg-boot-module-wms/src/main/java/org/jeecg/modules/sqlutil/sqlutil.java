package org.jeecg.modules.sqlutil;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.jeecg.modules.baseinfo.entity.WmsLoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class sqlutil {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @DS("multi-datasource1")
    public List<Map<String, Object>> getInfoBySql(String sql){
        List<Map<String, Object>> info = jdbcTemplate.queryForList(sql);
        return info;
    }

    @DS("multi-datasource1")
    public void delAndInsWms(String tbName, Map<String, Object> m){
        jdbcTemplate.execute("delete from " + tbName + " where order_id = " + m.get("order_id"));
        jdbcTemplate.execute("insert into " + tbName + "_his(order_id, goods_code, goods_quantity, tray_number) values("
                + m.get("order_id") + ", " + m.get("goods_code") +", "+m.get("goods_quantity")  +", "+ m.get("tray_number")  +")");
    }

    @DS("multi-datasource1")
    public void saveSimuStockinout(String tbName, String areaCode, WmsLoc wmsLoc, Integer orderId, String trayNumber) {
        String[] site = wmsLoc.getLocLoc().split("-");
        Integer rackLane = Integer.parseInt(areaCode);   //巷道
        Integer side = 1;   //左边
        if (site[0].equals("R") || site[0] == "R") {
            side = 2;      //右边
        }
        Integer column = Integer.parseInt(site[1]);    //列数
        Integer row = Integer.parseInt(site[2]);       //行数
        String sql = "insert into " + tbName + " values(" + rackLane + ", " + side +", "+column+", "+row + ", " + orderId +", " + trayNumber +")";
        jdbcTemplate.execute(sql);
    }

    @DS("multi-datasource1")
    public void delAndInsFinished(String tbName, String trayNumber, Date finishDate) {
        jdbcTemplate.execute("delete from " + tbName + " where tray_number = " + trayNumber);
        jdbcTemplate.execute("insert into " + tbName + "_his(tray_number, finish_date) values('"
                + trayNumber + "', '" + finishDate  +"')");
    }

    @DS("multi-datasource1")
    public void delNullTNFinished(String tbName) {
        jdbcTemplate.execute("delete from " + tbName + " where tray_number is null");
    }
}
