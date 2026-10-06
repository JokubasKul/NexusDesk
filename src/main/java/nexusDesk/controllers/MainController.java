package nexusDesk.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

import java.io.IOException;

/**
 * Functionality of MainView.fxml which has the main menu(side bar and top bar). Inside are the others
 */
public class MainController {

    @FXML
    public StackPane mainContent;

    /**
     * Opens ProjectsView.fxml
     */
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
    /**
     * Opens TasksView.fxml
     */
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
    /**
     * Opens NotesView.fxml
     */
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
    /**
     * Opens BookmarksView.fxml
     */
    public void openBookmarks(){
        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/BookmarksView.fxml")
            );

            Node bookmarksView = loader.load();

            BookmarksController controller = loader.getController();
            controller.setMainContent(mainContent);

            mainContent.getChildren().setAll(bookmarksView);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Opens ConvertersView.fxml
     */
    public void openConverters(){
        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/ConvertersView.fxml")
            );

            Node convertersView = loader.load();

            ConvertersController controller = loader.getController();
            controller.setMainContent(mainContent);

            mainContent.getChildren().setAll(convertersView);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}