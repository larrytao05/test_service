package com.example.test_service.Modifiers;

import java.awt.*;
import java.util.HashMap;

public class Climate extends Modifier {

    public HashMap<Double, Color> glacier, volcano, mountain;
    public HashMap<String, HashMap<Double, Color>> climates = new HashMap<String, HashMap<Double, Color>>();
    public HashMap<String,Double> waterLevels = new HashMap<>();
    //public HashMap<>
    public Climate() {

        glacier = new HashMap<Double,Color>();
        glacier.put(0.0, new Color(12, 40, 84)); //ocean
        glacier.put(125.0, new Color(98,187,255)); //really shallow water
        glacier.put(130.0, new Color(48, 61, 46)); //bare grass
        glacier.put(150.0, new Color(96, 106, 107)); //stone
        glacier.put(180.0, new Color(178, 206, 209)); //icey stone
        glacier.put(195.0, new Color(225, 249, 252)); //ice
        glacier.put(255.0, new Color(255, 255, 255)); //snow

        volcano = new HashMap<Double,Color>();
        volcano.put(255.0, new Color (255, 72, 50)); //hot lava
        volcano.put(215.0, new Color (179, 13, 4)); //cool lava
        volcano.put(215.0, new Color (101, 8, 3)); //more lava
        volcano.put(170.0, new Color(109, 78, 78)); //hardened lava
        volcano.put(120.0, new Color(49, 44, 42)); //asphalt/stone
        volcano.put(110.0, new Color(255, 227, 138)); //grass
        volcano.put(95.0, new Color(26, 106, 147)); //shallow water
        volcano.put(0.0, new Color(3,3,148)); //deep ocean

        mountain = new HashMap<Double,Color>();
        mountain.put(255.0,new Color(255,255,255)); //snow
        mountain.put(195.0,new Color(202, 253, 255)); //watery snow
        mountain.put(190.0,new Color(154,154,154)); //steeper stone
        mountain.put(169.0,new Color(120,120,120)); //stone
        mountain.put(150.0,new Color(32,151,63)); //grass
        mountain.put(135.0,new Color(156,95,16)); //dirt
        mountain.put(130.0,new Color(255,227,163)); //sand
        mountain.put(125.0,new Color(98,187,255)); //really shallow water
        mountain.put(104.0,new Color(58,58,233)); //shallow water
        mountain.put(0.0,new Color(3,3,148)); //deep ocean

        climates.put("glacier", glacier);
        climates.put("volcano", volcano);
        climates.put("mountain", mountain);
        waterLevels.put("glacier",130.0);
        waterLevels.put("volcano",110.0);
        waterLevels.put("mountain",130.0);
    }

    public HashMap<Double, Color> getClimate(String user) {
        return climates.get(user);
    }
    public double getWaterLevel(String user) {
        return waterLevels.get(user);
    }

    public int execute(int in) {
        //modification goes in here
        return in; //return modified value
    }
}
