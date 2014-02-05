import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.text.*;

/**
 * Represents a text area to contain diff output
 * @author Michael Litvak
 *
 */
public class DiffTextArea extends JTextPane{

	/**
	 * object for adding text styles
	 */
	StyledDocument doc;

	/**
	 * various styles
	 */
	Style nochange, delete, add;

	/**
	 * constructs new diff text area
	 */
	public DiffTextArea() {
		super();

		this.setEditable(true);
		
		doc = (StyledDocument)this.getDocument();

		nochange = doc.addStyle("Black",null);
		delete = doc.addStyle("Delete",null);
		add = doc.addStyle("Add",null);

		StyleConstants.setForeground(nochange, Color.BLACK);
		StyleConstants.setForeground(delete, Color.RED);
		StyleConstants.setForeground(add, Color.GREEN);
	}

	/**
	 * overwritten function for disabling line wrap
	 */
	public boolean getScrollableTracksViewportWidth() {
		return false;  
	}

	/**
	 * overwritten function for disabling line wrap
	 */
	public void setSize(Dimension d) {
		if(d.width < getParent().getSize().width) {
			d.width = getParent().getSize().width;
		}
		super.setSize(d);
	}

	/**
	 * inserting text to the text area with the
	 * corresponding style
	 * @param items : diff items to insert
	 */
	public void addTextFromItems(ArrayList<DiffItem> items) {
		try {
			for(int i = 0; i < items.size(); ++i) {
				DiffItem item = items.get(i);
				
				Style current = nochange;
				
				switch(item.getType()) {
				case(DiffItem.NOCHANGE): current = nochange; break;
				case(DiffItem.DELETE): current = delete; break;
				case(DiffItem.ADD): current = add; break; 
				}

				doc.insertString(doc.getLength(), item.getText(), current);
			}
		} catch(Exception e ) {
			e.printStackTrace();
		}
	}
}
