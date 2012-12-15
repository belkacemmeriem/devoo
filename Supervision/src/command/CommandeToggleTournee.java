package command;

import supervision.Controleur;

public class CommandeToggleTournee extends Commande {

	private Controleur ctrl;
	
	public CommandeToggleTournee(Controleur ctrl) {
		this.ctrl = ctrl;
	}
	
	private void toggleDo() {
		ctrl.toggleGenererTournee(false);
	}

	@Override
	public void undo() {
		toggleDo();
	}

	@Override
	public void redo() {
		toggleDo();
	}

}
