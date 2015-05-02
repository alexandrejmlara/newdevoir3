package newdevoir3;

import java.awt.Color;
/**
 * classe qui contient toute l'information sur un joueur
 */
public class TronPlayer {
	
	public String nonUtilisateur, nomMachine; // nom utilisateur et nom machine
	public Color couleur; // couleur du joueur
	public int positionDepartX, positionDepartY; // position de départ du joueur
	Trace trace; // trace du joueur
	
	
	/** Constructeur pour le joueur
	 * 
	 * @param nomUtilisateur : nom de l'utilisateur
	 * @param nomMachine : nom de la machine
	 * @param couleur : couleur du joueur
	 * @param positionDepartX : coordonnée horizontale de de la position initial du joueur
	 * @param positionDepartY : coordonnée verticale de de la position initial du joueur
	 */
	public TronPlayer(String nomUtilisateur, String nomMachine, String couleur, String positionDepartX, String positionDepartY){
		
		this.nonUtilisateur = nomUtilisateur;
		this.nomMachine = nomMachine;
		
		this.couleur= new Color (Integer.parseInt(couleur));
		this.positionDepartX = Integer.parseInt("positionDepartX");
		this.positionDepartY = Integer.parseInt("positionDepartY");
		
		this.trace = new Trace(this.positionDepartX, this.positionDepartY);
	}

}
