package model;

import java.util.ArrayList;


public class ZoneGeo
{
	protected
	ArrayList<Node> nodes = new ArrayList<Node>();
	ArrayList<Arc> arcs = new ArrayList<Arc>();
	int warehouseID;
	
	/**
	 * 
	 * @param node Node à ajouter.
	 * @pre Ne pas insérer deux objets avec le même id.
	 */
	public void addNode(Node node)
	{
			nodes.add(node.id, node);
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
	public ArrayList<Node> getNodes()
	{
		return nodes;
	}
	public Node getNode(Integer anID)
	{
		for(Node n : nodes)
		{
			if(n.getID()==anID)
			{
				return n;
			}
		}
        throw new IllegalStateException("The required id is unexisting");
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
