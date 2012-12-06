package test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

import Exception.GraphException;
import tsp.*;
import model.*;

public class GraphLivraisonsTest {
	GraphLivraisons graphLivraisons;
	static FeuilleDeRoute feuilleDeRoute;
	
	@BeforeClass
	public static void init(){
		ZoneGeo zoneGeo=new ZoneGeo();
		
		Node node;
		for(int i=0;i<6;i++)
		{
			node=new Node(0,0,i);
			zoneGeo.addNode(node);
		}
		zoneGeo.addArc(0, 1, 1, 10, "temp");
		zoneGeo.addArc(0, 2, 1, 20, "temp");
		zoneGeo.addArc(0, 3, 1, 20, "temp");
		zoneGeo.addArc(1, 2, 1, 10, "temp");
		zoneGeo.addArc(1, 3, 1, 10, "temp");
		zoneGeo.addArc(1, 4, 1, 20, "temp");
		zoneGeo.addArc(1, 5, 1, 20, "temp");
		zoneGeo.addArc(2, 1, 1, 10, "temp");
		zoneGeo.addArc(2, 3, 1, 10, "temp");
		zoneGeo.addArc(2, 4, 1, 20, "temp");
		zoneGeo.addArc(2, 5, 1, 20, "temp");
		zoneGeo.addArc(3, 1, 1, 10, "temp");
		zoneGeo.addArc(3, 2, 1, 10, "temp");
		zoneGeo.addArc(3, 4, 1, 20, "temp");
		zoneGeo.addArc(3, 5, 1, 10, "temp");
		zoneGeo.addArc(4, 5, 1, 10, "temp");
		zoneGeo.addArc(4, 0, 1, 10, "temp");
		zoneGeo.addArc(5, 4, 1, 10, "temp");
		zoneGeo.addArc(5, 4, 1, 20, "temp");
		
		//ajout de l'entrepot
		zoneGeo.setWarehouse(0);
		
		
		
		ArrayList<Schedule> listeSchedules=new ArrayList<Schedule>();
		Schedule schedule1=new Schedule(600,800,Color.GREEN);
		listeSchedules.add(schedule1);
		Schedule schedule2=new Schedule(900,1200,Color.RED);
		listeSchedules.add(schedule2);		
		
		feuilleDeRoute=new FeuilleDeRoute(listeSchedules, zoneGeo);
		
		feuilleDeRoute.addNode(zoneGeo.getNode(1), schedule1);
		feuilleDeRoute.addNode(zoneGeo.getNode(2), schedule1);
		feuilleDeRoute.addNode(zoneGeo.getNode(3), schedule1);
		feuilleDeRoute.addNode(zoneGeo.getNode(4), schedule2);
		feuilleDeRoute.addNode(zoneGeo.getNode(5), schedule2);
	}
	
	@Test
	public void testCreateGraph(){
		graphLivraisons=new GraphLivraisons(feuilleDeRoute);
		try {
			graphLivraisons.createGraph();
		} catch (GraphException e) {
			assertTrue(false);
		}
		
		int[][]listeCosts=graphLivraisons.getCost();
		assertEquals(10, listeCosts[0][1]);
		assertEquals(20, listeCosts[0][2]);
		assertEquals(20, listeCosts[0][3]);
		assertEquals(10, listeCosts[1][2]);
		assertEquals(10, listeCosts[1][3]);
		assertEquals(20, listeCosts[1][4]);
		assertEquals(20, listeCosts[1][5]);
		assertEquals(10, listeCosts[2][1]);
		assertEquals(10, listeCosts[2][3]);
		assertEquals(20, listeCosts[2][4]);
		assertEquals(20, listeCosts[2][5]);
		assertEquals(10, listeCosts[3][1]);
		assertEquals(10, listeCosts[3][2]);
		assertEquals(20, listeCosts[3][4]);
		assertEquals(10, listeCosts[3][5]);
		assertEquals(10, listeCosts[4][5]);
		assertEquals(10, listeCosts[4][0]);
		assertEquals(10, listeCosts[5][4]);
		assertEquals(20, listeCosts[5][0]);
	}
	
	@Test
	public void testCalcItineraire()
	{
		graphLivraisons=new GraphLivraisons(feuilleDeRoute);
		try {
			graphLivraisons.createGraph();

			ArrayList<Delivery> listeLivraisons;
			listeLivraisons = graphLivraisons.calcItineraire();

			assertEquals(listeLivraisons.size(),6);
			assertEquals(listeLivraisons.get(0).getDest().getID().intValue(),1);
			assertEquals(listeLivraisons.get(1).getDest().getID().intValue(),2);
			assertEquals(listeLivraisons.get(2).getDest().getID().intValue(),3);
			assertEquals(listeLivraisons.get(3).getDest().getID().intValue(),5);
			assertEquals(listeLivraisons.get(4).getDest().getID().intValue(),4);
			assertEquals(listeLivraisons.get(5).getDest().getID().intValue(),0);
		} catch (GraphException e) {
			assertTrue(false);
		}		
		
	}
}
