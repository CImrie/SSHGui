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
public class GUIMasterPassword {
    
    public GUIMasterPassword(Stage primaryStage) throws IOException{
        FXMLLoader loader = new FXMLLoader(MasterPasswordController.class.getResource("/sshgui/Controller/masterPassword.fxml"));
        Parent root = (Parent)loader.load();
        Scene scene = new Scene(root);
        Stage secondStage = new Stage();

        
        MasterPasswordController controller = (MasterPasswordController)loader.getController();
        controller.setStage(secondStage);
    
        secondStage.setTitle("SSHGui - Server Address Book");
        secondStage.setScene(scene);
        secondStage.show();
    }
    
}
