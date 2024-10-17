package geste;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import algebre.Vecteur;
import algebre.Vecteur2D;
import classifieur.Featured;
import ui.Style;
import ui.io.ReadWritePoint;

public class Trace implements Featured{

	private ArrayList<PointVisible> points;
	private Style style = new Style();
	private Vecteur features;
	private boolean visible;

	public Trace(boolean model) {
		if (model)
			style = Style.getModelStyle();
		points = new ArrayList<PointVisible>();
		visible = true;
	}

	public Trace(boolean model, String fileName) {
		this(model);
		File f = new File(fileName);
		ReadWritePoint rwp = new ReadWritePoint(f);
		points = rwp.read();
	}

	public void add(Point p, long timeStamp) {
		add(new PointVisible(p.x, p.y, timeStamp));
	}

	public void add(PointVisible p) {
		points.add(p);
	}

	public void showInfos(Graphics2D g) {
		Rectangle r = computeBoundingBox();
		String features = points.size() + " points ";
		g.translate(-r.x, -r.y);
		g.scale(2, 2);
		g.drawString(features, r.x, r.y - 10);
		g.scale(.5, .5);
		g.translate(r.x, r.y);
	}

	public void draw(Graphics2D g) {
		if (visible) {
			if (style.drawLine()) {
				drawLines(g);
			}
			if (style.drawPoints()) {
				drawPoints(g);
			}
			showInfos(g);
		}
	}

	public void drawPoints(Graphics2D g) {
		for (PointVisible p : points) {
			p.dessine(g, style);
		}
	}

	public void drawLines(Graphics2D g) {
		PointVisible p1, p2;
		//g.setColor(style.color());
		g.setColor(new Color(128,128,128));
		for (int i = 0; i < points.size() - 1; i++) {
			p1 = points.get(i);
			p2 = points.get(i + 1);
			g.drawLine(p1.x, p1.y, p2.x, p2.y);
		}
	}

	public Rectangle computeBoundingBox() {
		int minx, miny, maxx, maxy;
		minx = points.get(0).x;
		maxx = points.get(0).x;
		miny = points.get(0).y;
		maxy = points.get(0).y;
		for (PointVisible p : points) {
			if (p.x < minx)
				minx = p.x;
			if (p.y < miny)
				miny = p.y;
			if (p.x > maxx)
				maxx = p.x;
			if (p.y > maxy)
				maxy = p.y;
		}
		return new Rectangle(minx, miny, maxx - minx, maxy - miny);
	}
	
	public void initFeatures() {
		// F 1-5 F 12 F 13
		/*

		cette méthode calcule les valeurs des 7 premiers features proposés dans l'article de Rubine,
		et stocke le résultat dans un attribut privé de la classe Trace dont le type est un tableau de double.

			Cosinus de l'angle initial : Cette caractéristique capture l'orientation initiale du geste en mesurant le cosinus de l'angle au début du geste.
			Sinus de l'angle initial : Similaire à la première, elle enregistre le sinus de cet angle, permettant ainsi de capturer la direction du geste.
			Longueur de la diagonale de la boîte englobante : Il s'agit de la longueur de la diagonale qui englobe le geste dans une boîte rectangulaire.
			Angle de la diagonale de la boîte englobante : Cette fonctionnalité mesure l'angle formé par la diagonale de la boîte englobante par rapport à un repère fixe.
			Distance entre le premier et le dernier point : Elle mesure la distance directe entre le point de départ et le point d'arrivée du geste.

		 */
		double [] selFeatures = new double[7];

		PointVisible p0 = this.points.get(0);
		PointVisible p2 = this.points.get(2);
		Vecteur2D v1 = new Vecteur2D(p0,p2);
		selFeatures[0]  = v1.cosinus();
		selFeatures[1] = v1.sinus();

		int xMax = p0.x;
		int yMax = p0.y;
		int xMin = p0.x;
		int yMin = p0.y;

		for (PointVisible p : this.points){
			if (p.x > xMax){
				xMax = p.x;
			}
			if (p.x < xMin){
				xMin = p.x;
			}
			if (p.y > yMax){
				yMax = p.y;
			}
			if (p.y < yMin){
				yMin = p.y;
			}
		}

		PointVisible pmax = new PointVisible(xMax, yMax);
		PointVisible pmin = new PointVisible(xMin, yMin);

		double longueurAngleMaxMin = Math.sqrt((pmax.x-pmin.x)*(pmax.x-pmin.x)+(pmax.y-pmin.y)*(pmax.y-pmin.y)	);
		selFeatures[2] = longueurAngleMaxMin;

		Vecteur2D minMax = new Vecteur2D(pmax,pmin);
		double f4 = Math.atan(minMax.tangente());
		selFeatures[3] = f4;

		PointVisible pMoins1 = this.points.get(this.points.size()-2);
		double longueurAngle0Pm1 = Math.sqrt((pMoins1.x-p0.x)*(pMoins1.x-p0.x)+(pMoins1.y-p0.y)*(pMoins1.y-p0.y));
		selFeatures[4] = longueurAngle0Pm1;

		this.features = new Vecteur(selFeatures);



	}

	public int exportWhenConfirmed(String filePath) {
		Path p = Paths.get(filePath);
		int userInput = JOptionPane.NO_OPTION;
		if (Files.exists(p)) {
			userInput = JOptionPane.showConfirmDialog(null,
					p.getFileName() + ": file exists, overwrite existing file ?", "", JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE);
			System.out.println("Export cancelled");
			if (userInput != JOptionPane.YES_OPTION)
				return userInput;
		}
		export(filePath, true);
		return userInput;
	}

	private void export(String path, boolean overwrite) {
		File f = new File(path);
		if (f.exists() && !overwrite)
			return;
		ReadWritePoint rw = new ReadWritePoint(f);
		Rectangle r = computeBoundingBox();
		int x, y;
		for (PointVisible p : points) {
			x = p.x - r.x;
			y = p.y - r.y;
			rw.add(x + ";" + y + ";" + p.getTimeStamp());
		}
		rw.write();
	}

	public void setVisible(boolean b) {
		visible = b;
	}

	public Vecteur getFeatureVector() {
		return new Vecteur(features);
	}

	public int size() {
		return points.size();
	}

}
