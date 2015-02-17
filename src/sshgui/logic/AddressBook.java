package sshgui.logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.thoughtworks.xstream.XStream;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class AddressBook {
	private ArrayList<Address> addresses = new ArrayList<Address>();
	private File here = new File("");
	public String addressbookFileName = here.getAbsolutePath() + "/addressbook.xml";
	public String tempFileName = here.getAbsolutePath() + "/tempAddressBook.xml";

	public AddressBook(){
	}
	
	public void addAddress(Address address){
		this.addresses.add(address);
	}
	
	public ArrayList<Address> getList(){
		return this.addresses;
	}
	
}
