package pkg223071132_project;

import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class View_usersController implements Initializable {

    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, Integer> idColumn;
    @FXML private TableColumn<User, String> nameColumn;
    @FXML private TableColumn<User, String> emailColumn;
    @FXML private TableColumn<User, String> genderColumn;
    @FXML private TableColumn<User, String> roleColumn;
    @FXML private TableColumn<User, String> addressColumn;
    @FXML private TableColumn<User, String> phoneColumn;

    private ObservableList<User> userList = FXCollections.observableArrayList();
    @FXML
    private Button backAdmin;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));

        loadUsersFromDatabase();
    }

    private void loadUsersFromDatabase() {
        String url = "jdbc:mysql://localhost:3306/tanzu_app";
        String username = "root";
        String password = "1234";

        String sql = "SELECT id, full_name, email, gender, role, address, phone FROM users WHERE role != 'admin'";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String fullName = rs.getString("full_name");
                String email = rs.getString("email");
                String gender = rs.getString("gender");
                String role = rs.getString("role");
                String address = rs.getString("address");
                String phone = rs.getString("phone");

                userList.add(new User(id, fullName, email, gender, role, address, phone));
            }

            userTable.setItems(userList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handlebacktoadmin(ActionEvent event) {
        try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("admin_panel.fxml"));
        Parent root = loader.load();

        // Get current stage
        Stage stage = (Stage) userTable.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Admin Panel");
    } catch (IOException e) {
        e.printStackTrace();
    }

    }
}
