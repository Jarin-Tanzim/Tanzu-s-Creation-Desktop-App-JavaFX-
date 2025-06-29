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
import javafx.stage.Stage;

public class SignupController implements Initializable {

    @FXML private TextField EmailField;
    @FXML private TextField FullNameField;
    @FXML private PasswordField PasswordField;
    @FXML private PasswordField ConfrimPassField;
    @FXML private RadioButton GenderMale;
    @FXML private RadioButton GenderFemale;
    @FXML private ToggleGroup Group1;
    @FXML private Button SignupButton;
    @FXML private Label ErrorLable;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
    }

    @FXML
    private void HandleSignup(ActionEvent event) throws IOException {
        String fullName = FullNameField.getText().trim();
        String email = EmailField.getText().trim();
        String password = PasswordField.getText().trim();
        String confirmPassword = ConfrimPassField.getText().trim();
        String gender = GenderMale.isSelected() ? "Male" : GenderFemale.isSelected() ? "Female" : "";

       
        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || gender.isEmpty()) {
            ErrorLable.setText("Please fill in all fields.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            ErrorLable.setText("Passwords do not match.");
            return;
        }

        try {
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/tanzu_app", "root", "1234"
            );

           String checkSql = "SELECT id FROM users WHERE email = ?";
           PreparedStatement checkStmt = conn.prepareStatement(checkSql);
           checkStmt.setString(1, email);
           ResultSet checkResult = checkStmt.executeQuery();
           if (checkResult.next()) {
            ErrorLable.setText("Email already registered.");
            checkResult.close();
            checkStmt.close();
            conn.close();
            return;
}
checkResult.close();
checkStmt.close();

            
            String sql = "INSERT INTO users (full_name, email, password, gender, role) VALUES (?, ?, ?, ?, 'customer')";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, fullName);
            stmt.setString(2, email);
            stmt.setString(3, password);  
            stmt.setString(4, gender);

            int result = stmt.executeUpdate();
            if (result > 0) {
                ErrorLable.setText("Registration successful!");
                clearFields();

                
                FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
                Parent root = loader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Login");
                stage.show();

                ((Stage) SignupButton.getScene().getWindow()).close();
            } else {
                ErrorLable.setText("Registration failed.");
            }

            stmt.close();
            conn.close();

        } catch (SQLException e) {
            ErrorLable.setText("Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void clearFields() {
        FullNameField.clear();
        EmailField.clear();
        PasswordField.clear();
        ConfrimPassField.clear();
        GenderMale.setSelected(false);
        GenderFemale.setSelected(false);
    }
}
