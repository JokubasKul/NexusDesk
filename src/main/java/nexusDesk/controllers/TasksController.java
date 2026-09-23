package nexusDesk.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import nexusDesk.ColourPicker;
import nexusDesk.database.OptionDatabase;
import nexusDesk.database.TaskDatabase;
import nexusDesk.models.Colour;
import nexusDesk.models.Task;

import java.sql.SQLException;
import java.util.List;

public class TasksController {

    @FXML
    private VBox taskHeader;

    private int projectId;
    private int projectColourId;

    @FXML
    private Label taskSectionTitle;
    @FXML
    private VBox tasksList;
    @FXML
    private BorderPane taskDetails;


    private final OptionDatabase optionDatabase = new OptionDatabase();
    private final TaskDatabase taskDatabase = new TaskDatabase();


    public void setProject(int projectId, int colourId) {

        this.projectId = projectId;
        this.projectColourId = colourId;

        if (projectId == 0) {
            taskSectionTitle.setText("Personal Tasks");
        } else {
            taskSectionTitle.setText("Project Tasks");
        }

        try {
            String colour = optionDatabase.getColourHex(colourId);

            taskHeader.setStyle(
                    "-fx-background-color: " + colour + "4D;"
            );

        } catch (SQLException e) {
            e.printStackTrace();
        }

        loadTasks();
    }

    private void loadTasks() {

        tasksList.getChildren().clear();

        try {
            List<Task> tasks = taskDatabase.getAllProjectTasks(projectId);

            for (Task task : tasks) {

                HBox taskCard = new HBox(10);
                taskCard.getStyleClass().add("taskCard");

                Button completeButton = new Button("○");
                completeButton.getStyleClass().add("taskComplete");

                completeButton.setOnAction(event -> {

                    try {
                        taskDatabase.updateTaskCompletion(task.getTaskId());
                        tasksList.getChildren().remove(taskCard);

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });

                Label taskName = new Label(task.getName());
                taskName.getStyleClass().add("taskName");

                Button colourButton = new Button("●");
                colourButton.getStyleClass().add("taskColour");

                colourButton.setOnAction(event -> {

                    Colour selectedColour = ColourPicker.show();

                    if (selectedColour != null) {

                        try {
                            optionDatabase.updateTaskColour(
                                    task.getTaskId(),
                                    selectedColour.getColourId()
                            );

                            // Refresh task list later

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });

                Button commentButton = new Button("\ud83d\udcc4");
                commentButton.getStyleClass().add("taskComment");

                commentButton.setOnMouseClicked(event -> {
                    event.consume();
                    showComment(task);
                });

                String comment = task.getComment();

                String preview;

                if (comment == null || comment.isBlank()) {
                    preview = "No comment";
                } else {
                    preview = comment.length() > 50
                            ? comment.substring(0, 50) + "..."
                            : comment;
                }
                commentButton.setTooltip(new Tooltip(preview));

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                taskCard.getChildren().addAll(
                        completeButton,
                        taskName,
                        spacer,
                        colourButton,
                        commentButton
                );

                tasksList.getChildren().add(taskCard);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void addTask() {

        TextField textField = new TextField();
        textField.setPromptText("Task name...");

        textField.setOnAction(event -> saveTask(textField));

        tasksList.getChildren().add(0, textField);

        textField.requestFocus();
    }

    private void saveTask(TextField textField) {

        String name = textField.getText().trim();

        if (name.isEmpty()) {
            tasksList.getChildren().remove(textField);
            return;
        }
        try {
            taskDatabase.createTask(projectId, name);
            tasksList.getChildren().remove(textField);

            loadTasks();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showComment(Task task) {

        taskDetails.setTop(null);
        taskDetails.setCenter(null);
        taskDetails.setBottom(null);

        VBox commentBox = new VBox(10);
        commentBox.setPadding(new Insets(20));

        Label detailsTitle = new Label(task.getName());
        detailsTitle.getStyleClass().add("detailsTitle");

        Label commentTitle = new Label("Comment");
        commentTitle.getStyleClass().add("commentTitle");

        TextArea comment = new TextArea();

        if (task.getComment() != null) {
            comment.setText(task.getComment());
        }
        comment.setWrapText(true);

        HBox buttons = new HBox(10);
        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");

        buttons.getChildren().addAll(
                saveButton,
                cancelButton
        );

        saveButton.setOnAction(event -> {
            try {
                taskDatabase.updateTaskComment(
                        task.getTaskId(),
                        comment.getText()
                );

                task.setComment(comment.getText());
                taskDetails.setCenter(null);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        cancelButton.setOnAction(event -> {
            taskDetails.setCenter(null);
        });

        commentBox.getChildren().addAll(
                detailsTitle,
                commentTitle,
                comment,
                buttons
        );

        taskDetails.setCenter(commentBox);
    }

}