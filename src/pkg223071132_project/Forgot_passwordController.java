package pkg223071132_project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class Forgot_passwordController {

    @FXML private TextField emailField;
    @FXML private Button emailSubmitButton;
    @FXML private Label securityQuestionLabel;
    @FXML private TextField securityAnswerField;
    @FXML private Button answerSubmitButton;
    @FXML private Label resetPassLabel;
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Button resetPasswordButton;
    @FXML private Label messageLabel;

    private String correctAnswer;
    private String userEmail;

    @FXML
    private void initialize() {
        // Optional initialization
    }

    @FXML
    private void handleEmailSubmit(ActionEvent event) {
        userEmail = emailField.getText().trim();
        if (userEmail.isEmpty()) {
            messageLabel.setText("Please enter your email.");
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/tanzu_app", "root", "1234");
             PreparedStatement stmt = conn.prepareStatement("SELECT security_question, security_answer FROM users WHERE email = ?")) {

            stmt.setString(1, userEmail);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String question = rs.getString("security_question");
                correctAnswer = rs.getString("security_answer");

                securityQuestionLabel.setText("Security Question: " + question);
                securityQuestionLabel.setVisible(true);
                securityAnswerField.setVisible(true);
                answerSubmitButton.setVisible(true);

                emailField.setDisable(true);
                emailSubmitButton.setDisable(true);
                messageLabel.setText("");

            } else {
                messageLabel.setText("Email not found.");
            }

        } catch (SQLException e) {
            messageLabel.setText("Database error: " + e.getMessage());
        }
    }

    @FXML
    private void handleAnswerSubmit(ActionEvent event) {
        String userAnswer = securityAnswerField.getText().trim();
        if (userAnswer.equalsIgnoreCase(correctAnswer)) {
            securityQuestionLabel.setVisible(false);
            securityAnswerField.setVisible(false);
            answerSubmitButton.setVisible(false);

            resetPassLabel.setVisible(true);
            newPasswordField.setVisible(true);
            confirmPasswordField.setVisible(true);
            resetPasswordButton.setVisible(true);

            messageLabel.setText("");
        } else {
            messageLabel.setText("Incorrect answer to security question.");
        }
    }

    @FXML
    private void handleResetPassword(ActionEvent event) {
        String newPass = newPasswordField.getText();
        String confirmPass = confirmPasswordField.getText();

        if (!newPass.equals(confirmPass)) {
            messageLabel.setText("Passwords do not match.");
            return;
        }

        if (newPass.length() < 4) {
            messageLabel.setText("Password must be at least 4 characters.");
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/tanzu_app", "root", "1234");
             PreparedStatement stmt = conn.prepareStatement("UPDATE users SET password = ? WHERE email = ?")) {

            stmt.setString(1, newPass);
            stmt.setString(2, userEmail);

            int updated = stmt.executeUpdate();
            if (updated > 0) {
                messageLabel.setText("Password reset successful. Returning to login...");
                // Wait and reopen login
                new Thread(() -> {
                    try {
                        Thread.sleep(1500);
                        javafx.application.Platform.runLater(this::loadLoginPage);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            } else {
                messageLabel.setText("Failed to reset password.");
            }

        } catch (SQLException e) {
            messageLabel.setText("Database error: " + e.getMessage());
        }
    }

    private void loadLoginPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();

            
            Stage currentStage = (Stage) emailField.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBackToLogin(ActionEvent event) {
        loadLoginPage();
    }
}
