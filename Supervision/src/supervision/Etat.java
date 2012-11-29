package supervision;

public enum Etat {
	VIDE, // état initial où aucune zone n'est chargée
	REMPLISSAGE, // état où le superviseur établit les livraisons
	MODIFICATION // état où le superviseur modifie la feuille de route
}
