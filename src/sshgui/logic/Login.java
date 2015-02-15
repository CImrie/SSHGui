package sshgui.logic;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import library.*;

public class Login {
	private String username;
	private String password;
	private String masterHash;
	private Key key;
	
	public Login(String username, String password, boolean encrypt){
		if (encrypt){
			try {
				this.username = encrypt(username);
				this.password = encrypt(password);
			} catch (InvalidKeyException | NoSuchAlgorithmException
					| NoSuchPaddingException | UnsupportedEncodingException
					| IllegalBlockSizeException | BadPaddingException e) {
				e.printStackTrace();
			}
		}
		else {
			this.username = username;
			this.password = password;
		}
	}
	
	
	/**
	 * encrypt() encrypts a message using the masterPassword (hashed) as the
	 * encryption key.
	 * @param masterPassword
	 * @return 
	 * @throws NoSuchPaddingException 
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeyException 
	 * @throws UnsupportedEncodingException 
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 */
	public String encrypt(String message) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException{
		// Get a cipher object.
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, this.key);
	 
		// Gets the raw bytes to encrypt, UTF8 is needed for
		// having a standard character set
		byte[] stringBytes = message.getBytes("UTF8");
	 
		// encrypt using the cypher
		byte[] raw = cipher.doFinal(stringBytes);
	 
		// converts to base64 for easier display.
		BASE64Encoder encoder = new BASE64Encoder();
		String base64 = encoder.encode(raw);
	 
		return base64;
	}
	
	/**
	 * decrypt takes an encrypted message and decodes it using the masterPassword key
	 * @param message
	 * @return
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws IOException
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 */
	public String decrypt(String message, String masterPassword) throws IllegalBlockSizeException, BadPaddingException, IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException{
		//hash masterPassword
		this.hashMasterPassword(masterPassword);
		// Get a cipher object.
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, this.key);
	 
		//decode the BASE64 coded message
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] raw = decoder.decodeBuffer(message);
	 
		//decode the message
		byte[] stringBytes = cipher.doFinal(raw);
	 
		//converts the decoded message to a String
		String clear = new String(stringBytes, "UTF8");
		return clear;
	}
	
	/**
	 * setMasterPassword() hashes the masterPassword if this is not already set.
	 * It then stores the hash in a file that is read upon startup.
	 * @param masterPassword
	 */
	public void hashMasterPassword(String masterPassword){
		String hashed = BCrypt.hashpw(masterPassword, BCrypt.gensalt());
		this.masterHash = hashed;
		//store the hash in a file to use as encryption/decryption key
		SecretKey key = new SecretKeySpec(hashed.getBytes(), "AES");
		this.key = key;
	}
	
	public static String getHash(String secretMessage){
		String hashed = BCrypt.hashpw(secretMessage, BCrypt.gensalt());
		return hashed;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getMasterHash() {
		return masterHash;
	}

	public Key getKey() {
		return key;
	}


	public void setKey(Key key) {
		this.key = key;
	}
	
	//have master password and use the hash of this as an encryption/decryption key
	//encrypt/decrypt each saved password using hash
	
}
