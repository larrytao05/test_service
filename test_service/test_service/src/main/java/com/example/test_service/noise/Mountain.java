package com.example.test_service.noise;

import com.example.test_service.Modifiers.Climate;
import com.example.test_service.Modifiers.Difficulty;
import com.example.test_service.Modifiers.Modifier;
import com.example.test_service.Modifiers.WaterPresent;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Mountain {
    private HashMap<Double, Color> grad;
    private Noise n;
    public Noise cn; //climate noise
    private int fbm;
    static int SNOW = 255;
    static int ICE = 195;
    static int ICESTONE = 180;
    static int STONE = 150;
    private int[] size;
    public double waterLevel;
    private Random r = new Random();
    Climate c = null;
    Difficulty d = null;
    WaterPresent w = null;
    private double gauss(double x, double y) {
        r.setSeed(n.getSeed());
        double[] gaussCenter = {r.nextDouble()*2-1,r.nextDouble()*2-1};
        int[] gaussSigma = {(int)(size[0]/2.2),(int)(size[1]/2.2)};
        //gaussCenter= new double[]{0.0, 0.0};
        double amp = 1.4;
        return amp*Math.exp(-(Math.pow(x-gaussCenter[0],2)/(2*Math.pow(gaussSigma[0],2))+Math.pow(y-gaussCenter[1],2)/(2*Math.pow(gaussSigma[1],2))));
    }
    public int[] getSize() {
        return size;
    }
    public long getSeed() {
        return n.getSeed();
    }
    private int normalize(int v) {
        if (v>255) {
            return 255;
        } else if (v<0) {
            return 0;
        } else {
            return v;
        }
    }
    public Color gradient(double num) {
        if (num<0) {
            return grad.get(0.0);
        } else if (num>255) {
            return grad.get(255.0);
        } else if (grad.containsKey(num)) {
            return grad.get(num);
        } else{
            double lower =0;
            double upper = 255;
            Object[] keys = grad.keySet().toArray();
            for (int i=0; i<keys.length; i++) {
                if ((double)keys[i]<num && Math.abs(num-(double)keys[i])<Math.abs(num-lower)) {
                    lower = (double)keys[i];
                }
                if ((double)keys[i]>num && Math.abs(num-(double)keys[i])<Math.abs(num-upper)) {
                    upper = (double)keys[i];
                }
            }
            int red = (int)linear(grad.get(lower).getRed(),grad.get(upper).getRed(),(double)(num-lower)/(upper-lower));
            int green = (int)linear(grad.get(lower).getGreen(),grad.get(upper).getGreen(),(double)(num-lower)/(upper-lower));
            int blue = (int)linear(grad.get(lower).getBlue(),grad.get(upper).getBlue(),(double)(num-lower)/(upper-lower));
            Color newColor = new Color(red,green,blue);
            //System.out.println(grad.get(lower).getRed()+" "+grad.get(upper).getRed());
            //System.out.println(newColor);
            return newColor;
        }
    }
    private double linear(int min, int max, double percent) {
        return min+(max-min)*percent;
    }
    private void init(int[] size, int fbm, ArrayList<Modifier> modifiers) {
        for (int i=0; i<modifiers.size(); i++) {
            if (modifiers.get(i).getClass()==Climate.class) {
                c = (Climate) modifiers.get(i);
            } else if (modifiers.get(i).getClass()==Difficulty.class) {
                d = (Difficulty) modifiers.get(i);
                d.setSeed(n.getSeed());
            } else if (modifiers.get(i).getClass()==WaterPresent.class) {
                w = (WaterPresent) modifiers.get(i);
            }
        }
        this.size = size;
        String climateType = "mountain";
        grad = c.getClimate(climateType); //based on whatever String user inputs
        waterLevel = c.getWaterLevel(climateType);
        this.fbm = fbm;
    }
    public double get(double x,double y) {
        double val = n.noise(new double[]{x,y});
        for (int i=0; i<fbm; i++) {
            Noise n2 = new Noise(n.getSeed(),(int)(size[0]*Math.pow(2,i+1)+1),(int)(size[1]*Math.pow(2,i+1)+1));
            val+=n2.noise(new double[]{x*Math.pow(2,i+1),y*Math.pow(2,i+1)})/Math.pow(2,i+1);
        }
        double finalNum = normalize((int)((val+1)/2*255));
        //finalNum *= (gauss((double)x/res-size[0]/2,(double)y/res-size[1]/2));
        //System.out.println(finalNum);
        finalNum = d.execute((int)finalNum,new double[]{x,y});
        finalNum = w.execute((int)finalNum);
        return finalNum;
    }

    public double getClimate(double x,double y) {
        //subtract 20 from noise val and then multiply by climate noise
        double val = cn.noise(new double[]{x,y});
        for (int i=0; i<fbm; i++) {
            Noise cn2 = new Noise(cn.getSeed(),(int)(size[0]*Math.pow(2,i+1)+1),(int)(size[1]*Math.pow(2,i+1)+1));
            val+=cn2.noise(new double[]{x*Math.pow(2,i+1),y*Math.pow(2,i+1)})/Math.pow(2,i+1);
        }
        double finalNum = normalize((int)((val+1)/2*255));
        finalNum = d.execute((int)finalNum,new double[]{x,y});
        finalNum = w.execute((int)finalNum);

        return finalNum;
    }

    public int getFbm(){
        return fbm;
    }

    //returns hashmap mapping coordinate to height and climate noise
    public HashMap<Double[], Double[]> getMountain(int res){
        HashMap<Double[], Double[]> mountain = new HashMap<Double[], Double[]>();
        for (int x = 0; x < size[0] / res; x++) {
            for (int y = 0; y < size[1] / res; y++) {
                double xVal = (double) x;
                double yVal = (double) y;
                mountain.put(new Double[]{xVal / res, yVal / res}, new Double[]{get(xVal /res, yVal / res), cn.noise(new double[]{xVal / res,yVal / res})+1});
            }
        }
        return mountain;
    }

    public Mountain(int[] size, int fbm, ArrayList<Modifier> modifiers) {
        n = new Noise(size[0]+1,size[1]+1);
        cn=new Noise(size[0]+1,size[1]+1);
        init(size,fbm,modifiers);
    }
    public Mountain(long seed, int[] size, int fbm, ArrayList<Modifier> modifiers) {
        n = new Noise(seed,size[0]+1,size[1]+1);
        Random temp = new Random(seed);
        cn=new Noise(temp.nextLong(),size[0]+1,size[1]+1);
        init(size,fbm,modifiers);
    }
    public Mountain(String seed, int[] size, int fbm, ArrayList<Modifier> modifiers) {
        n = new Noise(seed,size[0]+1,size[1]+1);
        Random temp = new Random(new SeedSerializer().toLong(seed));
        cn=new Noise(temp.nextLong(),size[0]+1,size[1]+1);
        init(size,fbm,modifiers);
    }
    public HashMap<Double,Color> getGrad() {
        return grad;
    }
    public void setGrad(double index, Color color) {
        grad.put(index,color);
    }
    public void removeGrad(int index) {
        grad.remove(index);
    }
}

