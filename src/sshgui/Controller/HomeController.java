/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sshgui.Controller;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.ResourceBundle;

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
    private ByteArrayOutputStream bgOut;
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
    	OutputStream out = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                appendText(String.valueOf((char) b));
            }
        };
        System.setOut(new PrintStream(out, true));
        this.bgOut = new ByteArrayOutputStream() {
            @Override
            public void write(int b){
                appendTextBG(String.valueOf((char) b));
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
    public void appendTextBG(String str) {
        Platform.runLater(() -> quickConnectUsername.appendText(str));
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
        	Login l = new Login(this.quickConnectUsername.getText(), this.quickConnectPassword.getText(), false);
        	s.connect(l);
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
			this.connectedServer.sendCommand(this.consoleInput.getText());
			this.consoleInput.setText("");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
    
    @FXML
    private void getCurrentDirectory() throws IOException{
    	this.connectedServer.getCurrentDirectory(bgOut);
    	/*try {
			//System.out.println("Last Msg: " + this.connectedServer.readLastBGCommandOutput(bgOut));
		} catch (IOException e) {
			e.printStackTrace();
		}*/
    	bgOut.reset();
    	this.connectedServer.resetOutputStream();
    }
}
