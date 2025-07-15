package pkg223071132_project;

public class UserSession {
    private static boolean loggedIn = false;

    public static boolean isLoggedIn() {
        return loggedIn;
    }

    public static void login() {
        loggedIn = true;
    }

    public static void logout() {
        loggedIn = false;
    }
}
