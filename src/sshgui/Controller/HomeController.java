/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sshgui.Controller;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import sshgui.*;
import sshgui.logic.*;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author connorimrie
 */
public class HomeController implements Initializable {
    private Stage stage;
    private Server connectedServer;
    private OutputStream consoleOut;
    //private ByteArrayOutputStream hiddenOut = new ByteArrayOutputStream();
    private OutputStream hiddenOut;
    private String hidden = "";

    /* Menus and Menu Items */
    @FXML
    private MenuItem addressButton;
    @FXML
    private MenuItem exitButton;
    @FXML
    private Menu commonTasksMenu;
    
    /* Text/Password Fields */
    @FXML
    private TextField quickConnectServer;
    @FXML
    private TextField quickConnectUsername;
    @FXML
    private PasswordField quickConnectPassword;
    @FXML
    private TextField quickConnectPort;
    
    /* Buttons */
    @FXML
    private Button quickConnectButton;
    @FXML
    private Button directoryButton;
    /* Text Input/Output Regions */
    @FXML
    private TextArea console;
    @FXML
    private TextField consoleInput;
    @FXML
    private ListView<String> explorerList;
    @FXML
    private TreeView<String> directoryTree;
    @FXML
    private TextField breadcrumbs;
    
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
        this.consoleOut = new OutputStream() {
            @Override
            public void write(int b){
                appendText(String.valueOf((char) b));
            }
        };        
        
        this.hiddenOut = new OutputStream() {
            @Override
            public void write(int b){
                appendTextHidden(String.valueOf((char) b));
            }
        };  
        
        /*
         * disable any buttons/menus that depend on connected server
     	*/
        /*BooleanProperty bool = new SimpleBooleanProperty();
        bool.setValue(this.connectedServer == null);
        this.directoryButton.disableProperty().bind(bool);*/
     
    }
    
    public void appendText(final String str) {
        //Platform.runLater(() -> console.appendText(str));
    	Platform.runLater(new Runnable(){

			@Override
			public void run() {
				console.appendText(str);
			}
    		
    	});
    }
    
    public void appendTextHidden(String str) {
    	//Platform.runLater(() -> hidden = hidden + str);
    }

    @FXML
    private void exitProgram() {
    	System.exit(0);
    }
    @FXML
    private void quickConnect() throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, JSchException, IOException, InterruptedException{
    	if (this.quickConnectServer.getText().length() > 0
    			&& this.quickConnectPassword.getText().length() > 0
    			&& this.quickConnectUsername.getText().length() > 0
    			){
    		String port = this.quickConnectPort.getText();
    		if (this.quickConnectPort.getText().trim().isEmpty()){
    			port = "22";
    		}
    		//create server
        	Server s = new Server(this.quickConnectServer.getText(), port);
        	Login l = new Login(this.quickConnectUsername.getText(), this.quickConnectPassword.getText(), false, "");
			s.connect(l);
        	s.setOutputStream(this.consoleOut);
    		this.connectedServer = s;
    		//Set the password to blank to prevent anyone 'pinching' it
    		this.quickConnectPassword.setText("");
    	}
    	else {
    		System.out.println("Please enter some details");
    	}
    	
    }
    
    public void connectAddress(Server s, Login l){
    	s.setClassOutputStream(new PrintStream(this.consoleOut));
    	try {
			s.connect(l);
	    	this.connectedServer = s;
		} catch (IOException | InterruptedException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException | JSchException e) {
			try {
				new GUIAlert("Could not connect","Please check you are using the correct Master Password");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
    	s.setClassOutputStream(System.out);
    	s.setOutputStream(this.consoleOut);
    }
    
    @FXML
    private void openMasterPassword(){
    	try {
			new GUIMasterPassword(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    private void fileSearch(int fileSizeMB) {
    	if (this.connectedServer !=null){
    		try {
				this.connectedServer.sendCommand("find / -type f -size +" + (String.valueOf(fileSizeMB)) + "M -exec ls -lh {} \\; | awk '{ print $9 \": \" $5 }'");
			} catch (JSchException | IOException | InterruptedException e) {
				try {
					new GUIAlert("Command failure", "Could not action that command. Please try again");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
    	}
    }
    
    @FXML
    public void fileSearch15(){
    	this.fileSearch(15);
    }
    
    @FXML
    public void fileSearch50(){
    	this.fileSearch(50);
    }
    
    @FXML
    public void fileSearch100(){
    	this.fileSearch(100);
    }
    
    @FXML
    private void sendCommand() throws JSchException, IOException{
    	if (this.connectedServer !=null){
	    	try {
	    		this.connectedServer.setOutputStream(this.consoleOut);
				this.connectedServer.sendCommand(this.consoleInput.getText());
				this.consoleInput.setText("");		
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
    }
    
    @FXML
    private void getCurrentDirectory() throws IOException {
    	if (this.connectedServer !=null){
	    	this.sendHiddenCommand("pwd");
	
	    	try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.breadcrumbs.setText(this.hidden);
			//System.out.println("---" + this.hiddenOut.toString());
			System.out.println(this.hidden);
    	}
    }
    
    private void sendHiddenCommand(String command){
    	if (this.connectedServer !=null){
	       	try {
	    		this.connectedServer.channel.setOutputStream(this.hiddenOut);
				this.connectedServer.sendCommand(command);
			} catch (JSchException | InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    }
}
