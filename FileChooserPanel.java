import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.*;
import java.io.File;

import javax.swing.*;
import javax.swing.border.Border;

/**
 * Represents the panel for choosing
 * files and server options
 * @author Michael Litvak
 *
 */
public class FileChooserPanel extends JPanel implements ActionListener{
	
	/**
	 * labels that contain the files' path
	 */
	private JLabel label1, label2;
	
	/**
	 * buttons
	 */
	private JButton openButton1, openButton2, okButton;
	
	/**
	 * labels for the text of "server" and "port"
	 */
	private JLabel server_lbl, port_lbl;
	
	/**
	 * fields for the user to enter server and port
	 */
	private JTextField server_fld, port_fld;
	
	/**
	 * check box to enable/disable the server option
	 */
	private JCheckBox check_server;
	
	/**
	 * the main frame of the program
	 */
	private DiffJFrame parent;
	
	/**
	 * strings to save the paths of the files
	 */
	private String oldFile, newFile;
	
	/**
	 * the frame that contains the file chooser panel
	 */
	private JFrame f;
	
	/**
	 * the size of the labels
	 */
	private int labelMaxChars = 35;
	
	/**
	 * constructs a new file chooser panel
	 * @param fp : the main frame of the program
	 */
	public FileChooserPanel(DiffJFrame fp) {
		
		f = new JFrame("Open files");
		
		JMenuBar mb = new JMenuBar();
		JMenu menu_file = new JMenu("File");
		JMenuItem file_exit = new JMenuItem("Exit");
		file_exit.addActionListener(this);
		file_exit.setActionCommand("exit");
		menu_file.add(file_exit);
		mb.add(menu_file);
		f.setJMenuBar(mb);
		
		parent = fp;
		
		oldFile = "";
		newFile = "";
		
		label1 = new JLabel();
		label2 = new JLabel();
		
		Border border = BorderFactory.createLineBorder(Color.black);
		
		label1.setBorder(border);
		label2.setBorder(border);
		
		check_server = new JCheckBox("Connect to Server");
		check_server.addActionListener(this);
		
		openButton1 = new JButton("Old File");
		openButton2 = new JButton("New File");
		okButton = new JButton("OK");
		
		openButton1.addActionListener(this);
		openButton2.addActionListener(this);
		okButton.addActionListener(this);
		
		server_lbl = new JLabel("Server");
		port_lbl = new JLabel("Port");
		
		server_lbl.setPreferredSize(new Dimension(40,30));
		port_lbl.setPreferredSize(new Dimension(40,30));
		
		server_fld = new JTextField();
		port_fld = new JTextField();
		
		server_fld.setEnabled(false);
		port_fld.setEnabled(false);
		
		server_fld.setPreferredSize(new Dimension(220,30));
		port_fld.setPreferredSize(new Dimension(220,30));
		
		JPanel pcenter1 = new JPanel();
		//pcenter1.setLayout(new GridLayout(1,2));
		pcenter1.setLayout(new FlowLayout());
		label1.setPreferredSize(new Dimension(160,30));
		pcenter1.add(label1);
		pcenter1.add(openButton1);
		
		JPanel pcenter2 = new JPanel();
		pcenter2.setLayout(new FlowLayout());
		label2.setPreferredSize(new Dimension(160,30));
		//pcenter2.setLayout(new GridLayout(1,2));
		pcenter2.add(label2);
		pcenter2.add(openButton2);
		
		JPanel pcenter3 = new JPanel();
		pcenter3.add(check_server);
		
		JPanel pcenter4 = new JPanel();
		pcenter4.add(server_lbl);
		pcenter4.add(server_fld);
		
		JPanel pcenter5 = new JPanel();
		pcenter5.add(port_lbl);
		pcenter5.add(port_fld);
		
		JPanel pbottom = new JPanel();
		pbottom.add(okButton);
		
		JSeparator sp = new JSeparator();
		
		JPanel pcenter = new JPanel();
		pcenter.setPreferredSize(new Dimension(300,50));
		pcenter.setLayout(new GridLayout(5,1));
		pcenter.add(pcenter1);
		pcenter.add(pcenter2);
		pcenter.add(pcenter3);
		pcenter.add(pcenter4);
		pcenter.add(pcenter5);
		
		this.setLayout(new BorderLayout());
		this.add(pcenter, BorderLayout.CENTER);
		this.add(pbottom, BorderLayout.SOUTH);

		f.pack();
		f.setSize(300,400);
		f.add(this);
		f.setVisible(true);
	}
	
	/**
	 * handle events of clicks on buttons
	 */
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == openButton1 || e.getSource() == openButton2) {
			JFileChooser jfs = new JFileChooser();
			int ret = jfs.showOpenDialog(this);
			
			if(ret == JFileChooser.APPROVE_OPTION) {
				File file = jfs.getSelectedFile();
				
				if(e.getSource() == openButton1) {
					String path = file.getAbsolutePath();
					if(path.length() > labelMaxChars)
						label1.setText(path.substring(path.length()-labelMaxChars));
					else
						label1.setText(path);
					oldFile = path;
				} else {
					String path = file.getAbsolutePath();
					if(path.length() > labelMaxChars)
						label2.setText(path.substring(path.length()-labelMaxChars));
					else
						label2.setText(path);
					newFile = path;
				}
			}
		}
		
		if(e.getSource() == check_server) {
			if(check_server.isSelected()) {
				server_fld.setEnabled(true);
				port_fld.setEnabled(true);
			} else {
				server_fld.setEnabled(false);
				port_fld.setEnabled(false);
			}
		}
		
		if(e.getSource() == okButton) {
			if(!oldFile.equals("") && !newFile.equals("")) {
				if(check_server.isSelected()) {
					if(!server_fld.getText().equals("") && !port_fld.getText().equals("")) {
					Client client = new Client(oldFile, newFile, 
							server_fld.getText(), port_fld.getText(), parent);
					client.start();
					this.exit();
					}
				} else {
					parent.newDiff(oldFile, newFile);
					this.exit();
				}
			}
		}

		if(e.getActionCommand() == "exit") {
			this.exit();
		}
	}
	
	/**
	 * handle exit event
	 */
	private void exit() {
		f.dispose();
	}
}
