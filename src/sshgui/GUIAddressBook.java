/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sshgui;

import java.io.IOException;

import sshgui.Controller.AddressBookController;
import sshgui.Controller.HomeController;
import sshgui.Controller.MasterPasswordController;
import sshgui.logic.Login;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author connorimrie
 */
public class GUIAddressBook{
    
	public GUIAddressBook(String masterPassword) throws IOException{
    	
    	FXMLLoader loader = new FXMLLoader(AddressBookController.class.getResource("/sshgui/Controller/addressbook.fxml"));
        Parent root = (Parent)loader.load();
        Scene scene = new Scene(root);
        Stage secondStage = new Stage();

        
        AddressBookController controller = (AddressBookController)loader.getController();
        controller.setMasterHash(Login.getHash(masterPassword));
        controller.setStage(secondStage);
    
        secondStage.setTitle("SSHGui - Server Address Book");
        secondStage.setScene(scene);
        secondStage.show();
    }
    
}
