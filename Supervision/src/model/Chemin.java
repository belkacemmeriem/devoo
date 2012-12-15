package model;

import java.util.ArrayList;

public class Chemin {
	protected
	
	ArrayList<Node> trajectory;
	ArrayList<Arc> arcs = new ArrayList<Arc>();

	Node start;
	Node target;
	double duration;
	
	public Chemin(ArrayList<Node> contenu, double duration)
	{
		super();
		this.trajectory = contenu;
		this.start = contenu.get(0);
		this.target = contenu.get(contenu.size()-1);
		this.duration = duration;
		for(int i=1; i<trajectory.size();i++)
		{
			for(Arc j : trajectory.get(i).getInArcs())
			{
				if(j.getOrigin() ==trajectory.get(i-1))
				{
					arcs.add(j);

				}
			}
		}
	}
	
	public Node getNoeudDepart()
	{
		return start;
	}
	
	public Node getNoeudArrivee()
	{
		return target;

	}
	public ArrayList<Node> getTrajectory()
	{
		return trajectory;

	}
	public double getDuration()
	{
		return duration;
	}

	public ArrayList<Arc> getArcs() {
		return arcs;
	}


}
