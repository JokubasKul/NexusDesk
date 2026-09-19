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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ProjectsView.fxml"));
            Node projectsView = loader.load();
            mainContent.getChildren().setAll(projectsView);
        } catch (IOException e) {
            e.printStackTrace();
        };
    }
    public void openTasks(){
        System.out.println("Tasks opened");
    }
    public void openNotes(){
        System.out.println("Notes opened");
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
