package geste;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import algebre.Vecteur;
import algebre.Vecteur2D;
import ui.Style;
import ui.io.ReadWritePoint;

public class Trace {
	private ArrayList<PointVisible> points;
	private Style style = new Style();
	private Vecteur features;
	private boolean visible;

	public Trace(boolean model) {
		if (model)
			style = Style.getModelStyle();
		points = new ArrayList<PointVisible>();
		features = new Vecteur(13);
		visible = true;
	}

	public Trace(boolean model, String fileName) {
		this(model);
		File f = new File(fileName);
		ReadWritePoint rwp = new ReadWritePoint(f);
		if (model)
			System.out.println("loading model from " + f.getAbsolutePath());
		points = rwp.read();
	}
	public List<PointVisible> getPoints() {
		return points;
	}

	public void setPoints(List<PointVisible> points) {
		this.points.clear();
		this.points.addAll(points);
	}

	public void resample(double epsilon) {
		List<PointVisible> resampledPoints = new ArrayList<>();
		List<PointVisible> points = this.getPoints();

		if (points.size() < 2) {
			return;
		}

		PointVisible prevPoint = points.getFirst();
		resampledPoints.add(prevPoint);

		for (int i = 1; i < points.size(); i++) {
			PointVisible currentPoint = points.get(i);
			double distance = Math.sqrt(Math.pow(currentPoint.x - prevPoint.x, 2) + Math.pow(currentPoint.y - prevPoint.y, 2));

			if (distance >= epsilon) {
				resampledPoints.add(currentPoint);
				prevPoint = currentPoint;
			}
		}
		this.setPoints(resampledPoints);
	}

	public void add(Point p) {
		add(new PointVisible(p.x, p.y));
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
		g.setColor(style.color());
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

	public double traceLength() {
		double x = points.get(0).x, y = points.get(0).y, length = 0;
		Vecteur2D subPath = new Vecteur2D(x, y);
		for (int i = 1; i < points.size(); i++) {
			x = points.get(i).x - x;
			y = points.get(i).x - y;
			subPath.setCoords(x, y);
			length += subPath.norme();
		}
		return length;
	}

	public int traceNbOfPoints() {
		return points.size();
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
		System.out.println("Export " + p);
		export(filePath, true);
		return userInput;
	}

	public void export(String path, boolean overwrite) {
		File f = new File(path);
		if (f.exists() && !overwrite)
			return;
		ReadWritePoint rw = new ReadWritePoint(f);
		Rectangle r = computeBoundingBox();
		int x, y;
		for (PointVisible p : points) {
			x = p.x - r.x;
			y = p.y - r.y;
			rw.add(x + ";" + y + ";" + p.toString());
		}
		rw.write();
	}

	public void setVisible(boolean b) {
		visible = b;
	}

}
