package tsp;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Christine Solnon
 *
 */

public class RegularGraph implements Graph {
	private int nbVertices;
	private int maxArcCost;
	private int minArcCost;
	private int[][] cost; 
	private ArrayList<ArrayList<Integer>> succ; 

	/**
	 * Creates a graph such that each vertex is connected to the next <code>d</code> vertices (modulo <code>n</code>) and
	 * such that the cost of each arc is a randomly chosen integer ranging between <code>min</code> and <code>max</code>
	 * @param n a number of vertices such that <code>n > 0</code>
	 * @param d a degree such that <code>0 < d < n</code>
	 * @param min a minimal arc cost such that <code>0 < min</code>
	 * @param max a maximal arc cost such that <code>min < max</code>
	 */
	public RegularGraph(int n, int d, int min, int max){
		nbVertices = n;
		minArcCost = min;
		maxArcCost = max;
		cost = new int[nbVertices][nbVertices];
		succ = new ArrayList<ArrayList<Integer>>(); 
		for (int i=0; i<nbVertices; i++){
			for (int j=0; j<nbVertices; j++)
				cost[i][j] = maxArcCost+1;
			ArrayList<Integer> l = new ArrayList<Integer>();
			for (int j = i+1; j <= i+d; j++){
				int k = j % nbVertices;
				cost[i][k] = minArcCost + (int)(Math.random() * (maxArcCost-minArcCost+1));
				l.add(k);
			}
			succ.add(i,l);
		}
	}

	@Override
	public void display(){
		for (int i=0; i<nbVertices; i++){
			System.out.print("Vertex "+i+" has "+succ.get(i).size()+" successors: ");
			Iterator<Integer> it = succ.get(i).iterator();
			while (it.hasNext()){
				int j = it.next();
				System.out.print(j + "(cost = ");
				System.out.print(cost[i][j]+") ");
			}
			System.out.println("");
		}
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
	public int getCost(int i, int j) throws ArrayIndexOutOfBoundsException{
		if ((i<0) || (i>=nbVertices) || (j<0) || (j>=nbVertices))
			throw new ArrayIndexOutOfBoundsException();
		return cost[i][j];
	}

	@Override
	public int[][] getCost(){
		return cost;
	}

	@Override
	public ArrayList<Integer> getSucc(int i) throws ArrayIndexOutOfBoundsException{
		if ((i<0) || (i>=nbVertices))
			throw new ArrayIndexOutOfBoundsException();
		return succ.get(i);
	}


}
