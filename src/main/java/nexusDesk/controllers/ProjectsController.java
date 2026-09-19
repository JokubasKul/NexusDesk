package nexusDesk.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import nexusDesk.database.ProjectDatabase;
import nexusDesk.models.Project;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ProjectsController {

    @FXML
    private VBox projectCard;

    private final ProjectDatabase projectDatabase = new ProjectDatabase();

    @FXML
    public void initialize() {
        loadProjects();
    }

    private void loadProjects() {

        projectCard.getChildren().clear();

        try {
            List<Project> projects = projectDatabase.getAllProjects();

            for (Project project : projects) {

                Button projectButton = new Button(project.getTitle());

                projectButton.setMaxWidth(Double.MAX_VALUE);
                projectButton.getStyleClass().add("project-card");

                projectButton.setOnAction(event -> {
                    System.out.println("Opened project: " + project.getTitle());
                });
                projectCard.getChildren().add(projectButton);
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

        projectCard.getChildren().add(0, textField);

        textField.requestFocus();

    }

    private void saveProject(TextField textField) {

        String title = textField.getText().trim();

        if (title.isEmpty()) {
            projectCard.getChildren().remove(textField);
            return;
        }

        try {
            projectDatabase.createProject(title);
            projectCard.getChildren().remove(textField);

            loadProjects();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
