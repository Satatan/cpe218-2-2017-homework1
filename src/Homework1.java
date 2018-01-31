/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
 
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
 
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
 
import java.net.URL;
import java.io.IOException;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.ImageIcon;
import javax.swing.tree.DefaultTreeCellRenderer;
 
 
public class Homework1 {
 
    
    public static String input = "251-*32*+";
    public static int inputlength = input.length()-1;    
    
   
    public static void main(String[] args) {
        
        if (args.length > 0) {
            input = args[0];
            inputlength = input.length() - 1;
        }
         
        inorder(new Node(input.charAt(inputlength)));
        System.out.print(infix(root));
        System.out.println(" = " + calculate(root));
       
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                GUI.createAndShowGUI();
            }
        });        
    }
    static Node root;
   
    public static class Node {
        Node left;
        Node right;
        char key;
        public Node(char data) {
            this.key = data;
        }
        public String toString() {
            return Character.toString(key);
        }
    }
   
    public static boolean isOperator(Node n){
        return n.key == '+' || n.key == '-' || n.key == '*' || n.key == '/';
    }
   
    public static boolean isMath(Node n){
        return n.key == '+' || n.key == '-' || n.key == '*' || n.key == '/' || n.key == '0' || n.key == '1' || n.key == '2' || n.key == '3' || n.key == '4' || n.key == '5' || n.key == '6' || n.key == '7' || n.key == '8' || n.key == '9';
    }
   
    public static void inorder(Node n){
        if(root == null){
            root = n;
            inputlength--;
        }
        if(isOperator(n)){
            n.right = new Node(input.charAt(inputlength));
            inputlength--;
            inorder(n.right);
            n.left = new Node(input.charAt(inputlength));
            inputlength--;
            inorder(n.left);
        }
        else if(!isMath(n)){
            System.out.print("error");          
        }
    }
     
    static int num = 1;
    
    public static String infix(Node n){
        num = 1;      
        return infix2(n);
    }
    
    public static String infix2(Node n){   
        String output = "";
        if(isOperator(n) && num == 1){
            num++;
            output += infix2(n.left);           
            output += n.key;
            output += infix2(n.right);           
        }  
        else if(isOperator(n)){
            output += '(';
            output += infix2(n.left);
            output += n.key;
            output += infix2(n.right);
            output += ')';
        }
        else {
            output += Character.getNumericValue(n.key);
        }
        return output;
    }
    
    public static int calculate(Node n){
       
        if(isOperator(n)){
            if(n.key == '+'){
                return calculate(n.left) + calculate(n.right);
            }
            else if(n.key == '-'){
                return calculate(n.left) - calculate(n.right);
            }
            else if(n.key == '*'){
                return calculate(n.left) * calculate(n.right);
            }
            else if(n.key == '/'){
                return calculate(n.left) / calculate(n.right);
            }    
        }
        else if((isMath(n))){
            return Character.getNumericValue(n.key);
        }
        return 0;
    }
}
 
    class GUI extends JPanel
                      implements TreeSelectionListener {
    private JEditorPane htmlPane;
    private JTree tree;
    private URL helpURL;
    private static boolean DEBUG = false;
 
    //Optionally play with line styles.  Possible values are
    //"Angled" (the default), "Horizontal", and "None".
    private static boolean playWithLineStyle = false;
    private static String lineStyle = "Horizontal";
     
    //Optionally set the look and feel.
    private static boolean useSystemLookAndFeel = false;
 
    public GUI() {
        super(new GridLayout(1,0));
 
        //Create the nodes.
        DefaultMutableTreeNode top =
            new DefaultMutableTreeNode(Homework1.root);
        createNodes(top);
 
        //Create a tree that allows one selection at a time.
        tree = new JTree(top);
        tree.getSelectionModel().setSelectionMode
                (TreeSelectionModel.SINGLE_TREE_SELECTION);
 
        //Listen for when the selection changes.
        tree.addTreeSelectionListener(this);
 
        if (playWithLineStyle) {
            System.out.println("line style = " + lineStyle);
            tree.putClientProperty("JTree.lineStyle", lineStyle);
        }
 
        //Create the scroll pane and add the tree to it.
        JScrollPane treeView = new JScrollPane(tree);
 
        //Create the HTML viewing pane.
        htmlPane = new JEditorPane();
        htmlPane.setEditable(false);
        initHelp();
        JScrollPane htmlView = new JScrollPane(htmlPane);
 
        //Add the scroll panes to a split pane.
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setTopComponent(treeView);
        splitPane.setBottomComponent(htmlView);
 
        Dimension minimumSize = new Dimension(100, 50);
        htmlView.setMinimumSize(minimumSize);
        treeView.setMinimumSize(minimumSize);
        splitPane.setDividerLocation(100);
        splitPane.setPreferredSize(new Dimension(500, 300));
 
        //Add the split pane to this panel.
        add(splitPane);
       
        ImageIcon leafIcon = createImageIcon("middle.gif");
            if (leafIcon != null) {
            DefaultTreeCellRenderer renderer =
            new DefaultTreeCellRenderer();
           
            renderer.setOpenIcon(leafIcon);
            renderer.setClosedIcon(leafIcon);
           
            tree.setCellRenderer(renderer);
}
    }

    public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                           tree.getLastSelectedPathComponent();
       
        Homework1.Node n = (Homework1.Node)node.getUserObject();
       
        if (tree == null) return;
 
        htmlPane.setText(Homework1.infix(n)+"="+Homework1.calculate(n));
       
    }
   
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = GUI.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
   
    private class BookInfo {
        public String bookName;
        public URL bookURL;
 
        public BookInfo(String book, String filename) {
            bookName = book;
            bookURL = getClass().getResource(filename);
            if (bookURL == null) {
                System.err.println("Couldn't find file: "
                                   + filename);
            }
        }
 
        public String toString() {
            return bookName;
        }
    }
 
    private void initHelp() {
        String s = "TreeDemoHelp.html";
        helpURL = getClass().getResource(s);
        if (helpURL == null) {
            System.err.println("Couldn't open help file: " + s);
        } else if (DEBUG) {
            System.out.println("Help URL is " + helpURL);
        }
 
        displayURL(helpURL);
    }
 
    
    private void displayURL(URL url) {
        try {
            if (url != null) {
                htmlPane.setPage(url);
            } else { //null url
        htmlPane.setText("File Not Found");
                if (DEBUG) {
                    System.out.println("Attempted to display a null URL.");
                }
            }
        } catch (IOException e) {
            System.err.println("Attempted to read a bad URL: " + url);
        }
    }
 
    private static void createNodes(DefaultMutableTreeNode top) {
        Homework1.Node n = (Homework1.Node)top.getUserObject();        
        if(n.left!= null) {
            DefaultMutableTreeNode node1 = new DefaultMutableTreeNode(n.left);         
            top.add(node1);
            createNodes(node1);
            DefaultMutableTreeNode node2 = new DefaultMutableTreeNode(n.right);
            top.add(node2);
            createNodes(node2);
        }
    }
 
    public static void createAndShowGUI() {
        if (useSystemLookAndFeel) {
            try {
                UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                System.err.println("Couldn't use system look and feel.");
            }
        }
 
        //Create and set up the window.
        JFrame frame = new JFrame("TreeDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        //Add content to the window.
        frame.add(new GUI());
 
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
} 
