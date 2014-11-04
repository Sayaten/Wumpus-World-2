import java.util.ArrayList;


public class State {
	private int[] location;
	private int g, h, f;
	private char direction;
	
	private ArrayList<Integer> Action = new ArrayList<Integer>();

	private State before;
	
	public State(int[] initlocation, char initdirection)
	{
		location = new int[2];
		location[0] = initlocation[0];
		location[1] = initlocation[1];
		direction = initdirection;
	}
	
	public ArrayList<Integer> getAction() {
		return Action;
	}

	public void setAction(ArrayList<Integer> action) {
		Action = action;
	}

	public int getG() {
		return g;
	}

	public void setG(int g) {
		this.g = g;
	}

	public int getH() {
		return h;
	}

	public void setH(int h) {
		this.h = h;
	}

	public State getBefore() {
		return before;
	}

	public void setBefore(State before) {
		this.before = before;
	}

	public void setLocation(int[] location)
	{
		this.location[0] = location[0];
		this.location[1] = location[1];
	}
	
	public void calculateF()
	{
		f = g + h;
	}
	
	public void setDirection(char direction)
	{
		this.direction = direction;
	}
	
	public int getF()
	{
		return f;
	}
	
	public int[] getLocation()
	{
		return location;
	}
	
	public char getDirection()
	{
		return direction;
	}
	
	public boolean isSameLocation(int[] location)
	{
		if(this.location[0] == location[0] && this.location[1] == location[1])
			return true;
		else
			return false;
	}
}
