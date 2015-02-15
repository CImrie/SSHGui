/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sshgui;

import java.io.IOException;
import sshgui.Controller.HomeController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author connorimrie
 */
public class SSHGui extends Application {
    private Stage stage;
    
    @Override
    public void start(Stage primaryStage) throws IOException{
    	stage = primaryStage;
        FXMLLoader loader = new FXMLLoader(HomeController.class.getResource("/sshgui/Controller/home.fxml"));
        Parent root = (Parent)loader.load();
        HomeController controller = (HomeController)loader.getController();
        controller.setStage(primaryStage);
        Scene scene = new Scene(root);
    
        stage.setTitle("SSHGui");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
