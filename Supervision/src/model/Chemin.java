package model;

import java.util.ArrayList;

public class Chemin {
	protected
	
	ArrayList<Node> trajectory;
	Node start;
	Node target;
	float duration;
	
	public Chemin(ArrayList<Node> contenu, float duration)
	{
		super();
		this.trajectory = contenu;
		this.start = contenu.get(0);
		this.target = contenu.get(contenu.size()-1);
		this.duration = duration;
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
	public float getDuration()
	{
		return duration;
	}


}
