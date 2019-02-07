package com.github.bartimaeusnek.bettervoidworlds.common.world.chunkgenerator.noise;

import com.github.bartimaeusnek.bettervoidworlds.util.OpenSimplexNoise;
import com.github.bartimaeusnek.bettervoidworlds.util.XSTR;

public class UniversalNoise {

    /*
     * Special thanks to Amit Patel
     * for helping me understanding the concepts of noise
     * with his website:
     * https://www.redblobgames.com/maps/terrain-from-noise/
     */

    protected final XSTR rand;
    protected OpenSimplexNoise openSimplexNoise;
    protected double octaves = 1D;
    protected double frequency = 1D;
    protected double terraces = 1D;
    protected double redistribution = 1D;
    protected double multiplex = 1D;

    public UniversalNoise(long seed) {
        this.rand = new XSTR(seed);
        this.openSimplexNoise = new OpenSimplexNoise(rand.nextLong());
    }

    public double getMultiplex() {
        return multiplex;
    }

    public void setMultiplex(double multiplex) {
        this.multiplex = multiplex;
    }

    public double getRedistribution() {
        return redistribution;
    }

    public void setRedistribution(double redistribution) {
        this.redistribution = redistribution;
    }

    public double getOctaves() {
        return octaves;
    }

    public void setOctaves(double octaves) {
        this.octaves = octaves;
    }

    public double getFrequency() {
        return frequency;
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }

    public double getTerraces() {
        return terraces;
    }

    public void setTerraces(double terraces) {
        this.terraces = terraces;
    }

    public double getValue(double x, double z) {
        return (1.0 - Math.abs(openSimplexNoise.eval(x, z, 65536D))) * 2.0 - 1.0;
    }

    public double[][] getRegion(int cx, int cz, int w, int h) {
        return getRegion(cx, cz, w, h, null);
    }

    public double[][] getRegion(double cx, double cz, double w, double h, double[][] valureArray) {
        while (cx == 0)
            cx = rand.nextDouble() % 15;
        while (cz == 0)
            cz = rand.nextDouble() % 15;

        openSimplexNoise = new OpenSimplexNoise(rand.nextLong());
        if (valureArray == null) {
            valureArray = new double[(int) w][(int) h];
        }
        for (int x = 0; x < w; x++) {
            for (int z = 0; z < h; z++) {
                double dx = x + cx, dz = z + cz;
                if (cx + x == 0)
                    dx = cx;
                if (z + cz == 0)
                    dz = cz;
                double e = 0D;
                for (double i = 1D; i <= octaves; i++) {
                    e += (1D / octaves) * getValue(i * frequency * dx, i * frequency * dz);
                }
                valureArray[x][z] = multiplex * (Math.round(Math.pow(e, redistribution) * terraces) / terraces);
            }
        }
        return valureArray;
    }
}
