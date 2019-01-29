package com.github.bartimaeusnek.openutilityworlds.common.world.chunkgenerator;

import com.github.bartimaeusnek.openutilityworlds.common.config.ConfigHandler;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenIceSpike;

import java.util.ArrayList;
import java.util.List;

public class WinterChunkGenerator extends UniversalChunkGenerator {

    private static final int REGIONSIZE = 8;
    private static final int RATIOCHUNKBLOCK = 10;
    private static final int SEALEVEL = 60;
    private double[][] region;
    private double[][][][] subregion;
    private WorldGenIceSpike WGIS = new WorldGenIceSpike();

    public WinterChunkGenerator(World world) {
        super(world);

        universalNoise.setFrequency(0.001D);
        universalNoise.setOctaves(8D);
        universalNoise.setTerraces(8192D);
        region = universalNoise.getRegion(1, 1, REGIONSIZE, REGIONSIZE);
        universalNoise.setFrequency(0.01D);
        universalNoise.setOctaves(8D);
        universalNoise.setTerraces(512D);
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
                chunkPrimer.setBlockState(x, 0, z, Blocks.BEDROCK.getDefaultState());
                double d = (RATIOCHUNKBLOCK * region[dcx][dcz] + subregion[dcx][dcz][x][z]) / (RATIOCHUNKBLOCK + 1);
                double side;
                int initialheight = ((int) Math.floor(d * SEALEVEL));
                if (x < 8) {
                    int ccx = dcx - 1;
                    //wrap
                    if (dcx == 0)
                        ccx = region.length - 1;
                    side = (RATIOCHUNKBLOCK * region[ccx][dcz] + subregion[ccx][dcz][x + 8][z]) / (RATIOCHUNKBLOCK + 1);
                    side = (d + side) / (2D);
                    for (int y = 1; y <= ((initialheight + ((int) Math.floor(Math.min(SEALEVEL, side * SEALEVEL)))) / 2); y++) {
                        if (y < SEALEVEL / 5)
                            chunkPrimer.setBlockState(x, y, z, Blocks.PACKED_ICE.getDefaultState());
                        else if (y < SEALEVEL / 3)
                            chunkPrimer.setBlockState(x, y, z, Blocks.ICE.getDefaultState());
                        else if (y == ((initialheight + ((int) Math.floor(Math.min(SEALEVEL, side * SEALEVEL)))) / 2))
                            chunkPrimer.setBlockState(x, y, z, Blocks.SNOW_LAYER.getDefaultState());
                        else
                            chunkPrimer.setBlockState(x, y, z, Blocks.SNOW.getDefaultState());
                    }
                } else {
                    int ccx = dcx + 1;
                    //wrap
                    if (dcx == region.length - 1)
                        ccx = 0;
                    side = (RATIOCHUNKBLOCK * region[ccx][dcz] + subregion[ccx][dcz][x - 8][z]) / (RATIOCHUNKBLOCK + 1);
                    side = (d + side) / (2D);
                    for (int y = 1; y < ((initialheight + ((int) Math.floor(Math.min(SEALEVEL, side * SEALEVEL)))) / 2); y++) {
                        if (y < SEALEVEL / 5)
                            chunkPrimer.setBlockState(x, y, z, Blocks.PACKED_ICE.getDefaultState());
                        else if (y < SEALEVEL / 3)
                            chunkPrimer.setBlockState(x, y, z, Blocks.ICE.getDefaultState());
                        else if (y == ((initialheight + ((int) Math.floor(Math.min(SEALEVEL, side * SEALEVEL)))) / 2))
                            chunkPrimer.setBlockState(x, y, z, Blocks.SNOW_LAYER.getDefaultState());
                        else
                            chunkPrimer.setBlockState(x, y, z, Blocks.SNOW.getDefaultState());
                    }
                }
                if (z < 8) {
                    int ccx = dcz - 1;
                    //wrap
                    if (dcz == 0)
                        ccx = region.length - 1;
                    side = (RATIOCHUNKBLOCK * region[dcx][ccx] + subregion[dcx][ccx][x][z + 8]) / (RATIOCHUNKBLOCK + 1);
                    side = (d + side) / (2D);
                    for (int y = 1; y < ((initialheight + ((int) Math.floor(Math.min(SEALEVEL, side * SEALEVEL)))) / 2); y++) {
                        if (y < SEALEVEL / 5)
                            chunkPrimer.setBlockState(x, y, z, Blocks.PACKED_ICE.getDefaultState());
                        else if (y < SEALEVEL / 3)
                            chunkPrimer.setBlockState(x, y, z, Blocks.ICE.getDefaultState());
                        else if (y == ((initialheight + ((int) Math.floor(Math.min(SEALEVEL, side * SEALEVEL)))) / 2))
                            chunkPrimer.setBlockState(x, y, z, Blocks.SNOW_LAYER.getDefaultState());
                        else
                            chunkPrimer.setBlockState(x, y, z, Blocks.SNOW.getDefaultState());
                    }

                } else {
                    int ccx = dcz + 1;
                    //wrap
                    if (dcz == region.length - 1)
                        ccx = 0;
                    side = (RATIOCHUNKBLOCK * region[dcx][ccx] + subregion[dcx][ccx][x][z - 8]) / (RATIOCHUNKBLOCK + 1);
                    side = (d + side) / (2D);
                    for (int y = 1; y < ((initialheight + ((int) Math.floor(Math.min(SEALEVEL, side * SEALEVEL)))) / 2); y++) {
                        if (y < SEALEVEL / 5)
                            chunkPrimer.setBlockState(x, y, z, Blocks.PACKED_ICE.getDefaultState());
                        else if (y < SEALEVEL / 3)
                            chunkPrimer.setBlockState(x, y, z, Blocks.ICE.getDefaultState());
                        else if (y == ((initialheight + ((int) Math.floor(Math.min(SEALEVEL, side * SEALEVEL)))) / 2))
                            chunkPrimer.setBlockState(x, y, z, Blocks.SNOW_LAYER.getDefaultState());
                        else
                            chunkPrimer.setBlockState(x, y, z, Blocks.SNOW.getDefaultState());
                    }
                }

//                    if (x== 8 && z == 8)
//                        for (int y = 1; y < initialheight; y++) {
//                            chunkPrimer.setBlockState(x,y,z ,Blocks.SAND.getDefaultState());
//                        }
            }
        }

        Chunk chunk = new Chunk(this.world, chunkPrimer, cx, cz);
        byte[] biomes = chunk.getBiomeArray();
        int len = biomes.length;

        for (int i = 0; i < len; ++i) {
            biomes[i] = (byte) Biome.getIdForBiome(Biomes.ICE_MOUNTAINS);
        }

        chunk.generateSkylightMap();
        return chunk;
    }

    public void populate(int x, int z) {
        if (random.nextInt(100) < 25)
            WGIS.generate(world, random, new BlockPos(x * 16 + random.nextInt(8) - 4 + worldgenOffset, SEALEVEL, z * 16 + random.nextInt(8) - 4 + worldgenOffset));

        //new WorldGenIcePath(3).generate(world,random,new BlockPos(x*16+worldgenOffset,SEALEVEL,z*16+worldgenOffset));

    }

    @Override
    public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) {
        List<Biome.SpawnListEntry> ret = ConfigHandler.creatureSpawn ? this.world.getBiome(pos).getSpawnableList(creatureType) : new ArrayList<>();
        ret.add(new Biome.SpawnListEntry(EntitySnowman.class, 100, 2, 5));
        return ret;
    }
}
