package sshgui.logic;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import sshgui.GUIAlert;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import library.*;

public class Login {
	private String username;
	private String password;
	private String masterHash;
	private boolean encrypted;
	private String masterPassword;
	private byte[] iv;
	
	public Login(String username, String password, boolean encrypted, String masterPassword){
		this.encrypted = encrypted;
		this.masterPassword = masterPassword;
		if (!encrypted){
			try {
				this.username = username;
				this.password = encrypt(password, masterPassword);
				this.encrypted = true;
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
	public String encrypt(String message, String masterPassword) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException{
		// Get a cipher object.
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, this.getMasterKey(this.getHash(masterPassword)));
		this.iv = cipher.getIV();

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
	 * @throws InvalidAlgorithmParameterException 
	 */
	public String decrypt(String message, String masterPassword) throws IllegalBlockSizeException, IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException{
		// Get a cipher object.
		
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, this.getMasterKey(this.getHash(masterPassword)), (new IvParameterSpec(this.iv)));

		//decode the BASE64 coded message
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] raw = decoder.decodeBuffer(message);
	 
		//decode the message
		byte[] stringBytes;
		try {
			stringBytes = cipher.doFinal(raw);
			//converts the decoded message to a String
			String clear = new String(stringBytes, "UTF8");
			return clear;
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * setMasterPassword() hashes the masterPassword if this is not already set.
	 * It then stores the hash in a file that is read upon startup.
	 * @param masterPassword
	 */
	public SecretKey getMasterKey(String masterHash){
		byte[] trimmed = this.trim(masterHash.getBytes(), 16);
		SecretKey key = new SecretKeySpec(trimmed, "AES");
		return key;
	}
	
	private byte[] trim(byte[] input, int size){
		byte[] trimmed = new byte[size];
		for(int i = 0; i < trimmed.length; i++){
			trimmed[i] = input[i];
		}
		return trimmed;
	}
	
	public static String getHash(String secretMessage) throws NoSuchAlgorithmException{
		MessageDigest sha256 = MessageDigest.getInstance("SHA-256");        
	    byte[] secretBytes = secretMessage.getBytes();
	    byte[] secretHash = sha256.digest(secretBytes);
	    return new sun.misc.BASE64Encoder().encode(secretHash);
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getPassword() throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IOException {
		String temp = this.password;
		if (this.encrypted){
			temp = this.decrypt(this.password, this.masterPassword);
		}
		return temp;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getMasterHash() {
		return masterHash;
	}
	
	public String getEncryptedPassword(){
		return this.password;
	}
	
	public byte[] getIv() {
		return iv;
	}

	public void setIv(byte[] iv) {
		this.iv = iv;
	}
	
}
