package nexusDesk.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class MainController {

    @FXML
    public StackPane mainContent;

    public void openProjects(){
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/ProjectsView.fxml")
            );

            Node projectsView = loader.load();

            ProjectsController controller = loader.getController();
            controller.setMainContent(mainContent);

            mainContent.getChildren().setAll(projectsView);
        } catch (IOException e) {
            e.printStackTrace();
        };
    }
    @FXML
    public void openTasks() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/TasksView.fxml")
            );

            Node tasksView = loader.load();

            TasksController tasksController = loader.getController();
            tasksController.setMainContent(mainContent);
            tasksController.setProject(0, 26, "Personal tasks");

            mainContent.getChildren().setAll(tasksView);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void openNotes(){
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
    public void openBookmarks(){
        System.out.println("Bookmarks opened");
    }
    public void openConverters(){
        System.out.println("Converters opened");
    }
    public void openCalculator(){
        System.out.println("Calculator opened");
    }
}