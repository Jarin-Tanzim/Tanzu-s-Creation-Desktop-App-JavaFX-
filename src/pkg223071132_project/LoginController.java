package pkg223071132_project;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LoginController implements Initializable {

    @FXML private TextField usernamefield;
    @FXML private PasswordField passwordfield;
    @FXML private AnchorPane loginbutton;
    @FXML private Label errorlabel;
    @FXML private Button loginButton;
    @FXML private Hyperlink GoTosignup;
    @FXML
    private Hyperlink forgotPasswordLink;
    @FXML
    private Hyperlink backtohome;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialization code if needed
    }

    @FXML
    private void handlelogin(ActionEvent event) {
        String email = usernamefield.getText().trim();
        String password = passwordfield.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            errorlabel.setText("Please enter both email and password.");
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tanzu_app", "root", "1234");
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE email = ? AND password = ?")) {

            stmt.setString(1, email);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String fullName = rs.getString("full_name");
                    String role = rs.getString("role");  

                    int userId = rs.getInt("id");  

                   
                    UserSession.login(userId, role);

                    FXMLLoader loader;
                    if (role.equalsIgnoreCase("admin")) {
                        loader = new FXMLLoader(getClass().getResource("admin_panel.fxml"));
                    } else {
                        loader = new FXMLLoader(getClass().getResource("homepage.fxml"));
                    }

                    Parent root = loader.load();

                    
                    if (!role.equalsIgnoreCase("admin")) {
                        HomepageController homepageController = loader.getController();
                        homepageController.setLoggedIn(true, userId, role);  
                    }

                    
                    Stage stage = (Stage) loginButton.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.setTitle(role.equalsIgnoreCase("admin") ? "Admin Dashboard" : "Welcome, " + fullName);
                    stage.show();

                } else {
                    errorlabel.setText("Invalid credentials.");
                }
            }
        } catch (Exception e) {
            errorlabel.setText("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleGoTosign(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("signup.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Sign Up");
            stage.setScene(new Scene(root));
            stage.show();

            ((Stage) GoTosignup.getScene().getWindow()).close();
        } catch (IOException e) {
            errorlabel.setText("Unable to load Sign Up page.");
            e.printStackTrace();
        }
    }

    @FXML
private void handleForgotPassword(ActionEvent event) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("forgot_password.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Forgot Password");
        stage.setScene(new Scene(root));
        stage.show();

        
        ((Stage) forgotPasswordLink.getScene().getWindow()).close();

    } catch (IOException e) {
        errorlabel.setText("Unable to load Forgot Password page.");
        e.printStackTrace();
    }
}

    @FXML
    private void handlebacktohome(ActionEvent event) {
        
        try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("homepage.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow(); // <-- Node works for Button or Hyperlink!
        stage.setScene(new Scene(root));
        stage.setTitle("Home");
        stage.show();
    } catch (IOException e) {
        e.printStackTrace();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("Unable to load Home page.");
        alert.showAndWait();
    }
    }
}
