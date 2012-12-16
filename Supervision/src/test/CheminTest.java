package test;
import static org.junit.Assert.*;

import java.util.ArrayList;

import model.Arc;
import model.Path;
import model.Node;

import org.junit.BeforeClass;
import org.junit.Test;



public class CheminTest {
	static Path path;
	


	@BeforeClass
	public static void init()
	{
		Node n1 = new Node(0,0,0);
		Node n2 = new Node(0,1,1);
		Node n3 = new Node(1,0,2);
		Node n4 = new Node(1,1,3);
		Node n5 = new Node(20,20,4);
						
		Arc a1 = new Arc(n1,n2,10,10,"n1");
		Arc a2 = new Arc(n2,n3,15,15,"n2");
		Arc a3 = new Arc(n3,n4,7,7,"n3");
		Arc a4 = new Arc(n4,n5,40,40,"n4");
		
		n1.addOutArc(a1);
		n2.addInArc(a1);
		n1.addOutArc(a2);
		n3.addInArc(a2);
		n2.addOutArc(a3);
		n4.addInArc(a3);
		n3.addOutArc(a4);
		n5.addInArc(a4);
		
		ArrayList<Node> nodes = new ArrayList<Node>();
		nodes.add(n1);
		nodes.add(n2);
		nodes.add(n3);
		nodes.add(n4);
		nodes.add(n5);
		path = new Path(nodes, 100);
	}

	@Test
	public void testGetters() 
	{
		int sucess=0;
		if(path.getNoeudDepart().getID()==0) sucess++;
		if(path.getNoeudArrivee().getID()==4) sucess++;
		if(path.getTrajectory().get(0).getID()==0) sucess++;
		if(path.getTrajectory().get(1).getID()==1) sucess++;
		if(path.getTrajectory().get(2).getID()==2) sucess++;
		if(path.getTrajectory().get(3).getID()==3) sucess++;
		if(path.getTrajectory().get(4).getID()==4) sucess++;
		if(path.getDuration()==100) sucess++;


		//Toutes les verifications se sont bien passees
		if(sucess == 8)
			return;
		fail("Test Chemin failed");

	}
	
	@Test
	public void testArcs() 
	{
		int sucess=0;
		if(path.getArcs().get(0).getOrigin().getID()==0) sucess++;
		if(path.getArcs().get(0).getDest().getID()==1) sucess++;
		if(path.getArcs().get(1).getOrigin().getID()==1) sucess++;
		if(path.getArcs().get(1).getDest().getID()==2) sucess++;
		if(path.getArcs().get(2).getOrigin().getID()==2) sucess++;
		if(path.getArcs().get(2).getDest().getID()==3) sucess++;
		if(path.getArcs().get(3).getOrigin().getID()==3) sucess++;
		if(path.getArcs().get(3).getDest().getID()==4) sucess++;


		//Toutes les verifications se sont bien passees
		if(sucess == 8)
			return;
		fail("Test Chemin failed");

	}

}
