
package nexusDesk;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import nexusDesk.database.OptionDatabase;
import nexusDesk.models.Colour;

import java.sql.SQLException;
import java.util.List;

public class ColourPicker {

    public static Colour show() {

        try {
            OptionDatabase optionDatabase = new OptionDatabase();

            List<Colour> colours = optionDatabase.getAllColours();

            Dialog<Colour> dialog = new Dialog<>();

            dialog.setTitle("Select Colour");
            dialog.setHeaderText("Choose a colour");

            ButtonType cancel =
                    new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            dialog.getDialogPane().getButtonTypes().add(cancel);

            ListView<Colour> colourList = new ListView<>();
            colourList.getItems().addAll(colours);

            colourList.setCellFactory(list -> new ListCell<>() {

                @Override
                protected void updateItem(Colour colour, boolean empty) {

                    super.updateItem(colour, empty);

                    if (empty || colour == null) {
                        setGraphic(null);
                    } else {

                        HBox row = new HBox(10);
                        row.setAlignment(Pos.CENTER_LEFT);
                        row.setPadding(new Insets(5));

                        Pane preview = new Pane();
                        preview.setPrefSize(30, 20);

                        preview.setStyle(
                                "-fx-background-color: " + colour.getHex() + ";" +
                                        "-fx-border-color: #999999;" +
                                        "-fx-border-width: 1;"
                        );

                        Label name = new Label(colour.getColour());

                        row.getChildren().addAll(
                                preview,
                                name
                        );

                        setGraphic(row);
                    }
                }
            });

            dialog.getDialogPane().setContent(colourList);

            final Colour[] selectedColour = {null};

            colourList.setOnMouseClicked(event -> {

                if (event.getClickCount() == 1) {

                    Colour selected =
                            colourList.getSelectionModel().getSelectedItem();

                    if (selected != null) {
                        selectedColour[0] = selected;
                        dialog.close();
                    }
                }
            });

            dialog.showAndWait();

            return selectedColour[0];

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}