package com.beekeeper.model.comb;

import com.beekeeper.model.agent.Agent;
import com.beekeeper.model.comb.cell.CombCell;

public interface CombManagerServices {
	public boolean isFacingAComb(int combID);
	public CombCell getFacingCombCell(int combID, int combCellIndex);
	public void switchAgentHostComb(int startCombID, Agent who);
}
