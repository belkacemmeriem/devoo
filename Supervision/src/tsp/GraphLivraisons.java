package tsp;

import java.util.ArrayList;
import java.util.Calendar;

import dijkstra.*;
import model.*;
import Exception.*;

public class GraphLivraisons implements Graph {

	private final int timeLimit=10000;//milliseconds
	private final int timeLimitTotal=60000;//milliseconds indique le temps total accordé pour trouver une solution optimale
	private Chemin[][] listeChemins;
	private int[][] listeCosts;
	private FeuilleDeRoute feuilleDeRoute;
	private int maxArcCost=-1;
	private int minArcCost=-1;
	private int nbVertices=0;
	
	public GraphLivraisons(FeuilleDeRoute f){
		feuilleDeRoute=f;
	}
	
	/**
	 * Crée un graphe utilisable par la classe TSP. Les sommets des graphes sont les livraisons à effectuer.
	 * Les arcs sont les plus courts chemins entre les livraisons. L'entrepôt est considéré comme une livraison.
	 * @return void.
	 * @author arnaud MDM
	 * @throws GraphException 
	 */
	public void createGraph() throws GraphException{
		//on ajoute l'entrepot au livraisons pour connaitre le nombre de sommets
		nbVertices=feuilleDeRoute.getAllDeliveries().size();
		
		//si aucune livraison n'a été saisie
		if(nbVertices==1)
		{
			throw new GraphException(GraphException.NO_DELIVERIES);
		}
		
		listeChemins=new Chemin[nbVertices][nbVertices];
		listeCosts=new int[nbVertices][nbVertices];
		
		//index de la liste des schedules de feuilleDeRoute
		int indexSchedule = 0;
		
		//solveDijkstra pour le point de départ à l'entrepot
		//l'entrepot est stockée comme dernier point de livraison
		Node depart=feuilleDeRoute.getAllDeliveries().get(nbVertices-1).getDest();
		ArrayList<Node> listeArrivees=new ArrayList<Node>();
		
		//on ne prend pas un schedule qui ne possède aucune livraison
		while(feuilleDeRoute.getTimeZones().get(indexSchedule).getDeliveries().size()==0)
		{
			indexSchedule++;
		}
		
		for(Delivery d : feuilleDeRoute.getTimeZones().get(indexSchedule).getDeliveries())
		{
			listeArrivees.add(d.getDest());
		}
		
		//on fait appel à la classe static Dijkstra pour calculer les plus courts chemins
		ArrayList<Chemin> lC=Dijkstra.solve(feuilleDeRoute.getZoneGeo(), depart, listeArrivees);
		
		for(int i=0;i<lC.size();i++)
		{
			//on met à jour maxArcCost et minArcCost
			if(maxArcCost<lC.get(i).getDuration()){
				maxArcCost=(int) lC.get(i).getDuration();
				if(minArcCost==-1){
					minArcCost=maxArcCost;
				}
			}
			else if(minArcCost>lC.get(i).getDuration()){
				minArcCost=(int) lC.get(i).getDuration();
			}
			
			//on ajoute aux tableaux les chemins et couts trouvés
			listeChemins[0][i+1]=lC.get(i);
			listeCosts[0][i+1]=(int) lC.get(i).getDuration();
		}
		
		//solveDijkstra pour les livraions
		//inc permet de connaitre à quel index la première livraison d'un schedule appartient pour listeChemins et listeCosts
		int inc=1;
		
		//indexSchedule2 est l'index du schedule suivant à indexSchedule ayant des livraisons
		int indexSchedule2=indexSchedule+1;
		
		//tant qu'on est pas à la fin de la liste des schedules
		while(indexSchedule<feuilleDeRoute.getTimeZones().size()-1)
		{
			Schedule s1=feuilleDeRoute.getTimeZones().get(indexSchedule);
			
			//on prend toutes les livraions d'un schedule
			for(int j=0;j<s1.getDeliveries().size();j++)
			{
				depart=s1.getDeliveries().get(j).getDest();
				listeArrivees.clear();
				
				//on ajoute comme point d'arrivée les livraions du même schedule que la livraison de départ
				for(int k=0;k<s1.getDeliveries().size();k++)
				{
					if(k!=j)
					{
						listeArrivees.add(s1.getDeliveries().get(k).getDest());
					}
				}
				
				//on cherche le schedule suivant indexSchedule possédant des livraisons 
				while(indexSchedule2<feuilleDeRoute.getTimeZones().size() && feuilleDeRoute.getTimeZones().get(indexSchedule2).getDeliveries().size()==0)
				{
					indexSchedule2++;
				}
				
				Schedule s2=feuilleDeRoute.getTimeZones().get(indexSchedule2);
				for(Delivery d : s2.getDeliveries())
				{
					listeArrivees.add(d.getDest());
				}
				
				lC=Dijkstra.solve(feuilleDeRoute.getZoneGeo(), depart, listeArrivees);
				
				//l'offset permet de remplir correctement listeChemins et listeCosts
				int offset=0;
				
				//on remplit listeChemins et listeCosts
				for(int m=0;m<lC.size();m++)
				{
					if(maxArcCost<lC.get(m).getDuration())
					{
						maxArcCost=(int) lC.get(m).getDuration();
						if(minArcCost==-1)
						{
							minArcCost=maxArcCost;
						}
					}
					else if(minArcCost>lC.get(m).getDuration())
					{
						minArcCost=(int) lC.get(m).getDuration();
					}
					
					if(inc+j==inc+m)
						offset=1;
					
					//si la destination du chemin n'est pas l'entrepôt
					if((inc+m+offset)<nbVertices)
					{
						listeChemins[inc+j][inc+m+offset]=lC.get(m);
						listeCosts[inc+j][inc+m+offset]=(int) lC.get(m).getDuration();
					}
					else
					{
						listeChemins[inc+j][0]=lC.get(m);
						listeCosts[inc+j][0]=(int) lC.get(m).getDuration();
					}
				}
			}
			inc+=s1.getDeliveries().size();
			indexSchedule=indexSchedule2;
			indexSchedule2++;
		}
	}
	
	/**
	 * Calcule un itinéraire par le biais de la classe TSP.
	 * La fonction doit être appelée après avoir appelée <code>createGraphe</code>
	 * @return ArrayList<Delivery>. Il s'agit d'une liste ordonnée de livraison. La dernière livraison est l'entrepôt
	 * @author arnaud MDM
	 * @throws GraphException 
	 */
	public ArrayList<Delivery> calcItineraire() throws GraphException{
		TSP tsp=new TSP();
		int bound=(nbVertices+1)*maxArcCost;
		SolutionState retour;
		
		long timeStart=Calendar.getInstance().getTimeInMillis();
		
		retour=tsp.solve(timeLimit, bound, this);
		/*
		while((retour=tsp.solve(timeLimit, bound, this))==SolutionState.SOLUTION_FOUND && (Calendar.getInstance().getTimeInMillis()-timeStart)< timeLimitTotal)
		{
			bound=tsp.getTotalCost();
		}
		*/
		
		if(retour==SolutionState.INCONSISTENT)
		{
			throw new GraphException(GraphException.INCONSISTENT);
		}
		else if(retour==SolutionState.NO_SOLUTION_FOUND)
		{
			throw new GraphException(GraphException.NO_SOLUTION_FOUND);
		}
		
		int[] tabPos=tsp.getPos();
		int[] tabNext=tsp.getNext();
		ArrayList<Delivery> itineraire=new ArrayList<Delivery>();
		for(int i=0;i<tabPos.length-1;i++)
		{
			feuilleDeRoute.getAllDeliveries().get(tabPos[i+1]-1).setPathToDest(listeChemins[tabPos[i]][tabPos[i+1]]);
			itineraire.add(feuilleDeRoute.getAllDeliveries().get(tabPos[i+1]-1));
		}
		feuilleDeRoute.getAllDeliveries().get(feuilleDeRoute.getAllDeliveries().size()-1)
			.setPathToDest(listeChemins[tabPos[tabPos.length-1]][tabPos[0]]);
		itineraire.add(feuilleDeRoute.getAllDeliveries().get(feuilleDeRoute.getAllDeliveries().size()-1));
		return itineraire;
	}
	
	@Override
	public void display() {
		// TODO Auto-generated method stub

	}

	@Override
	public int getMaxArcCost() {
		return maxArcCost;
	}

	@Override
	public int getMinArcCost() {
		return minArcCost;
	}

	@Override
	public int getNbVertices() {
		return nbVertices;
	}

	@Override
	public int getCost(int i, int j) throws ArrayIndexOutOfBoundsException {
		return listeCosts[i][j];
	}

	@Override
	public int[][] getCost() {
		return listeCosts;
	}

	@Override
	public ArrayList<Integer> getSucc(int i)
			throws ArrayIndexOutOfBoundsException {
		ArrayList<Integer> listeInteger=new ArrayList<Integer>();
		for(int j=0;j<nbVertices;j++)
		{
			if(listeCosts[i][j]!=0)
				listeInteger.add(j);
		}
		return listeInteger;
	}
	
	public Chemin[][] getListeChemin()
	{
		return listeChemins;
	}

}
