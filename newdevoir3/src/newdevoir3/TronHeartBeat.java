package newdevoir3;

import java.awt.Color;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Random;

import newdevoir3.PlayerConnection.ConnectionException;

public class TronHeartBeat implements Runnable {
	
	ServerSocket serverSocket;
	ArrayList <PlayerConnection> connections;
	ArrayList <Thread> connectionsThreads;
	boolean isGameStarted;
	
	private BufferedReader in;
	private PrintWriter out;
	
	// Grid info
	int gridwidth, gridheight;
	
	// Grid table of true or false
	static boolean pointsAvailable[][];
	
	// Colors
	Boolean [] pickedColors = new Boolean[10];
	Color [] couleurs = {Color.RED, Color.GREEN, Color.BLUE, Color.CYAN, 
							Color.MAGENTA, Color.YELLOW, Color.PINK, Color.ORANGE,
							Color.DARK_GRAY, Color.LIGHT_GRAY};
	
	public TronHeartBeat(int serverPort, int gridwidth, int gridheight){
		isGameStarted = false;
		connections = new ArrayList<PlayerConnection>();
		connectionsThreads = new ArrayList<Thread>();
		initializePickedColorsArray();
		this.gridwidth = gridwidth;
		this.gridheight = gridheight;
		pointsAvailable = new boolean[gridwidth][gridheight];
		
		System.out.println("Initializing server...");
		try { 
			// Initializing the server TCP socket
			serverSocket = new ServerSocket(serverPort);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Server initialized...");
		
	}
	
	private void initializeDirectionMatrix(){
		pointsAvailable = new boolean[gridwidth][gridheight];
		
		for( int i = 0; i < gridwidth; i++ )
			for ( int j = 0; j < gridheight; j++ )
				pointsAvailable[i][j] = false;
		
	}
	
	private void sendMessage( PlayerConnection player, String message ){
		
		player.sendMessage(message);
		
	}
	
	private void sendMessageToAll( String message ){
		
		for ( PlayerConnection player : connections ){
			player.sendMessage(message);
		}
		System.out.println( "Message \'" + message + "\' " + "sent do all players" );
		
	}
	
	private void moveAllPlayers(){
		for ( PlayerConnection player : connections ){
			if( !player.isPlayerDead ) player.move();
		}
		System.out.println( "Moving all players" );
	}
	
	private void sendDirectionToAll(){
		String message = "s";
		for ( PlayerConnection player : connections ){
			message += player.direction;
		}
		System.out.println( "Sending direction \' " + message + " \' to all players" );
		sendMessageToAll(message);
	}
	
	private void sendPlayerInfoToAll( ){
		for ( PlayerConnection player : connections ){
			sendMessageToAll("+" + player.login);
			sendMessageToAll(player.hostname);
			sendMessageToAll(Integer.toString(generatePlayerColor().getRGB()));
			
			// Sending X and Y randomly
			Random rd = new Random();
			int positionX = rd.nextInt(gridwidth);
			int positionY = rd.nextInt(gridheight);
			pointsAvailable[positionX][positionY] = true; // Setting position occupied
			sendMessageToAll(String.valueOf(positionX));
			sendMessageToAll(String.valueOf(positionY));
			
			// Setting current position of each player on the server
			player.positionX = positionX;
			player.positionY = positionY;			
		}
		
		System.out.println( "Player info sent to all players..." );
		
	}
	
	private Color generatePlayerColor(){
		Random rd = new Random();
		int colorIndex = rd.nextInt(couleurs.length);
		
		// In the case of all cards already were picked, reinitialize the array
		// to choose a repeated one randomly
		if( isAllCardsPicked() ) initializePickedColorsArray();
		
		while( pickedColors[colorIndex] ){
			colorIndex = rd.nextInt(couleurs.length);			
		}
		
		Color color = couleurs[colorIndex];
		pickedColors[colorIndex] = true;
		
		return color;
	}

	private void initializePickedColorsArray(){
		
		for ( int i = 0; i < pickedColors.length; i++ ){
			pickedColors[i] = false;
		}
		
	}
	
	private boolean isAllCardsPicked(){
		
		boolean isCardsPicked = true;
		for ( int i = 0; i < pickedColors.length; i++ ){
			if(!pickedColors[i]) {
				isCardsPicked = false;
				break;
			}
		}
		
		return isCardsPicked;
		
	}

	@Override
	public void run() {
		// Accept incoming connections while game doesn't start
		while( !isGameStarted ){
			
			Socket clientSocket = null;
				
			try {
				// If 2 players or more in the game, wait for only 10 seconds
				if ( connections.size() > 1 ) serverSocket.setSoTimeout (10000); 
				
				// Accepting connections
				clientSocket = serverSocket.accept();
				System.out.println("Client connection accepted...");
				
				// Creating a player connection and adding it to the list of connections
				PlayerConnection pc = new PlayerConnection(clientSocket, gridwidth, gridheight);
				connections.add(pc);
				
				// Creating a thread and adding it to the list of threads
				Thread th = new Thread(pc);
				th.start();
				connectionsThreads.add(th);
			} catch ( SocketTimeoutException ste ) {
				//ste.printStackTrace();
				System.out.println("The game is starting now.");
				
				// Sending width and height to all players
				sendMessageToAll(String.valueOf(gridwidth));
				sendMessageToAll(String.valueOf(gridheight));
				
				// Sending player info to all players
				sendPlayerInfoToAll();
				
				isGameStarted = true;
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ConnectionException e) {
				e.printStackTrace();
			}		
			
		}
		
		// Set direction of all to North
		for ( PlayerConnection player : connections ){
			player.direction = 'n';			
		}
		
		// Game started
		while(true){
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			moveAllPlayers();
			sendDirectionToAll();
		}
		
	}
}
