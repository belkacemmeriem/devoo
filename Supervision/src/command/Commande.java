package command;

public class Commande {

	private String s;

	public Commande(String arg) {
		s = arg;
	}

	public void undo() {
		System.out.println("Undo : " + s);
	}

	public void redo() {
		System.out.println("Redo : " + s);
	}
}
