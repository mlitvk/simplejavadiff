import java.io.*;
import java.util.ArrayList;

/**
 * Represents a file that is
 * used in the comparison
 * @author Michael Litvak
 *
 */
public class DiffFile {	
	
	/**
	 * name of the file
	 */
	private String fileName;
	
	/**
	 * contains the lines of the file
	 */
	private ArrayList<String> words;
	
	/**
	 * contains the number of lines
	 */
	private int lines;
	
	/**
	 * buffer for reading the file
	 */
	private BufferedReader in;
	
	/**
	 * constructs a new diff file
	 * @param name : name of the file
	 */
	public DiffFile(String name) {
		this.fileName = name;
		words = new ArrayList<String>();
		lines = 0;
		
		try {
			in = new BufferedReader(new FileReader(fileName));
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		readFileLines();
	}

	/**
	 * reads the lines of the file and
	 * saves in array list
	 */
	public void readFileLines() {
		String line;
		try {
			while((line = in.readLine()) != null) {
				words.add(line);
				lines++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * write to the file
	 * @param txt : the text to write
	 */
	public void writeFile(String txt) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
			out.write(txt);
			out.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * returns the array list that contains
	 * the lines of the file
	 */
	public ArrayList<String> getWordsArrayList() {
		return words;
	}
	
	/**
	 * returns the number of lines
	 */
	public int getLines() {
		return lines;
	}

	/**
	 * returns the content of the file
	 */
	public String toString() {
		String s = "";
		for(int i = 0; i < words.size(); i++)
			s += words.get(i) + "\n";
		return s;
	}
}
