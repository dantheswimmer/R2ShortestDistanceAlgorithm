package r2pointsDistPackage;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilterInputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class PointOrderer 
{
	private ArrayList<MyPoint> points;
	String fileReadPath;
	JFrame frame;
	PointsVisualComponent visualComp;
	JLabel totalDistanceLabel;
	boolean noMoreCrosses = false;
	
	public PointOrderer(String fileP, JFrame frame)
	{
		fileReadPath = fileP;
		this.frame = frame;
		visualComp = new PointsVisualComponent();
		points = new ArrayList<MyPoint>();
	}
	
	public void readPointsFromFile() throws IOException
	{
		File fileToRead = new File(fileReadPath);
		FileReader fileReader = new FileReader(fileToRead);
		BufferedReader bReader = new BufferedReader(fileReader);
		int pointID = 0;
		while(bReader.ready())
		{
			String line = bReader.readLine();
			String splitStr[] = line.split("   ");
			if(splitStr.length >1)
			{
				int num1 = Integer.parseInt(splitStr[0]);
				int num2 = Integer.parseInt(splitStr[1]);
				MyPoint readPoint = new MyPoint(num1,num2, pointID);
				points.add(readPoint);
				pointID++;
			}
		}
		//autoSolve();
		writePointsToFile();
		bReader.close();
	}
	
	public void autoSolve()
	{
		greedyOrderPoints();
		while(!noMoreCrosses)
		{
			removeCrosses();
		}
		visualComp.repaint();
	}
	
	public int getTotalDistanceCurrentOrder()
	{
		int distance = 0;
		for(int i =0; i < points.size()-1; i++)
		{
			distance += distanceBetweenPoints(points.get(i),points.get(i+1));
		}
		totalDistanceLabel.setText("Total Distance: " + distance);
		return distance;
	}
	
	public void printDist()
	{
		System.out.println("Distance of path is: " + getTotalDistanceCurrentOrder());
	}
	
	public void greedyOrderPoints()
	{
		ArrayList<MyPoint> newList = new ArrayList<MyPoint>();
		
		MyPoint currentPoint = points.get(0);
		for(int i =0; i < points.size(); i++)
		{
			currentPoint.wasUsed = true;
			MyPoint nextPoint = null;
			int distance = 99999999;
			for(int j =0; j < points.size(); j++)
			{
				MyPoint newPoint = points.get(j);
				if(!newPoint.wasUsed)
				{
					int newDist = distanceBetweenPoints(newPoint,currentPoint);
					if(newDist < distance)
					{
						distance = newDist;
						nextPoint = newPoint;
					}
				}
			}
			newList.add(currentPoint);
			currentPoint = nextPoint;
		}
		points = newList;
		getTotalDistanceCurrentOrder();
	}
	
	public void removeCrosses()
	{
		boolean removed = false;
		for(int i = 0; i < points.size()-3; i++)
		{
			for(int j = i+2; j < points.size()-1; j++)
			{
				int distA1 = distanceBetweenPoints(points.get(i),points.get(i+1));
				int distA2 = distanceBetweenPoints(points.get(j),points.get(j+1));
				int distB1 = distanceBetweenPoints(points.get(i),points.get(j));
				int distB2 = distanceBetweenPoints(points.get(i+1),points.get(j+1));
				if(distA1 + distA2 > distB1 + distB2)
				{
					removed = true;
					//System.out.println("points are criscrossed at index: " + i);
					int blockStartIndex = i+1;
					int blockEndIndex = j;
					int blockSize = blockEndIndex-blockStartIndex;
					for(int y = 0; y < blockSize/2+1; y++)
					{
						switchPoints(blockStartIndex+y,blockEndIndex-y);
					}
				}
			}
		}
		if(!removed)
		{
			noMoreCrosses = true;
		}
		getTotalDistanceCurrentOrder();
	}
	
	public void writePointsToFile() throws IOException
	{
		String fileWritePath = "OUTPUT.txt";
		File fileToWrite = new File(fileWritePath);
		FileWriter fileWriter = new FileWriter(fileToWrite);
		BufferedWriter bWriter = new BufferedWriter(fileWriter);
		bWriter.write(Integer.toString(points.size()));
		bWriter.newLine();
		for(int i =0; i < points.size(); i++)
		{
			String writeStr = Integer.toString(((int)points.get(i).getX())) + "    " + Integer.toString(((int)points.get(i).getY()));
			bWriter.write(writeStr);
			bWriter.newLine();
		}
		
		bWriter.close();
	}
	
	public void printPoints()
	{
		for(int i =0; i < points.size(); i++)
		{
			System.out.println("Point: " + points.get(i).getX() + "," + points.get(i).getY());
		}
	}
	
	public void switchPoints(int p1index, int p2index)
	{
		MyPoint p1 = points.get(p1index);
		MyPoint p2 = points.get(p2index);
		points.set(p1index, p2);
		points.set(p2index, p1);
	}
	
	public int distanceBetweenPoints(MyPoint p1, MyPoint p2)
	{
		return (int)Math.sqrt( (p1.getX()-p2.getX())*(p1.getX()-p2.getX()) + (p1.getY()-p2.getY())*(p1.getY()-p2.getY()) );
	}
	
	class PointsVisualComponent extends JComponent
	{
		public void paintComponent(Graphics g)
		{
			for(int i = 0; i < points.size(); i++)
			{
				g.setColor(Color.yellow);
				MyPoint temp = points.get(i);
				g.drawOval(temp.getX()*2-2, temp.getY()*2-2, 3, 3);
				if(i == 0)
				{
					g.drawRect(temp.getX()*2, temp.getY()*2, 5, 5);
				}
				if(i < points.size()-1)
				{
					g.setColor(Color.white);
					MyPoint nextTemp = points.get(i+1);
					g.drawLine(temp.getX()*2, temp.getY()*2, nextTemp.getX()*2, nextTemp.getY()*2);
				}
			}
		}
	}
}
