package sshgui.logic;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Properties;

public class AddressBook {
	private HashMap<Server, Login> addresses;
	private String addressbookFileName = "addressbook.xml";
	
	public AddressBook(){
		this.loadFromXML();
	}
	
	public void storeInXML(){
		Collection<Server> keys = this.addresses.keySet();
		Properties prop = new Properties();
		for (Server key : keys){
			prop.setProperty(key.getName() + "_Name", key.getName());
			prop.setProperty(key.getName() + "_Host", key.getHost());
			prop.setProperty(key.getName() + "_Port", Integer.toString(key.getPort()));
			prop.setProperty(key.getName() + "_User", this.addresses.get(key).getUsername());
			prop.setProperty(key.getName() + "_Pass", this.addresses.get(key).getPassword());
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
			Server tempServer = new Server(name, host, port);
			Login tempLogin = new Login(user, pass, false);
			addresses.put(tempServer, tempLogin);
		}
		
		this.addresses = addresses;
	}
	
	public void addServer(Server server, Login login){
		this.addresses.put(server, login);
	}
}
