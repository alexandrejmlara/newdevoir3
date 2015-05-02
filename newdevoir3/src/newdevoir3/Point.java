package newdevoir3;

/**
 * classe pour représenter des points avec leurs coordonnées entières dans la grille de jeu virtuelle.
 *
 */
public class Point {

public int coordonneeX, coordonneeY;
	
	
	/** Constructeur de Point
	 * @param x : coordonnée horizontale du point
	 * @param y : coordonnée verticale du point
	 */
	public Point(int x, int y){
		
		this.coordonneeX = x;
		this.coordonneeY = y;
	}
}
