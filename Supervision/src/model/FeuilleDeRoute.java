package model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.awt.Color;
import java.util.ArrayList;

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
		
		//ajout d'un schedule r√©serv√© au retour √† l'entrepot
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
		
		// pour chaque Schedule, on cherche les deliveries concern√©es
		for (Schedule sch : timeZones)
		{
			ArrayList<Delivery> newDelivs = new ArrayList<Delivery>();
			for (Delivery d : result)
			{
				if (d.schedule == sch)
				{
					//on les ins√®re, dans l'ordre, dans la nouvelle liste du Schedule
					
					newDelivs.add(d);
					
				}
			}
			sch.setDeliveries(newDelivs);
		}
		computeArrivalTimes();
	}

	
	protected void computeArrivalTimes()
	{
		
	}
	
	public void generateReport() throws IOException
	{
	    PrintWriter writer;
	    int n = 5;

	    writer =  new PrintWriter(new BufferedWriter(new FileWriter("rapport.txt")));
	   
	    writer.println("Rapport de la feuille de route pour le livreur.");
	    writer.println("La tournée est divisée en "+timeZones.size()+" créneaux.");
	    for(Schedule s: timeZones)
	    {
		    writer.println("Le prochain créneau s'étend de" + Schedule.timeToString(s.startTime)+  " a "+Schedule.timeToString(s.endTime));
		    writer.println("Dans ce créneau il y a "+s.getDeliveries().size()+ " livraisons à faire." );
		    for(Delivery d : s.getDeliveries())
		    {
			    writer.println("La prochaine livraison aura lieu a "+d.getDest()+" ." );
			    writer.println("Arrivee prevue a:"+ d.getHeurePrevue());
			    Integer previous=d.getPathToDest().getTrajectory().get(0).getID();
		    	for(Node a :d.getPathToDest().getTrajectory())
		    	{
		    		writer.print("Passer par le point :"+ a.getID());
		    		for(Arc i :a.getInArcs())
		    		{
		    			if(i.getOrigin().getID()==previous)
		    			{
						    writer.print(" en empruntant la rue "+i.getName());

		    			}
		    		}

		    	}
			    writer.println("Quand vous serez arrivé, vous disposez de 15 minutes pour livrer le colis au client.");

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
}
