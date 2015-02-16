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
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import sshgui.*;
import sshgui.logic.*;
import javafx.application.Platform;
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
        
        this.quickConnectServer.setText("connorimrie.me");
        this.quickConnectUsername.setText("root");
        this.quickConnectPassword.setText("ha$R1014@");
        this.quickConnectPort.setText("22");
    }
    
    public void appendText(String str) {
        Platform.runLater(() -> console.appendText(str));
    }
    
    public void appendTextHidden(String str) {
    	Platform.runLater(() -> hidden = hidden + str);
    }

    @FXML
    private void exitProgram() {
    	System.exit(0);
    }
    @FXML
    private void quickConnect(){
    	if (this.quickConnectServer.getText().length() > 0
    			&& this.quickConnectPort.getText().length() > 0
    			&& this.quickConnectPassword.getText().length() > 0
    			&& this.quickConnectUsername.getText().length() > 0
    			){
    		//create server
        	Server s = new Server(this.quickConnectServer.getText(), this.quickConnectPort.getText());
        	Login l = new Login(this.quickConnectUsername.getText(), this.quickConnectPassword.getText(), false, null);
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
    
    @FXML
    private void openMasterPassword(){
    	try {
			new GUIMasterPassword(this.getStage());
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    @FXML 
    private void fileSearch() throws JSchException, IOException, InterruptedException{
    	this.connectedServer.sendCommand("ls");
    }
    
    @FXML
    private void sendCommand() throws JSchException, IOException{
    	try {
    		this.connectedServer.setOutputStream(this.consoleOut);
			this.connectedServer.sendCommand(this.consoleInput.getText());
			this.consoleInput.setText("");		
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
    
    @FXML
    private void getCurrentDirectory() throws IOException {
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
    
    private void sendHiddenCommand(String command){
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
