package model;

public class Delivery
{
	protected
	Node dest;



	Schedule schedule;
	Chemin pathToDest;
	
	int heurePrevue;
	boolean retardPrevu;
	
	
	public boolean isRetardPrevu()
	{
		return retardPrevu;
	}

	public void setRetardPrevu(boolean retardPrevu)
	{
		this.retardPrevu = retardPrevu;
	}
	
	public int getHeurePrevue()
	{
		return heurePrevue;
	}

	public void setSchedule(Schedule schedule)
	{
		this.schedule = schedule;
	}
	
	public void setHeurePrevue(int heurePrevue)
	{
		this.heurePrevue = heurePrevue;
	}


	public Node getDest()
	{
		return dest;
	}


	public Schedule getSchedule()
	{
		return schedule;
	}


	public Chemin getPathToDest()
	{
		return pathToDest;
	}


	public Delivery(Node destination, Schedule schedule)
	{
		super();
		this.dest = destination;
		this.schedule = schedule;
	}


	public void setPathToDest(Chemin pathToDest)
	{
		this.pathToDest = pathToDest;
	}
	
	
}
