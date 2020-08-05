package com.beekeeper.model.comb;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.function.Predicate;

import com.beekeeper.model.agent.Agent;
import com.beekeeper.model.agent.WorkingAgent;
import com.beekeeper.model.comb.cell.CellContent;
import com.beekeeper.model.comb.cell.CombCell;
import com.beekeeper.model.stimuli.StimuliMap;
import com.beekeeper.model.stimuli.manager.StimuliManagerServices;

public class Comb
{
	private ArrayList<Agent> agents = new ArrayList<Agent>();
	
	private ArrayList<CombCell> cells = new ArrayList<>();
	
	private StimuliManagerServices smServices;
	private CombManagerServices combManagerServices;
	
	public int ID;
	
	private TrafficJamManager jamManager;
	
	private Dimension size;
	
	private CombServices services = new CombServices() {		
		@Override
		public int getID() {
			return ID;
		}
		
		@Override
		public ArrayList<CombCell> getCells() {
			return cells;
		}	
		
		@Override
		public ArrayList<Agent> getBees() {
			return agents;
		}

		@Override
		public ArrayList<Integer> getNeighbors(int x, int y){			
			return CombUtility.getCellNeighbors(x, y, size);
		}

		@Override
		public boolean askMoveToCell(Agent who, Integer where) {
			CombCell newCell = cells.get(where);
			if(newCell.visiting == null)
			{				
				who.hostCell.visiting = null;
				who.hostCell = newCell;
				newCell.visiting = who;
				return true;
			}
			else
			{
				//Contact
				WorkingAgent a = (WorkingAgent) who;
				WorkingAgent b = (WorkingAgent) newCell.visiting;
				//System.out.println("Contact by movement " + a.getID() + " " + b.getID());
				StimuliMap.contactBetween(a.getBodySmells(), b.getBodySmells());

				if(a.hostCell == null || b.hostCell == null)
				{
					System.out.println("Contact");
					if(b.hostCell == null)
					{
						System.out.println(a.getStringName() + " on " + a.getCombId() + " cell" + a.hostCell.number);
						System.out.println(b.getStringName() + " on null");						
					}
					else
					{
						System.out.println(a.getStringName() + " on null");
						System.out.println(b.getStringName() + " on " + b.getCombId() + " cell" + b.hostCell.number);	
					}					
				}
				
				jamManager.registerSwapDemand(a.hostCell.number, b.hostCell.number);
				//System.out.println(a.getStringName() + " wants swap with " + b.getStringName());
				
				return false;
			}
		}

		@Override
		public ArrayList<WorkingAgent> getNeighborBees(int x, int y) {				
			ArrayList<WorkingAgent> agents = Comb.this.getNeighborBees(x, y, true);			
			Comb facing = combManagerServices.getFacingComb(getID());
			if(facing != null)
			{
				agents.addAll(facing.getNeighborBees(x, y, false));
			}
			return agents;			
		}

		@Override
		public ArrayList<Integer> getDownNeighbors(int x, int y) {
			return CombUtility.getCellNeighborsDown(x, y, getDimension());
		}

		@Override
		public ArrayList<Integer> getUpNeighbors(int x, int y) {
			return CombUtility.getCellNeighborsUp(x, y, getDimension());
		}

		@Override
		public void notifyTakeOff(Agent a) {
			//System.out.println(a.getStringName() + " taking off from C" + ID);
			//System.out.println(agents.remove(a));
			agents.remove(a);
		}

		@Override
		public void notifyLanding(Agent a) {
			Comb.this.notifyLanding(a);
		}

		@Override
		public CombCell getCellAt(int x, int y) {
			return cells.get(y*size.width + x);
		}

		@Override
		public void swap(int cellIndexSwap1, int cellIndexSwap2) {
			CombCell cell1 = cells.get(cellIndexSwap1);
			CombCell cell2 = cells.get(cellIndexSwap2);
			
			Agent tmp = cell1.visiting;
			cell1.visiting = cell2.visiting;
			cell2.visiting = tmp;
			
			if(cell2.visiting == null || cell1.visiting == null)
			{
				System.out.println("SwapError on " + cellIndexSwap1 + " and " + cellIndexSwap2 + " on comb " + ID);
			}
			
			cell1.visiting.hostCell = cell1;
			cell2.visiting.hostCell = cell2;
			
			//System.out.println("SWAP");
		}

		@Override
		public StimuliManagerServices getCurrentSManagerServices() {
			return smServices;
		}

		@Override
		public boolean isFacingAnotherComb() {
			return combManagerServices.isFacingAComb(ID);
		}

		@Override
		public boolean askMoveToFacingCell(Agent who, int cellNumber) {
			CombCell facingCell = combManagerServices.getFacingCombCell(ID, cellNumber);
			if(facingCell != null)
			{
				if(facingCell.visiting == null)
				{
					cells.get(cellNumber).visiting = null;
					facingCell.visiting = who;
					
					who.hostCell = facingCell;
					
					combManagerServices.switchAgentHostComb(ID, who);
					
					return true;
				}
			}
			return false;
		}
	};
	
	public void removeAgent(Agent a)
	{
		agents.remove(a);
	}
	
	public void notifyLanding(Agent a)
	{
		//System.out.println(a.getStringName() + " landing on C" + ID);
		agents.add(a);
		a.registerNewStimuliManagerServices(services.getCurrentSManagerServices());
	}
	
	public ArrayList<WorkingAgent> getNeighborBees(int x, int y, boolean isFacingComb)
	{
		ArrayList<Integer> c = services.getNeighbors(x, y);
		ArrayList<WorkingAgent> agents = new ArrayList<>();
		for(Integer i : c)
		{
			WorkingAgent a = (WorkingAgent)cells.get(i).visiting;
			if(a!=null)
			{
				agents.add(a);
			}
		}
		
		if(isFacingComb)
		{
			WorkingAgent a = getCell(x, y).getAgentInside();
			if(a!=null)
			{
				agents.add(a);
			}
		}

		return agents;
	}
	
	public StimuliManagerServices getServicesOfClone()
	{
		return smServices.createNewEqualAndGetServices();
	}
	
	public int getStimuliManagerServicesID()
	{
		return smServices.getId();
	}
	
	public void registerNewSManager(StimuliManagerServices smServices)
	{
		this.smServices = smServices;
		
		this.agents.forEach((Agent a) -> a.registerNewStimuliManagerServices(smServices));
		this.cells.forEach((CombCell cc) -> {
			if(cc.visiting != null)
			{
				cc.visiting.registerNewStimuliManagerServices(smServices);
			}
		});
	}
	
	public Comb(int ID, Dimension combSize, StimuliManagerServices smServices, CombManagerServices combManagerServices)
	{
		this.size = new Dimension(combSize);
		this.ID = ID;
		cells = CombUtility.fillCells(size,ID, services);
		
		jamManager = new TrafficJamManager(services);
		this.combManagerServices = combManagerServices;
		
		registerNewSManager(smServices);
	}
	
	protected void testNeighborhood() //Not private to avoid "not used" warning
	{
		int azerrefgd = 1;
		
		cells.get(azerrefgd).content = CellContent.food;
		
		for(Integer i : CombUtility.getCellNeighbors(cells.get(azerrefgd).x, cells.get(azerrefgd).y, size))
		{
			System.out.print(i + " ");
			cells.get(i).content = CellContent.food;
		}
	}

	public ArrayList<Agent> getAgents(){return agents;}
	
	public CombServices getServices()
	{
		return services;
	}

	public void update()
	{	
		/** REMOVE THE DEAD **/
		agents.removeIf(new Predicate<Agent>() {
			@Override
			public boolean test(Agent t) {
				return !t.alive;
			}
		});
		
		for(CombCell c : cells)
		{
			if(c.inside != null)
			{
				if(!c.inside.alive)
				{
					c.inside = null;
					c.content = CellContent.empty;
				}
			}
		}
		
		/** UPDATE MANAGER **/
		jamManager.resetAll();
	}
	
	public boolean isCellContentEmpty(int x, int y)
	{
		return this.cells.get(x + y * size.width).content == CellContent.empty;
	}

	public boolean isCellVisitEmpty(int x, int y) {
		return this.cells.get(x + y * size.width).visiting == null;
	}

	public boolean isCellTaken(int x, int y) {
		return !isCellVisitEmpty(x, y);
	}

	public void setCellVisit(int x, int y, WorkingAgent bee) {
		CombCell cell = this.cells.get(x + y * size.width); 
		cell.visiting = bee;
		bee.hostCell = cell;
		this.agents.add(bee);
	}
	
	public void setCellAgentContent(int x, int y, WorkingAgent brood)
	{
		CombCell cell = this.cells.get(x + y * size.width); 
		cell.inside = brood;
		cell.content = CellContent.brood;
		brood.hostCell = cell;
	}

	public Dimension getDimension() {
		return size;
	}
	
	public CombCell getRandomFreeVisitCell()
	{
		CombCell c = null;
		
		while(c == null)
		{
			c = cells.get((int) (Math.random() * cells.size()));
			if(c.visiting != null)
			{
				c = null;
			}
		}
		
		return c;
	}

	public ArrayList<CombCell> getLandingZone() {
		ArrayList<CombCell> lastRow = new ArrayList<>();
		for(int x = 0; x < size.width; x++)
		{
			lastRow.add(cells.get((size.height-1) * size.width + x));
		}
		return lastRow;
	}
	
	public CombCell getCell(int combCellIndex)
	{
		return cells.get(combCellIndex);
	}
	
	public CombCell getCell(int x, int y)
	{
		return cells.get(y * size.width + x);
	}
	
	public ArrayList<CombCell> getCells(ArrayList<Integer> combCellIndexes)
	{
		ArrayList<CombCell> cells = new ArrayList<>();
		
		combCellIndexes.forEach((Integer i) -> cells.add(getCell(i)));
		
		return cells;
	}

	public void addFood()
	{
		for(int i = 0; i<cells.size();++i)
		{
			CombCell c = cells.get(i);
			if(c.content == CellContent.brood)
			{
				for(Integer cIndex : CombUtility.getCellNeighbors(i, size))
				{
					CombCell otherCell = cells.get(cIndex);
					if(otherCell.content == CellContent.empty)
					{
						otherCell.content = CellContent.food;
					}
				}
			}
		}
		
	}

	public boolean isUp() {
		return this.combManagerServices.isCombUp(ID);
	}
}
