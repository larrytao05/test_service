package com.example.test_service.noise;

import java.util.Random;

interface Fade {
    double fade(double x);
}

public class Noise {
    private Random randomNum = new Random();
    private SeedSerializer s = new SeedSerializer();
    private long seed;
    private int[] dimensions;
    private Fade fade = x -> {return 6*Math.pow(x,5)-15*Math.pow(x,4)+10*Math.pow(x,3);};
    private Vector getVec(int[] pos) {
        randomNum.setSeed(seed);
        pos[0] = pos[0]%dimensions[0];
        pos[1] = pos[1]%dimensions[1];
        for (int i=0; i<dimensions[0]*2*pos[1]+pos[0]*2; i++) { //for loop searches for vector at the correct position and then stops
            randomNum.nextDouble();
        }
        double vectorX = randomNum.nextDouble()*2-1;
        double vectorY = randomNum.nextDouble()*2-1;
        Vector vec = new Vector(vectorX,vectorY); //this is the vector in the top left position of the point chosen.
        return vec;
    }
    private double square(double val) {
        return val-Math.floor(val);
    }
    public double noise(double[] pos) {
        randomNum.setSeed(seed);
        pos[0] = pos[0]%(dimensions[0]-1);
        pos[1] = pos[1]%(dimensions[1]-1);
        int[] topLeft = new int[]{(int)Math.floor(pos[0]),(int)Math.floor(pos[1])};
        int[] topRight = new int[]{(int)Math.ceil(pos[0]),(int)Math.floor(pos[1])};
        int[] bottomLeft = new int[]{(int)Math.floor(pos[0]),(int)Math.ceil(pos[1])};
        int[] bottomRight = new int[]{(int)Math.ceil(pos[0]),(int)Math.ceil(pos[1])};
        Vector[] corners = new Vector[4];
        corners[0] = getVec(topLeft);
        corners[1] = getVec(topRight);
        corners[2] = getVec(bottomLeft);
        corners[3] = getVec(bottomRight);
        Vector[] toPoint = new Vector[4];
        //the above lines simply get the vectors at each of the corners of the tile that the point is in
        //the vectors are found according to the seed of the random number generator.
        toPoint[0] = new Vector(pos[0]-topLeft[0],pos[1]-topLeft[1]);
        toPoint[1] = new Vector(pos[0]-topRight[0],pos[1]-topRight[1]);
        toPoint[2] = new Vector(pos[0]-bottomLeft[0],pos[1]-bottomLeft[1]);
        toPoint[3] = new Vector(pos[0]-bottomRight[0],pos[1]-bottomRight[1]);
        //above lines get the vectors from the corners pointing to the point.
        double[] products = new double[4]; //dot products
        products[0]=toPoint[0].dotProduct(corners[0]);
        products[1]=toPoint[1].dotProduct(corners[1]);
        products[2]=toPoint[2].dotProduct(corners[2]);
        products[3]=toPoint[3].dotProduct(corners[3]);
        double[] faded = new double[4];
        faded[0]=fade.fade(1-square(pos[0]))*fade.fade(1-square(pos[1]));
        faded[1]=fade.fade(square(pos[0]))*fade.fade(1-square(pos[1]));
        faded[2]=fade.fade(1-square(pos[0]))*fade.fade(square(pos[1]));
        faded[3]=fade.fade(square(pos[0]))*fade.fade(square(pos[1]));
        /*for (double i: faded) {
            System.out.println(i);
        }*/
        double value = faded[0]*products[0]+faded[1]*products[1]+faded[2]*products[2]+faded[3]*products[3];
        return value;
    }
    private void init(int dimX, int dimY) {
        dimensions = new int[]{dimX, dimY};
    }
    public Noise(String seed,int dimX, int dimY) {
        this.seed = s.toLong(seed);
        init(dimX,dimY);
    }
    public Noise(long seed,int dimX, int dimY) {
        this.seed = seed;
        init(dimX,dimY);
    }
    public Noise(int dimX, int dimY) {
        seed = randomNum.nextLong();
        init(dimX,dimY);
    }

    public long getSeed() {
        return seed;
    }
}
