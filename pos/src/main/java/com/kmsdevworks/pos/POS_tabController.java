package com.kmsdevworks.pos;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class POS_tabController {

    public static class Order {
        private String userID;
        private String product_id;
        private String tabName;

        public String getUserID() {return userID;}
        public String getProduct_id() {return product_id;}
        public String getTabName() {return tabName;}

    }

    @PostMapping("/currentOrderShow")
    public String currentOrderShow(@RequestBody Order order) throws SQLException {

        String data = "";

        String tabName = "";

        MySQLite MySQLiteConnection = new MySQLite("pos");

        ResultSet rs = MySQLiteConnection.selectSQL("tb_currentOrder");
        while (rs.next()){
            if (rs.getString("tab_name") != null && !rs.getString("tab_name").isEmpty() && tabName.isEmpty()) {tabName = "+" + rs.getString("tab_name");}
            data += productCurrent_layout(rs.getString("product_name"),rs.getString("qty"),rs.getInt("total_price"), rs.getString("product_id"));

        } MySQLiteConnection.close(); rs.close();

        if (tabName.isEmpty()) {tabName = "+-- NO NAME --";}

        return data + tabName;
    }

    @PostMapping("/retrieveOrderShow")
    public String retrieveOrderShow(@RequestBody Order order) throws SQLException {
        String data = "";
        int num_row = 0;
        String tab_name = "";

        MySQLite MySQLiteConnection = new MySQLite("pos");

        ResultSet rs = MySQLiteConnection.manualSQL_Result("SELECT tab_name, SUM(total_price), qty, date_time FROM tb_temp GROUP BY tab_name ORDER BY id ASC;");
        while (rs.next()){

            String inputDateStr = rs.getString("date_time");
            LocalDateTime fromDateTime = LocalDateTime.parse(inputDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            int totalHours = Integer.parseInt(String.valueOf(Duration.between(fromDateTime, LocalDateTime.now()).getSeconds() / 60));

            data += String.format("""
                    <tr>
                        <td class="table-light fw-bold text-dark">%s</td>
                        <td>P %s</td>
                        <td>%s</td>
                        <td>%s min ago</td>
                        <td class="text-center">
                            <div class="btn-group" role="group">
                                <button onclick="retrieveOrder('%s')" class="btn btn-primary" type="button" style="width: 100px;" data-bs-dismiss="modal">Resume</button>
                            </div>
                        </td>
                    </tr>
            """, rs.getString("tab_name"), String.format("%,d", rs.getInt("SUM(total_price)")), rs.getString("qty"), totalHours, rs.getString("tab_name"));

            num_row++;

            if (tab_name.isEmpty()){tab_name=rs.getString("tab_name");}

        } MySQLiteConnection.close(); rs.close();

        return data + "+Retrieve Order ("+num_row+")+"+tab_name;
    }

    @PostMapping("/retrieveOrder")
    public void retrieveOrder(@RequestBody Order order) throws SQLException {

        MySQLite MySQLiteConnection = new MySQLite("pos");
        ResultSet rs = MySQLiteConnection.selectSQL(order.getTabName(),"tab_name","tb_temp","ORDER BY id ASC");
        while (rs.next()){
            MySQLiteConnection.insertSQL(new String[]{ order.getTabName(), rs.getString("product_id"), rs.getString("product_name"), rs.getString("qty"), rs.getString("total_price"), rs.getString("date_time")},
                    new String[]{"tab_name", "product_id", "product_name", "qty", "total_price", "date_time"}, "tb_currentOrder");
        }
        MySQLiteConnection.manualSQL("DELETE FROM tb_temp WHERE tab_name = '" + order.getTabName() + "';");
        MySQLiteConnection.close(); rs.close();

    }

    @PostMapping("/plusProduct")
    public String plusProduct(@RequestBody Order order) throws SQLException {

        LocalDateTime dateTime = LocalDateTime.now();
        String productDateTime = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        MySQL mySQLConnection = new MySQL("db_pos");
        MySQLite MySQLiteConnection = new MySQLite("pos");

        ResultSet rs = mySQLConnection.selectSQL(order.getProduct_id(), "id", "tb_product", "");
        if (rs.next()) {

            //CHECK IF EXISTING PRODUCT IN CURRENT ORDER
            ResultSet rs1 = MySQLiteConnection.selectSQL(order.getProduct_id(),"product_id","tb_currentOrder","");
            if (rs1.next()){

                int sum = rs1.getInt("qty") + 1;

                int total_price = sum * rs.getInt("price");

                int rs2 = MySQLiteConnection.updateSQL(new String[]{String.valueOf(sum), String.valueOf(total_price)},new String[] {"qty", "total_price"},"tb_currentOrder",rs1.getString("id"));
                if (rs2 > 0){rs1.close(); rs.close(); MySQLiteConnection.close(); mySQLConnection.close(); return "true";} else {return "false1";}

            } else { return "false2";}

        }else {return "false3";}

    }

    @PostMapping("/minusProduct")
    public String minusProduct(@RequestBody Order order) throws SQLException {

        LocalDateTime dateTime = LocalDateTime.now();
        String productDateTime = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        MySQL mySQLConnection = new MySQL("db_pos");
        MySQLite MySQLiteConnection = new MySQLite("pos");

        ResultSet rs = mySQLConnection.selectSQL(order.getProduct_id(), "id", "tb_product", "");
        if (rs.next()) {

            ResultSet rs1 = MySQLiteConnection.selectSQL(order.getProduct_id(),"product_id","tb_currentOrder","");
            if (rs1.next()){

                int sum = rs1.getInt("qty") - 1;

                if (sum > 0) {

                    int total_price = sum * rs.getInt("price");

                    int rs2 = MySQLiteConnection.updateSQL(new String[]{String.valueOf(sum), String.valueOf(total_price)}, new String[]{"qty", "total_price"}, "tb_currentOrder", rs1.getString("id"));
                    if (rs2 > 0) {
                        rs1.close();
                        rs.close();
                        MySQLiteConnection.close();
                        mySQLConnection.close();
                        return "true";
                    } else {
                        return "false1";
                    }
                }
                else
                {
                    int rs2 = MySQLiteConnection.deleteSQL(rs1.getString("id"),"tb_currentOrder");
                    if (rs2 > 0) {
                        rs1.close();
                        rs.close();
                        MySQLiteConnection.close();
                        mySQLConnection.close();
                        return "true";
                    } else {
                        return "false2";
                    }
                }

            } else { return "false3";}
        }else {return "false4";}

    }

    @PostMapping("/createTabName")
    public String createTabName(@RequestBody Order order) throws SQLException {

        MySQLite MySQLiteConnection = new MySQLite("pos");

        ResultSet rs = MySQLiteConnection.selectSQL("tb_currentOrder");
        rs.next();

        if (rs.getString("tab_name") == null || rs.getString("tab_name").isEmpty()){
            int rs1 = MySQLiteConnection.manualSQL("UPDATE tb_currentOrder SET tab_name = '"+order.getTabName()+"' WHERE id > 0;");
            if (rs1 > 0)
            {
                MySQLiteConnection.close(); rs.close();
                return "true";
            }
            else {return "FAILED!";}
        }

        return "FAILED!";
    }

    @PostMapping("/holdOrder")
    public void holdOrder(@RequestBody Order order) throws SQLException {

        MySQLite MySQLiteConnection = new MySQLite("pos");

        boolean del = true;

        ResultSet rs = MySQLiteConnection.selectSQL("tb_currentOrder");
        while(rs.next()) {

            if (rs.getString("tab_name") == null || rs.getString("tab_name").isEmpty()) {
                MySQLiteConnection.insertSQL(new String[]{ order.getTabName(), rs.getString("product_id"), rs.getString("product_name"), rs.getString("qty"), rs.getString("total_price"), rs.getString("date_time")},
                        new String[]{"tab_name", "product_id", "product_name", "qty", "total_price", "date_time"}, "tb_temp");
            }
            else {
                if (del) {MySQLiteConnection.manualSQL("DELETE FROM tb_temp WHERE tab_name = '" + order.getTabName() + "';"); del= false;}

                MySQLiteConnection.insertSQL(new String[]{ rs.getString("tab_name"), rs.getString("product_id"), rs.getString("product_name"), rs.getString("qty"), rs.getString("total_price"), rs.getString("date_time")},
                        new String[]{"tab_name", "product_id", "product_name", "qty", "total_price", "date_time"}, "tb_temp");
            }
        }
        deleteCurrentOrder();
        MySQLiteConnection.close(); rs.close();
    }

    @PostMapping("/deleteCurrentOrder")
    public void deleteCurrentOrder(@RequestBody Order order) throws SQLException {
        deleteCurrentOrder();
    }

    //BELOS ARE METHODS ===========================================================================
    public String productCurrent_layout(String product_name, String qty, int total_price, String product_id){

        return String.format("""
        <div class="card border-0" style="margin: 10px 0;">
            <div class="card-body" style="padding: 0;">
                <div class="row">
                
                    <div class="col-10 col-sm-10 col-md-10 col-lg-10 col-xl-10 col-xxl-10" data-bss-hover-animate="pulse" style="padding-right: 0;">
                        <a class="text-decoration-none" href="#" data-bs-target="#editProductModal" data-bs-toggle="modal">
                            <div>
                                <div class="row" style="margin: 0 5px;">
                                    <div class="col-8 col-sm-8 col-md-7 col-lg-8 col-xl-8 col-xxl-8" style="padding: 0;">
                                        <h6 class="text-uppercase" style="color: var(--bs-gray-800);font-weight: bold;font-size: 12px;margin: 0;">%s</h6>
                                        <p class="text-uppercase text-secondary" style="margin: 0;font-size: 10px;"></p>
                                    </div>
                                    <div class="col-1 col-sm-1 col-md-1 col-lg-1 col-xl-1 col-xxl-1" style="padding: 0;">
                                        <h6 class="fw-bold text-center text-dark" style="font-size: 12px;">%s</h6>
                                    </div>
                                    <div class="col-3 col-sm-3 col-md-4 col-lg-3 col-xl-3 col-xxl-3" style="padding: 0;">
                                        <h6 class="fw-bolder" style="text-align:right; margin: 0;padding: 0;padding-left: 5px;font-size: 12px;">
                                            <span style="color: rgb(58, 59, 69);">₱ %s</span><br>
                                        </h6>
                                    </div>
                                </div>
                            </div>
                        </a>
                    </div>
                    
                    <div class="col-2 col-sm-2 col-lg-2" style="padding: 0;">
                        <h4 class="text-center text-success" style="margin-top: -10px;">
                            <a onclick="minusProduct(%s)" class="text-decoration-none" href="#" style="margin:0;">
                                <svg class="text-dark" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512" width="1em" height="1em" fill="currentColor" data-bss-hover-animate="pulse" style="margin: 0 2px;">
                                    <path d="M0 256C0 114.6 114.6 0 256 0C397.4 0 512 114.6 512 256C512 397.4 397.4 512 256 512C114.6 512 0 397.4 0 256zM168 232C154.7 232 144 242.7 144 256C144 269.3 154.7 280 168 280H344C357.3 280 368 269.3 368 256C368 242.7 357.3 232 344 232H168z"></path>
                                </svg>
                            </a>
                            <a onclick="plusProduct(%s)" class="text-decoration-none" href="#" style="margin:0;">
                                <svg class="text-success" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512" width="1em" height="1em" fill="currentColor" data-bss-hover-animate="pulse" style="margin: 0 2px;">
                                    <path d="M0 256C0 114.6 114.6 0 256 0C397.4 0 512 114.6 512 256C512 397.4 397.4 512 256 512C114.6 512 0 397.4 0 256zM256 368C269.3 368 280 357.3 280 344V280H344C357.3 280 368 269.3 368 256C368 242.7 357.3 232 344 232H280V168C280 154.7 269.3 144 256 144C242.7 144 232 154.7 232 168V232H168C154.7 232 144 242.7 144 256C144 269.3 154.7 280 168 280H232V344C232 357.3 242.7 368 256 368z"></path>
                                </svg>
                            </a>
                        </h4>
                    </div>
                </div>
            </div>
        </div>
                """, product_name, qty, String.format("%,d", total_price), product_id, product_id);
    }

    public void deleteCurrentOrder() throws SQLException {
        MySQLite MySQLiteConnection = new MySQLite("pos");

        MySQLiteConnection.manualSQL( "DELETE FROM tb_currentOrder;");
        MySQLiteConnection.manualSQL( "DELETE FROM sqlite_sequence WHERE name='tb_currentOrder';");
        MySQLiteConnection.close();
    }

}
