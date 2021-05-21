package com.example.test_service.Modifiers;

import java.util.Random;

//number of peaks - get to all of the peaks
public class Difficulty extends Modifier {
    private long seed;
    private int[] size;
    private double value;
    private double[][] centers;
    private double[] amps;
    private double[] sigmas;
    private int steepness;
    private Random r = new Random();
    public void setSeed(long seed) {
        this.seed = seed;
        resetRandoms();
    }
    public Difficulty(int val,int steepness, int[] size) {
        this.size = size;
        value = (double)val;
        this.steepness = steepness;
    }
    private void resetRandoms() {
        r.setSeed(seed);
        centers=new double[(int)value][2];
        amps = new double[(int)value];
        sigmas = new double[(int)value];
        for (int i=0; i<(int)value; i++) {
            centers[i]=new double[]{r.nextDouble()*size[0],r.nextDouble()*size[1]};
            amps[i]=r.nextDouble()*30+150;
            sigmas[i]=(r.nextDouble()*(size[0]/(3.333*value))+size[0]/20.0)-(steepness/5-1/5);
        }
    }
    public double gauss(double x,double sigma,double center,double amp) {
        return (amp*Math.pow(Math.E,(-1.0/2.0)*Math.pow(((x-center)/sigma),2)));
    }
    public double randDoubleGauss(double[] pos,int centerIndex) {
        return Math.sqrt(gauss(pos[0],sigmas[centerIndex],centers[centerIndex][0],amps[centerIndex])*gauss(pos[1],sigmas[centerIndex],centers[centerIndex][1],amps[centerIndex]));
    }
    public double execute(int in,double[] pos) {
        double finalVal = in/3;
        //double x = pos[0];
        //double y= pos[1];
        for (int i=0; i<(int)value; i++) {
            //System.out.println(randDoubleGauss(pos,i));
            //System.out.println("Center "+i+": "+centers[i][0]+","+centers[i][1]);
            //System.out.println("Seed = "+seed);
            finalVal+=randDoubleGauss(pos,i);
        }
        //modification goes in here
        //return (int)(in/2*finalVal*value); //return modified value
        return finalVal;
    }
}
