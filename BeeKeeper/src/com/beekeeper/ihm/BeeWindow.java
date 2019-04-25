package com.beekeeper.ihm;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class BeeWindow extends JFrame
{
	
	public BeeWindow(BeeDrawer drawer)
	{	
		setTitle("BeeKeeper");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel container = new JPanel();
		container.setLayout(new BorderLayout());
		container.add(drawer, BorderLayout.CENTER);
		setSize(1000,1000);

		this.setContentPane(container);
		
		setVisible(true);
		this.setLocationRelativeTo(null);
	}
	
}
