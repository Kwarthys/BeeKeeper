package com.beekeeper.ihm;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.beekeeper.controller.MainControllerServices;
import com.beekeeper.model.stimuli.Stimulus;

@SuppressWarnings("serial")
public class BeeWindow extends JFrame
{
	private ArrayList<CombDrawer> drawers;
	
	private JPanel container;
	private JComboBox<Stimulus> box;
	private FrameHandlerPanel frameHandler;
	
	public BeeWindow(TaskGrapher grapher, ArrayList<CombDrawer> drawers, MainControllerServices services)
	{	
		setTitle("BeeKeeper");
		this.drawers = drawers;
		
		this.addWindowListener(new WindowListener() {			
			@Override
			public void windowOpened(WindowEvent e) {}
			
			@Override
			public void windowIconified(WindowEvent e) {}
			
			@Override
			public void windowDeiconified(WindowEvent e) {}
			
			@Override
			public void windowDeactivated(WindowEvent e) {}
			
			@Override
			public void windowClosing(WindowEvent e) {
				//System.out.println("Closing");
				services.notifyWindowClosed();
			}
			
			@Override
			public void windowClosed(WindowEvent e){}
			
			@Override
			public void windowActivated(WindowEvent e) {}
		});

		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		container = new JPanel();
		container.setLayout(new GridBagLayout());
		
	
		Stimulus[] available = {Stimulus.Ocimene, Stimulus.AskFood};
		box = new JComboBox<Stimulus>(available);
		
		box.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				for(CombDrawer d : drawers)
				{
					d.drawnStimulus = (Stimulus)box.getSelectedItem();
				}
			}
		});
		
		frameHandler = new FrameHandlerPanel(new FrameHandlerCallback() {			
			@Override
			public void switchFrames(int index1, int index2) {
				services.switchFrames(index1, index2);
			}
			
			@Override
			public void reverseFrame(int index) {
				services.reverseFrame(index);
			}
		});
		
		updateDrawersPos();
		
		this.setContentPane(container);
		
		setSize(1800,800);
		
		container.setBackground(GraphicParams.BACKGROUND);
		
		setVisible(true);
		this.setLocationRelativeTo(null);
	}
	
	public void updateDrawersPos()
	{
		container.removeAll();
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 0;		
		container.add(box,c);
		
		
		
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 0.5;
		c.weighty = 0.5;
		
		for(CombDrawer drawer : drawers)
		{			
			c.gridx++;
			container.add(drawer, c);
		}
		
		c = new GridBagConstraints();
		//c.gridy = 2;
		//c.gridx = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		
		container.add(frameHandler);
		
		this.repaint();
		setVisible(true);
	}
	
}
