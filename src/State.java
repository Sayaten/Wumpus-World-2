
public class State {
	int x, y;
	int f;
	
	public void setXY(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public void setF(int f)
	{
		this.f = f;
	}
	
	public int getF()
	{
		return f;
	}
	public int[] getXY()
	{
		int[] value = new int[2];
		value[0] = y;
		value[1] = x;
		
		return value;
	}
}
