package com.kmsdevworks.pos;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

import static com.kmsdevworks.pos.PasswordHashers.generateSalt;
import static com.kmsdevworks.pos.PasswordHashers.hashPassword;

@RestController
public class ProfileController {

    public static class Profile {
        private String userID;
        private String currentPass;
        private String newPass1;
        private String newPass2;
        private String starting_cash;
        private String verifyButton;

        public String getUserID() {return userID;}
        public String getCurrentPass() {
            return currentPass;
        }
        public String getNewPass1() {
            return newPass1;
        }
        public String getNewPass2() {
            return newPass2;
        }
        public String getStarting_cash() {
            return starting_cash;
        }
        public String getVerifyButton() {
            return verifyButton;
        }
    }

    @PostMapping("/changePass")
    public String changePass(@RequestBody ProfileController.Profile profile) throws SQLException {

        if (profile.getCurrentPass() == null || profile.getCurrentPass().isEmpty() ||
                profile.getNewPass1() == null || profile.getNewPass1().isEmpty() ||
                profile.getNewPass2() == null || profile.getNewPass2().isEmpty()){
            return "All input must not be empty!";
        }

        MySQL mySQLConnection = new MySQL("db_pos");

        ResultSet rs = mySQLConnection.selectSQL(profile.getUserID(), "id", "tb_account", "");
        if (!rs.next()){return "Error 404!";}

        boolean VP = PasswordHashers.verifyPassword(profile.getCurrentPass(), rs.getString("pass_hash"), rs.getString("pass_salt"));
        if (!VP) { return "Incorrect password or new password did not matched!"; }

        if(!profile.getNewPass1().equals(profile.getNewPass2())) { return "Incorrect password or new password did not matched!"; }

        byte[] salt = generateSalt();
        String hashed = hashPassword(profile.getNewPass1().toCharArray(), salt);

        int rsUpdate = mySQLConnection.updateSQL(new String[]{hashed, Base64.getEncoder().encodeToString(salt)},new String[] {"pass_hash", "pass_salt"},"tb_account", profile.getUserID());
        if (rsUpdate > 0){return "true";}
        else {return "Incorrect password or new password did not matched! X123";}

    }

    @PostMapping("/session")
    public String session(@RequestBody ProfileController.Profile profile) throws SQLException {

        //FOR SETTINGS
        int defaultSessionHours = 12;

        MySQL mySQLConnection = new MySQL("db_pos");

        ResultSet rs2 = mySQLConnection.manualSQL("SELECT * FROM tb_session WHERE user_id = '"+profile.userID+"' AND session_closed = '' ORDER BY id DESC;");
        if (rs2.next()){
            //BELOW COMPUTING THE SESSION HOURS
            String inputDateStr = rs2.getString("session_start");
            LocalDateTime fromDateTime = LocalDateTime.parse(inputDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            int totalHours = Integer.parseInt(String.valueOf(Duration.between(fromDateTime, LocalDateTime.now()).getSeconds() / 3600));

            if (totalHours >= defaultSessionHours) {return "close";} else {return "continue";}
        }

        //BELOW OPENING FLOAT MODAL
        if (profile.getVerifyButton().equals("false")) {return "float";}

        //BELOW SESSION START
        LocalDateTime dateTime = LocalDateTime.now();
        String sessionDateTimeStart = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        int rs = mySQLConnection.insertSQL(
                new String[]{profile.getUserID(),sessionDateTimeStart, "", profile.getStarting_cash(), "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"},
                new String[] {"user_id","session_start", "session_closed", "starting_cash", "net_sales", "refunds", "service_charge", "discount", "cash","gcash", "debit", "credit", "cash_declared", "cash_drawer", "cash_difference"},
                "tb_session");

        if (rs <= 0){return "Failed to record your session. Please contact the administrator/manager.";}

        return "true";
    }

    @PostMapping("/session_checker")
    public String session_checker(@RequestBody ProfileController.Profile profile) throws SQLException {

        //FOR SETTINGS
        int defaultSessionHours = 12;

        MySQL mySQLConnection = new MySQL("db_pos");

        ResultSet rs = mySQLConnection.manualSQL("SELECT * FROM tb_session WHERE user_id = '"+profile.userID+"' AND session_closed = '' ORDER BY id DESC;");
        if (rs.next()){

            //BELOW COMPUTING THE SESSION HOURS
            String inputDateStr = rs.getString("session_start");
            LocalDateTime fromDateTime = LocalDateTime.parse(inputDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            int totalHours = Integer.parseInt(String.valueOf(Duration.between(fromDateTime, LocalDateTime.now()).getSeconds() / 3600));

            if (totalHours >= defaultSessionHours) {
                return "close+"+totalHours;
            }

            //BELOW IS SESSION CONTINUE
            String dateTimeString = rs.getString("session_start");
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime localDateTime = LocalDateTime.parse(dateTimeString, inputFormatter);
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("hh:mma MMMM dd, yyyy");

            return "continue+"+localDateTime.format(outputFormatter);
        }

        return "true";
    }

}
