package pkg223071132_project;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.*;
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialization (if needed)
    }

    @FXML
    private void handlelogin(ActionEvent event) {
        String email = usernamefield.getText().trim();
        String password = passwordfield.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            errorlabel.setText("Please enter both email and password.");
            return;
        }

        try {
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/tanzu_app", "root", "1234"
            );

            String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String fullName = rs.getString("full_name");

                // Load Homepage
                loadScene("homepage.fxml", "Welcome, " + fullName);

                // Close the login window
                ((Stage) loginButton.getScene().getWindow()).close();
            } else {
                errorlabel.setText("Invalid credentials.");
            }

            rs.close();
            stmt.close();
            conn.close();

        } catch (Exception e) {
            errorlabel.setText("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadScene(String fxmlFile, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle(title);
        stage.show();
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
}
