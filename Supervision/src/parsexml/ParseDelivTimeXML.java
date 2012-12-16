/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parsexml;

import Exception.ReadMapXMLException;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import model.*;
import org.jdom2.JDOMException;

/**
 * <b>ParseDelivTimeXML est la classe permettant la lecture des plages horaires.</b>
 * <p>
 * Le ParseDelivTimeXML est caractérisée par les informations suivantes :
 * <ul>
 * <li>Un document représentant l'entité XML à lire.</li>
 * <li>Une racine qui est la racine du document XML.</li>
 * <li>plagesHoraires qui est la liste dans laquelle les plages horaires lues dans le document XML seront rangées.</li>
 * </ul>
 * </p>
 * 
 * <p>
 * Pour que cette classe fonctionne correctement il faut que le fichier xml "horaire.xml" soit contenu dans le dossier content qui doit se trouver dans le dossier de projet
 * 
 * On suppose que ce fichier est écrit correctement
 * </p>
 * 
 * @see org.jdom2
 * @see org.jdom2.Element
 * @see org.jdom2.Document
 * @see org.jdom2.input.SAXBuilder
 * 
 * @author H4404
 */
public class ParseDelivTimeXML {
	/**
    * lien vers le document XML
    */
    protected org.jdom2.Document document;
	/**
    * racine du document XML
    */
    protected Element racine;
	/**
    * liste dans laquelle les plages horaires lues dans le document XML seront rangées.
    */	
    protected ArrayList<Schedule> plagesHoraires = new ArrayList<Schedule>();
    
	/**
    * Constructeur ParseDelivTimeXML.
	 * 
	 * @throws ReadMapXMLException  Si jamais la lecture ne s'est pas bien faite
	 */
    public ParseDelivTimeXML() throws ReadMapXMLException{
        //Création d'un parseur d'objet XML (SAX = Simple API for XML)
        SAXBuilder sxb = new SAXBuilder();
         
        String path=new File("").getAbsolutePath();
        
        //Ouverture du fichier XML
        try
        {
            document = sxb.build(new File(path+"/content/horaires.xml"));
        }
        catch(IOException ex){
            //récupération de l'erreur générée par un fichier corrompu ou inapproprié
            throw new ReadMapXMLException("Le fichier horaire.xml est corrompu ou inexistant");
        }
        catch(JDOMException ex){
            //récupération de l'erreur générée par un fichier corrompu ou inapproprié
            throw new ReadMapXMLException("Le fichier horaire.xml est corrompu ou inexistant");
        }
        catch(NullPointerException ex){
            //récupération de l'erreur générée par un fichier corrompu ou inapproprié
            throw new ReadMapXMLException("Le fichier horaire.xml est corrompu ou inexistant");
        }
        
        //Récupération de la racine XML
        racine = document.getRootElement();
        getAll();
    }
    
	/**
    * Récupère la totalité des plages horaires dans le document XML.
    */
    public void getAll()
    {
        List listNoeud = racine.getChildren("PlageHoraire");
        
        //On crée un Iterator sur notre liste
        Iterator i = listNoeud.iterator();
        while(i.hasNext())
        {
            //On recrée l'Element courant à chaque tour de boucle afin de
            //pouvoir utiliser les méthodes propres aux Element comme :
            //selectionner un noeud fils, modifier du texte, etc...
            Element courant = (Element)i.next();
            
            //On récupère les attributs de chaque plage horaire dans l'optique
            //de construire de nouveaux objets de type Schedule
            String start = courant.getAttributeValue("start");
            String end = courant.getAttributeValue("end");
            Color color = Color.decode("#"+courant.getAttributeValue("color"));
            plagesHoraires.add(new Schedule(GetTimeInMin(start),GetTimeInMin(end),color));
        }
    }
    
	/**
	 * Formate les heures lues en minutes.
	 * 
	 * @param hourIn heure lue en string sous le format HH:MM
	 * @return heure en minutes
	 */
    public int GetTimeInMin (String hourIn)
    {
        //Parse un string au format HH:MM pour le transformer 
        //en entier représentant le nombre de minutes
        String[] entiers = hourIn.split(":");
        int hour = Integer.parseInt(entiers[0]);
        int minute = Integer.parseInt(entiers[1]);
        int timeMin = hour*60 + minute;
        return timeMin;
    }

	/**
	 * Retourne les plages horaires telles qu'elles ont été lues dans le fichier XML.
	 * 
	 * @return ArrayList de Schedules
	 */
    public ArrayList<Schedule> getPlagesHoraires() {
        return plagesHoraires;
    }
}


