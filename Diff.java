import java.util.ArrayList;

/**
 * Responsible for performing the diff
 * algorithm on two files
 * @author Michael Litvak
 *
 */
public class Diff {
	
	/**
	 * the delimiter that is used to
	 * separate the contents of the files
	 */
	public static String DELIM = "\n";
	
	/**
	 * the matrix that is used for
	 * the calculations
	 */
	private int[][] matrix;
	
	/**
	 * strings on the x-axis
	 */
	private String[] x;
	
	/**
	 * strings on the y-axis
	 */
	private String[] y;
	
	/**
	 * size of array x
	 */
	private int xlen;
	
	/**
	 * size of array y
	 */
	private int ylen;

	/**
	 * if debug is true than various debug
	 * messages are printed
	 */
	private boolean debug = false;
	
	/**
	 * if it's true than the output contains
	 * lines that weren't changed
	 */
	private boolean showUnchanged = true;
	
	/**
	 * the output of the diff is saved
	 * as an array of diff items
	 */
	private ArrayList<DiffItem> items;
	
	/**
	 * constructs new diff from two arrays
	 * @param x : array of lines of the first file
	 * @param y : array of lines of the second file
	 */
	public Diff(String[] x, String[] y) {
		if(x[0] != "")
			x = insertToBeginning(x, "");
		
		if(y[0] != "")
			y = insertToBeginning(y, "");
		
		this.x = x;
		this.y = y;
		
		items = new ArrayList<DiffItem>();
		
		if(debug) {
			for(int i = 0; i < x.length; i++)
				System.out.print(i+"-"+x[i]+" ");
			System.out.println();
			for(int i = 0; i < y.length; i++)
				System.out.print(i+"-"+y[i]+" ");
			System.out.println();
		}
		
		xlen = x.length;
		ylen = y.length;

		matrix = new int[ylen][xlen];
		
		resetMatrix();
		
		fillMatrix();
		
		if(debug) {
			System.out.println(this.toString());
		}
	}
	
	/**
	 * constructs new diff from two strings
	 * @param x : content of the first file
	 * @param y : content of the second file
	 */
	public Diff(String x, String y) {
		this(x.split(DELIM),y.split(DELIM));
	}
	
	/**
	 * resets the matrix
	 */
	private void resetMatrix() {
		for(int i = 0; i < matrix.length; ++i)
			for(int j = 0; j < matrix[i].length; ++j)
				matrix[i][j] = -1;
	}
	
	/**
	 * fills every cell of the matrix with
	 * its value using lcs algorithm
	 */
	public void fillMatrix() {
		for(int i = 0; i < matrix.length; ++i)
			for(int j = 0; j < matrix[i].length; ++j)
				matrix[i][j] = lcs(i,j);
	}
	
	/**
	 * performs the LCS algorithm on a
	 * given index
	 * @param i : row in the matrix
	 * @param j : column in the matrix
	 * @return the value of the given cell
	 */
	private int lcs(int i, int j) {
		if(matrix[i][j] != -1)
			return matrix[i][j];
		
		if(i == 0 || j == 0)
			return 0;

		if(x[j].equals(y[i])) {
			return 1 + lcs(i-1, j-1);
		}
		
		return Math.max(lcs(i-1,j), lcs(i, j-1));
	}
	
	/**
	 * performs back trace on the matrix
	 * and returns array of diff items that
	 * represents the changes between the files
	 */
	public ArrayList<DiffItem> startBacktrace() {
		backtrace(matrix.length-1, matrix[0].length-1);
		return items;
	}

	/**
	 * performs a back trace on a given cell
	 * of the matrix
	 * @param i : row in the matrix
	 * @param j : column in the matrix
	 */
	public void backtrace(int i, int j) {
		if(debug) 
			System.out.println("("+i+","+j+") ("+x[j]+","+y[i]+")");
		
		if(i == 0 && j == 0)
			return;
		
		if(!x[j].equals("") && x[j].equals(y[i])) {
			if(showUnchanged) {
				items.add(0, new DiffItem(x[j],DiffItem.NOCHANGE));
				backtrace(i-1, j-1);
				return;
			} else {
				backtrace(i-1, j-1);
				return;
			}
		}
		
		if((i > 0 && j == 0) || (i > 0 && matrix[i-1][j] >= matrix[i][j-1])) {
			items.add(0, new DiffItem(y[i], DiffItem.ADD));
			backtrace(i-1,j);
			return;
		} else {
			items.add(0, new DiffItem(x[j], DiffItem.DELETE));
			backtrace(i,j-1);
			return;
		}
	}
	
	/**
	 * inserts a given value to the beginning
	 * of a given array, moving all the other
	 * elements one cell forward
	 * @param x : array to insert into
	 * @param elem : value to insert
	 * @return : the new array
	 */
	private String[] insertToBeginning(String[] x, String elem) {
		String[] newX = new String[x.length+1];
		newX[0] = elem;
		
		for(int i = 0; i < x.length; i++) {
			newX[i+1] = x[i];
		}
		
		return newX;
	}

	/**
	 * returns string representation
	 * of the matrix
	 */
	public String toString() {
		String s = "";
		for(int i = 0; i < matrix.length; ++i) {
			for(int j = 0; j < matrix[i].length; ++j)
				s += matrix[i][j] + "\t";
			s += "\n";
		}
		
		return s;
	}
}
