package com.beekeeper.model.agent;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;

import com.beekeeper.controller.MainControllerServices;
import com.beekeeper.model.comb.cell.CombCell;
import com.beekeeper.model.stimuli.StimuliMap;
import com.beekeeper.model.stimuli.Stimulus;
import com.beekeeper.model.stimuli.manager.StimuliManagerServices;
import com.beekeeper.model.tasks.Action;
import com.beekeeper.model.tasks.Task;
import com.beekeeper.parameters.ModelParameters;

public abstract class WorkingAgent extends EmitterAgent
{
	protected ArrayList<Task> taskList = new ArrayList<>();
	protected abstract void fillTaskList();

	protected MainControllerServices controllerServices;

	protected StimuliMap lastPercievedMap;

	protected Task currentTask = null;
	protected Action currentAction = null;

	protected WorkingAgent cooperativeInteractor = null;

	public Point2D.Double target = null;

	public Task getCurrentTask() {return currentTask;}

	protected double ovarianDev = 0;
	protected double hjTiter = 0;
	public double getHJ() {return hjTiter;}	

	public void interruptTask() {currentTask = null;}

	protected double motivation = 1;
	public double getMotivation() {return motivation;}

	protected WorkingAgentServices ownServices = new WorkingAgentServices() {		
		@Override
		public double getEnergy() {
			return WorkingAgent.this.getEnergy();
		}

		@Override
		public void emit(Stimulus smell, double amount) {
			WorkingAgent.this.emit(smell, amount);
		}

		@Override
		public void addToEnergy(double amount) {
			WorkingAgent.this.addToEnergy(amount);
		}

		@Override
		public void randomMove() {
			WorkingAgent.this.randomMove();			
		}

		@Override
		public void dropMotivation() {
			WorkingAgent.this.motivation = Math.max(0, WorkingAgent.this.motivation - ModelParameters.MOTIVATION_STEP);
		}

		@Override
		public void resetMotivation() {
			WorkingAgent.this.motivation = 1;
		}

		@Override
		public int getID() {
			return WorkingAgent.this.getID();
		}

		@Override
		public StimuliMap getLastPerception() {
			return WorkingAgent.this.lastPercievedMap;
		}

		@Override
		public boolean isHungry() {
			return WorkingAgent.this.isHungry();
		}

		@Override
		public boolean isReceivingFood() {
			return WorkingAgent.this.receivingFood;
		}

		@Override
		public WorkingAgent getCoopInteractor() {
			return WorkingAgent.this.cooperativeInteractor;
		}

		@Override
		public CombCell getHostCell() {
			return WorkingAgent.this.hostCell;
		}

		@Override
		public void resetCoopInteractor() {
			WorkingAgent.this.cooperativeInteractor = null;			
		}

		@Override
		public void setInteractorTo(WorkingAgent a) {
			WorkingAgent.this.cooperativeInteractor = a;			
		}

		@Override
		public double getHJTiter() {
			return WorkingAgent.this.hjTiter;
		}

		@Override
		public boolean isInside() {
			return  WorkingAgent.this.isInside();
		}

		@Override
		public boolean enterHive() {
			return WorkingAgent.this.enterHive();
		}

		@Override
		public boolean tryMoveDown() {
			return WorkingAgent.this.tryMoveDown();
		}

		@Override
		public boolean agentNearby() {
			if(hostCell == null)System.err.println("CEPANORMAL ICI " + getStringName() + " " + currentTask.taskName);
			return hostCell.getNeighborBees().size() > 0;
		}

		@Override
		public void killMotivation() {
			WorkingAgent.this.motivation = 0;
		}

		@Override
		public boolean tryMoveUp() {
			return WorkingAgent.this.tryMoveUp();
		}

		@Override
		public void receiveFood() {
			WorkingAgent.this.recieveFood();
		}

		@Override
		public int getCombId() {
			return WorkingAgent.this.getCombId();
		}

		@Override
		public void layEgg(){
			WorkingAgent.this.layEgg();
		}

		@Override
		public double getOvarianDev() {
			return ovarianDev;
		}
	};

	public WorkingAgent(StimuliManagerServices stimuliManagerServices, MainControllerServices controllerServices)
	{
		super(stimuliManagerServices);
		this.controllerServices = controllerServices;
		setEnergy(Math.random()*0.8+0.2);
		fillTaskList();

		hunger = Math.random() * 0.7;
		hjTiter = ModelParameters.getStartingBeeHJTiter();//Math.random() * Math.random() * Math.random();
	}
	
	/**
	 * For now only the queen implements this and can lay eggs. In the future they might all brood
	 */
	protected void layEgg() {};

	public void live()
	{		
		if(alive == false)
		{
			return;
		}

		if(getEnergy() == 0)
		{
			alive = false;
			return;
		}


		StimuliMap s;

		if(isInside())
		{
			s = stimuliManagerServices.getAllStimuliAround(new Point(hostCell.x, hostCell.y));
		}
		else
		{
			s = new StimuliMap();
		}

		s = addInternalPerceptions(s);
		lastPercievedMap = s;


		//System.out.println(ID + " living ! " + s.getAmount(Stimulus.HungryLarvae));

		if(currentAction == null)
		{
			if(isInside())
			{
				//System.out.println("Executing new Action");
				
				if(ModelParameters.BYPASS_MOTIVATION)
				{
					if(Math.random() < 1 || currentTask == null || getEnergy() < 0.1)
					{
						currentAction = chooseNewTask(s).search();
					}
					else
					{
						currentAction = currentTask.search();
					}
				}
				else
				{
					currentAction = chooseNewTask(s).search();
				}
			}
			else
			{
				currentAction = currentTask.search();
			}
		}

		
		//if(!isInside())System.out.println(this.ID + " " + currentTask.taskName);
		currentAction.run();			



		//If action is over, remove it
		if(currentAction.isOver())
		{
			//System.out.println("Action done");
			currentAction = null;
		}


		if(cooperativeInteractor != null)
		{
			spreadByContact(cooperativeInteractor);
		}

		advanceMetabolism();
		this.bodySmell.evaporate();
	}

	protected abstract void advanceMetabolism();


	private StimuliMap addInternalPerceptions(StimuliMap s)
	{
		s.addAmount(Stimulus.HungerBee, this.getHunger());
		s.addAmount(Stimulus.Energy, this.getEnergy());
		s.addAllAmounts(this.bodySmell);
		return s;
	}

	public boolean isHungry() {return hunger > 0.5 && currentTask.taskName == "Asking Food";}

	public void recieveFood()
	{
		hunger-= 0.8;
		hunger = Math.max(0, hunger);

		receivingFood = true;
	}

	public Task findATask(StimuliMap load)
	{		
		Task todo = null;
		double taskScore = -1;

		for(int ti = 0; ti < taskList.size(); ++ti)
		{
			Task current = taskList.get(ti);
			double currentScore = 0;

			if(current == currentTask && current.isMotivated() && !ModelParameters.BYPASS_MOTIVATION)
			{
				currentScore = motivation;
			}
			else
			{				
				currentScore = current.compute(load);
			}

			//if(currentScore != 0)
			//System.out.println(current.taskName + " " + currentScore + " t:" + current.threshold + "\n" + load.getDisplayString());
			if(currentScore > taskScore)
			{
				todo = current;
				taskScore = currentScore;
			}
		}
		return todo;
	}

	protected void spreadByContact(WorkingAgent other)
	{
		//System.out.println("ContactSpread between " + this.getStringName() + " and " + other.getStringName());
		StimuliMap.contactBetween(other.bodySmell, this.bodySmell);
	}

	protected Task chooseNewTask(StimuliMap load)
	{
		Task newTask = findATask(load);
		if(newTask != currentTask)
		{
			motivation = ModelParameters.MAX_MOTIVATION;
		}
		currentTask = newTask;
		//controllerServices.logMyTaskSwitch(currentTask, this.ID);

		return currentTask;
	}

	protected boolean enterHive()
	{
		if(isInside())
		{
			System.err.println("Already inside, can't reenter");
			return true;
		}

		hostCell = controllerServices.askLandingZone();
		if(hostCell != null)
		{
			hostCell.notifyLanding(this);
			return true;
		}
		else
		{
			return false;
		}
	}

	protected boolean tryMoveTo(ArrayList<Integer> cells)
	{
		int r = (int) (Math.random() * cells.size());
		return hostCell.askMoveToCell(this, cells.get(r));		
	}

	protected boolean tryMoveUp()
	{
		ArrayList<Integer> cells = hostCell.getUpCells();

		if(cells.size() == 0)
		{
			//We are at the Top !
			return false;
		}
		else
		{
			return tryMoveTo(cells);
		}
	}
	
	protected boolean tryMoveDown()
	{
		ArrayList<Integer> cells = hostCell.getDownCells();
		if(cells.size() == 0)
		{
			//We are at the bottom !
			hostCell.leaveCell();
			this.hostCell = null;
			return true;
		}
		else
		{
			return tryMoveTo(cells);
		}
	}

	public int getCombId() {return hostCell.getCombID();}

	public HashMap<String, Double> getAllThresholds()
	{
		HashMap<String, Double> ts = new HashMap<String, Double>();

		for(Task t : taskList)
		{
			ts.put(t.taskName, t.threshold);
		}

		return ts;
	}

	public HashMap<String, Double> getAllTaskScores()
	{
		HashMap<String, Double> d = new HashMap<>();
		for(Task t : taskList)
		{
			d.put(t.taskName, t.compute(lastPercievedMap));
		}
		return d;
	}

	public StimuliMap getPercievedStimuli() {
		return lastPercievedMap;
	}

	public String getTaskName() {
		return currentTask.taskName;
	}

	public double getPhysio() {
		return hjTiter;
	}
}
