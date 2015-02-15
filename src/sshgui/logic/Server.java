package sshgui.logic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.Buffer;
import java.util.Scanner;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class Server {
	private String name;
	private String host;
	private int port;
	private Session session;
	private ChannelShell channel;
	private PrintWriter toChannel;
	private ByteArrayOutputStream bgOut;
	
	public Server(String name, String host, String port){
		this.name = name;
		this.host = host;
		this.port = Integer.getInteger(port);
	}
	
	public Server(String host, String port){
		this.host = host;
		this.port = Integer.parseInt(port);
	}
	
	public void connect(Login login) {
		JSch jsch=new JSch();
		Session session;
		try {
			session = jsch.getSession(login.getUsername(), this.host, this.port);
			session.setPassword(login.getPassword());
			LocalUserInfo lui=new LocalUserInfo();
			session.setUserInfo(lui);
			session.connect();
			this.session = session;
			System.out.println("Connected");
		} 
		catch (JSchException e) {
			e.printStackTrace();
			System.out.println("Error, could not connect");
		}
		
		
		///////
		try {
			openChannel();
		} catch (JSchException | IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	public Session getSession(){
		return this.session;
	}
	
	public void setSession(Session session){
		this.session = session;
	}

	public void sendCommand(String command) throws JSchException, IOException, InterruptedException{
		//this.channel.setOutputStream(System.out);
		toChannel.println(command);
	}
	
	public void sendCommand(String command, OutputStream out){
		this.channel.setOutputStream(out);
		toChannel.println(command);
	}
	
	public void resetOutputStream(){
		this.channel.setOutputStream(System.out);
	}
	
	/**
	 * openChannel() initiates the connection to the server shell (bash/cmd etc) via SSH
	 * It initiates the two-way communication by setting up an input/output stream
	 * @throws JSchException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void openChannel() throws JSchException, IOException, InterruptedException{
    	ChannelShell channel=(ChannelShell) this.session.openChannel("shell");
    	this.channel = channel;
    	this.channel.setPtyType("dumb");
    	
    	this.toChannel = new PrintWriter(new OutputStreamWriter(this.channel.getOutputStream()), true);
    	
    	this.channel.connect();
    	readerThread(new InputStreamReader(this.channel.getInputStream()));
	}
	
	/**
	 * readerThread sets up a timer thread that constantly checks for new output from the server
	 * shell. It only starts when openChannel() is performed.
	 * @param tout - an InputStreamReader enclosing the channel InputStream
	 */
	private void readerThread(final InputStreamReader tout)
	{
	    Thread read2 = new Thread(){
	    @Override
	    public void run(){
	        StringBuilder line = new StringBuilder();
	        char toAppend = ' ';
	        try {
	            while(true){
	                try {
	                    while (tout.ready()) {
	                        toAppend = (char) tout.read();
	                        if(toAppend == '\n')
	                        {
	                        	//System.out.print(toAppend);
	                            System.out.println(line.toString());
	                            line.setLength(0);
	                        }
	                        else
	                            line.append(toAppend);
	                    }
	                } catch (Exception e) {
	                    e.printStackTrace();
	                    System.out.println("\n\n\n************error reading character**********\n\n\n");
	                }
	                Thread.sleep(500);
	            }
	        }catch (Exception ex) {
	            System.out.println(ex);
	            try{
	                tout.close();
	            }
	            catch(Exception e)
	            {}
	        }
	    }
	    };
	    read2.start();
	}
	
	public String getCurrentDirectory(ByteArrayOutputStream out) throws IOException{
		this.sendCommand("pwd", out);
		return this.readLastBGCommandOutput(out);
	}
	
	public String readLastBGCommandOutput(ByteArrayOutputStream out) throws IOException{
		//ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayInputStream readableOut = new ByteArrayInputStream(out.toByteArray());
		Scanner s = new Scanner(readableOut);
		
		String finalMsg = null;
		while(s.hasNext()){
			finalMsg = s.nextLine();
		}
		
		return finalMsg;
	}

}
