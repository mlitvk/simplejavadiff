/**
 * Represents one line and it's state -
 * added, deleted or not changed
 * @author Michael Litvak
 *
 */
public class DiffItem {
	/**
	 * represents no change
	 */
	public static final int NOCHANGE = 0;
	
	/**
	 * represents added line
	 */
	public static final int ADD = 1;
	
	/**
	 * represents deleted line
	 */
	public static final int DELETE = 2;
	
	/**
	 * string that represents no change
	 */
	public static final String NOCHANGE_STR = "|";
	
	/**
	 * string that represents added line
	 */
	public static final String ADD_STR = "+";
	
	/**
	 * string that represents deleted line
	 */
	public static final String DELETE_STR = "-";
	
	/**
	 * the text of the line
	 */
	private String text;
	
	/**
	 * the type of the change -
	 * NOCHANGE, ADD or DELETE
	 */
	private int type;
	
	/**
	 * constructs new diff item
	 * @param txt : the text of the line
	 * @param tp : type of change
	 */
	public DiffItem(String txt, int tp) {
		text = txt;
		type = tp;
	}
	
	/**
	 * returns the text
	 */
	public String getText() { 
		return text; 
	}
	
	/**
	 * returns the type
	 */
	public int getType() { 
		return type;
	}
	
	/**
	 * returns the type in string format
	 */
	public String getTypeString() {
		switch(type) {
		case NOCHANGE: return NOCHANGE_STR;
		case ADD: return ADD_STR;
		default: return DELETE_STR;
		}
	}

	/**
	 * string representation of the item
	 * which consists of the type and the text
	 */
	public String toString() {
		return getTypeString()+getText();
	}
}
