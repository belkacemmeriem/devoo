package command;

public abstract class Command {
	
	/**
	 * Annule une action
	 */
	public abstract void undo();
	
	/**
	 * Répète une action
	 */
	public abstract void redo();
}
