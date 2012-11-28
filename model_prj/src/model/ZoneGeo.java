package model;

import java.util.ArrayList;
import java.util.HashMap;


public class ZoneGeo
{
	protected
	HashMap<Integer, Node> nodes = new HashMap<Integer, Node>();
	ArrayList<Arc> arcs = new ArrayList<Arc>();
	int warehouseID;
	
	/**
	 * 
	 * @param node Node à ajouter.
	 * @pre Ne pas insérer deux objets avec le même id.
	 */
	public void addNode(Node node)
	{
			nodes.put(node.id, node);
	}
	
	
	/**
	 * 
	 * @param originID id du Node origine
	 * @param destID id du Node destination
	 * @param speed vitesse moyenne du troncon
	 * @param lenght longueur du troncon
	 * @param name nom de la rue
	 * @pre originID et originID doivent avoir été insérés!
	 */
	public void addArc(int originID, int destID, int speed, int lenght, String name)
	{
		
		Arc arc = new Arc(	nodes.get(originID),
							nodes.get(destID),
							speed,
							lenght,
							name );
		
		arcs.add(arc);
		nodes.get(originID).addOutArc(arc);
		nodes.get(destID).addInArc(arc);
	}
	/**
	 * 
	 * @pre doit etre un identifiant valide (pas de vérification).
	 */
	public void setWarehouse(int id)
	{
		warehouseID = id;
	}
	public HashMap<Integer, Node> getNodes()
	{
		return nodes;
	}
	public Node getNode(Integer anID)
	{
		return nodes.get(anID);
	}
	public Node getWarehouse()
	{
		return nodes.get(warehouseID);
	}
	
	public int getWarehouseID()
	{
		return warehouseID;
	}
	
}
