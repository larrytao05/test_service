package com.example.test_service.Modifiers;

import jdk.jshell.spi.ExecutionControl.NotImplementedException;

public abstract class Modifier {
    String name;
    double value;

    protected double execute(double in) throws NotImplementedException {
        throw new NotImplementedException("method not overridden");
    }
    protected double execute(int in, double[] pos) throws NotImplementedException {
        throw new NotImplementedException("method not overridden");
    }
}