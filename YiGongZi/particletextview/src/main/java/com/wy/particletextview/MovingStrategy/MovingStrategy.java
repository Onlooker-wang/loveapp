package com.wy.particletextview.MovingStrategy;


import com.wy.particletextview.Object.Particle;

public abstract class MovingStrategy {
    public abstract void setMovingPath(Particle particle, int rangeWidth, int rangeHeight, double[] targetPosition);
}
