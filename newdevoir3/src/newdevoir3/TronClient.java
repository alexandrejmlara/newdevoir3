package newdevoir3;

import TronPlayer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class TronClient {

	int gridwidth;
	int gridheight;
	
	String username;
	String machineHostName;
	String userColor;
	String xStartPoint;
	String yStartPoint;
	
	ArrayList<TronPlayer> players;
	
	public TronClient(String serverHostName, int serverport){
		
		players=new ArrayList<TronPlayer>();
		
		//chercher le nom de votre machine et le nom de l'utilisateur 
				this.username = System.getProperty("user.name");
				try {
					this.machineHostName=InetAddress.getLocalHost().getHostName();
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	}
	
	
	public static void main(String[] args){
			
			//on verifie s'il y a seulement 2 elements en args, si oui, on instancie un objet type TronClient
			if (args.length!=2) {
			      System.out.println("Usage: java Client <hostname> <port>"); 
			      System.exit(0);
			} else
				new TronClient(args[0],Integer.parseInt(args[1]));	  
	}
}
