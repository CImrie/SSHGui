package sshgui.logic;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class AddressBook {
	private ArrayList<Address> addresses = new ArrayList<Address>();
	private String addressbookFileName = "addressbook.xml";

	public AddressBook(){
		this.loadFromXML();
		this.addresses.add(new Address("test", "test", 22, "test", "test"));
	}
	
	public void storeInXML(){
		Properties prop = new Properties();
		for (int i = 0; i < this.addresses.size(); i++){
			Address temp = this.addresses.get(i);
			prop.setProperty(temp.getServerName() + "_Name", temp.getServerName());
			prop.setProperty(temp.getServerName() + "_Host", temp.getServerHost());
			prop.setProperty(temp.getServerName() + "_Port", Integer.toString(temp.getServerPort()));
			prop.setProperty(temp.getServerName() + "_User", temp.getServerUser());
			prop.setProperty(temp.getServerName() + "_Pass", temp.getServerPass());
		}
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(this.addressbookFileName);
			 prop.storeToXML(fos, "Address Book", "UTF-8");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void loadFromXML(){
		Properties prop = new Properties();
		FileInputStream fis;
		try {
			fis = new FileInputStream(this.addressbookFileName);
			prop.loadFromXML(fis);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Collection<Object> keys = prop.keySet();
		Object[] keysArr = keys.toArray();
		HashMap<Server, Login> addresses = new HashMap<Server, Login>();
		//+= 5 each time because each server has 5 properties
		for (int i = 0; i < keys.size(); i+=5){
			String name = "";
			String host = "";
			String port = "";
			String user = "";
			String pass = "";
			String currentKey = (String) keysArr[i];
			if(currentKey.contains("_Name")){
				name = prop.getProperty(currentKey);
			}
			if(currentKey.contains("_Host")){
				host = prop.getProperty(currentKey);
			}
			if(currentKey.contains("_Port")){
				port = prop.getProperty(currentKey);
			}
			if(currentKey.contains("_User")){
				user = prop.getProperty(currentKey);
			}
			if(currentKey.contains("_Pass")){
				pass = prop.getProperty(currentKey);
			}
			//Server tempServer = new Server(name, host, port);
			//Login tempLogin = new Login(user, pass, false);
			//addresses.put(tempServer, tempLogin);
			Address tempAddress = new Address(name, host, Integer.parseInt(port), user, pass);
			this.addresses.add(tempAddress);
		}
	}
	
	public void addAddress(Address address){
		this.addresses.add(address);
	}
	
	public ArrayList<Address> getList(){
		return this.addresses;
	}
	
}
