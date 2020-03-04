package com.beekeeper.ihm;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class FrameHandlerPanel extends JPanel {

	public FrameHandlerPanel(FrameHandlerCallback cb)
	{
		this.setLayout(new GridBagLayout());

		this.setBackground(GraphicParams.BACKGROUND);

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.HORIZONTAL;

		JButton bSwitch = new JButton("Switch");
		JButton bReverse = new JButton("Reverse");
		JTextField t1 = new JTextField("",20);
		JTextField t2 = new JTextField("",20);
		JTextField t3 = new JTextField("",40);

		c.weightx = 0;
		this.add(bSwitch, c);
		c.gridx++;
		c.weightx = 0.5;
		this.add(t1, c);
		c.gridx++;
		this.add(t2, c);

		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 0;
		this.add(bReverse,c);
		c.weightx = 0.1;
		c.gridx++;
		this.add(t3,c);

		this.setPreferredSize(new Dimension(200,200));
		this.setMinimumSize(new Dimension(200,200));

		bSwitch.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
				     int i1 = Integer.parseInt(t1.getText());
				     int i2 = Integer.parseInt(t2.getText()); 
				     
				     System.out.println("Clicked switch " + i1 + " " + i2);
				     cb.switchFrames(i1, i2);
				}
				catch (NumberFormatException ex) {}
			}
		});

		bReverse.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
				     int i = Integer.parseInt(t3.getText());
				     
				     System.out.println("Clicked reverse " + i);
				     cb.reverseFrame(i);
				}
				catch (NumberFormatException ex) {}
			}
		});
	}
}
