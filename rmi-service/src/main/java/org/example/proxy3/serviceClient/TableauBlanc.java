import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.rmi.Remote;
import java.rmi.RemoteException;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class TableauBlanc implements ServiceTableauBlanc, Remote {
    JPanel    canevas;
    Color     couleur_courante   = Color.black;
    Color[]   couleurs_possibles = { Color.black, Color.white, Color.blue, Color.green, Color.red, Color.yellow };



	public void afficherMessage(Dessin d) { // Dessiner un dessin sur le canevas
	Graphics g = canevas.getGraphics();
	g.setColor( d.c );
	g.fillRect( d.x - 5, d.y - 5, 10, 10 );


    }
       
    public JPanel CreerPaletteCouleur() { // Pallette des couleurs possibles
		final JPanel p = new JPanel() {
			public void paint(Graphics g) {
				for (int i = 0; i < couleurs_possibles.length; i++) {
				int dx = (int) ((300.0/couleurs_possibles.length) * i);
				int tx = (int) ((300.0/couleurs_possibles.length));
				g.setColor( couleurs_possibles[i] );
				if ( ! couleur_courante.equals(couleurs_possibles[i]) )
					g.fillRect(dx, 0, tx, 20);
				else
					g.drawRect(dx, 0, tx, 20);
				}
			}
	};

	p.setPreferredSize(new Dimension(300,20));
 
	p.addMouseListener(new MouseListener() {
		public void mousePressed(MouseEvent arg0) { // changer de couleur
		    int i = (int) ( (arg0.getX()) / (300./couleurs_possibles.length) );
		    couleur_courante = couleurs_possibles[i];
		    p.repaint();            
		}
        
		public void mouseReleased(MouseEvent arg0) { }
		public void mouseExited(MouseEvent arg0) { }
		public void mouseEntered(MouseEvent arg0) { }
		public void mouseClicked(MouseEvent arg0) { }
	    });
    
	return (p);
    }

    public TableauBlanc(ServiceDistributeur serviceDistrib) {
        canevas = new JPanel();                                      // Creation de la zone de dessin
        canevas.setPreferredSize( new Dimension(300, 300) );
        JPanel palette = CreerPaletteCouleur();                      // Creation de la zone de couleur (voir plus haut)
        JPanel global = new JPanel();                                // Creer un panel global et y poser la palette et le canevas
        global.setLayout( new BorderLayout() );
        global.add( canevas, BorderLayout.CENTER);
        global.add( palette, BorderLayout.SOUTH);
        JFrame fenetre = new JFrame();                               // Creer une fenetre avec le panel global
        fenetre.setContentPane( global );
        fenetre.pack();
        fenetre.setVisible( true );
        fenetre.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
    
        canevas.addMouseMotionListener( new MouseMotionListener() {  // Gestion des mouvements de souris
		public void mouseDragged(MouseEvent arg0) {
		    Dessin d = new Dessin();
		    d.x = arg0.getX();
		    d.y = arg0.getY();
		    d.c = couleur_courante;
            
		    afficherMessage(d);                              // Voir la methode plus haut

			try {
				serviceDistrib.distribuerMessage(d);
			}
			catch (RemoteException e)
			{
				e.printStackTrace();
			}

		}        
		public void mouseMoved(MouseEvent arg0) { }
	    });
    }
}
