package test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;
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
		zoneGeo.addArc(0, 2, 1, 30, "temp");
		zoneGeo.addArc(0, 3, 1, 30, "temp");
		zoneGeo.addArc(1, 2, 1, 10, "temp");
		zoneGeo.addArc(1, 3, 1, 10, "temp");
		zoneGeo.addArc(1, 4, 1, 30, "temp");
		zoneGeo.addArc(1, 5, 1, 30, "temp");
		zoneGeo.addArc(2, 1, 1, 10, "temp");
		zoneGeo.addArc(2, 3, 1, 10, "temp");
		zoneGeo.addArc(2, 4, 1, 30, "temp");
		zoneGeo.addArc(2, 5, 1, 30, "temp");
		zoneGeo.addArc(3, 1, 1, 10, "temp");
		zoneGeo.addArc(3, 2, 1, 10, "temp");
		zoneGeo.addArc(3, 4, 1, 30, "temp");
		zoneGeo.addArc(3, 5, 1, 10, "temp");
		zoneGeo.addArc(4, 5, 1, 10, "temp");
		zoneGeo.addArc(4, 0, 1, 10, "temp");
		zoneGeo.addArc(5, 4, 1, 10, "temp");
		zoneGeo.addArc(5, 4, 1, 30, "temp");
		
		//ajout de l'entrepot
		zoneGeo.setWarehouse(0);
		
		
		/*
		ArrayList<Schedule> listeSchedules=new ArrayList<Schedule>();
		Schedule schedule=new Schedule(600,720,Color.BLACK);
		Delivery delivery=new Delivery(zoneGeo.getNode(1),schedule);
		schedule.getDeliveries().add(delivery);
		delivery=new Delivery(zoneGeo.getNode(2), schedule);
		schedule.getDeliveries().add(delivery);
		delivery=new Delivery(zoneGeo.getNode(3), schedule);
		schedule.getDeliveries().add(delivery);
		listeSchedules.add(schedule);
		
		schedule=new Schedule(720,840,Color.BLACK);
		delivery=new Delivery(zoneGeo.getNode(4), schedule);
		schedule.getDeliveries().add(delivery);
		delivery=new Delivery(zoneGeo.getNode(5), schedule);
		schedule.getDeliveries().add(delivery);
		listeSchedules.add(schedule);*/
		
		
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
		graphLivraisons.createGraph();
		
		int[][]listeCosts=graphLivraisons.getCost();
		assertEquals(10, listeCosts[0][1]);
		assertEquals(30, listeCosts[0][2]);
		assertEquals(30, listeCosts[0][3]);
		assertEquals(10, listeCosts[1][2]);
		assertEquals(10, listeCosts[1][3]);
		assertEquals(30, listeCosts[1][4]);
		assertEquals(30, listeCosts[1][5]);
		assertEquals(10, listeCosts[2][1]);
		assertEquals(10, listeCosts[2][3]);
		assertEquals(30, listeCosts[2][4]);
		assertEquals(30, listeCosts[2][5]);
		assertEquals(10, listeCosts[3][1]);
		assertEquals(10, listeCosts[3][2]);
		assertEquals(30, listeCosts[3][4]);
		assertEquals(10, listeCosts[3][5]);
		assertEquals(10, listeCosts[4][5]);
		assertEquals(10, listeCosts[4][0]);
		assertEquals(10, listeCosts[5][4]);
		assertEquals(30, listeCosts[5][0]);
	}
}
