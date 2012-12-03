package model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;

import tsp.GraphLivraisons;

public class FeuilleDeRoute
{
	protected
	ArrayList<Schedule> timeZones;
	
	protected ZoneGeo zoneGeo;

	public FeuilleDeRoute(ArrayList<Schedule> timeZones, ZoneGeo zoneGeo)
	{
		super();
		this.timeZones = timeZones;
		this.zoneGeo = zoneGeo;
		
		//ajout d'un schedule réservé au retour à l'entrepot
		Schedule retourSch = new Schedule(0, 1440, Color.BLACK);
		Delivery entrepot = new Delivery(zoneGeo.getWarehouse(), retourSch);
		retourSch.appendDelivery(entrepot);
		timeZones.add(retourSch);
		
	}

	public ZoneGeo getZoneGeo() {
		return zoneGeo;
	}
	
	public ArrayList<Schedule> getTimeZones()
	{
		return timeZones;
	}
	
	public ArrayList<Node> getFullPath()
	{
		ArrayList<Node> fullPath = new ArrayList<Node>();
		for (Schedule s : timeZones)
		{
			for (Delivery d : s.getDeliveries())
			{
				 fullPath.addAll(d.getPathToDest().getTrajectory());
				 fullPath.add(d.getDest());
			}
		}
		return fullPath;
	}
	
	public void computeWithTSP()
	{
		
		GraphLivraisons gl = new GraphLivraisons(this);
		gl.createGraph();
		ArrayList<Delivery> result = gl.calcItineraire();
		
		// pour chaque Schedule, on cherche les deliveries concernées
		for (Schedule sch : timeZones)
		{
			LinkedList<Delivery> newDelivs = new LinkedList<Delivery>();
			for (Delivery d : result)
			{
				if (d.schedule == sch)
				{
					//on les insère, dans l'ordre, dans la nouvelle liste du Schedule
					
					newDelivs.add(d);
					
				}
			}
			sch.setDeliveries(newDelivs);
		}
		computeArrivalTimes();
	}

	
	protected void computeArrivalTimes()
	{
		int theTime = timeZones.get(0).getStartTime();
		for (Schedule sch : timeZones)
		{
			for (Delivery d : sch.getDeliveries())
			{
				theTime+= d.getPathToDest().getDuration();
				d.setHeurePrevue(theTime);
				d.setRetardPrevu(theTime > sch.getEndTime());	//si arrive hors schedule
			}
		}
	}
	
	public void generateReport(File path) throws IOException
	{
	    PrintWriter writer;
	    int n = 5;

	    writer =  new PrintWriter(new BufferedWriter(new FileWriter(path)));
	   
	    writer.println("Rapport de la feuille de route pour le livreur.");
	    writer.println("La tournee est divisee en "+timeZones.size()+" creneaux.");
	    for(Schedule s: timeZones)
	    {
		    writer.println("Le prochain creneau s'�tend de" + Schedule.timeToString(s.startTime)+  " a "+Schedule.timeToString(s.endTime));
		    writer.println("Dans ce creneau il y a "+s.getDeliveries().size()+ " livraisons a faire." );
		    for(Delivery d : s.getDeliveries())
		    {
			    writer.println("La prochaine livraison aura lieu a "+d.getDest()+" ." );
			    writer.println("Arrivee prevue a:"+ d.getHeurePrevue());
			    Integer previous=d.getPathToDest().getTrajectory().get(0).getID();
		    	for(Arc a :d.getPathToDest().getArcs())
		    	{
		    		writer.print("Passer par le point :"+ a.getDest().getID());
					writer.println(" en empruntant la rue "+a.getName()+".");
		    	}
			    writer.println("Quand vous serez arrive, vous disposez de 15 minutes pour livrer le colis au client.");
		    }
	    }

	    writer.close();
	}

	public ArrayList<Delivery> getAllDeliveries()
	{
		ArrayList<Delivery> retour = new ArrayList<Delivery>();
		for (Schedule sch : timeZones)
		{
			for (Delivery d : sch.getDeliveries())
			{
				retour.add(d);
			}
		}
		return retour;
	}
	
	// ajoute un node dans un schedule donné
	// utile seulement à l'init, avant le premier appel de computeWithTSP
	public void addNode(Node node, Schedule schedule)
	{
		Delivery deliv = new Delivery(node, schedule);
		schedule.appendDelivery(deliv);
	}
	
	/*
	 * WORK IN PROGRESS
	protected void insertNode(Node inserted, Delivery reference, boolean before)
	{
		for (Schedule sch : timeZones)
		{
			LinkedList<Delivery> schDelivs = sch.getDeliveries();
			if (schDelivs.contains(reference))
			{
				int insertIndex = schDelivs.indexOf(o)
				schDelivs.add(index, element)
			}
		}
	}
	*/
}
