package main;

import classifieur.Rubine;
import geste.Lexique;
import ui.TrainingAssistant;
import ui.Vue;

import javax.swing.JFrame;

public class RubineMainTest {
    public static void main(String[] args) {
        Lexique lexicon = new Lexique();

        Rubine rubine = new Rubine();
        rubine.init(lexicon);

        // Step 4: Test the recognizer
        double[] recognitionRates = rubine.test(lexicon);

        // Step 5: Print the recognition rates for each gesture
        for (int i = 0; i < recognitionRates.length; i++) {
            System.out.printf("Gesture %d recognition rate: %.2f%%\n", i, recognitionRates[i] * 100);
        }
    }
}
