package model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;

import dijkstra.Dijkstra;

import tsp.GraphLivraisons;

public class FeuilleDeRoute
{
	protected
	ArrayList<Schedule> timeZones;
	ZoneGeo zoneGeo;
	final int DUREE_LIVRAISON = 15;
	EtatFDR etat = EtatFDR.INIT;
	Delivery warehouseDelivery;
	
	
	
	public FeuilleDeRoute(ArrayList<Schedule> timeZones, ZoneGeo zoneGeo)
	{
		super();
		this.timeZones = timeZones;
		this.zoneGeo = zoneGeo;
		
		//ajout d'un schedule réservé au retour à l'entrepot
		Schedule retourSch = new Schedule(0, 1440, Color.BLACK);
		warehouseDelivery = new Delivery(zoneGeo.getWarehouse(), retourSch);
		retourSch.appendDelivery(warehouseDelivery);
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
	
	public Delivery getDelivery(Node n) {
		for (Delivery d : getAllDeliveries()) {
			if (d.getDest().getID() == n.getID()) {
				return d;
			}
		}
		return null;
	}

	public EtatFDR getEtat()
	{
		return etat;
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
		etat = EtatFDR.OPTIM;
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
				theTime+= DUREE_LIVRAISON;
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

	// ajoute un node dans un schedule donné
	// utile seulement à l'init, avant le premier appel de computeWithTSP
	public void addNode(Node node, Schedule schedule) throws RuntimeException
	{
		if (etat != EtatFDR.INIT)
		{
			throw new RuntimeException("trying to FeuilleDeRoute.addNode() while already initialized");
		}
		Delivery deliv = new Delivery(node, schedule);
		schedule.appendDelivery(deliv);
	}
	
	public void delNode(Node node) throws RuntimeException
	{
		if (etat != EtatFDR.INIT)
		{
			throw new RuntimeException("trying to FeuilleDeRoute.delNode() while already initialized");
		}
		Delivery deliv = getDelivery(node);
		deliv.getSchedule().removeDelivery(deliv);
	}
	
	public void insertNodeBefore(Node inserted, Delivery reference)
	{
		insertNode(inserted, reference, true);
	}
	
	public void insertNodeAfter(Node inserted, Delivery reference)
	{
		insertNode(inserted, reference, false);
	}
	
	
	
	protected void insertNode(Node inserted, Delivery reference, boolean before)
	{
		//recherche du bon schedule
		for (Schedule sch : timeZones)
		{
			LinkedList<Delivery> schDeliveries = sch.getDeliveries();

			if (schDeliveries.contains(reference))	//si le schedule contient la livraison de reference
			{
				
				
			//insertion du noeud dans le schedule
				Delivery newDelivery = new Delivery(inserted, sch);
				int insertIndex;	//futur index du noeud inséré
				if (before)
				{
					insertIndex = schDeliveries.indexOf(reference);
				}
				else
				{
					insertIndex = schDeliveries.indexOf(reference) +1;
				}
				sch.insert(newDelivery, insertIndex);
				
				
				
			//re-calcul du chemin précedent
				recalcPathTo(newDelivery);
			//re-calcul du chemin suivant
				recalcPathTo(nextDelivery(newDelivery));
				
				etat = EtatFDR.MODIF;
			}
		}
	}
	
	
	
	protected Delivery previousDelivery(Delivery delivery)
	{
		int idxSch = -1;
		Delivery returned = null;
		for (Schedule sch : timeZones)
		{
			idxSch++;
			LinkedList<Delivery> schDeliveries = sch.getDeliveries();

			if (schDeliveries.contains(delivery))	//si le schedule contient la livraison de reference
			{
				int idx = schDeliveries.indexOf(delivery);
				if (idx == 0)	//si premier element du schedule
				{
					if (idxSch == 0)						//et premier schedule
					{
						returned = warehouseDelivery;
					}
					else									
					{
						returned = timeZones.get(idxSch-1).getDeliveries().getLast();	//dernier node du sch précédent
					}
				}
				else
				{
					returned = schDeliveries.get(idx-1);
				}
			}
		}
		return returned;
	}
	
	
	protected Delivery nextDelivery(Delivery delivery)
	{
		int idxSch = -1;
		Delivery returned = null;
		for (Schedule sch : timeZones)
		{
			idxSch++;
			LinkedList<Delivery> schDeliveries = sch.getDeliveries();

			if (schDeliveries.contains(delivery))	//si le schedule contient la livraison de reference
			{
				int idx = schDeliveries.indexOf(delivery);
				if (delivery == schDeliveries.getLast())	//si dernier element du schedule
				{
					if (idxSch == 0)						//et dernier schedule
					{
						returned = warehouseDelivery;
					}
					else									
					{
						returned = timeZones.get(idxSch+1).getDeliveries().getFirst();	//dernier node du sch précédent
					}
				}
				else
				{
					returned = schDeliveries.get(idx+1);
				}
			}
		}
		return returned;
	}



	protected void recalcPathTo(Delivery delivery)
	{
		//recherche chemin
		ArrayList<Node> singleton = new ArrayList<Node>();
		singleton.add(delivery.getDest());
		ArrayList<Chemin> result = Dijkstra.solve(zoneGeo, previousDelivery(delivery).getDest(), singleton);
		delivery.pathToDest = result.get(0);
	}

}
