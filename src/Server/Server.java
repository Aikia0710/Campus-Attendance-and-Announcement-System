package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import Database.DatabaseConnection;

public class Server {
	public static final int SERVER_PORT=10000;
	
	/**This is the a map storing all socket which connect to clients in server as Value 
	 * and the corresponding client's username as the Key*/
	public static UserConnectionMap<String,Socket> lecturers=new UserConnectionMap<>();
	public static UserConnectionMap<String,Socket> students=new UserConnectionMap<>();
	private DatabaseConnection dc;
	
	public void init() throws ClassNotFoundException {
		
		Socket s;
		try {
			/*Server connect with database*/
			dc = new DatabaseConnection("jdbc:postgresql://mod-msc-sw1.cs.bham.ac.uk/group33", "group33", "1608yr478x");
			ServerSocket ss=new ServerSocket(SERVER_PORT);
			while(true) {
				/*ServerSocket keeps accepting new connection from new client*/
				s=ss.accept();
				try {
					/*for each connection, Server creates a new thread to check whether the client is valid*/
					new CheckClient(s,dc).start();
				} catch (Exception e) {
					e.printStackTrace();
					
				}
			}		
		}				
			catch (IOException e) {
				e.printStackTrace();
			}
	
		
	}
	
	public static void main(String[] args) throws ClassNotFoundException {
		Server server=new Server();
		server.init();
	}
}


