import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.*;

/**
 * Represents a panel that shows the
 * diff information to the user
 * @author Michael Litvak
 *
 */
public class DiffPanel extends JPanel implements ActionListener{
	
	/**
	 * contains the diff information
	 */
	private FilesDiff fd;
	
	/**
	 * text areas to contain the text
	 */
	private DiffTextArea tp_org, tp_diff;
	
	/**
	 * scroll panes to contain the text areas
	 */
	private JScrollPane sp_org, sp_diff;

	/**
	 * two panels for each file
	 */
	private JPanel pnl_org, pnl_diff;

	/**
	 * labels that show the path of the files
	 */
	private JLabel lbl_org, lbl_diff;

	/**
	 * panel at the bottom that contains a save button
	 */
	private JPanel save_panel;
	
	/**
	 * save button
	 */
	private JButton btn_save;

	/**
	 * the parent frame that contains this panel
	 */
	private DiffJFrame parent;
	
	/**
	 * constructs a new diff panel
	 * @param a : first file path
	 * @param b : second file path
	 * @param jparent : parent frame 
	 * @param network : true if we're in network mode
	 */
	public DiffPanel(String a, String b, DiffJFrame jparent, boolean network) {
		super();

		parent = jparent;

		if(!network)
			fd = new FilesDiff(a, b);

		pnl_org = new JPanel();
		pnl_org.setPreferredSize(new Dimension(500, 620));
		pnl_diff = new JPanel();
		pnl_diff.setPreferredSize(new Dimension(500, 620));

		save_panel = new JPanel();
		save_panel.setPreferredSize(new Dimension(500, 50));

		lbl_org = new JLabel(a);
		lbl_diff = new JLabel(b);

		btn_save = new JButton("Save");

		btn_save.addActionListener(this);

		tp_org = new DiffTextArea();
		tp_diff = new DiffTextArea();

		sp_org = new JScrollPane();
		sp_diff = new JScrollPane();

		sp_org.setPreferredSize(new Dimension(500,600));
		sp_diff.setPreferredSize(new Dimension(500,600));

		sp_org.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		sp_diff.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		sp_org.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		sp_diff.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		sp_org.getViewport().add(tp_org);
		sp_diff.getViewport().add(tp_diff);
		
		ChangeListener positionListener = new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JViewport source = (JViewport)e.getSource();
				setViewPosition(source.getViewPosition());
			}
		};
		
		sp_org.getViewport().addChangeListener(positionListener);
		sp_diff.getViewport().addChangeListener(positionListener);

		pnl_org.add(lbl_org);
		pnl_org.add(sp_org);

		pnl_diff.add(lbl_diff);
		pnl_diff.add(sp_diff);

		save_panel.add(btn_save);
		
		this.add(pnl_org);
		this.add(pnl_diff);
		
		if(!network) {
			this.add(save_panel);
			insertText();
		}
		
		sp_org.getViewport().setViewPosition(new Point(0,0));
		sp_diff.getViewport().setViewPosition(new Point(0,0));
	}
	
	/**
	 * overwritten function for synchronizing
	 * the scroll panes
	 * @param vp : current point of the scroll pane
	 */
	private void setViewPosition(Point vp) {
		sp_org.getViewport().setViewPosition(vp);
		sp_diff.getViewport().setViewPosition(vp);
		repaint();
	}

	/**
	 * inserting text to the text areas
	 */
	public void insertText() {
		tp_org.addTextFromItems(fd.getOrgItems());
		tp_diff.addTextFromItems(fd.getDiffItems());
	}
	
	/**
	 * inserting text to the text areas from
	 * given diff items 
	 * @param org_items : items for the original file
	 * @param new_items : items for the new file
	 */
	
	public void insertText(ArrayList<DiffItem> org_items, ArrayList<DiffItem> new_items) {
		tp_org.addTextFromItems(org_items);
		tp_diff.addTextFromItems(new_items);
	}

	/**
	 * handle clicks on the save button 
	 */
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();		
		String txt_org = tp_org.getText();
		String txt_diff = tp_diff.getText();
		fd.writeToFile("org", txt_org);
		fd.writeToFile("diff", txt_diff);
		parent.reloadFiles();
	}
}
