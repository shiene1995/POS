package com.kmsdevworks.pos;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ResultSet;
import java.sql.SQLException;

@RestController
public class LoginController {

    public static class Login {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }
        public void setUsername(String data) {
            this.username = data;
        }

        public String getPassword() {return password;}
        public void setPassword(String data) {this.password = data;}
    }

    @PostMapping("/login")
    public String loginUser(@RequestBody LoginController.Login login) throws SQLException {

        String data;

        if (login.getUsername() == null || login.getUsername().isEmpty() ||
                login.getPassword() == null || login.getPassword().isEmpty()) {
            return "Username and password must not be empty!";
        }

        MySQL mySQLConnection = new MySQL("db_pos");

        boolean usernameFilter = InputFilterUtils.isValidUsername(login.getUsername());
        if(!usernameFilter){return "Invalid username or password!";}

        ResultSet rs = mySQLConnection.selectSQL(login.getUsername(), "username", "tb_account", "");
        if (!rs.next()){return "Invalid username or password!";}

        if (!rs.getString("status").equals("true") && !rs.getString("status").equals("master"))
        {return "Your account is Locked due to some reason. Please contact the Manager.";}

        boolean VP = PasswordHashers.verifyPassword(login.getPassword(), rs.getString("pass_hash"), rs.getString("pass_salt"));
        boolean VU = PasswordHashers.verifyUsername(login.getUsername(),rs.getString("username")); //to case-sensitive the username
        //example: Admin & admin are different

        if (!VP || !VU) { return "Invalid username or password!"; }

        data  = rs.getInt("id") + ",";
        data += rs.getString("uname") + ",";
        data += rs.getString("status") + ",";
        data += rs.getString("acc_type") + ",";
        data += rs.getString("img_link");

        rs.close(); //Closed the ResultSet (For Select Query only)
        mySQLConnection.close(); //Closing MySQL Connection and stmt

        return data;

    }
}
