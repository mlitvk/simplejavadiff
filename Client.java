
import java.io.*;
import java.net.*;
import java.util.ArrayList;

import javax.swing.JOptionPane;

/**
 * Responsible for reading information from
 * the server and updating the frame
 * accordingly
 * @author Michael Litvak
 *
 */
class NetworkRead implements Runnable{
	/**
	 * the socket for the communication with
	 * the server
	 */
	Socket socket;
	
	/**
	 * parent frame
	 */
	DiffJFrame parent;

	/**
	 * constructs a new NetworkRead
	 * @param socket : socket for the communication
	 * @param parent : parent frame
	 */
	public NetworkRead(Socket socket, DiffJFrame parent){
		this.socket=socket;
		this.parent = parent;
	}
	
	/**
	 * reads lines from the server and then
	 * creating new diff panel and updating
	 * the parent frame
	 */
	public void run(){
		try{
			   
			BufferedReader in= new BufferedReader( new InputStreamReader(socket.getInputStream()));
			String line = "";
			ArrayList<DiffItem> items = new ArrayList<DiffItem>();
			while ((line=in.readLine())!=null)
			{
				items.add(new DiffItem(line.substring(1), Integer.parseInt(line.substring(0, 1))));
				System.out.println("server: "+line);
			}
			FilesDiff fd = new FilesDiff();
			fd.InitDiffOrgItems(items);
			
			DiffPanel panel = new DiffPanel("","",null,true);
			panel.insertText(fd.getOrgItems(), fd.getDiffItems());
			
			parent.setNewPanel(panel);
		}catch(IOException e){
			e.printStackTrace();
		}
		
		System.out.println("Disconnected from server");
	}

}

/**
 * Represents a network client
 * @author Michael Litvak
 *
 */
class Client implements Runnable{
	
	/**
	 * socket that is used for the
	 * communication with the server
	 */
	Socket socket;
	
	/**
	 * input buffer
	 */
	BufferedReader n;
	
	/**
	 * output buffer
	 */
	BufferedWriter out;
	
	/**
	 * object that represents the original file
	 */
	DiffFile f_old;
	
	/**
	 * object that represents the new file
	 */
	DiffFile f_new;
	
	/**
	 * the parent frame
	 */
	DiffJFrame parent;
	
	/**
	 * true if the client is connected
	 * to the server
	 */
	boolean connected;

	/**
	 * constructs a new client
	 * @param a : path of the first file
	 * @param b : path of the second file
	 * @param server : server address
	 * @param port : the port to use in the connection
	 * @param parent : parent frame
	 */
	public Client(String a, String b, String server, String port, DiffJFrame parent){
		f_old = new DiffFile(a);
		f_new = new DiffFile(b);
		
		this.parent = parent;
		
		connected = false;
		
		try{ 
			
			socket=new Socket(server,Integer.parseInt(port));
			out=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			connected = true;
		}
		catch(IOException e){
			JOptionPane.showMessageDialog(parent,
				    "Couldn't get I/O for the connection to server.",
				    "Error",
				    JOptionPane.ERROR_MESSAGE);

		}
		catch(NumberFormatException e){
			JOptionPane.showMessageDialog(parent,
				    "Illegal port number.",
				    "Error",
				    JOptionPane.ERROR_MESSAGE);
		}
	}


	/**
	 * starts the thread of the clients
	 * that is responsible for sending
	 * information to the server and reading
	 * from the server
	 */
	public void start(){
		if(connected) {
	      Thread logRead=new Thread(this, "client");
	      logRead.start();
		}
	}

	/**
	 * creates new thread for reading from
	 * the server, and sends to the server
	 * the number of the lines in each file and
	 * the contents of the files
	 */
	public void run(){
		try{
			NetworkRead readThread=new NetworkRead(socket, parent);
			Thread read=new Thread(readThread,"read");
			read.start();

			out.write(f_old.getLines() + ","+f_new.getLines()+"\n");
			out.write(f_old.toString() + f_new.toString());
			out.flush();
		   
		}catch (IOException e){
			System.err.println("Couldn't read or write");
			System.exit(1);
		}
	}
}
