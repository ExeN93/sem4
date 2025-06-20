// Andrzej Żwirko 55575 214A

package org.example;

import org.jfree.chart.*;
import org.jfree.data.xy.*;
import javax.swing.*;
import java.util.Arrays;

public class zad2 {
    public static void main(String[] args) {
        double fm = 10;
        double fn = 500;
        double fs = 2000; // próbkowanie
        double Tc = 1; // czas sygnału
        int N = (int)(Tc * fs);

        double[]kA = {0.5, 6, 30};
        double[]kP = {0.5, 2, 10};
        double[]kF = {0.5, 2, 10};

        for (int i = 0; i < 3; i++) {
            generateSpectrum(fm, fn, fs, N, kA[i], kP[i], kF[i], i+1);
        }
    }

    static void generateSpectrum(double fm, double fn, double fs, int N, double kA, double kP, double kF, int caseNum) {
        double[]amSignal = new double[N];
        double[]pmSignal = new double[N];
        double[]fmSignal = new double[N];

        for (int n = 0; n < N; n++) {
            double t = n / fs;
            double mt = Math.sin(2 * Math.PI * fm * t);

            amSignal[n] = (kA * mt + 1) * Math.cos(2 * Math.PI * fn * t);
            pmSignal[n] = Math.cos(2 * Math.PI * fn * t + kP * mt);
            fmSignal[n] = Math.cos(2 * Math.PI * fn * t + (kF/fm) * mt);
        }

        displaySpectrum(amSignal, fs, "AM" + caseNum + " kA = " + kA);
        displaySpectrum(pmSignal, fs, "PM" + caseNum + " kP = " + kP);
        displaySpectrum(fmSignal, fs, "FM" + caseNum + " kF = " + kF);
    }

    // copy-paste z poprzednich laboratoriów (rysowanie wykresu)
    static void displayChart(XYSeries series, String title, String xLabel, String yLabel) {
        XYSeriesCollection dataset = new XYSeriesCollection(series);
        JFreeChart chart = ChartFactory.createXYLineChart(title, xLabel, yLabel, dataset);
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new ChartPanel(chart));
        frame.pack();
        frame.setVisible(true);
    }

    static void displaySpectrum(double[] signal, double fs, String title) {
        int N = signal.length;
        double[] real = Arrays.copyOf(signal, N);
        double[] imag = new double[N];

        fft(real, imag);

        XYSeries spectrum = new XYSeries(title);
        for (int i = 0; i < N / 2; i++) {
            double frequency = i * fs / N;
            double magnitude = Math.sqrt(real[i]*real[i] + imag[i]*imag[i]) / N;
            spectrum.add(frequency, magnitude);
        }
        displayChart(spectrum, title, "częstotliwość", "amplituda");
    }

    static void fft(double[] real, double[] imag) {
        int n = real.length;

        if (n == 1) return;
        double[] evenReal = new double[n/2];
        double[] evenImag = new double[n/2];
        double[] oddReal = new double[n/2];
        double[] oddImag = new double[n/2];

        for (int i = 0; i < n/2; i++) {
            evenReal[i] = real[2 * i];
            evenImag[i] = imag[2 * i];
            oddReal[i] = real[2 * i + 1];
            oddImag[i] = imag[2 * i + 1];
        }

        fft(evenReal, evenImag);
        fft(oddReal, oddImag);

        for (int k = 0; k < n/2; k++) {
            double angle = (-2 * Math.PI * k / n);

            double cos = Math.cos(angle);
            double sin = Math.sin(angle);

            double tReal = cos * oddReal[k] - sin * oddImag[k];
            double tImag = sin * oddReal[k] + cos * oddImag[k];

            real[k + n/2] = evenReal[k] - tReal;
            imag[k + n/2] = evenImag[k] - tImag;

            real[k] = evenReal[k] + tReal;
            imag[k] = evenImag[k] + tImag;
        }
    }
}