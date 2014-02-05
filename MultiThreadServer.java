import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.*;

/**
 * Represents a single thread of the server
 * @author Michael Litvak
 *
 */
class ServerThread implements Runnable {

	/**
	 * the socket that handles the communication
	 * between the server and the client
	 */
	private Socket socket;
	
	/**
	 * if debug is true then the client's
	 * messages are printed to the console
	 */
	private boolean debug;
	
	/**
	 * constructs a new server thread
	 * @param socket : a socket to the client
	 */
	public ServerThread(Socket socket){
		this.socket=socket;
		
		debug = true;
	}
	
	/**
	 * this method is for handling client's
	 * messages and sending the diff output
	 */
	public void run(){
		try{
			BufferedWriter out =new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			BufferedReader in = new BufferedReader(new InputStreamReader (socket.getInputStream()));
			String line = "";
			
			line = in.readLine();
			String[] temp = line.split(",");
			
			if(debug) {
				System.out.println("client: "+line);
			}
			
			String[] file_old = new String[Integer.parseInt(temp[0])+1];
			String[] file_new = new String[Integer.parseInt(temp[1])+1];
			
			file_old[0] = "";
			file_new[0] = "";
			
			for(int i = 0; i < Integer.parseInt(temp[0]); i++) {
				line = in.readLine();
				file_old[i+1] = line;
				
				if(debug) {
					System.out.println("client: "+line);
				}
			}
			
			for(int i = 0; i < Integer.parseInt(temp[1]); i++) {
				line = in.readLine();
				file_new[i+1] = line;
				
				if(debug) {
					System.out.println("client: "+line);
				}	
			}
			
			Diff diff = new Diff(file_old, file_new);
			
			ArrayList<DiffItem> items = diff.startBacktrace();
			
			for(int i = 0; i < items.size(); i++) {
				DiffItem item = items.get(i);
				out.write(item.getType()+item.getText()+Diff.DELIM);
				out.flush();
			}
			
			
			out.close();
			in.close();
			socket.close();
		
		} catch (IOException e){
			e.printStackTrace();
		}
	}   
}

/**
 * Represents the multi threaded server
 * which receives files and returns the diff
 * output to the client
 */
public class MultiThreadServer{

	/**
	 * listening for connections and creating
	 * thread for each connection
	 * @param args : command line arguments
	 * (not used)
	 */
	public static void main(String [] args){
		ServerSocket ssock;
		try{

			ssock = new ServerSocket(4001);
			System.out.println("I am online");
			
			JFrame f = new JFrame();
			f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			f.setSize(160,100);
			
			f.getContentPane().setLayout(new FlowLayout());
			
			JLabel l = new JLabel("The server is online");
			f.getContentPane().add(l);
			
			JButton b = new JButton("Exit");
			f.getContentPane().add(b);
			
			b.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			});
			
			f.setVisible(true);
			
			while (true){
				Socket sock = ssock.accept();

				System.out.println("Connection accepted");
				ServerThread st=new ServerThread(sock);
				Thread t=new Thread(st); 
				t.start();
			}
		} catch (IOException e){
			System.out.println("Could not connect.");
			System.exit(1);        
		}
	
	}
}