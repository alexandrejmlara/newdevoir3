package newdevoir3;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class TronClient implements KeyListener {

	int gridwidth;
	int gridheight;
	
	String username;
	String machineHostName;
	String userColor;
	String xStartPoint;
	String yStartPoint;
	
	String address; 
	int port;
	
	ArrayList<TronPlayer> players;
	
	Socket socket=null;
    BufferedReader in=null;
    PrintWriter out=null;
	
	public TronClient(String serverHostName, int serverPort){
		
		address = serverHostName;
		port=serverPort;
		
		players=new ArrayList<TronPlayer>();
		
		//chercher le nom de votre machine et le nom de l'utilisateur 
		this.username = System.getProperty("user.name");
		try {
			this.machineHostName=InetAddress.getLocalHost().getHostName();
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
				
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
				
/*--------------------FALTA PARTE GRAFICA 4.5--------------------*/
		
		//appel à la méthode
		handleServerMessages();
									
	}
	
	
	public void addPlayer(String login, String nom_de_machine, String couleur, String xDepart, String yDepart){
		TronPlayer newPlayer = new TronPlayer(login, nom_de_machine, couleur, xDepart, yDepart);
		players.add(newPlayer);
	}
	
	/**
	 * Reinitialize la liste de players
	 */
	public void resetPlayerList(){
		players=new ArrayList<TronPlayer>();
	}

	public void handleServerMessages() {
		//boucle infinie
		while(true){
			try {
				String FirstLine = in.readLine();
				System.out.println("Serveur : \n"+FirstLine);
				System.out.flush();
				if(FirstLine.charAt(0)=='s'){
					for(int i =1;i<FirstLine.length(); i++){
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
				}
				if(FirstLine.charAt(0)=='R'){
					resetPlayerList();
				}
			
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

/*******************Réaction aux touches***********************/
	@Override
	public void keyTyped(KeyEvent e) {
		char c = e.getKeyChar();
		System.out.println(c);
	}
	@Override
	public void keyPressed(KeyEvent e) {}
	@Override
	public void keyReleased(KeyEvent e) {}

/****************FIN - Réaction aux touches********************/
	
	
	
  	public static void main (String[] args){
		
		//on verifie s'il y a seulement 2 elements en args, si oui, on instancie un objet type TronClient
		if (args.length!=2) {
		      System.out.println("Usage: java Client <hostname> <port>"); 
		      System.exit(0);
		} else
			new TronClient(args[0],Integer.parseInt(args[1]));	  
	}
}

