package com.kmsdevworks.pos;

import java.sql.*;

public class MySQLite {

    Connection conn = null;
    PreparedStatement pstmt;
    String query, whereSQL, tableBind = "";


    public MySQLite(String dbName) throws SQLException {
        try {
            String url = "jdbc:sqlite:"+dbName+".db";
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite established.");

        } catch (SQLException e) {System.out.println(e.getMessage());}
    }

    /* - README -

            MySQLiteConnection.createTable("tb_category2", new String[] {"category","icon"});
    */
    public void createTable(String table, String[] column) throws SQLException {
        try {

            assert conn != null;
            Statement stmt = conn.createStatement();

            String[] columnDefinitions = new String[column.length];

            for (int i = 0; i < column.length; i++) {
                columnDefinitions[i] = column[i] + " TEXT";
            }

            String columnsSQL = String.join(" , ", columnDefinitions);

            stmt.execute("CREATE TABLE IF NOT EXISTS " + table +
                    " (id INTEGER PRIMARY KEY AUTOINCREMENT, " + columnsSQL + ")");


        }catch (SQLException e) {throw new RuntimeException(e);}
    }

    public int manualSQL(String data) throws SQLException {
        try {pstmt = conn.prepareStatement(data);return pstmt.executeUpdate();}
        catch (SQLException e) {throw new RuntimeException(e);}
    }

    public ResultSet manualSQL_Result(String data) throws SQLException {
        try {pstmt = conn.prepareStatement(data);return pstmt.executeQuery();}
        catch (SQLException e) {throw new RuntimeException(e);}
    }

    /* - README -

        ResultSet rs = MySQLiteConnection.selectSQL("", "", "tb_category", "");

        while (rs.next()) {
            System.out.println("id: " + rs.getInt("id") + " category: " + rs.getString("category") + ", icon: " + rs.getString("icon"));
        }
    */

    public ResultSet selectSQL(String data, String column, String table, String addQuery) throws SQLException {
        try {

            if (data.isEmpty() && column.isEmpty() && addQuery.isEmpty()) {
                pstmt = conn.prepareStatement("SELECT * FROM " + table);
                return pstmt.executeQuery();
            }

            if (!data.isEmpty() && !column.isEmpty()) {
                query = "SELECT * FROM " + table + " WHERE " + column + " = ?";
            }

            if (!addQuery.isEmpty()) {
                query += " " + addQuery;
            }

            if (!data.isEmpty() && !column.isEmpty()) {
                pstmt = conn.prepareStatement(query);
                pstmt.setString(1, data);
            } else {
                pstmt = conn.prepareStatement("SELECT * FROM " + table + query);
            }

            return pstmt.executeQuery();

        }catch (SQLException e) {throw new RuntimeException(e);}
    }

    public ResultSet selectSQL(String table) throws SQLException {
        return selectSQL("","",table,"");
    }

    /* - README -

            ResultSet rs = MySQLiteConnection.searchSQL("Smile", new String[] {"id","category","icon"},"tb_category","");
            while(rs.next()){}
     */

    public ResultSet searchSQL(String data, String[] column, String table, String addQuery) throws SQLException {
        try {

            String[] whereClause = new String[column.length];

            if (data.isEmpty() && column.length == 0 && addQuery.isEmpty()) {
                pstmt = conn.prepareStatement("SELECT * FROM " + table);
                return pstmt.executeQuery();
            }

            if (!data.isEmpty() && column.length != 0) {
                for (int x = 0; x < column.length; x++) {
                    whereClause[x] = column[x] + " LIKE ?";
                }
                whereSQL = String.join(" OR ", whereClause);
            }

            if (!addQuery.isEmpty()) {
                whereSQL += " " + addQuery;
            }

            if (!data.isEmpty() && column.length != 0) {
                pstmt = conn.prepareStatement("SELECT * FROM " + table + " WHERE " + whereSQL);
                for (int x = 1; x <= column.length; x++) {
                    pstmt.setString(x, "%" + data + "%");
                }
            }

            return pstmt.executeQuery();

        }catch (SQLException e) {throw new RuntimeException(e);}
    }

    /* - README -

        int rs = MySQLiteConnection.insertSQL(new String[]{"Coffee", "Smile"},new String[] {"category", "icon"},"tb_category");
        if (rs > 0){
            System.out.println("SUCCESS!!");
        }
    */

    public int insertSQL(String[] data, String[] column, String table) throws SQLException {
        try {
            String[] binding = new String[column.length];

            if (data.length != column.length) {
                throw new IllegalArgumentException("Data and column arrays must be the same length.");
            }

            for (int x = 0; x < column.length; x++) {
                binding[x] = "?";
            }
            whereSQL = String.join(", ", binding);
            tableBind = String.join(", ", column);
            pstmt = conn.prepareStatement("INSERT INTO " + table + " (" + tableBind + ") VALUES (" + whereSQL + ")");

            for (int x = 1; x <= column.length; x++) {
                pstmt.setString(x, data[x - 1]);
            }

            return pstmt.executeUpdate();

        }catch (SQLException e) {throw new RuntimeException(e);}
    }

    /* - README -

            int rs1 = MySQLiteConnection.updateSQL(new String[]{"Smile"},new String[] {"icon"},"tb_category","1");
            if (rs1 > 0){
                System.out.println("SUCCESS!");
            }
     */
    public int updateSQL(String[] data, String[] column, String table, String id) throws SQLException {
        try {
            String[] whereClause = new String[column.length];

            if (data.length != column.length) {
                throw new IllegalArgumentException("Data and column arrays must be of the same length.");
            }

            for (int x = 0; x < column.length; x++) {
                whereClause[x] = column[x] + " = ?";
            }
            whereSQL = String.join(", ", whereClause);

            pstmt = conn.prepareStatement("UPDATE " + table + " SET " + whereSQL + " WHERE id = ?");
            for (int x = 1; x <= column.length; x++) {
                pstmt.setString(x, data[x - 1]);
            }
            pstmt.setString(column.length + 1, id);

            return pstmt.executeUpdate();

        }catch (SQLException e) {throw new RuntimeException(e);}
    }

    /* - README -

            int rs1 = MySQLiteConnection.deleteSQL("1","tb_category");
            if (rs1 > 0){
                System.out.println("HAS BEEN DELETED!");
            }
    */
    public int deleteSQL(String id, String table) throws SQLException {
        try {
            pstmt = conn.prepareStatement("DELETE FROM " + table + " WHERE id = ?");
            pstmt.setString(1, id);
            return pstmt.executeUpdate();
        }catch (SQLException e) {throw new RuntimeException(e);}
    }

    public void close() {
        try {
            if (pstmt != null) {
                pstmt.close();
            }
        } catch (SQLException e) {
            // Log the exception, but don't re-throw it.
            // We still want to try to close the connection.
            System.err.println("Error closing statement: " + e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                // Log the exception.
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) throws SQLException {
        /*
        MySQLite MySQLiteConnection = new MySQLite("pos");

        MySQLiteConnection.createTable("tb_category", new String[] {"category","icon"});

        ResultSet rs = MySQLiteConnection.searchSQL("1", new String[] {"id","category","icon"},"tb_category","");
        while (rs.next()) {
            System.out.println("id: " + rs.getInt("id") + " category: " + rs.getString("category") + ", icon: " + rs.getString("icon"));
        }

         */
    }
}
