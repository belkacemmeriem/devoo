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
        
        //Récupération dela racine XML
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
            
            //On crée une nouvelle instance de Node sur l'élément en cours de scan 
            //dans l'optique de l'insérer dans ZoneGeo
            String start = courant.getAttributeValue("start");
            String end = courant.getAttributeValue("end");
            Color color = Color.decode("#"+courant.getAttributeValue("color"));
        }
    }
    
    public int GetTimeInMin (String hourIn)
    {
        String[] entiers = hourIn.split(":");
        int hour = Integer.parseInt(entiers[0]);
        int minute = Integer.parseInt(entiers[1]);
        int timeMin = hour*60 + minute;
        return timeMin;
    }
}


