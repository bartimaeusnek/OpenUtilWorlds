package com.github.bartimaeusnek.bettervoidworlds.common.world.chunkgenerator;

import com.github.bartimaeusnek.bettervoidworlds.common.config.ConfigHandler;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenIceSpike;

import java.util.ArrayList;
import java.util.List;

public class WinterChunkGenerator extends UniversalChunkGenerator {

    private static final int REGIONSIZE = 8;
    private static final int RATIOCHUNKBLOCK = 5;
    private double[][] region;
    private double[][][][] subregion;
    private WorldGenIceSpike WGIS = new WorldGenIceSpike();

    public WinterChunkGenerator(World world) {
        super(world);

        universalNoise.setFrequency(0.1D);
        universalNoise.setOctaves(8192D);
        universalNoise.setTerraces(8192D);
        universalNoise.setMultiplex(2D);
        region = universalNoise.getRegion(1, 1, REGIONSIZE, REGIONSIZE);
        universalNoise.setFrequency(0.01D);
        universalNoise.setOctaves(8D);
        universalNoise.setTerraces(1024D);
        universalNoise.setMultiplex(1D);
        subregion = new double[REGIONSIZE][REGIONSIZE][16][16];
        for (int i = 0; i < REGIONSIZE; i++) {
            for (int j = 0; j < REGIONSIZE; j++) {
                subregion[i][j] = universalNoise.getRegion(random.nextInt(16), random.nextInt(16), 16, 16);
            }
        }
    }

    @Override
    public Chunk generateChunk(int cx, int cz) {

        int dcx = Math.abs(cx % (REGIONSIZE - 1));
        int dcz = Math.abs(cz % (REGIONSIZE - 1));

        ChunkPrimer chunkPrimer = new ChunkPrimer();

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                double d = (RATIOCHUNKBLOCK * region[dcx][dcz] + subregion[dcx][dcz][x][z]) / (RATIOCHUNKBLOCK + 1D);
                int initialheight = ((int) Math.floor(MathHelper.clamp(d * SEALEVEL, SEALEVEL / 2D, SEALEVEL * 2D)));
                this.genChunk(x, z, dcx, dcz, initialheight, chunkPrimer, d);
            }
        }

        Chunk chunk = new Chunk(this.world, chunkPrimer, cx, cz);
        byte[] biomes = chunk.getBiomeArray();

        for (int i = 0; i < biomes.length; ++i) {
            biomes[i] = (byte) Biome.getIdForBiome(Biomes.ICE_MOUNTAINS);
        }

        chunk.generateSkylightMap();
        return chunk;
    }

    private void genChunk(int x, int z, int dcx, int dcz, int initialheight, ChunkPrimer chunkPrimer, double d) {
        int ccx;
        int ccz;
        int cx;
        int cz;
        if (x < 8 && z < 8) {
            ccx = dcx - 1;
            ccz = dcz - 1;
            //wrap
            if (dcx == 0)
                ccx = REGIONSIZE - 2;

            if (dcz == 0)
                ccz = REGIONSIZE - 2;

            cx = x + 8;
            cz = z + 8;
        } else if (x < 8 && z >= 8) {
            ccx = dcx - 1;
            ccz = dcz + 1;
            //wrap
            if (dcx == 0)
                ccx = REGIONSIZE - 2;

            if (dcz == REGIONSIZE - 2)
                ccz = 0;
            cx = x + 8;
            cz = z - 8;
        } else if (x >= 8 && z < 8) {
            ccx = dcx + 1;
            ccz = dcz - 1;
            //wrap
            if (dcx == REGIONSIZE - 2)
                ccx = 0;

            if (dcz == 0)
                ccz = REGIONSIZE - 2;
            cx = x - 8;
            cz = z + 8;
        } else /*if (x >= 8 && z >= 8)*/ {
            ccx = dcx + 1;
            ccz = dcz + 1;
            //wrap
            if (dcx == REGIONSIZE - 2)
                ccx = 0;

            if (dcz == REGIONSIZE - 2)
                ccz = 0;
            cx = x - 8;
            cz = z - 8;
        }

        double side = (RATIOCHUNKBLOCK * region[ccx][ccz] + subregion[ccx][ccz][cx][cz]) / (RATIOCHUNKBLOCK + 1D);
        side = (d + side) / (2D);
        int yMax = (int) Math.floor((initialheight + MathHelper.clamp(side * SEALEVEL, SEALEVEL / 2D, SEALEVEL * 2D)) / 2);
        if (yMax < SEALEVEL / 2)
            yMax *= 2;
        for (int y = 0; y < yMax; y++) {
            if (y <= ConfigHandler.bedrockThickness)
                chunkPrimer.setBlockState(x, y, z, Blocks.BEDROCK.getDefaultState());
            else if (y < SEALEVEL / 5)
                chunkPrimer.setBlockState(x, y, z, Blocks.PACKED_ICE.getDefaultState());
            else if (y < SEALEVEL / 3)
                chunkPrimer.setBlockState(x, y, z, Blocks.ICE.getDefaultState());
            else
                chunkPrimer.setBlockState(x, y, z, Blocks.SNOW.getDefaultState());
        }
    }

    public void populate(int x, int z) {
        if (random.nextInt(100) < 25)
            WGIS.generate(world, random, new BlockPos(x * 16 + random.nextInt(8) - 4 + worldgenOffset, SEALEVEL, z * 16 + random.nextInt(8) - 4 + worldgenOffset));
    }

    @Override
    public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) {

        List<Biome.SpawnListEntry> ret = ConfigHandler.creatureSpawn ? this.world.getBiome(pos).getSpawnableList(creatureType) : (creatureType.getPeacefulCreature() && ConfigHandler.passiveSpawn) ? this.world.getBiome(pos).getSpawnableList(creatureType) : new ArrayList<>();
        ret.add(new Biome.SpawnListEntry(EntitySnowman.class, 100, 2, 5));
        return ret;
    }
}
