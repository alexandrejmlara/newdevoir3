package newdevoir3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class PlayerConnection implements Runnable {

	String login;
	String hostname;
	char direction;
	boolean isPartieEnCours;
	boolean isPlayerDead;
	int gridwidth; 
	int gridheight;
	
	// Current position
	int positionX;
	int positionY;
	
	private Socket clientSocket;
	private BufferedReader in;
	private PrintWriter out;
	
	/**
	 * Constructeur de la classe qui Établissez la socket de connection, et construisez les stream 
	 * d'entrée et sortie pour communiquer
	 * @param clientSocket
	 * @throws ConnectionException
	 */
	 public PlayerConnection(Socket clientSocket, int gridwidth, int gridheight) throws ConnectionException {
		 this.clientSocket = clientSocket;
		 this.gridwidth=gridwidth;
		 this.gridheight=gridheight;
		 isPlayerDead = false;
		 
		 
		 try {
			 in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		     out = new PrintWriter(clientSocket.getOutputStream());
		 }
		 catch (IOException e) {
			 System.err.println("Exception en ouvrant les streams du client: " + e);
		     throw new ConnectionException(e);
		 }
		 
	 }
	 
	 /*************************************nao sei se tem que usar esse trem nao*********************************/
	 protected void finalize() {
		 try {
			 // cleanup
		     out.close();
		     in.close();
		     clientSocket.close();
		 }
		 catch (IOException e) {
			 System.err.println("IOException en fermant les stream et le socket client" + e);
		 }
	 }
	 
	public void sendMessage(String message){
		out.println(message);
		out.flush();
	}
	
	public void move(){
		
		switch(direction){
			case 'n': // direction vers le haut
				positionY--;
				break;
			case 'w': // direction vers la gauche (west)
				positionX--;
				break;
			case 's': // direction vers le bas
				positionY++;
				break;
			case'e': // direction vers la droite (east)
				positionX++;
				break;
			default:
				System.out.println("commande invalide");
		}
		
		// Checking if one position hit another position		
		if ( TronHeartBeat.pointsAvailable[positionX][positionY] || 
				positionX < 0 || positionX > gridwidth || 
				positionY < 0 || positionY > gridheight ) {
			isPlayerDead = true;
			direction = 'x';
		}
			
		TronHeartBeat.pointsAvailable[positionX][positionY] = true; 
		
		
	}

	@Override
	public void run() {
		
		try {
			//Lit le username du joueur evoyé par le client
			login = in.readLine();
			
			//découvrir le nom de machine du client
			hostname = clientSocket.getInetAddress().getHostName();
			
			//
			/********************************fazer uma execao que presta, pfvr*******************************/	
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Accepting messages from the client
		while(true){
			
			try {
				String message = in.readLine();
				
				if( !isPlayerDead ) direction = message.charAt(0);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	      
		
	}

	class ConnectionException extends Exception { 
		  Exception e;
		  ConnectionException(Exception e) { this.e = e; }
		  ConnectionException(String msg) { super(msg); e=this; }
	}
	
}
