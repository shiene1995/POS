package com.kmsdevworks.pos;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TestingArea {
    public static void main(String[] args) throws SQLException {
        //MySQLite MySQLiteConnection = new MySQLite("pos");

        //MySQLiteConnection.createTable("tb_temp", new String[] {"tab_name","product_id", "product_name", "option", "qty", "total_price", "date_time"});

        String inputDateStr = "2025-11-21 06:47:46";
// Define the formatter to match your date_time string format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
// 1. Parse the input string into a LocalDateTime object
        LocalDateTime fromDateTime = LocalDateTime.parse(inputDateStr, formatter);

// 2. Calculate the Duration between the parsed time and the current time
        Duration duration = Duration.between(fromDateTime, LocalDateTime.now());

// 3. Get the total difference in seconds
        long totalSeconds = duration.getSeconds();

// --- Conversion to Minutes and Seconds ---

// 4. Calculate the total minutes
        long minutes = totalSeconds / 60; // Integer division gives the total minutes

// 5. Calculate the remaining seconds (less than 60)
        long seconds = totalSeconds % 60; // Modulo operator gives the remainder

// --- If you still need the total hours as in your original example: ---
        long totalHours = totalSeconds / 3600;

        System.out.println("Total Duration in Seconds: " + totalSeconds);
        System.out.println("Time Difference: " + minutes + " minutes and " + seconds + " seconds");
        System.out.println("Total Hours (as an integer): " + totalHours);

    }
}
