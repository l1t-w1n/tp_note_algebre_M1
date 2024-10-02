package geste;

import java.awt.Graphics2D;
import java.io.File;
import java.util.ArrayList;

public class Lexique {
	private String dataStoragePath; 	
	private ArrayList<Geste> gestes;
	
	public Lexique() {
		dataStoragePath = ui.config.Parameters.defaultFolder+"/";
		importData(dataStoragePath);
	}
	
	public void importData(String filePath) {
		gestes = new ArrayList<Geste>();
		File wd = new File(dataStoragePath);
		System.out.println("loading lexicon from "+wd.getAbsolutePath());
		
		if (wd.isDirectory()) {
			for (File f:wd.listFiles()) {
				if (f.isDirectory()){
					if ((new File (f.getPath()+ File.separator + f.getName() + "-" + ui.config.Parameters.baseModelName+ ".csv")).exists())
						gestes.add(new Geste(f.getName())); //crée le geste du nom du dir si le modèle existe et charge le modèle et les traces qui se trouvent dans le dir
				}
			}
		} else {
			System.out.println ("warning: loading data failed -> "+ wd.getAbsolutePath() + " is not a directory");
		}

	}

	public Geste get(int currentGesture) {
		return gestes.get(currentGesture);
	}
	
	public void draw(Graphics2D g2d) {
		int i = 1, j = 1, n = gestes.size();
		for(Geste g : gestes) {
			g.drawModel(g2d, i++, j);
			if ((i+1) % 4 == 0) {
				j += 1;
				i = 0;
			}
		}
	}

	public int size() {
		return gestes.size();
	}


}
