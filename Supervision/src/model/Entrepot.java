package model;

public class Entrepot {

	protected Node adresse;
	
	protected final int heureMinDepart=480;//conversion de l'heure et minutes en minutes
	protected final int heureMaxArrive=1080;
	
	protected int heureDepart;
	protected int heureArrivee;
	
	public Entrepot(Node n)
	{
		adresse=n;
	}

	public Node getAdresse() {
		return adresse;
	}
	
	
}
