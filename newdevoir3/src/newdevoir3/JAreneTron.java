package newdevoir3;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.BorderFactory;
import javax.swing.JComponent;

/**
 * classe qui représente l'arêne de jeu au complet
 *
 */
public class JAreneTron extends JComponent {
	private static final long serialVersionUID = 1L;
	
	private TronClient client;
	/**
	 * 
	 * @param width de l'arène
	 * @param height de l'arène
	 * @param client
	 */
	public JAreneTron( int width, int height, TronClient client ){
		this.client = client;
		setSize(width, height);
		setBorder(BorderFactory.createLineBorder(Color.black));
		setVisible(true);
	}
	/**
	 * redéfinission de la méthode paintComponent pour qu'elle dessine l'arêne de jeu et son contenu
	 */
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		for ( TronPlayer player : client.players ){ // pour chaque joueur
			
			g2.setColor(player.couleur);
			boolean firstPoint = true;
			Point beforePoint = null;
			
			for ( Point point : player.trace.points ) { // poue chaque point
				
				if( firstPoint ){
					g2.drawOval(point.coordonneeX, point.coordonneeY, 1, 1);
					firstPoint = false;
				}
				else {
					g2.drawLine(beforePoint.coordonneeX, beforePoint.coordonneeY, point.coordonneeX, point.coordonneeY);
				}
				
				beforePoint = point; // stocker le point courant à utiliser dans la prochaine itération
			}
		}
	}	
}
