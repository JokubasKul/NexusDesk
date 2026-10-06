package nexusDesk.controllers;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import nexusDesk.database.NoteDatabase;
import nexusDesk.database.OptionDatabase;
import nexusDesk.models.Note;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Functionality of Notes module's note
 */
public class NoteController {

    @FXML
    private Label noteTitle;
    @FXML
    private VBox noteHeader;
    @FXML
    private HBox titleBox;
    @FXML
    private Button editTitleButton;
    @FXML
    private TextArea noteContent;

    private Note note;
    private final NoteDatabase noteDatabase = new NoteDatabase();
    private final OptionDatabase optionDatabase = new OptionDatabase();

    private final PauseTransition saveDelay = new PauseTransition(Duration.millis(500));

    private StackPane mainContent;
    public void setMainContent(StackPane mainContent) {
        this.mainContent = mainContent;
    }

    /**
     * Sets up the note
     * @param note the note we are setting up
     */
    public void setNote(Note note) {

        this.note = note;
        noteTitle.setText(note.getTitle());

        if (note.getContent() != null) {
            noteContent.setText(note.getContent());
        }

        try {
            String colour = optionDatabase.getColourHex(note.getColourId());

            noteHeader.setStyle(
                    "-fx-background-color: " + colour + "4D;"
            );

        } catch (SQLException e) {
            e.printStackTrace();
        }

        noteContent.textProperty().addListener(
                (observable, oldValue, newValue) -> {

                    saveDelay.setOnFinished(event -> saveContent(newValue));

                    saveDelay.playFromStart();
                }
        );
    }

    /**
     * Updates the content in the database
     * @param content the content
     */
    private void saveContent(String content) {

        try {

            noteDatabase.updateNoteContent(
                    note.getNoteId(),
                    content
            );

            note.setContent(content);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns to the notes page
     */
    @FXML private void returnToNotes() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/NotesView.fxml")
            );

            Node notesView = loader.load();

            NotesController controller = loader.getController();
            controller.setMainContent(mainContent);

            mainContent.getChildren().setAll(notesView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the note title
     */
    @FXML
    private void editNoteTitle() {

        TextField titleField = new TextField(noteTitle.getText());
        titleField.setPrefWidth(200);
        titleField.setStyle("-fx-font-size: 18px;");

        int index = titleBox.getChildren().indexOf(noteTitle);

        titleBox.getChildren().set(index, titleField);

        titleField.requestFocus();
        titleField.selectAll();

        titleField.setOnAction(event ->
                saveNoteTitle(titleField)
        );

        titleField.setOnKeyPressed(event -> {

            if (event.getCode() == KeyCode.ESCAPE) {
                titleBox.getChildren().set(index, noteTitle);
            }
        });
    }

    /**
     * Saves the changes to the database
     * @param titleField the new name
     */
    private void saveNoteTitle(TextField titleField) {

        String newTitle = titleField.getText().trim();

        if (newTitle.isEmpty()) {
            return;
        }

        try {

            noteDatabase.updateNoteTitle(
                    note.getNoteId(),
                    newTitle
            );

            note.setTitle(newTitle);
            noteTitle.setText(newTitle);

            int index = titleBox.getChildren().indexOf(titleField);

            titleBox.getChildren().set(index, noteTitle);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}