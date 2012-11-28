package model;

import java.util.ArrayList;

public class Chemin {
	protected
	
	ArrayList<Node> contenu;
	Node noeudDepart;
	Node noeudArrivee;
	
	public Node getNoeudDepart()
	{
		return noeudDepart;
	}
	public Node getNoeudArrivee()
	{
		return noeudArrivee;

	}
	public ArrayList<Node> getContenu()
	{
		return contenu;

	}
	public void setContenu(ArrayList<Node> contenu) 
	{
		this.contenu = contenu;
	}
	public void setNoeudDepart(Node noeudDepart) {
		this.noeudDepart = noeudDepart;
	}
	public void setNoeudArrivee(Node noeudArrivee) {
		this.noeudArrivee = noeudArrivee;
	}

}
