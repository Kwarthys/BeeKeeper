package com.beekeeper.ihm;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.beekeeper.controller.MainControllerServices;

@SuppressWarnings("serial")
public class TimeClickerInterface extends JPanel
{
	public TimeClickerInterface(MainControllerServices services)
	{
		this.setLayout(new GridBagLayout());

		this.setBackground(GraphicParams.BACKGROUND);

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.HORIZONTAL;

		JButton bSwitch = new JButton("SpeedUpSeconds");
		JTextField t1 = new JTextField("",10);

		c.weightx = 0.2;
		this.add(bSwitch, c);
		c.gridx++;
		c.weightx = 0.8;
		this.add(t1, c);

		this.setPreferredSize(new Dimension(200,200));
		this.setMinimumSize(new Dimension(200,200));

		bSwitch.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
				     int i1 = Integer.parseInt(t1.getText());
				     
				     System.out.println("Clicked switch " + i1);
				     services.setNumberOfSecondsToGoFast(i1);
				}
				catch (NumberFormatException ex) {}
			}
		});
	}
}
