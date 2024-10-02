package ui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import geste.Lexique;

public class MainWindow {

	public static JFrame createTrainingWindow(int x, int y, int w, int h) {
		
		JFrame jf = new JFrame("Training");
		jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		Vue v1 = new Vue(w, h);
		TrainingAssistant ta = new TrainingAssistant(jf, v1, true);
		v1.setTrainingAssistant(ta);
		jf.add(ta, BorderLayout.NORTH);
		jf.add(new JScrollPane(v1));
		jf.pack();
		jf.setLocation(x, y);
		return jf;
	}
	
	public static JFrame createLexiconWindow(Lexique l) {
		JFrame jf = new JFrame("Lexique");
		jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		Vue v1 = new Vue(400, 400);
		v1.setLexicon(l);
		jf.add(new JScrollPane(v1));
		jf.pack();
		jf.setLocation(500,50);	
		return jf;
	}

}
