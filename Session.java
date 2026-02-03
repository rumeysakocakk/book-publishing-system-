package midproject1;

/**
 * Manages the current user session in the application.
 * It primarily holds the currently logged-in user.
 */
public class Session {

    private static User currentUser; // Holds the currently logged-in user

    /**
     * Gets the currently logged-in user for the session.
     * @return The User object for the current session, or null if no user is logged in.
     */
    public static User getCurrentUser() {
        return currentUser;
    }

    /**
     * Sets the currently logged-in user for the session.
     * @param user The User object to set as the current user.
     */
    public static void setCurrentUser(User user) {
        Session.currentUser = user;
    }

    /**
     * Clears the current user session, logging out the user.
     */
    public static void clearSession() {
        currentUser = null;
    }
}
