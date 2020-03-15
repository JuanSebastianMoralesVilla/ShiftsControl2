package model;

import java.util.Comparator;

public class ShiftTimeComparator implements Comparator<Shift> {

	@Override
	public int compare(Shift o1, Shift o2) {
		if(o1.getTime().getTime()<o2.getTime().getTime()) {
			return -1;
		}
		else if(o1.getTime().getTime()>o2.getTime().getTime()) {
			return 1;
		}
		return 0;
	}
	
}
