package test;
import static org.junit.Assert.*;

import java.util.ArrayList;

import model.Arc;
import model.Chemin;
import model.Node;
import model.ZoneGeo;

import org.junit.BeforeClass;
import org.junit.Test;

import dijkstra.Dijkstra;


public class DijkstraTest {
	static ZoneGeo uneZone;
	static Node source;
	static ArrayList<Node> eng;


	@BeforeClass
	public static void init()
	{
		//Creation d'un graphe 
				Node n1 = new Node(0,0,0);
				Node n2 = new Node(0,1,1);
				Node n3 = new Node(1,0,2);
				Node n4 = new Node(1,1,3);
				Node n5 = new Node(20,20,4);
				
				Arc a1 = new Arc(n1,n2,10,3,"n1");
				Arc a2 = new Arc(n1,n3,10,2,"n2");
				Arc a3 = new Arc(n2,n4,7,7,"n3");
				Arc a4 = new Arc(n3,n5,10,40,"n4");
				
				n1.addOutArc(a1);
				n2.addInArc(a1);
				n1.addOutArc(a2);
				n3.addInArc(a2);
				n2.addOutArc(a3);
				n4.addInArc(a3);
				n3.addOutArc(a4);
				n5.addInArc(a4);
				
				uneZone= new ZoneGeo();
				uneZone.addNode(n1);
				uneZone.addNode(n2);
				uneZone.addNode(n3);
				uneZone.addNode(n4);
				uneZone.addNode(n5);
				
				//Creation de la liste des points a atteindre
				eng= new ArrayList<Node>();
				eng.add(n5);
				eng.add(n3);
				source=n1;
	}

	@Test
	public void testNormalUse() 
	{
		//Appel de Dijkstra
		int sucess=0;
		ArrayList<Chemin> solution = Dijkstra.solve(uneZone, source, eng);
		
		// Verification du nombre de chemi resultat
		if(solution.size()==2) sucess ++;
		//Verification du premier chemin
		if(solution.get(0).getTrajectory().size()==3) sucess ++;
		if(solution.get(0).getTrajectory().get(0).getID()==0) sucess ++;
		if(solution.get(0).getTrajectory().get(1).getID()==2) sucess ++;
		if(solution.get(0).getTrajectory().get(2).getID()==4) sucess ++;
		
		//Verification du deuxieme chemin
		if(solution.get(1).getTrajectory().size()==2) sucess ++;
		if(solution.get(1).getTrajectory().get(0).getID()==0) sucess ++;
		if(solution.get(1).getTrajectory().get(1).getID()==2) sucess ++;
		

		//Toutes les verifications se sont bien passees
		if(sucess == 8)
			return;
		fail("Not yet implemented");

	}

}
