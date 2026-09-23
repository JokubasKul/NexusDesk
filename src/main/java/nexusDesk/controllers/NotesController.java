package nexusDesk.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import nexusDesk.ColourPicker;
import nexusDesk.database.NoteDatabase;
import nexusDesk.database.OptionDatabase;
import nexusDesk.models.Colour;
import nexusDesk.models.Note;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class NotesController {

    @FXML
    private VBox notesList;

    private StackPane mainContent;

    private final NoteDatabase noteDatabase = new NoteDatabase();
    private final OptionDatabase optionDatabase = new OptionDatabase();

    @FXML
    public void initialize() {
        loadNotes();
    }

    public void setMainContent(StackPane mainContent) {
        this.mainContent = mainContent;
    }

    private void loadNotes() {

        notesList.getChildren().clear();

        try {

            List<Note> notes = noteDatabase.getAllNotes();

            for (Note note : notes) {

                HBox noteCard = new HBox(10);
                noteCard.setAlignment(Pos.CENTER_LEFT);
                noteCard.getStyleClass().add("noteCard");

                noteCard.setOnMouseClicked(event -> openNote(note));

                String colourHex = optionDatabase.getColourHex(note.getColourId());
                noteCard.setStyle(
                        "-fx-background-color: " + colourHex + "99;"
                );


                Label title = new Label(note.getTitle());
                title.getStyleClass().add("noteTitle");


                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);


                Button colourButton = new Button("\uD83C\uDFA8");
                colourButton.getStyleClass().add("noteColour");

                colourButton.setStyle(
                        "-fx-background-color: transparent;"
                );

                colourButton.setOnAction(event -> {
                    event.consume();

                    Colour selectedColour = ColourPicker.show();

                    if (selectedColour != null) {
                        try {
                            optionDatabase.updateNoteColour(
                                    note.getNoteId(),
                                    selectedColour.getColourId()
                            );

                            loadNotes();

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });


                Button deleteButton = new Button("✕");
                deleteButton.getStyleClass().add("noteDelete");

                deleteButton.setOnAction(event -> {

                    event.consume();

                    try {

                        noteDatabase.deleteNote(
                                note.getNoteId()
                        );

                        notesList.getChildren().remove(noteCard);

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });


                noteCard.getChildren().addAll(
                        colourButton,
                        title,
                        spacer,
                        deleteButton
                );

                notesList.getChildren().add(noteCard);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void addNote() {

        TextField textField = new TextField();
        textField.setPromptText("Note title...");

        textField.setOnAction(event -> saveNote(textField));

        notesList.getChildren().add(0, textField);

        textField.requestFocus();
    }

    private void saveNote(TextField textField) {

        String title = textField.getText().trim();

        if (title.isEmpty()) {

            notesList.getChildren().remove(textField);

            return;
        }

        try {

            noteDatabase.createNote(title);

            notesList.getChildren().remove(textField);

            loadNotes();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void openNote(Note note) {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/NoteView.fxml")
            );

            Node noteView = loader.load();

            NoteController controller = loader.getController();

            controller.setMainContent(mainContent);
            controller.setNote(note);

            mainContent.getChildren().setAll(noteView);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}