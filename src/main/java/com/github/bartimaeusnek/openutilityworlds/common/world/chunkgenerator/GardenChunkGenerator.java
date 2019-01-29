package com.github.bartimaeusnek.openutilityworlds.common.world.chunkgenerator;

import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.terraingen.TerrainGen;

public class GardenChunkGenerator extends UniversalChunkGenerator {

    private static WorldGenTrees worldGenTrees = new WorldGenTrees(false, 4, Blocks.LOG.getDefaultState(), Blocks.LEAVES.getDefaultState(), false);

    public GardenChunkGenerator(World world) {
        super(world);
    }

    @Override
    public void populate(int x, int z) {
        if (x == 0 && z == 0)
            return;
        Biomes.PLAINS.decorate(this.world, this.random, new BlockPos(x * 16, 0, z * 16));
        TerrainGen.decorate(this.world, this.random, new ChunkPos(new BlockPos(x * 16, 0, z * 16)), DecorateBiomeEvent.Decorate.EventType.TREE);
        worldGenTrees.generate(this.world, this.random, new BlockPos(x * 16 + this.random.nextInt(16) + worldgenOffset, 5, z * 16 + this.random.nextInt(16) + worldgenOffset));
    }

    @Override
    public Chunk generateChunk(int cx, int cz) {
        ChunkPrimer chunkPrimer = new ChunkPrimer();
        for (int y = 0; y < 5; ++y) {
            for (int x = 0; x < 16; ++x) {
                for (int z = 0; z < 16; ++z) {
                    if (y == 0)
                        chunkPrimer.setBlockState(x, y, z, Blocks.BEDROCK.getDefaultState());
                    else if (y == 4)
                        chunkPrimer.setBlockState(x, y, z, Blocks.GRASS.getDefaultState());
                    else
                        chunkPrimer.setBlockState(x, y, z, Blocks.DIRT.getDefaultState());
                }
            }
        }
        Chunk chunk = new Chunk(this.world, chunkPrimer, cx, cz);
        chunk.generateSkylightMap();
        for (int i = 0; i < chunk.getBiomeArray().length; ++i) {
            chunk.getBiomeArray()[i] = (byte) 1;
        }
        chunk.generateSkylightMap();
        return chunk;
    }
}
