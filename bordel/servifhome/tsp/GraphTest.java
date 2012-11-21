package tsp;
import static org.junit.Assert.*;
import java.util.Iterator;
import org.junit.Test;


public class GraphTest {
	
	/**
	 * Checks that each vertex of <code>RegularGraph(nbVertices,degree,minArcCost,maxArcCost)</code> 
	 * has <code>degree</code> successors
	 */
	@Test
	public void testDegree(){
		int nbVertices = 50;
		int degree = 8;
		int minArcCost = 1;
		int maxArcCost = 10;
		Graph g = new RegularGraph(nbVertices,degree,minArcCost,maxArcCost);	
		for (int i=0; i<g.getNbVertices(); i++)
			assertEquals(g.getSucc(i).size(),degree);
	}

	/**
	 * Checks that each arc of <code>RegularGraph(nbVertices,degree,minArcCost,maxArcCost)</code> 
	 * has a cost ranging between<code>minArcCost</code> and <code>maxArcCost</code>
	 */
	@Test
	public void testArcCost(){
		int nbVertices = 50;
		int degree = 8;
		int minArcCost = 5;
		int maxArcCost = 10;
		Graph g = new RegularGraph(nbVertices,degree,minArcCost,maxArcCost);
		for (int i=0; i<g.getNbVertices(); i++){
			Iterator<Integer> it = g.getSucc(i).iterator();
			while (it.hasNext()){
				int j = it.next();
				assertTrue(g.getCost()[i][j] >= minArcCost);
				assertTrue(g.getCost()[i][j] <= maxArcCost);
			}
		}
	}
	
	/**
	 * Checks that for each couple of vertices <code>(i,j)</code> of <code>RegularGraph(nbVertices,degree,minArcCost,maxArcCost)</code> 
	 * such that <code>(i,j)</code> is not an arc, <code>getCost()[i][j]</code> returns <code>getMaxArcCost()+1</code>
	 */
	@Test
	public void testNonArcCost(){
		int nbVertices = 50;
		int degree = 8;
		int minArcCost = 1;
		int maxArcCost = 10;
		Graph g = new RegularGraph(nbVertices,degree,minArcCost,maxArcCost);	
		for (int i=0; i<g.getNbVertices(); i++){
			for (int j=0; j<g.getNbVertices(); j++){
				if (!g.getSucc(i).contains(j))
					assertEquals(g.getCost()[i][j],g.getMaxArcCost()+1);
			}
		}
	}

}
