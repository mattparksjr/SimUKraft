package dev.simukraft.data.pack;

import com.google.gson.*;
import dev.simukraft.SimUKraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataReloadListener extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();

    public static List<Structure> structures = new ArrayList<>();

    public DataReloadListener() {
        super(GSON, SimUKraft.MOD_ID);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> pObject, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
        ResourceLocation resourcelocation = new ResourceLocation(SimUKraft.MOD_ID, "structures/buildings.json");

        List<Structure> structureList = new ArrayList<>();
        for (Resource res : pResourceManager.getResourceStack(resourcelocation)) {
            try (InputStream is = res.open(); Reader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                JsonObject json = GsonHelper.fromJson(GSON, reader, JsonObject.class);
                JsonArray structures = json.get("structures").getAsJsonArray();
                boolean replace = json.get("replace").getAsBoolean();

                if (replace) {
                    SimUKraft.LOGGER.info("Data pack {} has replaced all buildings.", res.sourcePackId());
                    structureList.clear();
                }

                for (JsonElement structure : structures) {
                    JsonObject structureData = structure.getAsJsonObject();
                    Structure finalStruct = new Structure(
                            structureData.get("id").getAsString(),
                            structureData.get("name").getAsString(),
                            structureData.get("category").getAsString(),
                            structureData.get("file").getAsString(), structureData.get("author").getAsString());
                    structureList.add(finalStruct);
                }

            } catch (RuntimeException | IOException ex) {
                SimUKraft.LOGGER.error("Couldn't read structure list {} in data pack {}", resourcelocation, res.sourcePackId(), ex);
            }
        }
        structures = structureList;
        SimUKraft.LOGGER.debug("Loaded {} structures", structureList.size());
    }
}
