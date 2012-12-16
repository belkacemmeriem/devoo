package Exception;

public class GraphException extends Exception{
	private static final long serialVersionUID = 1L;
	public static final int INCONSISTENT=0;
	public static final int NO_SOLUTION_FOUND=1;
	public static final int NO_DELIVERIES=3;
	
	protected int erreur;
	
	public GraphException(int erreur) {
		this.erreur=erreur;
	}
	
	public int getException(){
		return erreur;
	}
}
