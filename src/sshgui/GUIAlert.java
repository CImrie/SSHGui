/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sshgui;

import java.io.IOException;

import sshgui.Controller.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author connorimrie
 */
public class GUIAlert {
    
    public GUIAlert(String message, String details) throws IOException{
        FXMLLoader loader = new FXMLLoader(AlertController.class.getResource("/sshgui/Controller/alert.fxml"));
        Parent root = (Parent)loader.load();
        Scene scene = new Scene(root);
        Stage secondStage = new Stage();

        
        AlertController controller = (AlertController)loader.getController();
        controller.setStage(secondStage);
        controller.setMessage(message);
        controller.setDetails(details);
        
        secondStage.setTitle("SSHGui - Alert");
        secondStage.setScene(scene);
        secondStage.show();
    }
    
}
