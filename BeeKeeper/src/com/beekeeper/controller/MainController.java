package com.beekeeper.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Predicate;

import javax.swing.SwingUtilities;

import com.beekeeper.controller.logger.MyLogger;
import com.beekeeper.ihm.BeeWindow;
import com.beekeeper.ihm.CombDrawer;
import com.beekeeper.ihm.TaskGrapher;
import com.beekeeper.model.agent.Agent;
import com.beekeeper.model.comb.Comb;
import com.beekeeper.model.comb.CombManager;
import com.beekeeper.model.comb.CombServices;
import com.beekeeper.model.comb.cell.CombCell;
import com.beekeeper.model.tasks.Task;
import com.beekeeper.parameters.ModelParameters;

public class MainController
{	
	ArrayList<CombDrawer> drawers = new ArrayList<CombDrawer>();

	private ArrayList<Comb> combs = new ArrayList<Comb>();

	private MyLogger logger = new MyLogger();
	
	private CombManager combManager;

	private BeeWindow window;

	private AgentFactory agentFactory;

	//private int simuStep = 0;

	private boolean closed;

	private MainControllerServices controlServices = new MainControllerServices() {

		@Override
		public void logMyTaskSwitch(Task newTask, int beeID) {
			MainController.this.logger.logTask(beeID, newTask.taskName);				
		}

		@Override
		public CombCell askLandingZone() {
			ArrayList<Comb> theCombs = new ArrayList<>(combs);
			Collections.shuffle(theCombs);
			for(Comb cb : theCombs)
			{				
				ArrayList<CombCell> landingCells = new ArrayList<>(cb.getLandingZone());
				Collections.shuffle(landingCells);
				for(CombCell c : landingCells)
				{
					if(c.visiting == null)
					{
						return c;
					}
				}
			}

			return null;
		}

		@Override
		public void notifyWindowClosed() {
			MainController.this.closed = true;
		}
	};

	public MainController()
	{
		this.agentFactory = new AgentFactory();
		
		this.combManager = new CombManager();
		this.combManager.initiateCombs(3, agentFactory, this.controlServices);

		for(CombServices c : combManager.getCombsServices())
		{
			this.drawers.add(new CombDrawer(c));
		}
		
		TaskGrapher g = new TaskGrapher(agentFactory.allAgents);

		if(ModelParameters.UI_ENABLED == true)
		{
			this.window = new BeeWindow(g,drawers, this.controlServices);
			closed = false;			
		}

		combManager.reverseFrame(0);
		combManager.switchFrames(0, 2);
		programLoop();

		if(this.window != null)
		{
			this.window.dispose();
		}

		this.logger.closing();

		try {
			System.out.println("Waiting");
			this.logger.getThread().join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		/*
		while(!logger.threadFinished())
		{
			System.out.println("Waiting");
			try {
				Thread.sleep(500);//30
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		 */
		System.out.println("expe done");
	}

	/*
	private void logTurn(int turnIndex, int beeID, String beeTaskName, double beePhysio)
	{
		logger.log(turnIndex, beeID, beeTaskName, beePhysio);
	}
*/
	
	private void logTurn(String... ss)
	{
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < ss.length; ++i)
		{
			if(i!=0)
			{
				sb.append(",");
			}
			sb.append(ss[i]);
		}

		logger.log(sb.toString());
	}

	private void programLoop()
	{
		int turnIndex = 0;
		int displayBar = 20;

		System.out.print("|");
		for(int i = 1; i < displayBar-1; ++i)
		{			
			System.out.print("-");
		}
		System.out.println("|");

		logTurn("turnIndex", "beeID", "TaskName", "Physio");
		while(turnIndex < ModelParameters.SIMU_LENGTH && !closed)
		{
			if(turnIndex%(int)(ModelParameters.SIMU_LENGTH/displayBar) == 0)
			{
				System.out.print("|");
			}



			turnIndex++;

			ArrayList<Agent> copy = new ArrayList<>(agentFactory.allAgents);
			Collections.shuffle(copy);

			for(Agent b : copy)
			{
				b.live();
				/*
				if(b.getBeeType() == AgentType.ADULT_BEE || b.getBeeType() == AgentType.BROOD_BEE)
				{
					WorkingAgent w = (WorkingAgent) b;
					logTurn(turnIndex, b.getID(), w.getTaskName(), w.getPhysio());
				}
				*/
			}

			for(Comb c : combs)
			{
				c.update();
			}

			agentFactory.allAgents.removeIf(new Predicate<Agent>() {
				@Override
				public boolean test(Agent t) {
					return !t.alive;
				}
			});
			
			this.combManager.updateStimuli();

			if(this.window != null)
			{
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						MainController.this.window.repaint();					
					}
				});
			}

			//System.out.println(turnIndex);

			try {
				Thread.sleep(30);//30
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println();
	}
}
