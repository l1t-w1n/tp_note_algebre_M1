package main;

import geste.Trace;

public class Resampling {
    public static void main(String[] args) {
        String fileName;
        for (int i = 0; i < 16; i++) {
            if (i!=2) {
                for (int j = 0; j < 10; j++) {
                    fileName = "./data/geste" + i + "/" + "geste-" + j + ".csv";
                    Trace trace2 = new Trace(false, fileName);
                    System.out.println(trace2.traceLength());
                    System.out.println(trace2.traceNbOfPoints());
                    trace2.resample(2);
                    trace2.export(fileName, true);
                    System.out.println("after resample");
                    System.out.println(trace2.traceLength());
                    System.out.println(trace2.traceNbOfPoints());
                }
            }
        }
    }
}
