package pkg223071132_project;

public class UserSession {
    private static boolean loggedIn = false;
    private static int loggedInUserId = -1;
    private static String role = "";  

    public static boolean isLoggedIn() {
        return loggedIn;
    }

    public static void login(int userId, String userRole) {
        loggedIn = true;
        loggedInUserId = userId;
        role = userRole;
    }

    public static void logout() {
        loggedIn = false;
        loggedInUserId = -1;
        role = "";
    }

    public static int getLoggedInUserId() {
        return loggedInUserId;
    }

    public static String getRole() {
        return role;
    }
}
