package com.example.test_service.noise;

public class Vector {
    double x;
    double y;
    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }
    public String toString() {
        return "("+x+","+y+")";
    }
    public double dotProduct(Vector vec) {
        return vec.x*this.x+vec.y*this.y;
    }
}