/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parsexml;
import Exception.*;
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
    
    public ParseMapXML(File file, ZoneGeo zone) throws ReadMapXMLException {
        zonegeo = zone;
        //Création d'un parseur d'objet XML (SAX = Simple API for XML)
        SAXBuilder sxb = new SAXBuilder();
        
        //Ouverture du fichier XML
        try
        {
            document = sxb.build(file);
            
            //Récupération dela racine XML
            racine = document.getRootElement();
        }
        catch(IOException ex){
            //récupération de l'erreur générée par un fichier corrompu ou inapproprié
            throw new ReadMapXMLException(getErrorMessage(9,null,null));
        }
        catch(JDOMException ex){
            //récupération de l'erreur générée par un fichier corrompu ou inapproprié
            throw new ReadMapXMLException(getErrorMessage(9,null,null));
        }
        catch(NullPointerException ex){
            //récupération de l'erreur générée par un fichier corrompu ou inapproprié
            throw new ReadMapXMLException(getErrorMessage(9,null,null));
        }
        
        setEntrepot();
        getNodes();
        getArcs();
    }
    
    public static void setEntrepot() throws ReadMapXMLException
    //récupère l'id associé à la balise XML 'Entrepot' et l'envoie 
    //dans l'objet de type ZoneGeo
    {
        try{
            Integer id = Integer.parseInt(racine.getChild("Entrepot").getAttributeValue("id"));
            List isThereaNode = xpathNodeQuery(id);
            if(!isThereaNode.isEmpty())
            {
            zonegeo.setWarehouse(id);
            }
            else
            {
                //Si l'id associé à l'entrepôt n'existe pas
                throw new ReadMapXMLException(getErrorMessage(6,id,null));
            }
        }
        catch(NullPointerException ex){
            //les attributs x et y d'un noeud sont nuls ou non numériques
            throw new ReadMapXMLException(getErrorMessage(7,null,null));
        }
        catch(NumberFormatException ex){
            //les attributs x et y d'un noeud sont nuls ou non numériques
            throw new ReadMapXMLException(getErrorMessage(7,null,null));
        }
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

    public static void getNodes() throws ReadMapXMLException 
    //Récupération de tous les Noeuds et sauvegarde sous forme d'objet Node dans l'objet de type ZoneGeo
    {
        //Récupération de toutes les balises 'Noeud' contenues dans 
        //la balise 'Noeuds'
        List listNoeud = racine.getChild("Noeuds").getChildren("Noeud");
        if(!listNoeud.isEmpty())
        {
            //On crée un Iterator sur notre liste
            Iterator i = listNoeud.iterator();
            Integer cpt = 0;
            while(i.hasNext())
            {
                //On recrée l'Element courant à chaque tour de boucle afin de
                //pouvoir utiliser les méthodes propres aux Element comme :
                //selectionner un noeud fils, modifier du texte, etc...
                Element courant = (Element)i.next();

                //On teste la lecture de l'ID afin d'adapter le message d'erreur potentiel associé
                Integer id;
                try {
                    id = Integer.parseInt(courant.getAttributeValue("id"));
                    if(zonegeo.getNode(id)==null) {
                        //On crée une nouvelle instance de Node sur l'élément en cours de scan 
                        //dans l'optique de l'insérer dans ZoneGeo                
                        try {
                            Node nodeBuffer = new Node(
                                Integer.parseInt(courant.getAttributeValue("x")),
                                Integer.parseInt(courant.getAttributeValue("y")),
                                id);
                            zonegeo.addNode(nodeBuffer);
                        }
                        catch(NullPointerException ex){
                            //les attributs x et y d'un noeud sont nuls ou non numériques
                            throw new ReadMapXMLException(getErrorMessage(4,id,null));
                        }
                        catch(NumberFormatException ex){
                            //les attributs x et y d'un noeud sont nuls ou non numériques
                            throw new ReadMapXMLException(getErrorMessage(4,id,null));
                        }
                    }
                    else
                    {
                        throw new ReadMapXMLException(getErrorMessage(8,id,null));
                    }
                }
                catch(NullPointerException ex){
                            //les attributs x et y d'un noeud sont nuls ou non numériques
                    throw new ReadMapXMLException(getErrorMessage(3,cpt,null));
                }
                catch(NumberFormatException ex){
                            //les attributs x et y d'un noeud sont nuls ou non numériques
                    throw new ReadMapXMLException(getErrorMessage(3,cpt,null));
                }
                cpt++;
            }
        }
        else
        {
            throw new ReadMapXMLException(getErrorMessage(0,null,null));
        }
    }

    public static void getArcs() throws ReadMapXMLException
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
                
                if(!troncon.isEmpty())
                {
                    //Parcours des troncons trouvés
                    Iterator j = troncon.iterator();
                    while(j.hasNext())
                    {
                        Element actuel = (Element)j.next();

                        try {
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
                            else
                            {
                                //Le noeud de destination n'existe pas
                                throw new ReadMapXMLException(getErrorMessage(1,destId,id));
                            }
                        }
                        catch(NullPointerException ex){
                            //les attributs destination, vitesse et position d'un tronçon sont nuls ou 
                            //les attributs nomRue, destination, vitesse et position d'un tronçon sont non numériques
                            throw new ReadMapXMLException(getErrorMessage(5,id,null));
                        }
                        catch(NumberFormatException ex){
                            //les attributs destination, vitesse et position d'un tronçon sont nuls ou 
                            //les attributs nomRue, destination, vitesse et position d'un tronçon sont non numériques
                            throw new ReadMapXMLException(getErrorMessage(5,id,null));
                        }
                    }
                }
                else
                {
                    //Pas de tronçons associés au noeuds considéré
                    throw new ReadMapXMLException(getErrorMessage(2,id,null));
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
    
    static String getErrorMessage(Integer codeErreur, Object element1, Object element2)
    {
        String message="La structure du fichier XML est incohérente ou corrompue.\n\n ";
        switch(codeErreur)
        {
            //fichier vide
            case 0: message = "La structure du fichier XML est vide ou corrompue.\n\n Aucun "
                    + "noeud n'a pu être trouvé dans le fichier.";break;
                
            //id de noeud inexistant
            case 1: message = message+"Le noeud distant d'id "+element1+" rattaché à l'un "
                    + "des tonçons du noeud d'id "+element2+" n'existe pas.";break;
                
            //pas de tronçons pour un noeud donné
            case 2: message = message+"Le noeud d'id "+element1+" n'est rattaché à aucun "
                    + "tronçon.";break;
                
            //id de noeud nul
            case 3: message = message+"L'ID du noeud d'indice "
                                    +element1+" est nul ou n'est pas un chiffre.";break;
                
            //les attributs x et y d'un noeud sont nuls ou non numériques
            case 4: message = message+"Le noeud d'ID "+element1+" possède un attribut x"
                    + " ou y nul ou qui n'est pas un chiffre.";break;
                
            //les attributs destination, vitesse et position d'un tronçon sont nuls ou 
            //les attributs nomRue, destination, vitesse et position d'un tronçon sont non numériques
            case 5: message = message+"Le noeud d'ID "+element1+" possède un "
                    + "attribut d'un de ses tronçons nul ou qui n'est pas un chiffre.";break;
                
            //Le noeud associé à l'entrepot n'existe pas
            case 6: message = message+"Le noeud d'id "+element1+" associé à l'entrepot est inexistant."
                    + "\n\n Votre fichier est incomplet ou vide";break;
                
            //id incorrect pour l'entrepôt
            case 7: message = message+"l'attribut id de l'entrepot n'est pas "
                    + "un chiffre ou est nul";break;
                
            //identifiant de noeud en double
            case 8: message = message+"L'attribut de noeud id="+element1+" est multiple.";break;
                
            //La structure du fichier XML est corrompue ou le fichier n'est pas un fichier XML
            case 9: message = "La structure du fichier XML est corrompue ou le fichier n'est pas un fichier XML.";break;
        }
        message=message+"\n\n Veuillez corriger ou changer de fichier XML";
        return message;
    }

}
