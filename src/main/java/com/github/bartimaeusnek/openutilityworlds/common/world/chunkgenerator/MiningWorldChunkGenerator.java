package com.github.bartimaeusnek.openutilityworlds.common.world.chunkgenerator;

import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;

public class MiningWorldChunkGenerator extends UniversalChunkGenerator {

    public MiningWorldChunkGenerator(World world) {
        super(world);
    }

    @Override
    public Chunk generateChunk(int cx, int cz) {

        ChunkPrimer chunkPrimer = new ChunkPrimer();

        for (int y = 0; y < 64; ++y) {
            for (int x = 0; x < 16; ++x) {
                for (int z = 0; z < 16; ++z) {
                    if (y < 5)
                        chunkPrimer.setBlockState(x, y, z, Blocks.BEDROCK.getDefaultState());
                    else if (y < 61)
                        chunkPrimer.setBlockState(x, y, z, Blocks.STONE.getDefaultState());
                    else if (y == 63)
                        chunkPrimer.setBlockState(x, y, z, Blocks.GRASS.getDefaultState());
                    else
                        chunkPrimer.setBlockState(x, y, z, Blocks.DIRT.getDefaultState());
                }
            }
        }
        Chunk chunk = new Chunk(this.world, chunkPrimer, cx, cz);
        byte[] biomes = chunk.getBiomeArray();
        int len = biomes.length;

        for (int i = 0; i < len; ++i) {
            biomes[i] = (byte) 3;
        }

        chunk.generateSkylightMap();
        return chunk;
    }

    public void populate(int x, int z) {
        Biomes.EXTREME_HILLS.decorate(this.world, this.random, new BlockPos(x * 16, 0, z * 16));
    }
}
