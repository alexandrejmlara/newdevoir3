package newdevoir3;

import java.util.ArrayList;

public class Trace {

	public int positionX, positionY, longueur;
	public ArrayList<Point> trace;
	
	public Trace(int initialX, int initialY){
			
			this.positionX = initialX;
			this.positionY = initialY;
			
			Point p = new Point (initialX,initialY);
			
			this.trace = new ArrayList<Point>();
			this.trace.add(p);
			
			this.longueur = 1;
	}
	
public void allonge(char direction) {
		
		int x, y;
		
		switch(direction){
			case 'n': // direction vers le haut
				x = positionX;
				y = positionY - 1;
				this.trace.add(new Point(x, y));
				this.longueur++;
				break;
			case 'w': // direction vers la gauche (west)
				x = positionX - 1;
				y = positionY;
				this.trace.add(new Point(x, y));
				this.longueur++;
				break;
			case 's': // direction vers le bas
				x = positionX;
				y = positionY + 1;
				this.trace.add(new Point(x, y));
				this.longueur++;
				break;
			case'e': // direction vers la droite (east)
				x = positionX + 1;
				y = positionY;
				this.trace.add(new Point(x, y));
				this.longueur++;
				break;
			default:
				System.out.println("commande invalide");
		}
	}
}
