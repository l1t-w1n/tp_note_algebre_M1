package classifieur;

import algebre.Matrice;
import algebre.Vecteur;
import geste.Geste;
import geste.Lexique;
import geste.Trace;

public class Rubine implements Recognizer{
	private Lexique lexicon;
	private Matrice eotccm; // estimate of the common covariance matrix
	private Matrice inverseEotccm; // inverse of eotccm
	

	@Override
	public void init(Lexique l) {
		l.initData();
		this.lexicon = l;

		int numGestures = lexicon.size();
		int totalTraces = 0;
		Matrice sumCovariance = null;

		for (Geste geste : lexicon.getGestes()) {
			Matrice covMatrix = geste.getCovMatrix();
			totalTraces += geste.getTraces().size();
			if (sumCovariance == null) {
				sumCovariance = covMatrix;
			} else {
				sumCovariance = sumCovariance.plus(covMatrix);
			}
		}

		// Step 2: Calculate the estimate of the common covariance matrix (eotccm)
		if (sumCovariance != null) {
			double divisor = -numGestures + totalTraces;
			this.eotccm = sumCovariance.scalarMult(1.0 / divisor);
		}

		// Step 3: Calculate the inverse of the covariance matrix
		Matrice i = new Matrice(eotccm.getDimension());
		if (eotccm != null) {
			this.inverseEotccm = eotccm.inverse(i);
		}

		for (Geste geste : lexicon.getGestes()) {
			geste.initEstimators(inverseEotccm);
		}

	}

	@Override
	//le lexique passé en paramètre doit être initialisé avant l'appel à test
	public double[] test(Lexique lexicon) {
		return null; //todo
	}

	@Override
	public Geste recognize(Trace t) {
		return null; //todo
	}

	@Override
	public double squaredMahalanobis(Vecteur t, Vecteur g) {
		Vecteur diff = new Vecteur(t.getDimension());
		for (int i = 0; i < t.getDimension(); i++) {
			diff.setValueForFeature(i, t.get(i) - g.get(i));
		}

		Vecteur intermediate = inverseEotccm.mult(diff);

		return diff.produitScalaire(intermediate);
	}


}
