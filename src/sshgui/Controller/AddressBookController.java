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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author connorimrie
 */
public class AddressBookController implements Initializable {
    private Stage stage;
    private String masterHash; 
    private AddressBook book;
    private ObservableList<Address> addressBook;
    /* Menus and Menu Items */

    /* Text/Password Fields */
    @FXML
    private TextField nameField;
    @FXML
    private TextField hostField;
    @FXML
    private TextField portField;
    @FXML
    private TextField userField;
    @FXML
    private PasswordField passField;
    /* Buttons */
    
	/* Table Columns */
	@FXML
	private TableView<Address> serverTable;
	@FXML
	private TableColumn<Address, String> serverName;
	@FXML
	private TableColumn<Address, String> serverHost;
	@FXML
	private TableColumn<Address, Integer> serverPort;
	@FXML
	private TableColumn<Address, String> serverUser;
	@FXML
	private TableColumn<Address, String> serverPass;
	

    
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
    	this.book = book;
    	
    	
    	assert this.serverTable != null : "fx:id=\"tableview\" was not injected: check your FXML file 'UserMaster.fxml'.";
        serverName.setCellValueFactory(
        	new PropertyValueFactory<Address,String>("serverName"));
        serverHost.setCellValueFactory(
        	new PropertyValueFactory<Address,String>("serverHost"));
        serverPass.setCellValueFactory(
    		new PropertyValueFactory<Address, String>("serverPass"));
        /*changes the serverPass to a password field*/
        serverPass.setCellFactory(param -> new PasswordLabelCell());
        serverPort.setCellValueFactory(
        		new PropertyValueFactory<Address, Integer>("serverPort"));
        serverUser.setCellValueFactory(
        		new PropertyValueFactory<Address, String>("serverUser"));
        
        
        
       /*colPassword.setCellValueFactory(                
           new PropertyValueFactory<Usermaster,String>("userPassword"));
       colUserType.setCellValueFactory(
           new PropertyValueFactory<Usermaster,String>("userType"));        
       colPhoto.setCellValueFactory(
           new PropertyValueFactory<Object,ImageView>("userPhoto"));*/
        this.populateAddressBook();
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
    
    private void populateAddressBook(){
    	this.addressBook = FXCollections.observableArrayList(this.book.getList());
    	this.serverTable.setItems(this.addressBook);
    }
    
    @FXML
    public void saveServer(){
    	Server s = new Server(nameField.getText(), hostField.getText(), portField.getText());
    	Login l = new Login(userField.getText(), passField.getText(), true, this.masterHash);
    	Address a = new Address(s.getName(), s.getHost(), s.getPort(), l.getUsername(), l.getPassword());
    	this.addressBook.add(a);
    }

    @FXML
    public void connectToAddress(){
    	
    }
}
