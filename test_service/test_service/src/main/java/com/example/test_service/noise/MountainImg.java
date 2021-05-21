package com.example.test_service.noise;

import com.example.test_service.Modifiers.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class MountainImg {
    public HashMap<Integer, Color> grad;
    private int[] size;
    private Mountain m;
    private Random r = new Random();

    public long getSeed() {
        return m.getSeed();
    }

    private void init(int[] size, int res) {
        BufferedImage img = new BufferedImage(size[0] * res, size[1] * res, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        for (int x = 0; x < size[0] * res; x++) {
            for (int y = 0; y < size[1] * res; y++) {
                Color color = m.gradient((int) m.get(((double)x / res), ((double)y / res)));
                g.setColor(color);
                g.drawRect(x, y, 1, 1);
            }
        }
        g.dispose();
        File output = new File("mountain.jpg");
        try {
            ImageIO.write(img, "jpg", output);
            System.out.println("Image saved!");
        } catch (IOException e) {
            System.out.println("Unable to save Mountain.");
        }
    }

    private void init(HashMap<Double[], Double[]> info, int res){
        BufferedImage img = new BufferedImage(size[0] * res, size[1] * res, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        for (int x = 0; x < size[0] * res; x++) {
            for (int y = 0; y < size[1] * res; y++) {
                Color color =  m.gradient(info.get(new Double[]{(double) x / res, (double) y / res})[y]);
                g.setColor(color);
                g.drawRect(x, y, 1, 1);
            }
        }
    }


    public MountainImg(int[] size, int res, int fbm, ArrayList<Modifier> modifiers) {
        m = new Mountain(size, fbm, modifiers);
        init(size,res);
    }

    public MountainImg(long seed, int[] size, int res, int fbm, ArrayList<Modifier> modifiers) {
        m = new Mountain(seed, size, fbm, modifiers);
        init(size,res);
    }

    public MountainImg(String seed, int[] size, int res, int fbm, ArrayList<Modifier> modifiers) {
        m = new Mountain(seed, size, fbm, modifiers);
        init(size,res);
    }
}
