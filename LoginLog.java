/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package midproject1;
import java.time.LocalDateTime;

public class LoginLog {
    private String username;  // Username of the user
    private String userType;  // Type of the user (admin, author, editor, etc.)
    private LocalDateTime loginTime;  // Login time (date and time)

    // Constructor
    public LoginLog(String username, String userType, LocalDateTime loginTime) {
        this.username = username;  // Set the username
        this.userType = userType;  // Set the user type
        this.loginTime = loginTime;  // Set the login time
    }

    // Getter methods
    public String getUsername() {
        return username;  // Return the username
    }

    public String getUserType() {
        return userType;  // Return the user type
    }

    public LocalDateTime getLoginTime() {
        return loginTime;  // Return the login time
    }

    // toString method to print the log information in a readable format
    @Override
    public String toString() {
        return "User: " + username + " - Type: " + userType + " - Login Time: " + loginTime.toString();
    }
}

