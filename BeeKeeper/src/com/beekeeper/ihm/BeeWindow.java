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
import com.beekeeper.model.tasks.beetasks.AskFoodTask;
import com.beekeeper.model.tasks.beetasks.FeedLarva;
import com.beekeeper.model.tasks.beetasks.ForagerTask;
import com.beekeeper.model.tasks.beetasks.GiveFoodTask;
import com.beekeeper.model.tasks.generaltasks.RandomMoveTask;

@SuppressWarnings("serial")
public class BeeWindow extends JFrame
{
	private ArrayList<CombDrawer> drawers;
	
	private JPanel container;
	private JComboBox<Stimulus> box;
	private JComboBox<String> taskBox;
	//private FrameHandlerPanel frameHandler;
	private TimeClickerInterface timerInterface;
	@SuppressWarnings("unused")
	private TaskGrapher grapher;
	
	private JPanel boxContainer = new JPanel();
	
	public BeeWindow(TaskGrapher grapher, ArrayList<CombDrawer> drawers, MainControllerServices services)
	{	
		setTitle("BeeKeeper");
		this.drawers = drawers;
		this.grapher = grapher;
		
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
		
	
		Stimulus[] available = {Stimulus.EthyleOleate, Stimulus.AskFood};
		box = new JComboBox<Stimulus>(available);
		
	
		String[] availableTask = {"All", GiveFoodTask.giveFoodTaskName, AskFoodTask.AskingFoodTaskName, ForagerTask.foragingTaskName, FeedLarva.feedLarvaeTaskName, RandomMoveTask.randomWalkTaskName};
		taskBox = new JComboBox<String>(availableTask);
		
		box.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				for(CombDrawer d : drawers)
				{
					d.drawnStimulus = (Stimulus)box.getSelectedItem();
				}
			}
		});
		
		taskBox.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				for(CombDrawer d : drawers)
				{
					d.drawnTasks = (String)taskBox.getSelectedItem();
				}
			}
		});
		
		timerInterface = new TimeClickerInterface(services);
		
		boxContainer.setBackground(GraphicParams.BACKGROUND);
		
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
		boxContainer.add(box);
		boxContainer.add(taskBox);
		container.add(boxContainer,c);
		
		

		c = new GridBagConstraints();
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
		c.gridy = 0;
		c.gridx = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		
		container.add(timerInterface,c);
		

		
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridy = 1;
		c.weightx=1.0;
		c.weighty=1.0;
		//container.add(grapher,c);

		
		this.repaint();
		setVisible(true);
	}
	
}
