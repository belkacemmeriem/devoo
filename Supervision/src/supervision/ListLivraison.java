package supervision;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import model.Schedule;

/* ListLivraison.java requires no other files. */
public class ListLivraison extends JPanel {
	
    private JTree tree;
    private DefaultTreeModel treeModel;
    private ArrayList<Schedule> schedules;
	private JButton jButtonSupprimer;
    private ArrayList<Integer> idSchedules; /*numero de ligne de la Jlist 
    ou commence la liste des livraisons de la plage horaire*/

    public ListLivraison(){
        super(new BorderLayout());
    }
    
    public void setjButtonSupprimer(JButton jb){
    	this.jButtonSupprimer=jb;
    }
    
	//Teste la pr�sence d'un noeud dans l'arbre par l'id 
    public boolean livExists(String addr){
		//recherche de la racine
		DefaultMutableTreeNode root = (DefaultMutableTreeNode)treeModel.getRoot();
		
		//Scan de l'arbre par plage horaire puis par children de ces plages horaires.
		DefaultMutableTreeNode nodeBuffer;
    	for(int i = 0;i<treeModel.getChildCount(root);i++){
			nodeBuffer = (DefaultMutableTreeNode)treeModel.getChild(root, i);
			for(int j = 0;j<treeModel.getChildCount(nodeBuffer);j++){
				if(addr.equals(((DefaultMutableTreeNode)treeModel.getChild(nodeBuffer, j)).toString())){
					//noeud trouv�
					return true;
				}
			}
    	}
    	return false;
    }
    
	//R�cup�re l'id d'une adresse de livraison
    private Integer getIdSchedule(Schedule schedule){
		
    	for(int i = 0;i<schedules.size();i++){
    		if(schedule.getEndTime()==schedules.get(i).getEndTime()){
    			return i;
    		}
    	}
    	return -1;
    }
    
	
    public void addLiv (Schedule schedule, String addr){
		//r�cup�ration de l'indice du noeud de plage horaire voulue
    	Integer idSchedule=getIdSchedule(schedule);
		//r�cup�re le noeud relatif � une plage horaire
		DefaultMutableTreeNode scheduleNode=(DefaultMutableTreeNode)((DefaultMutableTreeNode)treeModel.getRoot()).getChildAt(idSchedule);
		//ins�re un noeud dans la plage horaire trouv�e au dessus
    	treeModel.insertNodeInto(new DefaultMutableTreeNode(addr),scheduleNode, scheduleNode.getChildCount());
    }
    
    /*Supprimer une livraison de la liste*/
    public void remLiv (){
		//r�cup�re le noeud s�lectionn�
    	DefaultMutableTreeNode node = ((DefaultMutableTreeNode)tree.getLastSelectedPathComponent());
    	//System.out.println("node supprim� "+node);
		
		//supprime le noeud en question
    	treeModel.removeNodeFromParent(node);
    }
    
    public void setSchedule(ArrayList<Schedule> aschedules) {
        schedules = aschedules;
		//d�finition de la racine de l'arbre et declaration du model de l'arbre (contient la totalite des donnees et conditionne le comportement de l'arbre)
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Plages horaires");
        treeModel = new DefaultTreeModel(root);
		
		//creee les noeuds relatifs aux plages horaires
        for(int i=0;i<schedules.size();i++){
			DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(schedules.get(i).getSliceString());
			//treeNode.setAllowsChildren(true);
            treeModel.insertNodeInto(treeNode, root, i);
        }
        
        //creee l'arbre, edite son mode de fonctionnement, son etat initial et le met dans le scrollpane
        tree = new JTree(treeModel);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setSelectionPath(new TreePath(root));
        JScrollPane listScrollPane = new JScrollPane(tree);
        add(listScrollPane, BorderLayout.CENTER);

        /*listener qui detecte une selection sur la liste
        et enable le bouton supprimer s'il s'agit d'une livraison*/
    	tree.addTreeSelectionListener(new TreeSelectionListener() {
			
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				
				//la ligne selectionnee est une livraison?
				if(((DefaultMutableTreeNode)tree.getLastSelectedPathComponent()).getLevel()>1){
					//System.out.println("liv");
					jButtonSupprimer.setEnabled(true);
				}
				else{
					jButtonSupprimer.setEnabled(false);
				}
			}
		});
    }
	
	/*
	 * 
	 * USELESS !!!!!!!!!!!!!!!!!!!!
	 * 
	 * 
	 */
    //This listener is shared by the text field and the hire button.
    class HireListener implements ActionListener, DocumentListener {
        private boolean alreadyEnabled = false;
        private JButton button;

        public HireListener(JButton button) {
            this.button = button;
        }

        //Required by ActionListener.
        public void actionPerformed(ActionEvent e) {
            String name="";
            //User didn't type in a unique name...
            if (name.equals("") || alreadyInList(name)) {
                Toolkit.getDefaultToolkit().beep();
                //employeeName.requestFocusInWindow();
                //employeeName.selectAll();
                return;
            }
			DefaultMutableTreeNode nodebuffer = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
            int index = tree.getRowForPath(tree.getSelectionPath()); //get selected index
            if (index == -1) { //no selection, so insert at beginning
                index = 0;
            } else {           //add after the selected item
                index++;
            }

            //listModel.insertElementAt(employeeName.getText(), index);
            //If we just wanted to add to the end, we'd do this:
            //listModel.addElement(employeeName.getText());

            //Reset the text field.
            //employeeName.requestFocusInWindow();
            //employeeName.setText("");

            //Select the new item and make it visible.
            tree.setSelectionRow(index);
            tree.makeVisible(tree.getSelectionPath());
        }

        //This method tests for string equality. You could certainly
        //get more sophisticated about the algorithm.  For example,
        //you might want to ignore white space and capitalization.
        protected boolean alreadyInList(String name) {
            return treeModel.equals(new DefaultMutableTreeNode(name));
        }

        //Required by DocumentListener.
        public void insertUpdate(DocumentEvent e) {
            enableButton();
        }

        //Required by DocumentListener.
        public void removeUpdate(DocumentEvent e) {
            handleEmptyTextField(e);
        }

        //Required by DocumentListener.
        public void changedUpdate(DocumentEvent e) {
            if (!handleEmptyTextField(e)) {
                enableButton();
            }
        }

        private void enableButton() {
            if (!alreadyEnabled) {
                button.setEnabled(true);
            }
        }

        private boolean handleEmptyTextField(DocumentEvent e) {
            if (e.getDocument().getLength() <= 0) {
                button.setEnabled(false);
                alreadyEnabled = false;
                return true;
            }
            return false;
        }
    }

	
	/*
	 * 
	 * USELESS !!!!!!!!!!!!!!!!!!!!
	 * 
	 * 
	 */
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("ListDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        JComponent newContentPane = new ListLivraison();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

	/*
	 * 
	 * USELESS !!!!!!!!!!!!!!!!!!!!
	 * 
	 * 
	 */
    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

	//Retourne l'arbre
    public JTree getList() {
        return tree;
    }

	//Retourne le model de l'arbre
    public DefaultTreeModel getListModel() {
        return treeModel;
    }
    
}