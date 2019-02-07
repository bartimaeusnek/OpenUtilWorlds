package com.github.bartimaeusnek.bettervoidworlds.common.world.chunkgenerator;

import com.github.bartimaeusnek.bettervoidworlds.common.config.ConfigHandler;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;

public class DuneChunkGenerator extends UniversalChunkGenerator {
    public DuneChunkGenerator(World world) {
        super(world);
    }

    @Override
    public Chunk generateChunk(int x, int z) {

        ChunkPrimer chunkprimer = new ChunkPrimer();
        for (int lx = 0; lx < 16; lx++) {
            for (int lz = 0; lz < 16; lz++) {
                for (int y = 0; y <= ConfigHandler.bedrockThickness; y++) {
                    chunkprimer.setBlockState(lx, y, lz, Blocks.BEDROCK.getDefaultState());
                }
                for (int y = ConfigHandler.bedrockThickness + 1; y < (SEALEVEL + Math.abs(lx - 8)); y++) {
                    chunkprimer.setBlockState(lx, y, lz, Blocks.SAND.getDefaultState());
                }
            }
        }

        Chunk chunk = new Chunk(this.world, chunkprimer, x, z);
        for (int i = 0; i < chunk.getBiomeArray().length; ++i) {
            chunk.getBiomeArray()[i] = (byte) Biome.getIdForBiome(Biomes.DESERT);
        }
        chunk.generateSkylightMap();
        return chunk;
    }

}
