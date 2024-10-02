package ui;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import geste.Geste;
import geste.Lexique;
import geste.Trace;

public class Vue extends JPanel {
	private Color bgColor;
	private Color fgColor; 
	private int width, height;
	private Tracker tracker;

	private TrainingAssistant trainingAssistant;
	private Lexique lexicon;
		
	public Vue(int width, int height) {
		super();
		this.bgColor = Couleur.bg; 
		this.fgColor = Couleur.fg; 
		this.width = width;
		this.height = height;	
		this.setBackground(Couleur.bg);
		this.setPreferredSize(new Dimension(width, width));
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setPaintMode(); 
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,	RenderingHints.VALUE_ANTIALIAS_ON);

		if (trainingAssistant != null) {
			trainingAssistant.showInfos(g2d, width, height);
			if(tracker.trace != null)
				tracker.trace.draw(g2d);
		}
		if (lexicon != null)
			lexicon.draw(g2d);
	}

	public void displayTraces(Geste geste, Graphics2D g2d) {
		g2d.setColor(fgColor);
		g2d.translate(10, 10);
		if (geste != null)
			for (Trace go: geste.getTraces()) {
				go.draw(g2d);
			}
	}
	
	public void setTrainingAssistant(TrainingAssistant ta) {
		trainingAssistant = ta;
		tracker = new Tracker(this, ta);
		this.addMouseListener(tracker);
		this.addMouseMotionListener(tracker);		
	}

	public void setLexicon(Lexique l) {
		lexicon = l;		
	}
}
