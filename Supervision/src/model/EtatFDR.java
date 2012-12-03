package model;

public enum EtatFDR
{
	init,	// avant première passe de TSP
	OPTIM,	// après calcul TSP
	MODIF	//losrqu'une modification a été effectuée. Appliquer le TSP pour repasser OPTIM
}
