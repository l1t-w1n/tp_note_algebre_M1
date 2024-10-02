package main;

import javax.swing.JFrame;

import algebre.Matrice;
import ui.MainWindow;

public class Main {

    public static void main(String s[]) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				int x = 10, y = 10, w = 800, h = 600;
				JFrame frame = MainWindow.createTrainingWindow(x, y, w, h);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
			}});
    }
}
