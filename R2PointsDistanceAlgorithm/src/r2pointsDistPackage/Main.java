package r2pointsDistPackage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Main 
{
	PointOrderer p;
	JFrame f;

	public static void main(String[] args) throws IOException
	{
		JFrame frame = new JFrame();
		frame.getContentPane().setBackground(Color.black);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1500, 1200);
		frame.setVisible(true);
		String filePath = new String("rtest5.dat.txt");
		PointOrderer p = new PointOrderer(filePath, frame);
		Main m = new Main(p,frame);
		p.readPointsFromFile();
	}
	
	
	public Main(PointOrderer p, JFrame f)
	{
		this.p = p;
		this.f = f;
		GreedyL b1l = new GreedyL();
		CrossRemoveL b2l= new CrossRemoveL();
		JButton b1 = new JButton("Greedy Sort");
		b1.addActionListener(b1l);
		JButton b2 = new JButton("Run cross remover");
		b2.addActionListener(b2l);
		JLabel l1 = new JLabel();
		JPanel panel = new JPanel();
		panel.setBackground(Color.blue);
		p.totalDistanceLabel = l1;
		panel.add(b1);
		panel.add(b2);
		panel.add(l1);
		f.add(panel,BorderLayout.NORTH);
		f.add(p.visualComp,BorderLayout.CENTER);
	}
	
	
	
	class GreedyL implements ActionListener
	{

		public void actionPerformed(ActionEvent e) 
		{
			p.greedyOrderPoints();
			p.visualComp.repaint();
		}
		
	}
	
	class CrossRemoveL implements ActionListener
	{

		public void actionPerformed(ActionEvent e) 
		{
			p.removeCrosses();
			p.visualComp.repaint();
		}
		
	}
	
	
}
