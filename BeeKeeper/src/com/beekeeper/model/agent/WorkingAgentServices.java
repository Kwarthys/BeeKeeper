package com.beekeeper.model.agent;

import com.beekeeper.model.agent.implem.BroodBee;
import com.beekeeper.model.comb.cell.CombCell;
import com.beekeeper.model.stimuli.StimuliMap;
import com.beekeeper.model.stimuli.Stimulus;

public interface WorkingAgentServices {
	public void emit(Stimulus smell, double amount);
	public void addToEnergy(double amount);
	public double getEnergy();
	public double getHJTiter();
	public double getOvarianDev();
	
	public BroodBee.LarvalState getLarvalState();
	
	public void dropMotivation();
	public void killMotivation();
	public void resetMotivation();
	
	public void randomMove();

	public boolean isInside();
	public boolean isReceivingFood();
	public void receiveFood();
	public StimuliMap getLastPerception();
	public boolean isHungry();
	public int getID();
	public int getCombId();
	public CombCell getHostCell();
	public WorkingAgent getCoopInteractor();
	public void resetCoopInteractor();
	public void setInteractorTo(WorkingAgent agentInside);
	public void tryEnterHive();
	public boolean agentNearby();
	
	public void layEgg();

	public boolean tryMoveDown(boolean goOut);
	public boolean tryMoveUp();
	
}
