/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parsexml;

import java.awt.Color;
import java.io.File;
import java.text.ParseException;
import java.util.*;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import model.*;

/**
 *
 * @author Sherlock
 */
public class ParseDelivTimeXML {
    static org.jdom2.Document document;
    static Element racine;
    static ArrayList<Schedule> plagesHoraires = new ArrayList<Schedule>();
    
    public ParseDelivTimeXML(){
        //Création d'un parseur d'objet XML (SAX = Simple API for XML)
        SAXBuilder sxb = new SAXBuilder();
         
        String path=new File("").getAbsolutePath();
        
        //Ouverture du fichier XML
        try
        {
            document = sxb.build(new File(path+"/content/horaires.xml"));
        }
        catch(Exception e){}
        
        //Récupération de la racine XML
        racine = document.getRootElement();
        getAll();
    }
    
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

    public ArrayList<Schedule> getPlagesHoraires() {
        return plagesHoraires;
    }
}


