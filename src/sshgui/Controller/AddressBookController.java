/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sshgui.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jcraft.jsch.JSchException;

import sshgui.GUIAddressBook;
import sshgui.logic.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author connorimrie
 */
public class AddressBookController implements Initializable {
    private Stage stage;
    private String masterHash; 
    /* Menus and Menu Items */

    /* Text/Password Fields */

    /* Buttons */

    
    public void setStage(Stage stage){
        this.stage = stage;
    }
    
    public Stage getStage(){
    	return this.stage;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	AddressBook book = new AddressBook();
    	book.loadFromXML();
    }    

    @FXML
    private void exitProgram() {
    	Platform.exit();
    }
    
    private String getMasterPassword(){
    	return this.masterHash;
    }
    
    public void setMasterHash(String hash){
    	this.masterHash = hash;
    }
    

}
