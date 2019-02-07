package com.github.bartimaeusnek.bettervoidworlds.common.world.chunkgenerator;

import com.github.bartimaeusnek.bettervoidworlds.common.config.ConfigHandler;
import com.github.bartimaeusnek.bettervoidworlds.common.world.chunkgenerator.noise.UniversalNoise;
import com.github.bartimaeusnek.bettervoidworlds.util.XSTR;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.IChunkGenerator;

import javax.annotation.Nullable;
import java.util.List;

public abstract class UniversalChunkGenerator implements IChunkGenerator {

    protected static final byte worldgenOffset = 8;
    protected static final int SEALEVEL = 60 + ConfigHandler.bedrockThickness;
    protected final XSTR random;
    protected final World world;
    protected final UniversalNoise universalNoise;

    public UniversalChunkGenerator(World world) {
        this.world = world;
        random = new XSTR(world.getSeed());
        universalNoise = new UniversalNoise(world.getSeed());
    }


    @Override
    public Chunk generateChunk(int x, int z) {
        ChunkPrimer chunkprimer = new ChunkPrimer();
        Chunk chunk = new Chunk(this.world, chunkprimer, x, z);
        chunk.generateSkylightMap();
        for (int i = 0; i < chunk.getBiomeArray().length; ++i) {
            chunk.getBiomeArray()[i] = (byte) 1;
        }
        chunk.generateSkylightMap();
        return chunk;
    }

    @Override
    public void populate(int x, int z) {
    }

    @Override
    public boolean generateStructures(Chunk chunkIn, int x, int z) {
        return false;
    }

    @Override
    public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) {
        return ConfigHandler.creatureSpawn ? this.world.getBiome(pos).getSpawnableList(creatureType) : (creatureType.getPeacefulCreature() && ConfigHandler.passiveSpawn) ? this.world.getBiome(pos).getSpawnableList(creatureType) : null;
    }

    @Nullable
    @Override
    public BlockPos getNearestStructurePos(World worldIn, String structureName, BlockPos position, boolean findUnexplored) {
        return null;
    }

    @Override
    public void recreateStructures(Chunk chunkIn, int x, int z) {

    }

    @Override
    public boolean isInsideStructure(World worldIn, String structureName, BlockPos pos) {
        return false;
    }
}