package tsp;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;


public class TSPTest {
	static Graph g;
	TSP tsp;
	
	/**
	 * Creates a graph which is used by <code>testPosCost()</code> and 
	 * <code>testAllPermutations()</code> thus allowing one to compare CPU times of Choco 
	 * with a brute force algorithm which enumerates all tours.
	 */
	@BeforeClass
	public static void init(){
		g = new RegularGraph(20,10,1,10);
	}
	
	/**
	 * Checks that <code>tsp.getTotalCost()</code> is equal to the cost of the tour defined by <code>tsp.getPos()</code>
	 */
	@Test
	public void testPosCost(){
		int totalCost = 0;
		int n = g.getNbVertices();
		tsp = new TSP();	
		tsp.solve(100000,g.getNbVertices()*g.getMaxArcCost()+1,g);
		if (tsp.getSolutionState() != SolutionState.INCONSISTENT){
			for (int i=0; i<n-1; i++)
				totalCost += g.getCost(tsp.getPos()[i], tsp.getPos()[i+1]);
			totalCost += g.getCost(tsp.getPos()[n-1], tsp.getPos()[0]);
			assertEquals(totalCost,tsp.getTotalCost());
		}
		else
			assertTrue("No solution found after 100 seconds...", false);
	}

	/**
	 * Checks (with a brute force algorithm) that there does not exist a permutation 
	 * the cost of which is lower than <code>tsp.getTotalCost()</code>
	 */
	@Test
	public void testAllPermutations(){
		tsp = new TSP();	
		tsp.solve(200000,g.getNbVertices()*g.getMaxArcCost()+1,g);
		if (tsp.getSolutionState() == SolutionState.OPTIMAL_SOLUTION_FOUND){
			int[] perm = new int[g.getNbVertices()];
			for (int i=0; i<g.getNbVertices(); i++)
				perm[i] = i;
			permutations(1,0,perm,tsp.getTotalCost());
		}
		else 
			assertTrue("Optimal solution not found within 200 seconds",false);
	}
	
	/**
	 * Iteratively searches for tours until finding the optimal tour and proving its optimality, 
	 * by increasing the CPU timeLimit after each call to <code>solve</code>
	 */
	@Test
	public void testLargeGraph() {
		Graph g = new RegularGraph(30,10,1,10);
		TSP tsp = new TSP();
		int bound = g.getNbVertices()*g.getMaxArcCost() + 2;
		for (int t = 1; (tsp.getSolutionState() != SolutionState.OPTIMAL_SOLUTION_FOUND) && 
						 (tsp.getSolutionState() != SolutionState.INCONSISTENT); t*=2){
			System.out.println("Search of a tour strictly lower than "+bound+" within a time limit of "+t+" seconds.");
			tsp.solve(t*1000,bound-1,g);
			if (tsp.getSolutionState() == SolutionState.NO_SOLUTION_FOUND){
				System.out.println("Time limit "+t+" reached before having completed the search.");
				System.out.println("No solution strictly lower than "+bound+" has been found.");
			}
			else if (tsp.getSolutionState() == SolutionState.INCONSISTENT){
				System.out.println("There does not exist a solution strictly lower than "+bound);
			}
			else if (tsp.getSolutionState() == SolutionState.OPTIMAL_SOLUTION_FOUND){
				System.out.println("Optimal solution found: totalCost="+tsp.getTotalCost());
			}
			else{ // etat = SOLUTION_FOUND
				System.out.println("Time limit "+t+" reached before having completed the search.");
				System.out.println("Best solution found so far: totalCost="+tsp.getTotalCost());
				bound = tsp.getTotalCost();
			}
		}	
	}
	
	/**
	 * Case of a graph such that minArcCost = maxArcCost
	 */
	@Test
	public void testConstantCosts(){
		Graph g = new RegularGraph(10,9,4,4);
		TSP tsp = new TSP();
		int bound = g.getNbVertices()*g.getMaxArcCost() + 1;
		tsp.solve(50000,bound,g);
		assertEquals(g.getNbVertices()*g.getMaxArcCost(),tsp.getTotalCost());
	}
	
	private void permutations(int i, int cout, int[] perm, int coutMin) throws ArrayIndexOutOfBoundsException{
		if (i == g.getNbVertices()){
			cout += g.getCost(perm[g.getNbVertices()-1], 0);
			assertTrue(cout >= coutMin);
		}
		else if (cout < coutMin){
			int ival = perm[i];
			for (int j=i; j<g.getNbVertices(); j++){
				if (g.getCost(perm[i-1],perm[j]) <= g.getMaxArcCost()){
					int jval = perm[j];
					perm[j] = ival;
					perm[i] = jval;
					permutations(i+1,cout + g.getCost(perm[i-1], jval),perm,coutMin);
					perm[j] = jval;
					perm[i] = ival;
				}
			}
		}
	}


}


