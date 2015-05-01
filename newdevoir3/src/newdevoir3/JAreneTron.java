package newdevoir3;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.BorderFactory;
import javax.swing.JComponent;

public class JAreneTron extends JComponent {
	private static final long serialVersionUID = 1L;
	
	private TronClient client;
	
	public JAreneTron( int width, int height, TronClient client ){
		this.client = client;
		setSize(width, height);
		setBorder(BorderFactory.createLineBorder(Color.black));
		setVisible(true);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		
		for ( TronPlayer player : client.players ){ // For each player
			
			g2.setColor(player.couleur);
			boolean firstPoint = true;
			Point beforePoint = null;
			
			for ( Point point : player.trace.points ) { // For each point
				
				if( firstPoint ){
					g2.drawOval(point.coordonneeX, point.coordonneeY, 1, 1);
					firstPoint = false;
				}
				else {
					g2.drawLine(beforePoint.coordonneeX, beforePoint.coordonneeY, point.coordonneeX, point.coordonneeY);
				}
				
				beforePoint = point; // Storing the current point to be used in the next iteration
				
			}
			
		}
	}
	
}
