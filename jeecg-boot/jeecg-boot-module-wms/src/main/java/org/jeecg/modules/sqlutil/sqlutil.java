package org.jeecg.modules.sqlutil;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.commons.collections.map.HashedMap;
import org.jeecg.modules.baseinfo.entity.WmsLoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class sqlutil {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @DS("multi-datasource1")
    public List<Map<String, Object>> getInfoBySqlWithOrderId(String sql, String tbName){
        List<Map<String, Object>> info = jdbcTemplate.queryForList(sql);
//        StringBuffer updateSql = new StringBuffer("UPDATE  "+ tbName + " set has_read = 1 where order_id in (");
//        for (int i = 0; i < info.size(); i++) {
//            updateSql.append(info.get(i).get("order_id"));
//            if (i == (info.size() - 1)) {
//                updateSql.append(")");
//                jdbcTemplate.execute(updateSql.toString());
//                break;
//            }
//            updateSql.append(",");
//        }
        return info;
    }

    @DS("multi-datasource1")
    public List<Map<String, Object>> getInfoBySqlWithTrayNum(String sql, String tbName){
        List<Map<String, Object>> info = jdbcTemplate.queryForList(sql);
//        StringBuffer updateSql = new StringBuffer("UPDATE  "+ tbName + " set has_read = 1 where tray_number in (");
//        for (int i = 0; i < info.size(); i++) {
//            updateSql.append(info.get(i).get("tray_number"));
//            if (i == (info.size() - 1)) {
//                updateSql.append(")");
//                jdbcTemplate.execute(updateSql.toString());
//                break;
//            }
//            updateSql.append(",");
//        }
        return info;
    }

    @DS("multi-datasource1")
    public void delAndInsWms(String tbName, Map<String, Object> m){
        jdbcTemplate.execute("delete from " + tbName + " where order_id = " + m.get("order_id"));
        jdbcTemplate.execute("insert into " + tbName + "_his(order_id, goods_code, goods_quantity, tray_number, goods_level) values("
                + m.get("order_id") + ", " + m.get("goods_code") +", "+m.get("goods_quantity")  +", "+ m.get("tray_number") +", "+ m.get("goods_level")   +")");
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
        String sql = "insert into " + tbName + " values(" + orderId +", " + trayNumber + ", " + rackLane + ", " + side +", "+column+", "+row + ")";
        jdbcTemplate.execute(sql);
    }

    @DS("multi-datasource1")
    public void delAndInsFinished(String tbName, int id, int trayNumber, Date finishDate) {
        jdbcTemplate.execute("delete from " + tbName + " where id = " + id);
        jdbcTemplate.execute("insert into " + tbName + "_his values("
                + id + ", " + trayNumber + ", '" + finishDate  +"')");
    }

    @DS("multi-datasource1")
    public void execute(String sql) {
        jdbcTemplate.execute(sql);
    }
}
