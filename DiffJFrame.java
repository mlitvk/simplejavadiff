import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;

/**
 * Represents the frame that shows
 * the diff results to the user
 * @author Michael Litvak
 *
 */
public class DiffJFrame extends JFrame implements ActionListener{
	
	/**
	 * the height of the frame
	 */
	public static final int HEIGHT = 1024;
	
	/**
	 * the width of the frame
	 */
	public static final int WIDTH = 768;
	
	/**
	 * the current panel that the frame contains
	 */
	private JPanel cur_dp;
	
	/**
	 * names of the compared files
	 */
	private String filea, fileb;
	
	/**
	 * constructs a new diff jframe
	 * @param network : true if we're in network mode
	 */
	public DiffJFrame(boolean network) {
		super();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		filea = "";
		fileb = "";
		
		if(!network)
			createMenu();
		
		this.setSize(HEIGHT, WIDTH);
		this.setVisible(true);
		
		if(!network) {
			FileChooserPanel fcd = new FileChooserPanel(this);
		}
	}
	
	/**
	 * start a new diff comparison and shows
	 * it in the frame
	 * @param a
	 * @param b
	 */
	public void newDiff(String a, String b) {
		
		filea = a;
		fileb = b;
		
		DiffPanel dp = new DiffPanel(a,b, this, false);

		this.setNewPanel(dp);
	}
	
	/**
	 * change the current panel
	 * @param dp : the new panel to show
	 */
	public void setNewPanel(DiffPanel dp) {
		if(cur_dp != null) {
			this.getContentPane().remove(cur_dp);
		}
		
		cur_dp = dp;
		this.getContentPane().add(dp);
		this.validate();
	}
	
	/**
	 * reload the current comparison
	 */
	public void reloadFiles() {
		if(!filea.equals("") && !fileb.equals(""))
			this.newDiff(filea, fileb);
	}
	
	/**
	 * creates the top menu
	 */
	private void createMenu() {
		JMenuBar mb = new JMenuBar();
		
		JMenu menu_file = new JMenu("File");
		JMenuItem mi_open = new JMenuItem("Open");
		mi_open.setActionCommand("open");
		JMenuItem mi_reload = new JMenuItem("Reload");
		mi_reload.setActionCommand("reload");
		JMenuItem mi_close = new JMenuItem("Exit");
		mi_close.setActionCommand("exit");
		
		mi_open.addActionListener(this);
		mi_reload.addActionListener(this);
		mi_close.addActionListener(this);
		
		menu_file.add(mi_open);
		menu_file.add(mi_reload);
		menu_file.add(new JSeparator());
		menu_file.add(mi_close);
		
		mb.add(menu_file);
		
		this.setJMenuBar(mb);
	}
	
	/**
	 * handles user actions
	 */
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		
		if(cmd.equals("exit")) {
			this.exit();
		} else if(cmd.equals("open")) {
			FileChooserPanel fcd = new FileChooserPanel(this);
		} else if(cmd.equals("reload")) {
			this.reloadFiles();
		}
	
	}

	/**
	 * closes the frame and exits the program
	 */
	private void exit() {
		this.dispose();
		System.exit(0);
	}
}
