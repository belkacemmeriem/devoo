/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parsexml;
import java.io.*;
import org.jdom2.*;
import org.jdom2.input.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.*;
import org.jdom2.xpath.XPath;

/**
 *
 * @author Benoît
 */
public class ParseMapXML {

    static org.jdom2.Document document;
    static Element racine;
    static ZoneGeo zonegeo;
    
    public ParseMapXML(File file, ZoneGeo zone) {
        zonegeo = zone;
        //Création d'un parseur d'objet XML (SAX = Simple API for XML)
        SAXBuilder sxb = new SAXBuilder();
        
        //Ouverture du fichier XML
        try
        {
            document = sxb.build(file);
        }
        catch(Exception e){}
        
        //Récupération dela racine XML
        racine = document.getRootElement();
        
        setEntrepot();
        getNodes();
        getArcs();
    }
    
    public static void setEntrepot()
    //récupère l'id associé à la balise XML 'Entrepot' et l'envoie 
    //dans l'objet de type ZoneGeo
    {
        zonegeo.setWarehouse(Integer.parseInt(racine.getChild("Entrepot").getAttributeValue("id")));
    }
            
    public static void afficheALL()
    //afficheur bête et méchant de test sur les Noeuds
    {
        //Récupération de toutes les balises 'Noeud' contenues dans 
        //la balise 'Noeuds'
        List listNoeud = racine.getChild("Noeuds").getChildren("Noeud");

        //On crée un Iterator sur notre liste
        Iterator i = listNoeud.iterator();
        while(i.hasNext())
        {
            //On recrée l'Element courant à chaque tour de boucle afin de
            //pouvoir utiliser les méthodes propres aux Element comme :
            //selectionner un noeud fils, modifier du texte, etc...
            Element courant = (Element)i.next();
            //On affiche les informations de Noeud
            System.out.println("id="+courant.getAttributeValue("id")+" x="+courant.getAttributeValue("x")+" y="+courant.getAttributeValue("y"));
        }
    }

    public static void getNodes() 
    //Récupération de tous les Noeuds et sauvegarde sous forme d'objet Node dans l'objet de type ZoneGeo
    {
        //Récupération de toutes les balises 'Noeud' contenues dans 
        //la balise 'Noeuds'
        List listNoeud = racine.getChild("Noeuds").getChildren("Noeud");
        
        //On crée un Iterator sur notre liste
        Iterator i = listNoeud.iterator();
        while(i.hasNext())
        {
            //On recrée l'Element courant à chaque tour de boucle afin de
            //pouvoir utiliser les méthodes propres aux Element comme :
            //selectionner un noeud fils, modifier du texte, etc...
            Element courant = (Element)i.next();
            
            //On crée une nouvelle instance de Node sur l'élément en cours de scan 
            //dans l'optique de l'insérer dans ZoneGeo
            Node nodeBuffer = new Node(
                    Integer.parseInt(courant.getAttributeValue("x")),
                    Integer.parseInt(courant.getAttributeValue("y")),
                    Integer.parseInt(courant.getAttributeValue("id")));
            zonegeo.addNode(nodeBuffer);
        }
    }

    public static void getArcs() 
    {
        //Récupération de tous les noeuds contenus dans l'objet de type ZoneGeo
        HashMap<Integer, Node> nodes = zonegeo.getNodes();
        
        //Récupération de la liste des clés de la HashMap dans l'optique 
        //de pouvoir créer un itérateur sur la HashMap
        Set cles = nodes.keySet();
        Iterator i = cles.iterator();
        while(i.hasNext())
        {
            Integer id = (Integer) i.next();
            
            //Récupération du Noeud d'indice 'id' via une requête XPath
            List results = xpathNodeQuery(id);
            
            if (!results.isEmpty())
            {
                Element noeudCourant = (Element) results.get(0);
                
                //Récupération des troncons associés à l'Element 'Noeud' 
                //retourné par la requête XPath
                List troncon = noeudCourant.getChildren("TronconSortant");
                
                //Parcours des troncons trouvés
                Iterator j = troncon.iterator();
                while(j.hasNext())
                {
                    Element actuel = (Element)j.next();
                    
                    //Récupération de l'indice du Noeud de destination dans le but de tester son existance
                    Integer destId = Integer.parseInt(actuel.getAttributeValue("destination"));
                    List isThereaNode = xpathNodeQuery(destId);
                    if(!isThereaNode.isEmpty() && nodes.get(destId)!=null)
                    {
                        //troncon valide pour ajout dans ZoneGeo
                        zonegeo.addArc(id, destId, 
                                Integer.parseInt(actuel.getAttributeValue("vitesse")), 
                                Integer.parseInt(actuel.getAttributeValue("longueur")), 
                                actuel.getAttributeValue("nomRue"));
                        
                    }
                }
            }
        }
    }
    
    public static List xpathNodeQuery(int nodeid)
    {
        XPath xpa;
        try {
            //Recherche d'une Element de balise 'Noeud' d'indice nodeId
            xpa = XPath.newInstance("//Noeud[@id='" + nodeid + "']");
            List results = xpa.selectNodes(racine);
            return results;
        } catch (JDOMException ex) {
            Logger.getLogger(ParseMapXML.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Echec du XPath
        return null;
    }

}
