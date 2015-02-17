package sshgui.logic;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class LoginTest {



public static void main(String[] args) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException, InvalidAlgorithmParameterException{
	String masterPass = "test";
	String pass = "ha$R1014@";
	Login l = new Login("root", pass, false, masterPass);
	String encrypted = l.encrypt(pass, masterPass);
	System.out.println(encrypted);
	System.out.println(l.decrypt(encrypted, masterPass));
}

}