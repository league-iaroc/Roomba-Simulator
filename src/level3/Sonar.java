package level3;

import level2.Roomba;

public class Sonar {

	private int rightFilteredDistance;
	private int leftFilteredDistance;
	private int centerFilteredDistance;

	public Sonar(Roomba roomba) {
		// TODO Auto-generated constructor stub
	}

	int readSonar(String position){
		if (position.equals("right"))
	    {
		return rightFilteredDistance;
	    }
	if (position.equals("left"))
	    {
		return leftFilteredDistance;
	    }
	if (position.equals("center"))
	    {
		return centerFilteredDistance;
	    }
return -1;
	}
}
