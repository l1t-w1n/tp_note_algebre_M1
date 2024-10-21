package geste;
import java.awt.Color;
import java.awt.Graphics2D;
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

import static java.lang.Double.NaN;

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
		double[] feat = new double[11];
		Vecteur2D v = new Vecteur2D(points.getFirst(), points.get(2));
		feat[0] = v.cosinus();
		feat[1] = v.sinus();

		int minx, miny, maxx, maxy;
		minx = points.getFirst().x;
		maxx = points.getFirst().x;
		miny = points.getFirst().y;
		maxy = points.getFirst().y;
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

		PointVisible p_max = new PointVisible((int) maxx, (int) maxy);
		PointVisible p_min = new PointVisible((int) minx, (int) miny);

		Vecteur2D v1 = new Vecteur2D(p_max, p_min);
		feat[2]=v1.norme();
		feat[3]=Math.atan((double) (maxy - miny) /(maxx-minx));

		Vecteur2D v2 = new Vecteur2D(points.getFirst(), points.getLast());
		feat[4]=v2.norme();
		feat[5]=v2.cosinus();
		feat[6]=v2.sinus();

		double f8 = 0;
		double f9 = 0;
		double f10 = 0;
		double f11 = 0;

		for (int i = 0; i < points.size()-1; i++) {
			double dx_p = points.get(i+1).x - points.get(i).x;
			double dy_p = points.get(i+1).y - points.get(i).y;
			Vecteur2D v3 = new Vecteur2D(dx_p, dy_p);
			f8 += v3.norme();
		}
		feat[7] = f8;

		for (int i = 1; i < points.size() - 1; i++) {
			double dx_p = points.get(i+1).x - points.get(i).x;
			double dy_p = points.get(i+1).y - points.get(i).y;

			double dx_p_1 = points.get(i).x - points.get(i-1).x;
			double dy_p_1 = points.get(i).y - points.get(i-1).y;

			double theta = Math.atan((dx_p * dy_p_1 - dx_p_1 * dy_p) / (dx_p * dx_p_1 + dy_p * dy_p_1));

			if(!Double.isNaN(theta)){
				f9 += theta;
				f10 += Math.abs(theta);
				f11 += Math.pow(theta, 2);
			}

		}

		feat[8] = f9;
		feat[9] = f10;
		feat[10] = f11;

		features = new Vecteur(feat);
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
