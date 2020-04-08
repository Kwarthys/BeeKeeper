package com.beekeeper.model.comb;

import com.beekeeper.model.agent.Agent;
import com.beekeeper.model.comb.cell.CombCell;

public interface CombManagerServices {
	public boolean isFacingAComb(int combID);
	public CombCell getFacingCombCell(int combID, int combCellIndex);
	
	/**
	 * 
	 * @param combID
	 * @return facing comb or Null
	 */
	public Comb getFacingComb(int combID);
	public void switchAgentHostComb(int startCombID, Agent who);
}
