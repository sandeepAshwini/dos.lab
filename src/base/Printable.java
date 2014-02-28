package base;

import java.util.ArrayList;
import java.util.List;

/**
 * Interface to allow synchronized printing in the client tablet.
 * 
 * @author sandeep
 *
 */
public abstract class Printable {
	public void printContents() {
	}
	public List<Printable> convertToList() {
		List<Printable> tempList = new ArrayList<Printable>();
		tempList.add(this);
		return tempList;
	}
	
	
}
