package geste;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Geste {
	private String nom;
	private Image img;
	private ArrayList<Trace> traces; 
	private Trace modele;
	
	public Geste(String nom) {
		this.nom = nom;
		load(nom);
	}
	
	public void load(String name) {
		traces = new ArrayList<Trace>();
		File wd = new File(ui.config.Parameters.defaultFolder +"/" +name);
		//System.out.println("loading traces from "+wd.getAbsolutePath());
		
		if (wd.isDirectory()) {
			for (File f:wd.listFiles()) {
				if (!(f.isDirectory())){
					if (f.getName().equals(wd.getName() + "-" + ui.config.Parameters.baseModelName+ ".csv"))
						modele = new Trace(true, f.getAbsolutePath());
					else 
						addTrace(new Trace(false, f.getAbsolutePath()));
				}
			}
		}else {
			System.out.println ("warning: loading data failed");
		}
	}
	
	public void exportImage(String path) {
		if (traces.size() > 0) {
			Rectangle bb = traces.get(0).computeBoundingBox();
			BufferedImage img = new BufferedImage(bb.width, bb.height, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2d = (Graphics2D) img.getGraphics();
			g2d.clipRect(bb.x, bb.y, bb.width, bb.height);
			try {
				ImageIO.write(img, "PNG", new java.io.File(path + nom + ".png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Warning : trying to export png on empty traces");
		}
	}

	public ArrayList<Trace> getTraces() {
		return this.traces;
	}

	public void addTrace(Trace t) {
		traces.add(t);	
	}

	public Trace get(int i) {
		return traces.get(i);
	}

	public void clear() {
		traces.clear();		
	}

	public String getName() {
		return nom;
	}

	public void hide() {
		for (Trace t: traces)
			t.setVisible(false);
	}

	public void drawModel(Graphics2D g) {
		if (modele == null) System.out.println("this model is null");
		else {
			g.translate(20, 20);
			modele.drawLines(g);
		}
	}

	public void drawModel(Graphics2D g2d, int i, int j) {
		if (modele == null) System.out.println("this model is null");
		else {
			AffineTransform t = g2d.getTransform();
			g2d.translate(20*i, 20*j);
			modele.drawLines(g2d);
			g2d.setTransform(t);
		}
		
	}
	


}
