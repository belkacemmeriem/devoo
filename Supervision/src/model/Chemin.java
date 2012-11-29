package model;

import java.util.ArrayList;

public class Chemin {
	protected
	
	ArrayList<Node> trajectory;
	Node start;
	Node target;
	float duration;
	
	public Chemin(ArrayList<Node> contenu, Node noeudDepart, Node noeudArrivee,
			float duration)
	{
		super();
		this.trajectory = contenu;
		this.start = noeudDepart;
		this.target = noeudArrivee;
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
