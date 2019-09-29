package Client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;


public class Client {
	public Socket s;
	private int valid=0;
	private String username="";
	private String password="";
	public BufferedReader br;
	public BufferedWriter out;
	
	public Client(){
		try {
			s=new Socket(InetAddress.getByName("127.0.0.1"),Server.Server.SERVER_PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			out=new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
			br=new BufferedReader(new InputStreamReader(s.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}

	/*setters and getters of private variable*/
		public void setUsername(String data) {
			username=data;
		}
		public String getUsername() {
			return username;
		}
		public void setPassword(String data) {
			password=data;
		}
		public String getPassword() {
			return password;
		}
		public void setValid(int i) {
			valid=i;
		}
		public int getValid() {
			return valid;
		}

}
