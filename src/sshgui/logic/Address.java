package sshgui.logic;

public class Address {
	private String serverName;
	private String serverHost;
	private int serverPort;
	private String serverUser;
	private String serverPass;
	private byte[] iv;

	public Address(String name, String host, int port, String username, String passHash, byte[] iv){
		this.serverName = name;
		this.serverHost = host;
		this.serverPort = port;
		this.serverUser = username;
		this.serverPass = passHash;
		this.iv = iv;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getServerHost() {
		return serverHost;
	}

	public void setServerHost(String serverHost) {
		this.serverHost = serverHost;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public String getServerUser() {
		return serverUser;
	}

	public void setServerUser(String serverUser) {
		this.serverUser = serverUser;
	}

	public String getServerPass() {
		return serverPass;
	}

	public void setServerPass(String serverPass) {
		this.serverPass = serverPass;
	}
	
	public void setIV(byte[] iv){
		this.iv = iv;
	}
	
	public byte[] getIV(){
		return this.iv;
	}
}
