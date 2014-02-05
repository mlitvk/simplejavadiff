import java.util.ArrayList;

/**
 * Represents two files and the diff 
 * information of them
 * @author Michael Litvak
 *
 */
public class FilesDiff {
	/**
	 * first file
	 */
	private DiffFile a;
	
	/**
	 * second file
	 */
	private DiffFile b;
	
	/**
	 * diff information of the two
	 * file's comparison
	 */
	private Diff diff;
	
	/**
	 * diff information for the new file
	 */
	private ArrayList<DiffItem> diffItems;
	
	/**
	 * diff information for the original file
	 */
	private ArrayList<DiffItem> orgItems;
	
	/**
	 * constructs a new FilesDiff
	 */
	public FilesDiff() {
		diffItems = new ArrayList<DiffItem>();
		orgItems = new ArrayList<DiffItem>();
	}
	
	/**
	 * constructs a new FilesDiff
	 * @param an : first file path
	 * @param bn : second file path
	 */
	public FilesDiff(String an, String bn) {
		a = new DiffFile(an);
		b = new DiffFile(bn);
		
		diff = new Diff(a.toString(), b.toString());
		
		diffItems = new ArrayList<DiffItem>();
		orgItems = new ArrayList<DiffItem>();
		
		InitDiffOrgItems(null);
	}

	/**
	 * return the diff information
	 */
	public Diff getDiff() {
		return diff; 
	}
	
	/**
	 * receives the information of the
	 * comparison, and divides it to
	 * two parts
	 * @param items : the information of
	 * the comparison
	 */
	public void InitDiffOrgItems(ArrayList<DiffItem> items) {
		if(items == null)
			items = diff.startBacktrace();
		
		int minus = 0, plus = 0;
		
		String delim = Diff.DELIM;
		
		for(int i = 0; i < items.size(); ++i) {
			DiffItem item = items.get(i);
			if(item.getType() == DiffItem.NOCHANGE) {
				while(minus > plus) {
					diffItems.add(new DiffItem(delim, DiffItem.NOCHANGE));
					--minus;
				}
				while(plus > minus) {
					orgItems.add(new DiffItem(delim, DiffItem.NOCHANGE));
					--plus;
				}
				minus = 0;
				plus = 0;
			}
			if(item.getType() == DiffItem.ADD)
				++plus;
			else if(item.getType() == DiffItem.DELETE)
				++minus;
			
			if(item.getType() == DiffItem.NOCHANGE || item.getType() == DiffItem.DELETE)
				orgItems.add(new DiffItem(item.getText() + delim, item.getType()));
			
			if(item.getType() == DiffItem.NOCHANGE || item.getType() == DiffItem.ADD)
				diffItems.add(new DiffItem(item.getText() + delim, item.getType()));
		}
	}

	/**
	 * updated the contents of a file
	 * @param action : org for the first file
	 * or diff for the second file
	 * @param txt : the text to write
	 */
	public void writeToFile(String action, String txt) {
		if(action.equals("org")) {
			a.writeFile(txt);
		} else if(action.equals("diff")) {
			b.writeFile(txt);
		}
	}
	
	/**
	 * returns diff information of the
	 * second file
	 */
	public ArrayList<DiffItem> getDiffItems() {
		return diffItems; 
	}
	
	/**
	 * returns diff information of the
	 * first file
	 */
	public ArrayList<DiffItem> getOrgItems() { 
		return orgItems; 
	}
}
