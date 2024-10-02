package ui;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import geste.Trace;

public class Tracker implements MouseMotionListener, MouseListener{
	Vue vue;
	Trace trace;
	boolean on;
	private TrainingAssistant ta;
	
	public Tracker(Vue vue, TrainingAssistant ta) {
		this.vue = vue;
		this.ta = ta;
	}

	private void startTracking(Point P0) {
		trace = new Trace(false);
		ta.add(trace);
		trace.add(P0);
		on = true;
	}

	private void stopTracking() {
		on = false;
		trace = null;
		ta.nextDrawing();
	}


	@Override
	public void mouseMoved(MouseEvent e) {
	}
	

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		stopTracking();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		if (on == true) stopTracking();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		startTracking(e.getPoint());
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (on) {
			trace.add(e.getPoint());
			vue.repaint();
		}
	}

}
