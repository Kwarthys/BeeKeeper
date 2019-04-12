package com.beekeeper.model.agent;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import com.beekeeper.model.tasks.Task;

public class AdultBee extends EmptyBee
{
	protected ArrayList<Task> taskList = new ArrayList<>();	
	
	public AdultBee()
	{
		fillTaskList();
		this.position = new Point2D.Double(Math.random()*300, Math.random()*300);
	}

	@Override
	protected void fillTaskList()
	{
		Task randomTask = new Task() {			
			@Override
			public void execute() {
				AdultBee.this.randomMove();
			}
		};		
		randomTask.energyCost = 0.1;
		
		taskList.add(randomTask);
	}
	
	public void randomMove()
	{
		this.move(Math.random()*2-1, Math.random()*2-1);
	}

	@Override
	public void live() {
		randomMove();	
	}

	@Override
	public void move(double dx, double dy) {
		this.position.setLocation(this.position.getX() + dx, this.position.getY() + dy);	
	}
}
