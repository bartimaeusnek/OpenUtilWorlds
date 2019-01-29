package com.github.bartimaeusnek.openutilityworlds.common.world.chunkgenerator.noise;

import com.github.bartimaeusnek.openutilityworlds.util.OpenSimplexNoise;
import com.github.bartimaeusnek.openutilityworlds.util.XSTR;

public class UniversalNoise {

    /*
     * Special thanks to Amit Patel
     * for helping me understanding the concepts of noise
     * with his website:
     * https://www.redblobgames.com/maps/terrain-from-noise/
     */

    protected final XSTR rand;
    protected OpenSimplexNoise openSimplexNoise;
    protected double octaves;
    protected double frequency;
    protected double terraces;

    public UniversalNoise(long seed) {
        this.rand = new XSTR(seed);
        this.openSimplexNoise = new OpenSimplexNoise(rand.nextLong());
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
        for (double x = -w / 2; x < w / 2; x++) {
            for (double z = -h / 2; z < h / 2; z++) {
                double e = 0;
                for (double i = 1; i <= octaves; i++) {
                    e += (1 / octaves) * getValue(i * frequency * (x + cx), i * frequency * (z + cz));
                }
                valureArray[(int) (x + w / 2)][(int) (z + h / 2)] = Math.round(e * terraces) / terraces;
            }
        }
        return valureArray;
    }
}
