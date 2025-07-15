package pkg223071132_project;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.control.ListView;
import java.io.IOException;

public class CartItemCell extends ListCell<Product> {
    private HBox root;
    private CartItemCellController controller;

    public CartItemCell() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CartItemCell.fxml"));
            root = loader.load();
            controller = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void updateItem(Product item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setGraphic(null);
        } else {
            controller.setData(item, getListView(), () -> {}); // Pass empty callback for now
            setGraphic(root);
        }
    }
}
