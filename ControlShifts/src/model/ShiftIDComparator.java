package model;

import java.util.Comparator;

public class ShiftIDComparator implements Comparator<Shift> {

	@Override
	public int compare(Shift s, Shift s2) {

		if(s.toString().compareTo(s2.toString())<0) {
			return -1;
		}
		else if(s.toString().compareTo(s2.toString())>0) {
			return 1;
		}
		else {
			return 0;
		}
	}

}
