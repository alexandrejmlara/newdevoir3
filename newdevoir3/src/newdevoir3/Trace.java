package newdevoir3;

import java.util.ArrayList;

/**
 * classe pour représenter le tracé d'un joueur (et possiblement le mur d'enceinte)
 */
public class Trace {

	public int positionX, positionY, longueur;
	
	//liste de Points considérés reliés. 
	public ArrayList<Point> points;
	
	
	/** Constructeur de la trace pour un joueur 
	 * @param initialX : coordonnée horizontale de départ du joueur
	 * @param initialY : coordonnée verticale de départ du joueur
	 */
	public Trace(int initialX, int initialY){
			
			this.positionX = initialX;
			this.positionY = initialY;
			
			// nouveau point servant de point de départ pour la trace
			Point p = new Point (initialX,initialY);
			
			this.points = new ArrayList<Point>();
			this.points.add(p);
			
			this.longueur = 1;
	}
	
	/** Méthode pour allonger la trace
	 * @param direction : input du joueur donnant la direction de déplacement (possibilité: n, s, w, e)
	 */
	public void allonger(char direction) {
			
			
			switch(direction){
				case 'n': // direction vers le haut
					positionY--;
					this.points.add(new Point(positionX, positionY));
					this.longueur++;
					break;
				case 'w': // direction vers la gauche (west)
					positionX--;
					this.points.add(new Point(positionX, positionY));
					this.longueur++;
					break;
				case 's': // direction vers le bas
					positionY++;
					this.points.add(new Point(positionX, positionY));
					this.longueur++;
					break;
				case'e': // direction vers la droite (east)
					positionX++;
					this.points.add(new Point(positionX, positionY));
					this.longueur++;
					break;
				default:
					System.out.println("commande invalide");
			}
	}
}


