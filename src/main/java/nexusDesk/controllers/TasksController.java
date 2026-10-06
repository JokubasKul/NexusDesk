package nexusDesk.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import nexusDesk.ColourPicker;
import nexusDesk.database.OptionDatabase;
import nexusDesk.database.ProjectDatabase;
import nexusDesk.database.SubtaskDatabase;
import nexusDesk.database.TaskDatabase;
import nexusDesk.models.Colour;
import nexusDesk.models.Subtask;
import nexusDesk.models.Task;
import nexusDesk.controllers.MainController;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Functionality of Tasks module
 */
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
    private final SubtaskDatabase subtaskDatabase = new SubtaskDatabase();
    private final ProjectDatabase projectDatabase = new ProjectDatabase();

    private final ColourPicker colourPicker = new ColourPicker();

    /**
     * Sets up the tasks page
     * @param projectId the id of the project, needed to tell if the project is for personal tasks(projectId=0) or project tasks
     * @param colourId the colour id of the project needed to decide the colour of the header
     * @param projectTitle the project title
     */
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

    /**
     * Loops through all the tasks and sets them up properly
     */
    private void loadTasks() {

        tasksList.getChildren().clear();

        try {
            List<Task> tasks = taskDatabase.getAllProjectTasks(projectId);

            for (Task task : tasks) {

                HBox taskCard = new HBox(10);
                taskCard.setAlignment(Pos.CENTER_LEFT);
                taskCard.getStyleClass().add("taskCard");

                taskCard.setOnMouseClicked(event -> {
                    showTaskDetails(task);
                });

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
                        playCompletionSound();

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

    /**
     * Adds a new task
     */
    @FXML
    private void addTask() {

        TextField textField = new TextField();
        textField.setPromptText("Task name...");

        textField.setOnAction(event -> saveTask(textField));

        tasksList.getChildren().add(0, textField);

        textField.requestFocus();
    }

    /**
     * Saves new task to the database and updates the task list
     * @param textField the name of the new task
     */
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

    /**
     * Shows the subtasks in the detailsBox
     * @param task the task whose subtasks we are showing
     */
    private void showTaskDetails(Task task) {

        taskDetails.setTop(null);
        taskDetails.setCenter(null);
        taskDetails.setBottom(null);

        VBox detailsBox = new VBox(15);
        detailsBox.setPadding(new Insets(20));


        HBox titleBox = new HBox(8);
        titleBox.setAlignment(Pos.CENTER_LEFT);

        Label taskName = new Label(task.getName());
        taskName.getStyleClass().add("subtaskDetailsTitle");


        Button editTaskButton = new Button("✎");
        editTaskButton.getStyleClass().add("editTitleButton");

        editTaskButton.setOnAction(event ->
                editTaskName(task, taskName)
        );


        titleBox.getChildren().addAll(
                taskName,
                editTaskButton
        );


        VBox subtasksList = new VBox(8);

        Label addSubtask = new Label("+ Add Subtask");
        addSubtask.getStyleClass().add("addSubtask");

        addSubtask.setOnMouseClicked(event ->
                addSubtask(task, subtasksList)
        );


        detailsBox.getChildren().addAll(
                titleBox,
                addSubtask,
                subtasksList
        );

        loadSubtasks(task, subtasksList);

        taskDetails.setCenter(detailsBox);
    }

    /**
     * Loops through all the subtasks and sets them up
     * @param task the task whose subtasks we are loading
     * @param subtasksList the VBox where the subtasks are stored
     */
    private void loadSubtasks(Task task, VBox subtasksList) {

        subtasksList.getChildren().clear();

        try {
            List<Subtask> subtasks = subtaskDatabase.getAllSubtasks(task.getTaskId());

            for (Subtask subtask : subtasks) {

                HBox subtaskBox = new HBox(8);
                subtaskBox.setAlignment(Pos.CENTER_LEFT);
                subtaskBox.getStyleClass().add("subtaskCard");


                Button completeButton = new Button("○");
                completeButton.getStyleClass().add("subtaskComplete");

                completeButton.setOnAction(event -> {

                    try {
                        subtaskDatabase.updateSubtaskCompletion(subtask.getSubtaskId());

                        loadSubtasks(task, subtasksList);

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });


                Label subtaskName = new Label(subtask.getName());
                subtaskName.getStyleClass().add("subtaskName");

                TextField editName = new TextField(subtask.getName());
                editName.setVisible(false);
                editName.setManaged(false);


                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);


                Button editButton = new Button("✎");
                editButton.getStyleClass().add("subtaskEdit");

                editButton.setOnAction(event ->
                        editSubtaskName(subtask, subtaskName, editName
                        )
                );


                subtaskBox.getChildren().addAll(
                        completeButton,
                        subtaskName,
                        editName,
                        spacer,
                        editButton
                );

                subtasksList.getChildren().add(subtaskBox);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a new subtask to the database and updates the list
     * @param task the task whose subtasks we are managing
     * @param subtasksList the VBox where the subtasks are managed
     */
    private void addSubtask(Task task, VBox subtasksList) {

        TextField textField = new TextField();
        textField.setPromptText("Subtask name...");

        textField.setOnAction(event -> {

            String name = textField.getText().trim();

            if (name.isEmpty()) {
                subtasksList.getChildren().remove(textField);
                return;
            }

            try {
                subtaskDatabase.createSubtask(task.getTaskId(), name);

                loadSubtasks(task, subtasksList);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        subtasksList.getChildren().add(
                0,
                textField
        );

        textField.requestFocus();
    }

    /**
     * Changes the name of the task
     * @param task the task whose name we are changing
     */
    private void editTaskName(Task task, Label taskName) {

        TextField textField = new TextField(task.getName());

        textField.setPrefWidth(200);

        int index = ((HBox) taskName.getParent()).getChildren().indexOf(taskName);
        HBox parent = (HBox) taskName.getParent();

        parent.getChildren().set(index, textField);

        textField.requestFocus();
        textField.selectAll();

        textField.setOnAction(event -> {

            String newName = textField.getText().trim();

            if (newName.isEmpty()) {
                return;
            }

            try {
                taskDatabase.updateTaskName(
                        task.getTaskId(),
                        newName
                );

                task.setName(newName);

                parent.getChildren().set(
                        index,
                        taskName
                );

                taskName.setText(newName);
                loadTasks();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                parent.getChildren().set(
                        index,
                        taskName
                );
            }
        });
    }

    /**
     * Changes the subtask's name
     * @param subtask the subtask whose name we are changing
     * @param name the current name of the subtask
     * @param editName the textField where the new name is typed in
     */
    private void editSubtaskName(Subtask subtask, Label name, TextField editName) {

        name.setVisible(false);
        name.setManaged(false);

        editName.setVisible(true);
        editName.setManaged(true);

        editName.requestFocus();
        editName.selectAll();


        editName.setOnAction(event -> {

            String newName = editName.getText().trim();

            if (!newName.isEmpty()) {

                try {

                    subtaskDatabase.updateSubtaskName(
                            subtask.getSubtaskId(),
                            newName
                    );

                    subtask.setName(newName);
                    name.setText(newName);

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            editName.setVisible(false);
            editName.setManaged(false);

            name.setVisible(true);
            name.setManaged(true);
        });

        editName.setOnKeyPressed(event -> {

            if (event.getCode() == KeyCode.ESCAPE) {

                editName.setVisible(false);
                editName.setManaged(false);

                name.setVisible(true);
                name.setManaged(true);
            }
        });
    }

    /**
     * Shows the comment in detailsBox
     * @param task the task whose comment we are looking at
     */
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

    /**
     * Loads the projects page
     */
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

    /**
     * Changes the project name
     */
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

    /**
     * Changes the project name in the database
     */
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

    /**
     * Plays a sound when completing a task
     */
    private void playCompletionSound() {

        try {

            String sound = getClass()
                    .getResource("/sounds/completeTask.mp3")
                    .toExternalForm();

            Media media = new Media(sound);
            MediaPlayer mediaPlayer = new MediaPlayer(media);

            mediaPlayer.play();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}