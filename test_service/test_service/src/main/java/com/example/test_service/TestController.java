package com.example.test_service;

import com.example.test_service.Modifiers.*;
import com.example.test_service.noise.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;

@RestController
public class TestController {
    ArrayList<HashMap<Double[], Double[]>> mountains = new ArrayList<>();
    ArrayList<Modifier> modifiers = new ArrayList<Modifier>();
    Mountain m;

    @GetMapping("/create")
    public int create(@RequestParam(value = "id", defaultValue = "null") int id
            , @RequestParam String climate
            , @RequestParam boolean waterPresent
            , @RequestParam int difficulty
            , @RequestParam int steepness
            , @RequestParam int width
            , @RequestParam int height) {
        HashMap<Double[], Double[]> mountain = new HashMap<>();
        modifiers.add(new Climate());
        modifiers.add(new Difficulty(difficulty, steepness, new int[]{width, height}));
        modifiers.add(new WaterPresent(waterPresent));
        m = new Mountain(new int[]{20, 20}, 2, modifiers);
        //input mountain values here
        for (int x = 0; x < width;x++){
            for (int y = 0; y < height; y++){
                double climateVal = m.getClimate(x, y);
                double noiseVal = m.get(x, y);
                mountain.put(new Double[]{(double) x, (double) y}, new Double[]{noiseVal, climateVal});
            }
        }

        //store server side
        mountains.add(id, mountain);

        //return value
        return id;
    }

    @GetMapping("/mountain")
    public String mountain(@RequestParam(value = "id", defaultValue = "null") int id) throws JsonProcessingException {
        String json = new ObjectMapper().writeValueAsString(mountains.get(id));
        return json;
    }

    @GetMapping("/coordinate")
    public String coordinate(@RequestParam int id, @RequestParam int x, @RequestParam int y) throws JsonProcessingException {
        String json = new ObjectMapper().writeValueAsString(mountains.get(id).get(new Double[]{(double) x, (double) y}));
        return json;
    }

    @GetMapping("/test")
    public String test(){
        return "hello";
    }


    public double get(double x,double y, int id, Difficulty d, WaterPresent w) {
        Noise n = new Noise(String.valueOf(id), 20, 20);
        double val = n.noise(new double[]{x,y});
        for (int i=0; i<m.getFbm(); i++) {
            Noise n2 = new Noise(n.getSeed(),(int)(m.getSize()[0]*Math.pow(2,i+1)+1),(int)(m.getSize()[1]*Math.pow(2,i+1)+1));
            val+=n2.noise(new double[]{x*Math.pow(2,i+1),y*Math.pow(2,i+1)})/Math.pow(2,i+1);
        }
        double finalNum = normalize((int)((val+1)/2*255));
        //finalNum *= (gauss((double)x/res-size[0]/2,(double)y/res-size[1]/2));
        //System.out.println(finalNum);
        finalNum = d.execute((int)finalNum,new double[]{x,y});
        finalNum = w.execute((int)finalNum);
        return finalNum;
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


}
