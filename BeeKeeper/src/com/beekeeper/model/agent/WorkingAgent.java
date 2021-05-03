package com.beekeeper.model.agent;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import com.beekeeper.controller.MainControllerServices;
import com.beekeeper.controller.logger.MyLogger;
import com.beekeeper.model.agent.implem.BroodBee.LarvalState;
import com.beekeeper.model.comb.cell.CombCell;
import com.beekeeper.model.stimuli.StimuliMap;
import com.beekeeper.model.stimuli.Stimulus;
import com.beekeeper.model.stimuli.manager.StimuliManagerServices;
import com.beekeeper.model.tasks.Action;
import com.beekeeper.model.tasks.Task;
import com.beekeeper.model.tasks.beetasks.AskFoodTask;
import com.beekeeper.parameters.ModelParameters;

public abstract class WorkingAgent extends EmitterAgent
{
	private static DecimalFormat df = getDF();
	
	private static DecimalFormat getDF()
	{
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
		otherSymbols.setDecimalSeparator('.');
		DecimalFormat f = new DecimalFormat("#.####", otherSymbols);
		
		return f;
	}
	
	protected ArrayList<Task> taskList = new ArrayList<>();
	protected abstract void fillTaskList();

	protected MainControllerServices controllerServices;

	protected StimuliMap lastPercievedMap;

	protected Task currentTask = null;
	protected Action currentAction = null;
	protected Task lastTask = null;

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
		public void tryEnterHive() {
			WorkingAgent.this.askEnterHive();
		}

		@Override
		public boolean tryMoveDown(boolean goOut) {
			return WorkingAgent.this.tryMoveDown(goOut);
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

		@Override
		public LarvalState getLarvalState() {
			return WorkingAgent.this.getLarvalState();
		}
	};
	
	public LarvalState getLarvalState()
	{
		return LarvalState.None;
	}

	public WorkingAgent(StimuliManagerServices stimuliManagerServices, MainControllerServices controllerServices)
	{
		this(stimuliManagerServices, controllerServices, true);
	}
	
	public WorkingAgent(StimuliManagerServices stimuliManagerServices, MainControllerServices controllerServices, boolean randomInit)
	{
		super(stimuliManagerServices);
		this.controllerServices = controllerServices;
		//this.bodySmell.setControllerServices(controllerServices);
		fillTaskList();

		setEnergy(Math.random()*0.5+0.5);
		hunger = Math.random() * 0.7;
		initPhysiology(randomInit);
	}
	
	protected abstract void initPhysiology(boolean randomInit);

	/**
	 * For now only the queen implements this and can lay eggs. In the future they might all brood
	 */
	protected void layEgg() {};
	
	@Override
	public void logTurn(MyLogger logger, int turnIndex)
	{
		if(!alive)
		{
			//System.out.println("Trying to log a ded agent.");
			return;
		}
		logger.log(String.valueOf(turnIndex), String.valueOf(getID()), getTaskName(), df.format(getPhysio()), df.format(getEO()), String.valueOf(getRealAge()), df.format(getTotalExchangedAmount()));
	}

	public void live()
	{		
		boolean debugtime = false;//Math.random() > 0.99;
		
		age++;
		
		long startlive = 0;
		if(debugtime)
		{
			startlive = System.nanoTime();
		}
		
		if(alive == false || getEnergy() == 0)
		{
			alive = false;
			controllerServices.notifyDeath(this);			
			
			if(getLarvalState() == LarvalState.None && getEnergy() == 0)
			{
				//System.out.println("Just died of starvation : " + getLarvalState() + " at " + age + "/" + ModelParameters.larvaLarvaUntilAge);
				System.out.print("x");
			}	
				
			
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
		
		if(debugtime)
		{
			System.out.println("added internal perception at t+" + (System.nanoTime() - startlive)/1000 + "us.");
		}


		//System.out.println(ID + " looking for a new task ? " + (currentAction == null));
		/*
		if(lastTask!=null)
		{
			System.out.println(ID + " " + lastTask.taskName + " E" + getEnergy());
			//System.out.println("cost = " + ModelParameters.FORAGING_ENERGYCOST + " * " + ModelParameters.FORAGING_TIME + " = " + ModelParameters.FORAGING_ENERGYCOST * ModelParameters.FORAGING_TIME);
		}
		*/

		if(currentAction == null)
		{
			lastTask = currentTask;
			
			if(isInside())
			{				
				if(debugtime)
				{
					System.out.println("start isInside t+" + (System.nanoTime() - startlive)/1000 + "us.");
				}
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
					if(debugtime)
					{
						System.out.println("start action lookup t+" + (System.nanoTime() - startlive)/1000 + "us.");
					}
					currentAction = chooseNewTask(s).search();
					//if(getID() == 999)System.out.println(getID() + " found " + currentTask.taskName);
				}
			}
			else
			{					
				if(debugtime)
				{
					System.out.println("start foraging action lookup t+" + (System.nanoTime() - startlive)/1000 + "us.");
				}
				currentAction = currentTask.search();
			}
		}

		
		if(debugtime)
		{
			System.out.println("action choosen at t+" + (System.nanoTime() - startlive)/1000 + "us.");
		}
		
		//if(getID() == 999)System.out.println(getID() + " " + currentTask.taskName);
		
		//if(!isInside())System.out.println(this.ID + " " + currentTask.taskName);
		currentAction.run();	
		

		if(loggerAgent && ModelParameters.BEELOGGING)
		{
			logger.log(currentTask.taskName, df.format(hjTiter), df.format(lastPercievedMap.getAmount(Stimulus.EthyleOleate)));
		}


		
		try 
		{			
			if(currentAction == null)
			{
				//This should not happen, but is happening once every billion timestep
				System.err.println("Null Action after running it - Working Agent - Live | lastTask: " + lastTask.taskName);
			}
			//If action is over, remove it
			else if(currentAction.isOver())
			{
				//System.out.println("Action done");
				currentAction = null;
			}
			
			
		}
		catch(Exception e)
		{
			System.out.println("currentTask: " + currentTask + " chooseNewTask: " + chooseNewTask(s).taskName);			
			e.printStackTrace();
			System.exit(1);
		}
		
		


		if(cooperativeInteractor != null)
		{
			spreadByContact(cooperativeInteractor);
		}

		
		advanceMetabolism();
		this.bodySmell.evaporate();
		
		if(debugtime)
		{
			System.out.println("fully lived at t+" + (System.nanoTime() - startlive)/1000 + "us.");
		}
		
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

	public boolean isHungry()
	{
		boolean isAskingFood = false;
		if(currentTask != null)
		{
			isAskingFood = currentTask.taskName == AskFoodTask.AskingFoodTaskName;
		}
		return hunger > 0.5 && isAskingFood;
	}

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
		
		if(todo==null)
		{
			System.out.println("WorkingAgent - FindATask() - couldn't find a task and returned null");
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

	protected void askEnterHive()
	{
		if(isInside())
		{
			System.err.println("Already inside, can't reenter");
			return;
		}

		controllerServices.askLanding(this);
		
		/*
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
		*/
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
	
	protected boolean tryMoveDown(boolean goOut)
	{
		if(!isInside())
		{
			System.err.println("Outside agent asked to move inside in " + currentTask.taskName);
			return false;
		}
		
		ArrayList<Integer> cells = hostCell.getDownCells();
		if(cells.size() == 0)
		{
			//We are at the bottom !
			if(goOut)
			{
				//System.out.println(ID + " liftoff from " + hostCell);
				controllerServices.notifyLiftoff(this);
				hostCell.freeCell();
				this.hostCell = null;
				//if(getID() == 999)System.out.println(this.getID() + " liftoff !");
				return true;			
			}
			return false;
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
		if(lastPercievedMap != null)
		{
			for(Task t : taskList)
			{
				d.put(t.taskName, t.compute(lastPercievedMap));
			}
		}
		return d;
	}

	public StimuliMap getPercievedStimuli() {
		return lastPercievedMap;
	}

	public String getTaskName() {
		if(currentTask==null)
		{
			System.err.println("WorkingAgent - GetTaskName - TaskNullHere"); //TODO TaskNullHere
			return "noTask";
		}
		return currentTask.taskName;
	}

	public double getPhysio() {
		return hjTiter;
	}

	public double getEO() {
		if(lastPercievedMap==null) return 0;
		return lastPercievedMap.getAmount(Stimulus.EthyleOleate);
	}
}
