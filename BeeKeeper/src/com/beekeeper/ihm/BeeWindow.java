package com.beekeeper.ihm;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.beekeeper.model.stimuli.Stimulus;

@SuppressWarnings("serial")
public class BeeWindow extends JFrame
{
	
	public BeeWindow(TaskGrapher grapher, ArrayList<CombDrawer> drawers)
	{	
		setTitle("BeeKeeper");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel container = new JPanel();
		container.setLayout(new GridBagLayout());
		
	
		Stimulus[] available = {Stimulus.Ocimene, Stimulus.AskFood};
		JComboBox<Stimulus> box = new JComboBox<Stimulus>(available);
		
		box.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				for(CombDrawer d : drawers)
				{
					d.drawnStimulus = (Stimulus)box.getSelectedItem();
				}
			}
		});
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 1;
		
		container.add(box);
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
