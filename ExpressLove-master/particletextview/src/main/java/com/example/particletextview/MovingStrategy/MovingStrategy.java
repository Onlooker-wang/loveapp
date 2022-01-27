package com.example.particletextview.MovingStrategy;


import com.example.particletextview.Object.Particle;

public abstract class MovingStrategy {
    public abstract void setMovingPath(Particle particle, int rangeWidth, int rangeHeight, double[] targetPosition);
}
