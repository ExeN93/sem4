// Andrzej Żwirko 55575 214A

package org.example;

import org.jfree.chart.*;
import org.jfree.data.xy.*;
import javax.swing.*;
import java.util.Arrays;

public class zad1 {
    public static void main(String[] args) {
        String text = "CS12345";
        int[] bits = asciiToBits(text);
        int B = bits.length;
        double Tc = 2;
        double Tb = Tc / B;
        int W = 10;
        double fn = W / Tb;
        double fs = 1000;
        int N = (int)(Tc * fs);

        double fn1 = (W + 1) / Tb;
        double fn2 = (W + 2) / Tb;
        double A1 = 1, A2 = 2;

        double[] time = new double[N];
        double[] ask = new double[N];
        double[] fsk = new double[N];
        double[] psk = new double[N];

        for (int n = 0; n < N; n++) {
            double t = n / fs;
            time[n] = t;
            int bitIndex = (int)(t / Tb);

            int bit;
            if(bitIndex < B) {
                bit = bits[bitIndex];
            }
            else {
                bit = 0;
            }

            double c = 2 * Math.PI * t;
            if (bit == 0) {
                ask[n] = Math.sin(c * fn) * A1;
                fsk[n] = Math.sin(c * fn1);
                psk[n] = Math.sin(c * fn);
            }
            else {
                ask[n] = Math.sin(c * fn) * A2;
                fsk[n] = Math.sin(c * fn2);
                psk[n] = Math.sin(c * fn + Math.PI);
            }
        }

        displaySpectrum(ask, fs, "ASK");
        displaySpectrum(fsk, fs, "FSK");
        displaySpectrum(psk, fs, "PSK");
    }

    // laby 6.
    static int[] asciiToBits(String text) {
        int n = text.length();
        int[] bits = new int[n * 7];
        int index = 0;

        for (int i = 0; i < n; i++) {
            int character = text.charAt(i);
            for (int b = 6; b >= 0; b--) {
                int bit = (character >> b) & 1;
                bits[index] = bit;
                index++;
            }
        }
        return bits;
    }

    // copy-paste z poprzednich labów
    static void displayChart(XYSeries series, String title, String xLabel, String yLabel) {
        XYSeriesCollection dataset = new XYSeriesCollection(series);
        JFreeChart chart = ChartFactory.createXYLineChart(title, xLabel, yLabel, dataset);
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new ChartPanel(chart));
        frame.pack();
        frame.setVisible(true);
    }

    // z 5. labów
    static void displaySpectrum(double[] signal, double fs, String title) {
        int N = signal.length;
        double[] real = Arrays.copyOf(signal, N);
        double[] imag = new double[N];

        fft(real, imag);

        double[] magnitudes = new double[N / 2];
        double maxDb = Double.NEGATIVE_INFINITY;

        XYSeries spectrum = new XYSeries(title);
        for (int i = 0; i < N / 2; i++) {
            double frequency = i * fs / N;
            double magnitude = Math.sqrt(real[i]*real[i] + imag[i]*imag[i]) / N;
            double db = 20 * Math.log10(magnitude + 1e-12);
            magnitudes[i] = db;
            if (db > maxDb) maxDb = db;
            spectrum.add(frequency, db);
        }

        double[] thresholds = {3.0, 6.0, 10.0};
        for (double th : thresholds) {
            double level = maxDb - th;
            double fmin = -1, fmax = -1;

            for (int i = 0; i < magnitudes.length; i++) {
                if (magnitudes[i] >= level) {
                    fmin = i * fs / N;
                    break;
                }
            }

            for (int i = magnitudes.length - 1; i >= 0; i--) {
                if (magnitudes[i] >= level) {
                    fmax = i * fs / N;
                    break;
                }
            }
            double bandwidth = (fmin >= 0 && fmax >= 0) ? (fmax - fmin) : 0;
            System.out.printf("%s %.0fdb: %.2f hz\n", title, th, bandwidth);
        }
        displayChart(spectrum, title, "częstotliwość", "amplituda");
    }

    // z 5. labów
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

            real[k+n/2] = evenReal[k] - tReal;
            imag[k+n/2] = evenImag[k] - tImag;

            real[k] = evenReal[k] + tReal;
            imag[k] = evenImag[k] + tImag;
        }
    }
}