package com.github.bartimaeusnek.bettervoidworlds.common.world.chunkgenerator;

import com.github.bartimaeusnek.bettervoidworlds.common.config.ConfigHandler;
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

        for (int y = 0; y < SEALEVEL; ++y) {
            for (int x = 0; x < 16; ++x) {
                for (int z = 0; z < 16; ++z) {
                    if (y <= ConfigHandler.bedrockThickness)
                        chunkPrimer.setBlockState(x, y, z, Blocks.BEDROCK.getDefaultState());
                    else if (y < SEALEVEL - 3)
                        chunkPrimer.setBlockState(x, y, z, Blocks.STONE.getDefaultState());
                    else if (y == SEALEVEL - 1)
                        chunkPrimer.setBlockState(x, y, z, Blocks.GRASS.getDefaultState());
                    else
                        chunkPrimer.setBlockState(x, y, z, Blocks.DIRT.getDefaultState());
                }
            }
        }
        Chunk chunk = new Chunk(this.world, chunkPrimer, cx, cz);
        byte[] biomes = chunk.getBiomeArray();

        for (int i = 0; i < biomes.length; ++i) {
            biomes[i] = (byte) 3;
        }

        chunk.generateSkylightMap();
        return chunk;
    }

    public void populate(int x, int z) {
        Biomes.EXTREME_HILLS.decorate(this.world, this.random, new BlockPos(x * 16, 0, z * 16));
    }
}
