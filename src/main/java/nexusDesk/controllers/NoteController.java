package nexusDesk.controllers;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import nexusDesk.database.NoteDatabase;
import nexusDesk.models.Note;

import java.sql.SQLException;

public class NoteController {

    @FXML
    private Label noteTitle;

    @FXML
    private TextArea noteContent;

    private StackPane mainContent;

    private Note note;

    private final NoteDatabase noteDatabase = new NoteDatabase();

    private final PauseTransition saveDelay =
            new PauseTransition(Duration.millis(500));

    public void setMainContent(StackPane mainContent) {
        this.mainContent = mainContent;
    }

    public void setNote(Note note) {

        this.note = note;

        noteTitle.setText(note.getTitle());

        if (note.getContent() != null) {
            noteContent.setText(note.getContent());
        }

        noteContent.textProperty().addListener(
                (observable, oldValue, newValue) -> {

                    saveDelay.setOnFinished(event -> saveContent(newValue));

                    saveDelay.playFromStart();
                }
        );
    }

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
}