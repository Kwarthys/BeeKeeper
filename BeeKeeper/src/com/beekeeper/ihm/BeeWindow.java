package com.beekeeper.ihm;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class BeeWindow extends JFrame
{
	
	public BeeWindow(TaskGrapher grapher, ArrayList<CombDrawer> drawers)
	{	
		setTitle("BeeKeeper");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel container = new JPanel();
		container.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		
		container.add(grapher, c);
		
		for(CombDrawer drawer : drawers)
		{			
			c.gridx++;
			container.add(drawer, c);
		}
		
		setSize(1800,800);

		this.setContentPane(container);
		
		container.setBackground(GraphicParams.BACKGROUND);
		
		setVisible(true);
		this.setLocationRelativeTo(null);
	}
	
}
