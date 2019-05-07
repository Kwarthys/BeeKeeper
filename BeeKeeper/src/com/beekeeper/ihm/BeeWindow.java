package com.beekeeper.ihm;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class BeeWindow extends JFrame
{
	
	public BeeWindow(Grapher grapher, ArrayList<CombDrawer> drawers)
	{	
		setTitle("BeeKeeper");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel container = new JPanel();
		container.setLayout(new GridLayout(0,drawers.size()+1));
		
		container.add(grapher);
		
		for(CombDrawer drawer : drawers)
		{			
			container.add(drawer);
		}
		setSize(1000,1000);

		this.setContentPane(container);
		
		setVisible(true);
		this.setLocationRelativeTo(null);
	}
	
}
