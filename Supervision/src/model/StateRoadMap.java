package model;

public enum StateRoadMap
{
	INIT,	// avant première passe de TSP
	OPTIM,	// après calcul TSP
	MODIF	//losrqu'une modification a été effectuée. Appliquer le TSP pour repasser OPTIM
}
