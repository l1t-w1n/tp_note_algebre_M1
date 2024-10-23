package geste;

import java.awt.Graphics2D;
import java.io.File;
import java.util.ArrayList;

import ui.config.Parameters;

public class Lexique {
	private ArrayList<Geste> gestes;

	public Lexique() {
		this.gestes = new ArrayList<Geste>();
	}

	public Geste get(int currentGesture) {
		return gestes.get(currentGesture);
	}

	public void add(Geste g) {
		gestes.add(g);
	}

	public void draw(Graphics2D g2d) {
		int i = 1, j = 1;
		for (Geste g : gestes) {
			g.drawModel(g2d, i++, j);
			if ((i + 1) % 4 == 0) {
				j += 1;
				i = 0;
			}
		}
	}

	public void initGestes(){
		for (Geste geste : gestes) {
			geste.initCovEsp();
		}
	}

	public void initData() {
		String name, extension;
		File dataDir = new File(Parameters.defaultFolder + "/");

		Trace model, t;
		Geste geste;

		if (dataDir.exists() && dataDir.isDirectory()) {
			for (File traceDir : dataDir.listFiles()) {
				name = traceDir.getName();
				if(name.matches("geste.*") && !name.equals("geste9")) {
					//geste9 folder bugged and is empty
					System.out.println("\nModel file name: " + Parameters.defaultFolder + "/" + name + "/" + name
							+ "-" + Parameters.baseModelName + ".csv");
					model = new Trace(true, Parameters.defaultFolder + "/" + name + "/" + name
							+ "-" + Parameters.baseModelName + ".csv");
					geste = new Geste(name, model);
					add(geste);
					System.out.println("creating gesture for " + name);
					for (File trace : traceDir.listFiles()) {
						name = trace.getName();
						if(name.contains("model")){continue;}

						extension = name.substring(name.lastIndexOf('.'), name.length());
						if (extension.equals(".csv")) {
							name = name.substring(0, name.lastIndexOf('.'));
							t = new Trace(false, trace.getPath());
							if (t.size() > 2) geste.addTrace(t);
							//System.out.println("loading trace from "+trace.getPath());
						}
					}
				}
			}
		} else {
			System.out.println("Warning: file " + dataDir.getName() + " does not exist");
		}
	}

	public int size() {
		return gestes.size();
	}

	public ArrayList<Geste> getGestes() {
		return this.gestes;
	}

}
