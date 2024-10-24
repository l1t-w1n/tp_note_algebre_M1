package main;

import classifieur.Rubine;
import geste.Lexique;

public class RubineMainTest {
    public static void main(String[] args) {
        Lexique lexicon = new Lexique();

        Rubine rubine = new Rubine();

        double[] recognitionRates = rubine.test(lexicon);

        for (int i = 0; i < lexicon.size(); i++) {
            System.out.printf("Gesture %d recognition rate: %.2f%%\n", i, recognitionRates[i] * 100);
        }
    }
}
