package supervision;

public enum State {
	EMPTY, // état initial où aucune zone n'est chargée
	FILLING, // état où le superviseur établit les livraisons
	MODIFICATION // état où le superviseur modifie la feuille de route
}
