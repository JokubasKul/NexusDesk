package nexusDesk.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import nexusDesk.ColourPicker;
import nexusDesk.database.OptionDatabase;
import nexusDesk.database.ProjectDatabase;
import nexusDesk.models.Colour;
import nexusDesk.models.Project;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ProjectsController {

    @FXML
    private VBox projectsList;

    private StackPane mainContent;

    private final ProjectDatabase projectDatabase = new ProjectDatabase();
    private final OptionDatabase optionDatabase = new OptionDatabase();

    private final ColourPicker colourPicker = new ColourPicker();

    public void setMainContent(StackPane mainContent) {
        this.mainContent = mainContent;
    }

    @FXML
    public void initialize() {
        loadProjects();
    }

    private void loadProjects() {

        projectsList.getChildren().clear();

        try {
            List<Project> projects = projectDatabase.getAllProjects();

            for (Project project : projects) {

                HBox projectCard = new HBox(10);
                projectCard.getStyleClass().add("projectCard");

                String colourHex = optionDatabase.getColourHex(project.getColourId());
                projectCard.setStyle(
                        "-fx-background-color: " + colourHex + "99" + ";"
                );

                String darkenedColour = colourPicker.darkenColour(colourHex);
                projectCard.setOnMouseEntered(mouseEvent ->
                        projectCard.setStyle(
                                "-fx-background-color:"  + darkenedColour + "99;"
                        )
                );
                projectCard.setOnMouseExited(mouseEvent ->
                        projectCard.setStyle(
                                "-fx-background-color:"  + colourHex + "99;"
                        )
                );


                Button colourButton = new Button("\uD83C\uDFA8");
                colourButton.getStyleClass().add("colourButton");

                colourButton.setOnAction(event -> {
                    Colour selectedColour = ColourPicker.show();

                    if (selectedColour != null) {

                        try {
                            optionDatabase.updateProjectColour(
                                    project.getProjectId(),
                                    selectedColour.getColourId()
                            );

                            loadProjects();

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });


                Button projectName = new Button(project.getTitle());
                projectName.getStyleClass().add("projectName");

                projectName.setOnAction(event -> {

                    try {
                        FXMLLoader loader = new FXMLLoader(
                                getClass().getResource("/fxml/TasksView.fxml")
                        );

                        Node tasksView = loader.load();

                        TasksController tasksController = loader.getController();

                        tasksController.setMainContent(mainContent);

                        tasksController.setProject(
                                project.getProjectId(),
                                project.getColourId(),
                                project.getTitle()
                        );

                        mainContent.getChildren().setAll(tasksView);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                HBox.setHgrow(projectName, Priority.ALWAYS);
                projectName.setMaxWidth(Double.MAX_VALUE);

                Button deleteButton = new Button("×");
                deleteButton.getStyleClass().add("deleteButton");

                deleteButton.setOnAction(event -> {
                    try {
                        projectDatabase.deleteProject(project.getProjectId());
                        loadProjects();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });

                projectCard.getChildren().addAll(
                        projectName,
                        colourButton,
                        deleteButton
                );

                projectsList.getChildren().add(projectCard);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void addProject() {

        TextField textField = new TextField();
        textField.setPromptText("Project name...");
        textField.setOnAction(event -> saveProject(textField));

        projectsList.getChildren().add(0, textField);

        textField.requestFocus();

    }

    private void saveProject(TextField textField) {

        String title = textField.getText().trim();

        if (title.isEmpty()) {
            projectsList.getChildren().remove(textField);
            return;
        }

        try {
            projectDatabase.createProject(title);
            projectsList.getChildren().remove(textField);

            loadProjects();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}