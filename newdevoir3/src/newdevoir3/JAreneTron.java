package newdevoir3;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;

public class JAreneTron extends JComponent {
	private static final long serialVersionUID = 1L;
	static int w1 = 10, h1 = 20, w2 = 100, h2 = 200;
	
	private TronClient client;
	
	public JAreneTron( TronClient client ){
		this.client = client;
		setSize(500, 500);
		setBorder(BorderFactory.createLineBorder(Color.black));
		setVisible(true);
		repaint();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		for ( TronPlayer player : client.players ){
			
		}
		
		g2.setColor(Color.GREEN);
		
		w1 += 20;
		h1 += 20;
		w2 += 20;
		h2 += 20;
		
		g2.drawLine(w1, h1, w2, h2);
	}
	
	public static void main(String[] args) {
		JFrame testFrame = new JFrame();
		JAreneTron jat = new JAreneTron(null);
		jat.setPreferredSize(new Dimension(500, 500));
		testFrame.add(jat);
		
		testFrame.pack();
		testFrame.setVisible(true);
    }
	
}
