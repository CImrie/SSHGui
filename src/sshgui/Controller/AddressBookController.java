/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sshgui.Controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.jcraft.jsch.JSchException;
import com.thoughtworks.xstream.XStream;

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
    private String masterPassword; 
    private AddressBook book;
    private ObservableList<Address> addressBook;
    private HomeController home;
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
        this.populateAddressBook();
        this.loadXML();
    }    

    @FXML
    private void exitProgram() {
    	stage.close();
    }
    
    private String getMasterPassword(){
    	return this.masterPassword;
    }
    
    public void setMasterPassword(String pass){
    	this.masterPassword = pass;
    }
    
    private void populateAddressBook(){
    	this.addressBook = FXCollections.observableArrayList(this.book.getList());
    	this.serverTable.setItems(this.addressBook);
    }
    
    @FXML
    public void saveServer() throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IOException{
    	Server s = new Server(nameField.getText(), hostField.getText(), portField.getText());
    	Login l = new Login(userField.getText(), passField.getText(), false, this.masterPassword);
    	Address a = new Address(s.getName(), s.getHost(), s.getPort(), l.getUsername(), l.getEncryptedPassword(), l.getIv());
    	this.addressBook.add(a);
    }

    @FXML
    public void connectToAddress(){
    	Address a = serverTable.getSelectionModel().getSelectedItem();
    	connect(a);
    }
    
    @FXML
    public void deleteServer(){
    	this.addressBook.remove(serverTable.getSelectionModel().getSelectedItem());
    }
    
	private void connect(Address a){
		Server s = new Server(a.getServerName(), a.getServerHost(), String.valueOf(a.getServerPort()));
		Login l = new Login(a.getServerUser(), a.getServerPass(), true, this.masterPassword);
		l.setIv(a.getIV());
		home.connectAddress(s, l);
		this.stage.close();
	}
	
	public void setHomeController(HomeController home){
		this.home = home;
	}
	
	@FXML
	private void storeXML(){
		XStream xstream = new XStream();
		  xstream.alias("address", Address.class);

		  // Convert ObservableList to a normal ArrayList
		  ArrayList<Address> addressList = new ArrayList<>(this.addressBook);

		  String xml = xstream.toXML(addressList);
		  try {
			File file = new File(new File("").getAbsolutePath() + "/addressbook.xml");
		    FileUtil.saveFile(xml, file );

		    //setPersonFilePath(file);
		  } catch (Exception e) { // catches ANY exception
			  System.out.println("ERROR");
		    /*Dialogs.showErrorDialog(primaryStage,
		        "Could not save data to file:\n" + file.getPath(),
		        "Could not save data", "Error", e);*/
		  }
		  finally{
			  stage.close();
		  }
	}
	
	private void loadXML(){
		XStream xstream = new XStream();
		  xstream.alias("address", Address.class);

		  try {
			File file = new File(new File("").getAbsolutePath() + "/addressbook.xml");
		    String xml = FileUtil.readFile(file);

		    ArrayList<Address> addressList = (ArrayList<Address>) xstream.fromXML(xml);

		    this.addressBook.clear();
		    this.addressBook.addAll(addressList);

		  } catch (Exception e) { // catches ANY exception
			  System.out.println("ERROR");
		    /*Dialogs.showErrorDialog(primaryStage,
		        "Could not load data from file:\n" + file.getPath(),
		        "Could not load data", "Error", e);*/
		  }
	}
}
