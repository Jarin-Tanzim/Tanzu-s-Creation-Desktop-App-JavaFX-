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

    
    @FXML private ComboBox<String> SecurityQuestionBox;
    @FXML private TextField SecurityAnswerField;
    @FXML
    private Hyperlink BacktoLogin;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        SecurityQuestionBox.getItems().addAll(
            "What is your mother's maiden name?",
            "What was the name of your first pet?",
            "What is your favorite food?",
            "What city were you born in?"
        );
    }

    @FXML
    private void HandleSignup(ActionEvent event) throws IOException {
        String fullName = FullNameField.getText().trim();
        String email = EmailField.getText().trim();
        String password = PasswordField.getText().trim();
        String confirmPassword = ConfrimPassField.getText().trim();
        String gender = GenderMale.isSelected() ? "Male" : GenderFemale.isSelected() ? "Female" : "";
        String question = SecurityQuestionBox.getValue();
        String answer = SecurityAnswerField.getText().trim();

        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()
                || gender.isEmpty() || question == null || answer.isEmpty()) {
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

            String sql = "INSERT INTO users (full_name, email, password, gender, role, security_question, security_answer) VALUES (?, ?, ?, ?, 'customer', ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, fullName);
            stmt.setString(2, email);
            stmt.setString(3, password);
            stmt.setString(4, gender);
            stmt.setString(5, question);
            stmt.setString(6, answer);

            int result = stmt.executeUpdate();
            if (result > 0) {
            clearFields();

  
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Signup Successful");
    alert.setHeaderText(null);
    alert.setContentText("You have signed up successfully!");
    alert.showAndWait();

    
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
        SecurityQuestionBox.getSelectionModel().clearSelection();
        SecurityAnswerField.clear();
    }

    @FXML
    private void handleBacktologin(ActionEvent event) {
         try {
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Scene scene = new Scene(root);

        
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    } catch (IOException e) {
        e.printStackTrace(); 
    }
    }
}
