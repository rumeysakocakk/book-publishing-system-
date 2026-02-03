package midproject1;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class LoginLogManager {
    
    public static ArrayList<LoginLog> logs = new ArrayList<>();  // ArrayList to store login logs

    // Method to add a new login attempt log (updated)
    public static void addLoginAttempt(String username, LocalDateTime now) {
        // Yeni bir LoginLog nesnesi oluştur ve userType olarak "LoginAttempt" kullan
        LoginLog log = new LoginLog(username, "LoginAttempt", now);
        logs.add(log);  // Log'u ArrayList'e ekle
        System.out.println("Login attempt logged for user: " + username + " at " + now); // Konsola bilgi yazdır
    }

    // Method to add a new login log
    public void addLoginLog(String username, String userType) {
        LoginLog log = new LoginLog(username, userType, LocalDateTime.now());  // Create a log with the current time
        logs.add(log);  // Add the log to the ArrayList
    }

    // Method to get all login logs
    public static List<LoginLog> getLoginLogs() {
        return logs;  // Return the static logs list
    }

    // Method to get all login logs as a string
    public String getAllLoginLogs() {
        StringBuilder allLogs = new StringBuilder();  // StringBuilder to hold all logs
        for (LoginLog log : logs) {
            allLogs.append(log.toString()).append("\n");  // Append each log to the StringBuilder
        }
        return allLogs.toString();  // Return the concatenated logs
    }

    // Method to display login logs in a message dialog
    public void displayLoginLogs() {
        String loginLogs = getAllLoginLogs();  // Get all login logs
        JOptionPane.showMessageDialog(null, loginLogs, "Login Logs", JOptionPane.INFORMATION_MESSAGE);  // Display the logs
    }
}