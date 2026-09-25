package nexusDesk.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.util.Duration;
import nexusDesk.ColourPicker;
import nexusDesk.database.OptionDatabase;
import nexusDesk.database.ProjectDatabase;
import nexusDesk.database.TaskDatabase;
import nexusDesk.models.Colour;
import nexusDesk.models.Task;
import nexusDesk.controllers.MainController;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class TasksController {

    @FXML
    private VBox taskHeader;
    @FXML
    private HBox titleBox;
    @FXML
    private Label taskSectionTitle;
    @FXML
    private Button editTitleButton;

    private int projectId;
    private int projectColourId;

    @FXML
    private VBox tasksList;
    @FXML
    private BorderPane taskDetails;

    private StackPane mainContent;

    public void setMainContent(StackPane mainContent) {
        this.mainContent = mainContent;
    }

    private final OptionDatabase optionDatabase = new OptionDatabase();
    private final TaskDatabase taskDatabase = new TaskDatabase();
    private final ProjectDatabase projectDatabase = new ProjectDatabase();

    private final ColourPicker colourPicker = new ColourPicker();


    public void setProject(int projectId, int colourId, String projectTitle) {

        this.projectId = projectId;
        this.projectColourId = colourId;

        taskSectionTitle.setText(projectTitle);

        if (projectId == 0) {
            taskSectionTitle.setText("Personal Tasks");
            editTitleButton.setDisable(true);
            editTitleButton.setVisible(false);
        } else {
            taskSectionTitle.setText(projectTitle);
            editTitleButton.setDisable(false);
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
                taskCard.setAlignment(Pos.CENTER_LEFT);
                taskCard.getStyleClass().add("taskCard");

                String colourHex = optionDatabase.getColourHex(task.getColourId());
                taskCard.setStyle(
                        "-fx-background-color: " + colourHex + "99" + ";"
                );

                String darkenedColour = colourPicker.darkenColour(colourHex);
                taskCard.setOnMouseEntered(mouseEvent ->
                        taskCard.setStyle(
                                "-fx-background-color:"  + darkenedColour + "99;"
                        )
                );
                taskCard.setOnMouseExited(mouseEvent ->
                        taskCard.setStyle(
                                "-fx-background-color:"  + colourHex + "99;"
                        )
                );


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


                Button colourButton = new Button("\uD83C\uDFA8");
                colourButton.getStyleClass().add("taskColour");

                colourButton.setOnAction(event -> {
                    Colour selectedColour = ColourPicker.show();

                    if (selectedColour != null) {

                        try {
                            optionDatabase.updateTaskColour(
                                    task.getTaskId(),
                                    selectedColour.getColourId()
                            );

                            loadTasks();

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

                Label tooltipLabel = new Label(task.getComment());
                tooltipLabel.setWrapText(true);
                tooltipLabel.setPrefWidth(300);

                Tooltip commentTooltip = new Tooltip();
                commentTooltip.setGraphic(tooltipLabel);
                commentTooltip.setShowDelay(Duration.ZERO);

                commentButton.setTooltip(commentTooltip);

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

    @FXML
    private void returnToProjects() throws IOException {

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/ProjectsView.fxml")
            );

            Node projectsView = loader.load();

            ProjectsController projectsController = loader.getController();
            projectsController.setMainContent(mainContent);

            mainContent.getChildren().setAll(projectsView);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void editProjectTitle() {

        TextField titleField = new TextField(taskSectionTitle.getText());
        titleField.setPrefWidth(200);
        titleField.setStyle("-fx-font-size: 18px;");

        int index = titleBox.getChildren().indexOf(taskSectionTitle);

        titleBox.getChildren().set(index, titleField);

        titleField.requestFocus();
        titleField.selectAll();

        titleField.setOnAction(event -> saveProjectTitle(titleField));

        titleField.setOnKeyPressed(event -> {

            if (event.getCode() == KeyCode.ESCAPE) {
                titleBox.getChildren().set(index, taskSectionTitle);
            }
        });
    }

    private void saveProjectTitle(TextField titleField) {

        String newTitle = titleField.getText().trim();

        if (newTitle.isEmpty()) {
            return;
        }

        try {

            projectDatabase.updateProjectTitle(
                    projectId,
                    newTitle
            );

            taskSectionTitle.setText(newTitle);

            int index = titleBox.getChildren().indexOf(titleField);

            titleBox.getChildren().set(index, taskSectionTitle);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}