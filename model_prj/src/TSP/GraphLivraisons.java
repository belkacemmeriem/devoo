package TSP;

import java.util.ArrayList;
import dijkstra.*;
import model.*;

public class GraphLivraisons implements Graph {

	private final int timeLimit=10000;//milliseconds
	private Chemin[][] listeChemins;
	private int[][] listeCosts;
	private FeuilleDeRoute feuilleDeRoute;
	private int maxArcCost=-1;
	private int minArcCost=-1;
	private int nbVertices=0;
	
	public GraphLivraisons(FeuilleDeRoute f){
		feuilleDeRoute=f;
	}
	
	public void createGraph(){
		nbVertices=feuilleDeRoute.getFullPath().size()+1;
		listeChemins=new Chemin[nbVertices][nbVertices];
		listeCosts=new int[nbVertices][nbVertices];
		
		//solveDijkstra pour le point de départ à l'entrepot
		Node depart=feuilleDeRoute.getEntrepot().getAdresse();
		ArrayList<Node> listeArrivees=new ArrayList<Node>();
		for(Delivery d : feuilleDeRoute.getTimeZones().get(0).getDeliveries())
		{
			listeArrivees.add(d.getDest());
		}
		
		ArrayList<Chemin> lC=Dijkstra.solve(feuilleDeRoute.getZoneGeo(), depart, listeArrivees);
		for(int i=0;i<lC.size();i++)
		{
			if(maxArcCost<lC.get(i).getDuration()){
				maxArcCost=(int) lC.get(i).getDuration();
				if(minArcCost==-1){
					minArcCost=maxArcCost;
				}
			}
			else if(minArcCost>lC.get(i).getDuration()){
				minArcCost=(int) lC.get(i).getDuration();
			}
			
			listeChemins[0][i+1]=lC.get(i);
			listeCosts[0][i+1]=(int) lC.get(i).getDuration();
		}
		
		//solveDijkstra pour les livraions
		int inc=1;
		for(int i=0;i<feuilleDeRoute.getTimeZones().size();i++)
		{
			Schedule s1=feuilleDeRoute.getTimeZones().get(i);
			
			for(int j=0;j<s1.getDeliveries().size();j++)
			{
				depart=s1.getDeliveries().get(0).getDest();
				listeArrivees.clear();
				
				for(int k=0;k<s1.getDeliveries().size();k++)
				{
					if(k!=j){
						listeArrivees.add(s1.getDeliveries().get(k).getDest());
					}
				}
				
				//si les livraisons ne font pas partie de la dernière plage horaire
				if(i<feuilleDeRoute.getTimeZones().size()-1){
					Schedule s2=feuilleDeRoute.getTimeZones().get(i+1);
					for(Delivery d : s2.getDeliveries())
					{
						listeArrivees.add(d.getDest());
					}
				}
				else{
					listeArrivees.add(feuilleDeRoute.getEntrepot().getAdresse());
				}
				
				lC=Dijkstra.solve(feuilleDeRoute.getZoneGeo(), depart, listeArrivees);
				int offset=0;
				for(int m=0;m<lC.size();m++)
				{
					if(maxArcCost<lC.get(m).getDuration()){
						maxArcCost=(int) lC.get(m).getDuration();
						if(minArcCost==-1){
							minArcCost=maxArcCost;
						}
					}
					else if(minArcCost>lC.get(m).getDuration()){
						minArcCost=(int) lC.get(m).getDuration();
					}
					
					
					if(inc+j==inc+m)
						offset=1;
					
					listeChemins[inc+j][inc+m+offset]=lC.get(m);
					listeCosts[inc+j][inc+m+offset]=(int) lC.get(m).getDuration();
				}
			}
			inc+=s1.getDeliveries().size();
		}
	}
	
	
	public void calcItineraire(){
		TSP tsp=new TSP();
		int bound=nbVertices*maxArcCost;
		SolutionState retour;
		while((retour=tsp.solve(timeLimit, bound, this))==SolutionState.SOLUTION_FOUND)
		{
			bound=tsp.getTotalCost();
		}
		int[] tabPos=tsp.getPos();
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

}
