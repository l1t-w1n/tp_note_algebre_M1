package ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;

import geste.Geste;
import geste.Lexique;
import geste.PointVisible;
import geste.Trace;
import ui.config.Parameters;

public class TrainingAssistant extends JToolBar {
	JFileChooser jfc;
	JFrame toplevel;
	Vue vue;
	File importFile, exportFile;

	private boolean training;
	private JFrame lexiconWindow;
	private Lexique lexicon;
	private int currentGesture, currentTrace, numberOfTraces = 10;
	private String currentMessage;

	public TrainingAssistant(JFrame f, Vue v1, boolean training) {
		lexicon = new Lexique();
		vue = v1;
		toplevel = f;
		jfc = new JFileChooser();
		jfc.setCurrentDirectory(new File(Parameters.defaultFolder));
		jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);



		initLexicon();
		initTraining();
	}

	private void initTraining() {
		
		training = true;
		currentGesture = 0;
		currentTrace = 0;
		currentMessage = "Dessiner le geste qui s'affiche de la manière la plus précise possible";
		vue.repaint();
	}

	public void showInfos(Graphics2D g2d, int width, int height) {
		if (training) {
			g2d.setColor(Color.white);
			g2d.drawString(currentMessage, width / 2, 20);
			int i = currentTrace + 1;
			g2d.drawString("Essai courant: " + i + "(/ " + numberOfTraces + ")", 3 * width / 4, 40);
			(lexicon.get(currentGesture)).drawModel(g2d);
		}
	}

	void initLexicon() {
		String dirName, fileName;
		File dir, f;
		for (int i = 0; i < 16; i++) {
			dirName = Parameters.defaultFolder + "/" + Parameters.baseGestureFileName + i;
			dir = new File(dirName);
			if (!(dir).exists())
				dir.mkdir();
			fileName = Parameters.defaultFolder + "/" + Parameters.lexiconFolder + "/" + Parameters.baseTraceFileName
					+ "-" + i + ".csv";
			f = new File(fileName);
			if (f.exists()) {
				System.out.println(" copying lexicon from " + fileName);
				try {
					Files.copy(
							FileSystems.getDefault().getPath(Parameters.defaultFolder, Parameters.lexiconFolder,
									Parameters.baseTraceFileName + "-" + i + ".csv"),
							FileSystems.getDefault().getPath(Parameters.defaultFolder,
									Parameters.baseGestureFileName + i,
									Parameters.baseGestureFileName + i + "-" + Parameters.baseModelName + ".csv"),
							java.nio.file.StandardCopyOption.REPLACE_EXISTING,
							java.nio.file.StandardCopyOption.COPY_ATTRIBUTES);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				System.out.println("Warning: lexicon not found in " + fileName);
			}
		}
	}

	public void add(Trace t) {
		Geste geste = (lexicon.get(currentGesture));
		if (geste != null)
			geste.addTrace(t);
	}

	// convention dirName = geste.nom
	public void exportData(String dirName) {
		File dir = new File(dirName);
		boolean isDir = dir.isDirectory();
		int i = 0;
		if (!(isDir)) {
			dir.mkdir();
		}
		Geste geste = (lexicon.get(currentGesture));
		for (Trace g : geste.getTraces()) {
			g.exportWhenConfirmed(dirName + File.separator + Parameters.baseGestureFileName + "-" + i + ".csv");
			i++;
		}
	}

	public void exportWorkspaceData() {
		Geste geste = (lexicon.get(currentGesture));
		exportData(Parameters.defaultFolder + "/" + geste.getName());
	}

	public void nextDrawing() {
		if (currentTrace + 1 == numberOfTraces) {
			currentTrace = -1;
			if (currentGesture + 1 < lexicon.size()) {
				int result = JOptionPane.showConfirmDialog(jfc, "Export data and go to next gesture?");
				if (result == JOptionPane.OK_OPTION) {
					exportWorkspaceData();
					currentGesture += 1;

					vue.repaint();
				} else {
					// rien?
				}
			}
		}
		currentTrace += 1;
	}

}
