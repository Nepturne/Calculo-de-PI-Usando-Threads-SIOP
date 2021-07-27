package threads;

import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        int NUM_EXEC = 5;
        double[] runtime = new double[NUM_EXEC];
        double[] pi = new double[NUM_EXEC];

        Scanner reply = new Scanner(System.in);
        System.out.print("Digite o número de termos: ");
        int numTerms = reply.nextInt();
        System.out.print("Digite o número de threads: ");
        int numThreads = reply.nextInt();

        for(int i = 0; i < NUM_EXEC; i++){
            long start = System.nanoTime();

            ApproxiPi[] threads = new ApproxiPi[numThreads];
            int numTermsPerThread = numTerms / numThreads;
            int nInitial = 0;
            for(int j = 0; j < numThreads; j++){
                threads[j] = new ApproxiPi(nInitial, numTermsPerThread);
                threads[j].start();
                nInitial += numTermsPerThread;
            }

            for(int j = 0; j < numThreads; j++){
                threads[j].join();
                pi[i] += threads[j].pi;
            }

            runtime[i] = (double) (System.nanoTime() - start) / (double) 100_000;
        }

        double averageRuntime = 0;
        for(int i = 0; i < NUM_EXEC; i++){
            averageRuntime += runtime[i] / (double) NUM_EXEC;
        }

        double varRuntime = 0;
        for(int i = 0; i < NUM_EXEC; i++){
            double term = runtime[i] - averageRuntime;
            varRuntime += Math.pow(term, 2) / (double) NUM_EXEC;
        }

        double patternDeviationRuntime = Math.sqrt(varRuntime);
        System.out.printf("\n\nNum execuções:%d\nmédia duração:%.4fms\n", numTerms, averageRuntime);
        System.out.printf("desvio padrão duração:%.4fms\n", patternDeviationRuntime);
        System.out.printf("coeficiente de duração:%.4f%%\n",(patternDeviationRuntime / averageRuntime) * 100);
        System.out.printf("Pi:%s\n", Arrays.toString(pi));
    }
    private static class ApproxiPi extends Thread{
        double pi;
        int nInitial;
        int numTerms;

        ApproxiPi(int nInitial, int numTerms){
            this.nInitial = nInitial;
            this.numTerms = numTerms;
        }

        @Override
        public void run() {
            this.pi = 0;
            for(int n = this.nInitial; n < this.nInitial + this.numTerms; n++){
                pi += Math.pow(-1, n) / (2*n + 1);
            }

            pi *= 4;
        }
    }
}
