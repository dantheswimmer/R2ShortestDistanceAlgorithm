package r2pointsDistPackage;

import java.util.ArrayList;

public class MyPoint 
{
	private int pointID;
	private int x;
	private int y;
	public boolean wasUsed = false;
	
	public MyPoint(int x, int y, int pointID)
	{
		this.x = x;
		this.y = y;
		this.pointID = pointID;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
}
