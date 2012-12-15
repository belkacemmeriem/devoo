/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package supervision;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;

import javax.swing.*;
import model.Schedule;


/**
 *
 * @author Mignot
 */
public class Fenetre extends Frame {

	private final String TITRE = "Supervision des Livraisons Itinerantes Planifies";
	private final String CHAMP_INDISP_CREA = "(ce champ est indisponible en"
			+ " mode creation)";
	private final String CHAMP_NO_LIV_SELEC = "Aucune livraison n'est selectionnee";
	private Fenetre fenetre;
	private int selectedZone;
	private boolean masquerPopUpZone = false;
	private Controleur controleur;
	private JFileChooser jFileChooserXML;
	private JFileChooser jFileChooserA;
	private Menu menuFichier;
	private Menu menuEdition;

	private ArrayList<Schedule> schedules;
	private ArrayList<JToggleButton> jToggleButtonSchedules;

	private ListLivraison listeLivraison;

	/**
	 * Creates new form Fenetre
	 */
	public Fenetre() {
		fenetre=this;
		initComponents();
		this.setTitle(TITRE);
		creeMenu();
		setFonts();
		setPopups();
		setKeyEvents();
	}
	
	public void update() {
		if (controleur == null)
			return;
		
		switch (controleur.getEtat()) {
		case VIDE:
			menuFichier.getItem(0).setEnabled(false);
			menuEdition.getItem(0).setEnabled(false);
			menuEdition.getItem(1).setEnabled(false);
			insertBeforeButton.setEnabled(false);
			insertAfterButton.setEnabled(false);
			jButtonGenTourn.setEnabled(false);
			jButtonSupprimerLiv.setEnabled(false);
			jButtonValiderLiv.setEnabled(false);
			break;
		
		case REMPLISSAGE:
			menuFichier.getItem(0).setEnabled(false);
			menuEdition.getItem(0).setEnabled(controleur.undoAble());
			menuEdition.getItem(1).setEnabled(controleur.redoAble());
			insertBeforeButton.setEnabled(false);
			insertAfterButton.setEnabled(false);
			jButtonGenTourn.setEnabled(controleur.nbDeliveries() > 0);
			jButtonGenTourn.setSelected(false);
			jButtonSupprimerLiv.setEnabled(controleur.deliverySelected() && ! controleur.warehouseSelected());
			jButtonValiderLiv.setEnabled(controleur.nodeSelected()
					&& controleur.getSelectedSchedule() != null
					&& ! controleur.warehouseSelected());
			for (JToggleButton jtb : jToggleButtonSchedules)
				jtb.setEnabled(true);
			jLabelAddLivCurr.setEnabled(false);
			jLabelAddLivPrec.setEnabled(false);
			jLabelAddLivSuiv.setEnabled(false);
			jLabelLivCurr.setEnabled(false);
			jLabelLivPrec.setEnabled(false);
			jLabelLivSuiv.setEnabled(false);
			jLabelAddLivPrec.setText(CHAMP_NO_LIV_SELEC+
					" "+CHAMP_INDISP_CREA);
			jLabelAddLivSuiv.setText(CHAMP_NO_LIV_SELEC+
					" "+CHAMP_INDISP_CREA);
			break;
			
		case MODIFICATION:
			menuFichier.getItem(0).setEnabled(true);
			menuEdition.getItem(0).setEnabled(controleur.undoAble());
			menuEdition.getItem(1).setEnabled(controleur.redoAble());
			insertBeforeButton.setEnabled(controleur.nodeSelected() && ! controleur.deliverySelected());
			insertAfterButton.setEnabled(controleur.nodeSelected() && ! controleur.deliverySelected());
			jButtonGenTourn.setEnabled(true);
			jButtonGenTourn.setSelected(true);
			jButtonSupprimerLiv.setEnabled(controleur.deliverySelected() 
					&& ! controleur.warehouseSelected()
					&& controleur.nbDeliveries() > 1);
			jButtonValiderLiv.setEnabled(false);
			for (JToggleButton jtb : jToggleButtonSchedules)
				jtb.setEnabled(false);
			jLabelAddLivPrec.setEnabled(false);
			jLabelAddLivSuiv.setEnabled(false);
			jLabelLivPrec.setEnabled(false);
			jLabelLivSuiv.setEnabled(false);
			jLabelAddLivPrec.setText(CHAMP_NO_LIV_SELEC+
					" "+CHAMP_INDISP_CREA);
			jLabelAddLivSuiv.setText(CHAMP_NO_LIV_SELEC+
					" "+CHAMP_INDISP_CREA);
			break;
		}
	}
	
	public void setInsertButton(int i) {
		insertBeforeButton.setSelected(i == 1);
		insertAfterButton.setSelected(i == 2);
	}
	
	public void setSchedule(Schedule schedule) {
		int index = schedules.indexOf(schedule);
		for(int i=0 ; i<jToggleButtonSchedules.size() ; i++) {
			jToggleButtonSchedules.get(i).setSelected(i == index);
		}
	}

	public void setSchedules(ArrayList<Schedule> aschedules) {
		this.schedules = aschedules;
		jToggleButtonSchedules = new ArrayList<JToggleButton>();
		jPanelHoraires.removeAll();

		for(int i =0;i<schedules.size();i++)
		{
			String s = schedules.get(i).getSliceString();
			jToggleButtonSchedules.add(new JToggleButton(s));
			ActionListener a = new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					int j=0;
					JToggleButton jtb = (JToggleButton)e.getSource();
					for(j=0;j<jToggleButtonSchedules.size();j++)
					{
						JToggleButton jtb2 = jToggleButtonSchedules.get(j);
						if(jtb!=jtb2){
							jtb2.setSelected(false);
						} else {
							if (jtb.isSelected()) {
								controleur.setSelectedSchedule(schedules.get(j));
							} else {
								controleur.setSelectedSchedule(null);
							}
						}
					}
					update();
				}
			};
			jToggleButtonSchedules.get(i).addActionListener(a);
			jPanelHoraires.add(jToggleButtonSchedules.get(i));
			listeLivraison = new ListLivraison();
			listeLivraison.setjButtonSupprimer(jButtonSupprimerLiv);
			listeLivraison.setSchedule(schedules);
			jPaneLivraisons.add(listeLivraison);
		}
	}

	public Dessin getDessin() {
		return (Dessin)jPanelPlan;
	}

	public ListLivraison getListLivraison()
	{
		return listeLivraison;
	}	
	
	public void setControleur(Controleur ctrl) {
		controleur = ctrl;
	}
	
	public void setKeyEvents() {
		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		manager.addKeyEventDispatcher( new KeyEventDispatcher() {
			@Override
			public boolean dispatchKeyEvent(KeyEvent e) {
		        if(e.getID() == KeyEvent.KEY_TYPED) {
		        	int k = (int) e.getKeyChar();
		            switch (k) {
		            case 26: // CTRL-Z
		            	if(menuEdition.getItem(0).isEnabled())
		            		controleur.undo();
		            	break;
		            	
		            case 25: // CTRL-Y
		            	if(menuEdition.getItem(1).isEnabled())
		            		controleur.redo();
		            	break;
		            	
		            case 10: // ENTER
		    			if (jButtonValiderLiv.isEnabled())
		    				controleur.add();
		            	break;
		            	
		            case 127: // SUPPR
		            	if (jButtonSupprimerLiv.isEnabled())
		    				controleur.del();
		            	break;
		            
		            case 19: // CTRL-S
		            	if(menuFichier.getItem(0).isEnabled())
		            		controleur.exportReport(trouverCheminRapport());
		            	break;
		            	
		            case 15: // CTRL-O
		            	if(menuFichier.getItem(1).isEnabled())
		            		controleur.loadZone(ouvrirFichierXML());
		            	break;
		            }
		        }
		        return false;
		    }
		});
	}

	/**
	 *  Associe les messages popups aux différentes actions qui les déclenchent
	 * 
	 */
	private void setPopups() {
		/*Combo box de la zone
		 * On demande confirmation quand l'utilisateur veut changer de zone
		 */
		Vector<String> listPlan = new Vector<String>();
		File folder = new File("./content/");
		File[] listOfFiles = folder.listFiles();
		for (File file : listOfFiles) {
		    if (! file.isDirectory()) {
		        String name = file.getName();
		        if (name.startsWith("plan") && name.endsWith(".xml"))
		        	listPlan.add(name.substring(0, name.length()-4));
		    }
		}

		jComboBoxZone.setModel(new javax.swing.DefaultComboBoxModel(listPlan));
		selectedZone = jComboBoxZone.getSelectedIndex();
		ItemListener itemListenerZone = new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==e.SELECTED)
				{     
					if(masquerPopUpZone)
					{
						masquerPopUpZone=false;
					}
					else
					{
						Object[] options = { "Confirmer", "Annuler" };
						int optionChoisie = JOptionPane.showOptionDialog(new JFrame(),
								"Etes-vous sur de vouloir changer de zone ?"
										+ "\n (les donnees de la tournee en cours seront définitivement effacées)",
										"Confirmation de changement de zone",
										JOptionPane.OK_CANCEL_OPTION, 
										JOptionPane.WARNING_MESSAGE, null,
										options, options[1]);

						//Si l'utilisateur annule, on ne fait aucun changement
						if(optionChoisie==1)
						{
							masquerPopUpZone=true;
							jComboBoxZone.setSelectedIndex(selectedZone);
						}
						else
						{
							File path = new File("./content/" + e.getItem() + ".xml");
							controleur.loadZone(path);
							selectedZone=jComboBoxZone.getSelectedIndex();
						}
					}
				}
			}
		};
		jComboBoxZone.addItemListener(itemListenerZone);
	}

	/**
	 *  Associe les polices aux différents labels
	 * 
	 */
	private void setFonts(){
		//Titre edition livraison
		Font fontEdLivTitre = new Font("Sans", Font.PLAIN, 20);
		jLabelEdLivTitre.setFont(fontEdLivTitre);

		//Titre livraisons
		jLabelTitreLivraisons.setFont(fontEdLivTitre);
	}

	private void creeMenu(){
		// Creation de deux menus, chaque menu ayant plusieurs items
		// et association d'un ecouteur d'action a chacun de ces items

		menuFichier = new Menu("Fichier");
		menuEdition = new Menu("Edition");
		
		ActionListener a4 = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				controleur.exportReport(trouverCheminRapport());
			}
		};
		ajoutItem("Generer rapport", menuFichier, a4);
		
		ActionListener a5 = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				controleur.loadZone(ouvrirFichierXML());
			}
		};
		ajoutItem("Ouvrir un fichier XML", menuFichier, a5);

		ActionListener a6 = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				controleur.undo();
			}
		};
		ajoutItem("Undo", menuEdition, a6);

		ActionListener a7 = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				controleur.redo();
			}
		};
		ajoutItem("Redo", menuEdition, a7);

		MenuBar barreDeMenu = new MenuBar();
		barreDeMenu.add(menuFichier);
		barreDeMenu.add(menuEdition);
		this.setMenuBar(barreDeMenu);
	}

	private File trouverCheminRapport(){
		//Opens filechooser
		jFileChooserA  = new JFileChooser(JFileChooser.FILE_FILTER_CHANGED_PROPERTY); 
		jFileChooserA.setDialogTitle("Choisir le dossier ou enregister le rapport");
		jFileChooserA.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		if (jFileChooserA.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) 
			return fileSaver(jFileChooserA);
		return null;
	}

	private File fileSaver(JFileChooser fc)
	//Saves string to file, pass in FileChooser
	{
		File file = fc.getSelectedFile();
		String textToSave = " ";
		BufferedWriter writer = null;

		//Check for legal file extension (.txt)	
		String fileExtension = file.getPath();

		//Set extension to .txt if not already	
		if(!fileExtension.toLowerCase().endsWith(".txt"))
		{
			Calendar c = Calendar.getInstance ();
			file = new File(fileExtension + "/Rapport - "+ c.getTime().toString()+".txt");
		}

		try
		{
			writer = new BufferedWriter( new FileWriter(file));
			writer.write(textToSave.replaceAll("\n", System.getProperty("line.seperator")));

			JOptionPane.showMessageDialog(this, "Message saved. (" + file.getName() + ")", "Page Saved Successfully", JOptionPane.INFORMATION_MESSAGE);
		}
		catch (IOException e)
		{ }

		//Close writer
		finally
		{
			try
			{
				if(writer != null)
				{
					writer.close();
				}
			}
			catch (IOException e)
			{e.printStackTrace(); }
		}
		return file;
	}
	//End fileSaver
	private File ouvrirFichierXML(){
		jFileChooserXML = new JFileChooser();
		// Note: source for ExampleFileFilter can be found in FileChooserDemo,
		// under the demo/jfc directory in the JDK.
		ExampleFileFilter filter = new ExampleFileFilter();
		filter.addExtension("xml");
		filter.setDescription("Fichier XML");
		jFileChooserXML.setFileFilter(filter);
		jFileChooserXML.setFileSelectionMode(JFileChooser.FILES_ONLY);

		if (jFileChooserXML.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) 
			return new File(jFileChooserXML.getSelectedFile().getAbsolutePath());
		return null;
	}

	private void ajoutItem(String intitule, Menu menu, ActionListener a){
		MenuItem item = new MenuItem(intitule);
		menu.add(item);
		item.addActionListener(a);
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelGauche = new javax.swing.JPanel();
        jPanelBoutonsGen = new javax.swing.JPanel();
        jComboBoxZone = new javax.swing.JComboBox();
        jButtonGenTourn = new javax.swing.JToggleButton();
        SpeedRateSlider = new javax.swing.JSlider();
        jPanelPlan = new Dessin();
        jPanelEditionLivraison = new javax.swing.JPanel();
        jLabelEdLivTitre = new javax.swing.JLabel();
        jButtonSupprimerLiv = new javax.swing.JButton();
        jLabelAddLivCurr = new javax.swing.JLabel();
        jButtonValiderLiv = new javax.swing.JButton();
        jPanelHoraires = new javax.swing.JPanel();
        jLabelLivCurr = new javax.swing.JLabel();
        jLabelLivPrec = new javax.swing.JLabel();
        jLabelAddLivPrec = new javax.swing.JLabel();
        jLabelLivSuiv = new javax.swing.JLabel();
        jLabelAddLivSuiv = new javax.swing.JLabel();
        insertBeforeButton = new javax.swing.JButton();
        insertAfterButton = new javax.swing.JButton();
        jPanelDroite = new javax.swing.JPanel();
        jLabelTitreLivraisons = new javax.swing.JLabel();
        jPaneLivraisons = new javax.swing.JPanel();

        setBackground(new java.awt.Color(51, 51, 51));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        jPanelGauche.setBackground(new java.awt.Color(51, 51, 51));

        jPanelBoutonsGen.setBackground(new java.awt.Color(51, 51, 51));
        jPanelBoutonsGen.setForeground(new java.awt.Color(51, 51, 51));

        jComboBoxZone.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxZone.setToolTipText("Changer de zone");

        jButtonGenTourn.setText("Générer tournée");
        jButtonGenTourn.setPreferredSize(new java.awt.Dimension(113, 20));
        jButtonGenTourn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGenTournActionPerformed(evt);
            }
        });

        SpeedRateSlider.setBackground(new java.awt.Color(51, 51, 51));
        SpeedRateSlider.setForeground(new java.awt.Color(51, 51, 51));
        SpeedRateSlider.setMajorTickSpacing(1);
        SpeedRateSlider.setMaximum(300);
        SpeedRateSlider.setSnapToTicks(true);
        SpeedRateSlider.setToolTipText("Edite la vitesse de rafraichissement");
        SpeedRateSlider.setPreferredSize(new java.awt.Dimension(200, 20));
        SpeedRateSlider.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                SpeedRateSliderMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanelBoutonsGenLayout = new javax.swing.GroupLayout(jPanelBoutonsGen);
        jPanelBoutonsGen.setLayout(jPanelBoutonsGenLayout);
        jPanelBoutonsGenLayout.setHorizontalGroup(
            jPanelBoutonsGenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBoutonsGenLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jComboBoxZone, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(76, 76, 76)
                .addComponent(SpeedRateSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(75, 75, 75)
                .addComponent(jButtonGenTourn, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanelBoutonsGenLayout.setVerticalGroup(
            jPanelBoutonsGenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBoutonsGenLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelBoutonsGenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonGenTourn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxZone, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(SpeedRateSlider, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanelPlan.setFenetre(this);
        jPanelPlan.setBackground(new java.awt.Color(51, 51, 51));
        jPanelPlan.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanelEditionLivraison.setBackground(new java.awt.Color(51, 51, 51));

        jLabelEdLivTitre.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelEdLivTitre.setText("Edition de livraison");

        jButtonSupprimerLiv.setText("Supprimer");
        jButtonSupprimerLiv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSupprimerLivActionPerformed(evt);
            }
        });

        jLabelAddLivCurr.setText("Aucune livraison sélectionnée");

        jButtonValiderLiv.setText("Ajouter");
        jButtonValiderLiv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonValiderLivActionPerformed(evt);
            }
        });

        jPanelHoraires.setBackground(new java.awt.Color(51, 51, 51));
        jPanelHoraires.setLayout(new java.awt.GridLayout(1, 0));

        jLabelLivCurr.setText("Adresse de livraison :");

        jLabelLivPrec.setText("Livraison précedente :");

        jLabelAddLivPrec.setText("Aucune livraison sélectionnée");

        jLabelLivSuiv.setText("Livraison suivante :");

        jLabelAddLivSuiv.setText("Aucune livraison sélectionnée");

        insertBeforeButton.setToolTipText("");

        insertAfterButton.setToolTipText("");
        insertAfterButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertAfterButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelEditionLivraisonLayout = new javax.swing.GroupLayout(jPanelEditionLivraison);
        jPanelEditionLivraison.setLayout(jPanelEditionLivraisonLayout);
        jPanelEditionLivraisonLayout.setHorizontalGroup(
            jPanelEditionLivraisonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelHoraires, javax.swing.GroupLayout.DEFAULT_SIZE, 569, Short.MAX_VALUE)
            .addGroup(jPanelEditionLivraisonLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelEditionLivraisonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelEdLivTitre, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE)
                    .addComponent(jLabelLivPrec, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE)
                    .addComponent(jLabelLivCurr, javax.swing.GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE)
                    .addComponent(jLabelLivSuiv, javax.swing.GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE))
                .addGroup(jPanelEditionLivraisonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanelEditionLivraisonLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelEditionLivraisonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabelAddLivPrec, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
                            .addComponent(jLabelAddLivCurr, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabelAddLivSuiv, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanelEditionLivraisonLayout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(insertBeforeButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(insertAfterButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonValiderLiv)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonSupprimerLiv)))
                .addContainerGap())
        );
        jPanelEditionLivraisonLayout.setVerticalGroup(
            jPanelEditionLivraisonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelEditionLivraisonLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelEditionLivraisonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanelEditionLivraisonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabelEdLivTitre, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonSupprimerLiv, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonValiderLiv, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(insertAfterButton, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(insertBeforeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanelEditionLivraisonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelLivCurr, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelAddLivCurr, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanelEditionLivraisonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelLivPrec, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelAddLivPrec, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanelEditionLivraisonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelLivSuiv, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelAddLivSuiv, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addComponent(jPanelHoraires, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanelGaucheLayout = new javax.swing.GroupLayout(jPanelGauche);
        jPanelGauche.setLayout(jPanelGaucheLayout);
        jPanelGaucheLayout.setHorizontalGroup(
            jPanelGaucheLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelBoutonsGen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanelEditionLivraison, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanelGaucheLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jPanelPlan, javax.swing.GroupLayout.DEFAULT_SIZE, 559, Short.MAX_VALUE))
        );
        jPanelGaucheLayout.setVerticalGroup(
            jPanelGaucheLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelGaucheLayout.createSequentialGroup()
                .addComponent(jPanelBoutonsGen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelPlan, javax.swing.GroupLayout.DEFAULT_SIZE, 481, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelEditionLivraison, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanelDroite.setBackground(new java.awt.Color(255, 255, 255));
        jPanelDroite.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabelTitreLivraisons.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelTitreLivraisons.setText("Liste des livraisons");

        jPaneLivraisons.setBackground(new java.awt.Color(255, 255, 255));
        jPaneLivraisons.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout jPanelDroiteLayout = new javax.swing.GroupLayout(jPanelDroite);
        jPanelDroite.setLayout(jPanelDroiteLayout);
        jPanelDroiteLayout.setHorizontalGroup(
            jPanelDroiteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelDroiteLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabelTitreLivraisons, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(64, 64, 64))
            .addGroup(jPanelDroiteLayout.createSequentialGroup()
                .addComponent(jPaneLivraisons, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 1, Short.MAX_VALUE))
        );
        jPanelDroiteLayout.setVerticalGroup(
            jPanelDroiteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDroiteLayout.createSequentialGroup()
                .addComponent(jLabelTitreLivraisons, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPaneLivraisons, javax.swing.GroupLayout.DEFAULT_SIZE, 657, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanelGauche, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanelDroite, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelDroite, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanelGauche, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

	public void nodeClicked(int id)
	{
		jLabelAddLivCurr.setText(""+id);
	}

	/**
	 * Exit the Application
	 */
	private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
		System.exit(0);
	}//GEN-LAST:event_exitForm

	private void jButtonGenTournActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGenTournActionPerformed
		controleur.toggleGenererTournee(true);
	}//GEN-LAST:event_jButtonGenTournActionPerformed

	private void jButtonValiderLivActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonValiderLivActionPerformed
		if (controleur.getEtat() != Etat.VIDE) {
			controleur.add();
		}
	}//GEN-LAST:event_jButtonValiderLivActionPerformed

	private void jButtonSupprimerLivActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSupprimerLivActionPerformed
		if (controleur.getEtat() != Etat.VIDE) {
			controleur.del();
			jButtonSupprimerLiv.setEnabled(false);
		}
	}//GEN-LAST:event_jButtonSupprimerLivActionPerformed

	private void insertBeforeButtonActionPerformed(java.awt.event.ActionEvent evt) {                                                  
		// TODO add your handling code here:
		controleur.setInsertButton(1);
	}                                                 
	
	private void insertAfterButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insertAfterButtonActionPerformed
		// TODO add your handling code here:
		controleur.setInsertButton(2);
	}//GEN-LAST:event_insertAfterButtonActionPerformed

private void SpeedRateSliderMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SpeedRateSliderMouseReleased
	controleur.getViewMain().updatePulseSleep(SpeedRateSlider.getValue());
}//GEN-LAST:event_SpeedRateSliderMouseReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton insertAfterButton;
    private javax.swing.JToggleButton insertBeforeButton;
    private javax.swing.JToggleButton jButtonGenTourn;
    private javax.swing.JSlider SpeedRateSlider;
    private javax.swing.JButton jButtonSupprimerLiv;
    private javax.swing.JButton jButtonValiderLiv;
    private javax.swing.JComboBox jComboBoxZone;
    private javax.swing.JLabel jLabelAddLivCurr;
    private javax.swing.JLabel jLabelAddLivPrec;
    private javax.swing.JLabel jLabelAddLivSuiv;
    private javax.swing.JLabel jLabelEdLivTitre;
    private javax.swing.JLabel jLabelLivCurr;
    private javax.swing.JLabel jLabelLivPrec;
    private javax.swing.JLabel jLabelLivSuiv;
    private javax.swing.JLabel jLabelTitreLivraisons;
    private javax.swing.JPanel jPaneLivraisons;
    private javax.swing.JPanel jPanelBoutonsGen;
    private javax.swing.JPanel jPanelDroite;
    private javax.swing.JPanel jPanelEditionLivraison;
    private javax.swing.JPanel jPanelGauche;
    private javax.swing.JPanel jPanelHoraires;
    private Dessin jPanelPlan;
    // End of variables declaration//GEN-END:variables

}