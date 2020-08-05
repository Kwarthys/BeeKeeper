package com.beekeeper.model.agent;

public class AgentStateSnapshot
{
	public int agentID;
	public double jhAmount;
	public String taskName;
	
	public AgentStateSnapshot(WorkingAgent agent)
	{
		agentID = agent.ID;
		jhAmount = agent.getHJ();
		taskName = agent.getTaskName();
	}

}
