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
		l.initGestes();
		this.lexicon = l;

		int numGestures = lexicon.size();
		System.out.println("num gestures:"+ numGestures);
		int totalTraces = 0;
		Matrice sumCovariance = null;

		for (Geste geste : lexicon.getGestes()) {
			Matrice covMatrix = geste.getCovMatrix();
			System.out.println("covMatrix: "+covMatrix);
			totalTraces += geste.getTraces().size();
			if (sumCovariance == null) {
				sumCovariance = covMatrix;
			} else {
				sumCovariance = sumCovariance.plus(covMatrix);
			}
		}

		System.out.println("Sum covarience: "+sumCovariance);
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
	public double[] test(Lexique lex) {
		this.init(lex);
		int numGestures = lexicon.size();
		double[] recognitionRates = new double[numGestures];

		// Iterate through each gesture in the lexicon
		for (int i = 0; i < numGestures; i++) {
			Geste geste = lexicon.get(i);
			int numTraces = geste.getTraces().size();
			int recognizedTraces = 0;

			// Test each trace of the gesture
			for (Trace trace : geste.getTraces()) {
				Geste recognizedGeste = recognize(trace);
				if (recognizedGeste != null && recognizedGeste == geste) {
					recognizedTraces++;
				}
			}

			// Calculate the recognition rate for the gesture
			recognitionRates[i] = (double) recognizedTraces / numTraces;
		}
		return recognitionRates;
	}

	@Override
	public Geste recognize(Trace t) {
		Geste recognizedGesture = null;
		double maxScore = Double.NEGATIVE_INFINITY;

		// Iterate through each gesture in the lexicon
		for (Geste geste : lexicon.getGestes()) {
			Vecteur weightVector = geste.getWeightVector();
			double bias = geste.getBias();

			// Calculate the measure: <weightVector, featureVector(t)> + bias
			Vecteur featureVector = t.getFeatureVector();
			double score = weightVector.produitScalaire(featureVector) + bias;

			// Update the recognized gesture if the current score is higher
			if (score > maxScore) {
				maxScore = score;
				recognizedGesture = geste;
			}
		}

		// Verify if the gesture passes the Mahalanobis distance threshold
		if (recognizedGesture != null) {
			double threshold = 0.5 * recognizedGesture.getWeightVector().getDimension();
			double mahalanobisDistance = squaredMahalanobis(t.getFeatureVector(), recognizedGesture.getEsperance());
			if (mahalanobisDistance > threshold) {
				recognizedGesture = null;
			}
		}

		return recognizedGesture;
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
