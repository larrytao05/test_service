package com.example.test_service.noise;

import java.math.BigInteger;

public class SeedSerializer {
    public SeedSerializer() {

    }
    private String padZeros(String s,int length) {
        while (s.length() < length) {
            s="0".concat(s);
        }
        //System.out.println(s);
        return s;
    }
    public long toLong(String seed) {
        String binaryString = "";
        for (int c = 0; c< seed.length(); c++) {
            char current = seed.charAt(c);
            String bin = Integer.toBinaryString(current);
            //System.out.println(bin);
            binaryString=binaryString.concat(padZeros(bin,16));

        }
        return (new BigInteger(binaryString,2).longValue());
    }
}
