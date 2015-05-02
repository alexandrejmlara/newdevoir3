package newdevoir3;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

import javax.swing.*;

/**
 * classe qui comporte le programme principal du client
 */
public class TronClient implements KeyListener {

	//les dimensions de la grille de jeu virtuelle
	int gridwidth;
	int gridheight;
	//une liste de TronPlayer, qui contiendra tous les joueurs de la partie en cours
	ArrayList<TronPlayer> players;
	
	String username;
	String machineHostName;
	String userColor;
	String xStartPoint;
	String yStartPoint;
	
	String address; 
	int port;
	
	JAreneTron component;
	
	Socket socket=null;
    BufferedReader in=null;
    PrintWriter out=null;
	
    /**
     * constructeur de TronClient
     * @param serverHostName
     * @param serverPort
     */
	public TronClient(String serverHostName, int serverPort){
		
		address = serverHostName;
		port=serverPort;
		
		//initialise le tableau ou la liste de TronPlayer
		players=new ArrayList<TronPlayer>();
		
		//cherche le nom de votre machine et le nom de l'utilisateur 
		this.username = System.getProperty("user.name");
		try {
			this.machineHostName=InetAddress.getLocalHost().getHostName();
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
/*******************Composantes Graphiques***********************/
		//créer un JFrame munie d'un layout de type BorderLayout
		JFrame frame = new JFrame();
		       
		final int FRAME_WIDTH = 1000;
		final int FRAME_HEIGHT = 600;
		       
		frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		frame.setTitle("Jeu de Tron");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	                
		component = new JAreneTron(500,500, this);
		component.addKeyListener(this);
		component.setFocusable(true);
			
		JPanel p = new JPanel();
		   
		p.setLayout(new BorderLayout());
		p.add(component, BorderLayout.CENTER);
		p.addKeyListener(this);   
		// Affichage des noms d'utilisateur et de leur machine
		for(int i=0; i < players.size(); i++){
			JLabel noms = new JLabel(username+"@"+machineHostName);
			p.add(noms, BorderLayout.EAST);
		}
		   
		frame.add(p);
		p.setBackground(Color.black);
		frame.setVisible(true);
				
/*******************Comunication Reauseau***********************/
		//Établissez la socket de connection au serveur, et construisez les stream 
		//d'entrée et sortie pour communiquer avec le serveur
		try {
			socket = new Socket(address,port);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream());
		}
		catch (IOException e) {
			System.err.println("IOException pour ouvrir le Socket ou ses streams: "+e);
			System.exit(1);
		}
		
		try { // TRY PROTOCOLE
			if (socket==null || in==null || out == null) {
				System.out.println("Pas de connection au serveur: socket="+socket +", socket input stream = "+in+
				                           ", socket output stream = "+out);
				System.exit(1);
			}
			// Envoyez le nom d'utilisateur au serveur
			out.println(username+"\n");
			out.flush();
			System.out.println("nom d'utilizateur de "+username+" envouyé au serveur");
			System.out.flush();
				  	
			/*Lisez les deux lignes que le serveur vous envoie comme réponse, qui vont contenir 
			* les dimensions de la grille virtuelle de jeu, convertissez-les en entier, et 
			* mettez à jour les propriétés gridwidth et gridheight de votre classe.
			*/
			String answer1=in.readLine();
			String answer2=in.readLine();
			this.gridwidth=Integer.parseInt(answer1);
			this.gridheight=Integer.parseInt(answer2);
			System.out.println("Serveur : \n"+answer1+"\n"+answer2);
		}
		catch (IOException e) { // pour TRY PROTOCOLE
					System.err.println("Exception: I/O error trying to talk to server: "+ e);
		}

/****************FIN - Comunication Reauseau********************/
				
/*-----------------------------------FALTA PARTE GRAFICA 4.5-------------------------------*/
		
		//appel à la méthode
		handleServerMessages();
									
	}
	
	/**
	 *  methode qui ajoute un nouveau joueur à sa liste.
	 * @param login
	 * @param nom_de_machine
	 * @param couleur
	 * @param xDepart
	 * @param yDepart
	 */
	public void addPlayer(String login, String nom_de_machine, String couleur, String xDepart, String yDepart){
		TronPlayer newPlayer = new TronPlayer(login, nom_de_machine, couleur, xDepart, yDepart);
		players.add(newPlayer);
	}
	
	/**
	 * methode qui initialize la liste de players
	 */
	public void resetPlayerList(){
		players=new ArrayList<TronPlayer>();
	}

	/**
	 * méthode qui, en boucle infinie, lit une ligne du serveur, puis gère la commande ainsi reçue.
	 */
	public void handleServerMessages() {
		//boucle infinie
		while(true){
			try {
				//on lit la premiere ligne et vérifie son premier caractere
				String FirstLine = in.readLine();
				System.out.println("Serveur : \n"+FirstLine);
				System.out.flush();
				if(FirstLine.charAt(0)=='s'){
					for(int i =1;i<FirstLine.length(); i++){
						if(FirstLine.charAt(i)!='x'||FirstLine.charAt(i)!='X')
							players.get(i).trace.allonger(FirstLine.charAt(i));
					}
				}
				else if(FirstLine.charAt(0)=='+'){
					String username = FirstLine.substring(1, FirstLine.length());
					String nom_de_machine = in.readLine();
					String couleur = in.readLine();
					String xDepart = in.readLine();
					String yDepart = in.readLine();
					System.out.println(nom_de_machine+"\n"+couleur+"\n"+xDepart+"\n"+yDepart);
					System.out.flush();
					addPlayer(username, nom_de_machine, couleur, xDepart, yDepart);
					
					component.repaint();
				}
				if(FirstLine.charAt(0)=='R'){
					resetPlayerList();
					
					component.repaint();
				}
			
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

/*******************Réaction aux touches***********************/
	/**
	 * methode pour récupérer le caractère de la touche appuyée 
	 */
	@Override
	public void keyTyped(KeyEvent e) {
		char c = e.getKeyChar();
		if(c=='e'||c=='E'){
			out.println("n");
			out.flush();
		}
		else if (c=='x'||c=='X'){
			out.println("s");
			out.flush();
		}
		else if (c=='s'||c=='S'){
			out.println("e");
			out.flush();
		}
		else if (c=='d'||c=='D'){
			out.println("w");
			out.flush();
		}
	}
	@Override
	public void keyPressed(KeyEvent e) {}
	@Override
	public void keyReleased(KeyEvent e) {}

/****************FIN - Réaction aux touches********************/

	/**
	 * fonction static main() de TronClient qui se limite à récupérer les paramètres de ligne de commande, 
	 * et instancie un objet de type TronClient en lui passant ces paramètres.
	 * @param args
	 */
  	public static void main (String[] args){
		
		//on verifie s'il y a seulement 2 elements en args, si oui, on instancie un objet type TronClient
		if (args.length!=2) {
		      System.out.println("Usage: java Client <hostname> <port>"); 
		      System.exit(0);
		} else
			new TronClient(args[0],Integer.parseInt(args[1]));	  
	}
}

