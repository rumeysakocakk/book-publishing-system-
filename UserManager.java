package midproject1;

import midproject1.dao.UserDao;
import javax.swing.JOptionPane;
import java.util.List;

/**
 * Manages user registration and login operations by interacting with the UserDao.
 * This class works with the database persistence layer rather than in-memory lists.
 * @author asus
 */
public class UserManager {

    private static UserDao userDao = new UserDao();

    /**
     * Registers a new user (author or editor) in the database.
     * Performs validation checks before attempting to save the user.
     * @param username The desired username.
     * @param password The user's password.
     * @param confirmPassword Confirmation of the password.
     * @param email The user's email address.
     * @param role The role of the user ("author" or "editor").
     * @return true if registration is successful, false otherwise.
     */
    public static boolean registerUser(String username, String password, String confirmPassword, String email, String role) {
        if (!role.equals("author") && !role.equals("editor")) {
            JOptionPane.showMessageDialog(null, "Invalid role. Only 'author' or 'editor' can register.");
            return false;
        }

        if (userDao.findByUsername(username) != null) {
            JOptionPane.showMessageDialog(null, "This username is already taken.");
            return false;
        }

        if (password.length() < 7) {
            JOptionPane.showMessageDialog(null, "Password must be at least 7 characters long.");
            return false;
        }

        if (!email.contains("@")) {
            JOptionPane.showMessageDialog(null, "Email must contain '@'.");
            return false;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(null, "Passwords do not match.");
            return false;
        }

        User newUser = null;
        if (role.equals("author")) {
            newUser = new Author(username, password, email, role);
        } else if (role.equals("editor")) {
            newUser = new Editor(username, password, email, role);
        } else {
            JOptionPane.showMessageDialog(null, "An unexpected error occurred during user creation.");
            return false;
        }

        try {
            userDao.save(newUser);
            JOptionPane.showMessageDialog(null, "User registered successfully: " + username);
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error during registration: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Authenticates a user (admin, author, or editor) against the database using UserDao.
     * @param username The username provided.
     * @param password The password provided.
     * @param role The role selected.
     * @return true if login is successful, false otherwise.
     */
    public static boolean loginUser(String username, String password, String role) {
        User authenticatedUser = userDao.findByUsernameAndPasswordAndRole(username, password, role);

        if (authenticatedUser != null) {
            Session.setCurrentUser(authenticatedUser);
            JOptionPane.showMessageDialog(null, "Login successful!");
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Incorrect username, password, or role!");
            return false;
        }
    }

    /**
     * Finds a user by username from the database using UserDao.
     * @param username The username to search for.
     * @return The User object if found, null otherwise.
     */
    public static User findUserByUsername(String username) {
        return userDao.findByUsername(username);
    }

    /**
     * Checks if a username is already taken in the database using UserDao.
     * @param username The username to check.
     * @return true if the username is taken, false otherwise.
     */
    public static boolean isUsernameTaken(String username) {
        return userDao.findByUsername(username) != null;
    }

    /**
     * Retrieves all users from the database using UserDao.
     * @return A List of all User objects.
     */
    public static List<User> getUsers() {
        return userDao.findAll();
    }
}
