package dev.simukraft.data.pack;

import com.google.gson.*;
import dev.simukraft.SimUKraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class NameReloadListener extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    private static final Random ran = new Random();

    public static List<String> MALE_NAMES = new ArrayList<>();
    public static List<String> FEMALE_NAMES = new ArrayList<>();
    public static List<String> LAST_NAMES = new ArrayList<>();

    public NameReloadListener() {
        super(GSON, SimUKraft.MOD_ID);
    }

    @Override
    protected void apply(@NotNull Map<ResourceLocation, JsonElement> pObject, @NotNull ResourceManager pResourceManager, @NotNull ProfilerFiller pProfiler) {
        ResourceLocation male_names = new ResourceLocation(SimUKraft.MOD_ID, "folk/names_male.json");
        ResourceLocation female_names = new ResourceLocation(SimUKraft.MOD_ID, "folk/names_female.json");
        ResourceLocation last_names = new ResourceLocation(SimUKraft.MOD_ID, "folk/names_last.json");

        readNames(pResourceManager, male_names, MALE_NAMES);
        readNames(pResourceManager, female_names, FEMALE_NAMES);
        readNames(pResourceManager, last_names, LAST_NAMES);
    }

    private void readNames(ResourceManager manager, ResourceLocation location, List<String> list) {
        for (Resource res : manager.getResourceStack(location)) {
            try (InputStream is = res.open(); Reader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                JsonObject json = GsonHelper.fromJson(GSON, reader, JsonObject.class);

                if(json == null) {
                    SimUKraft.LOGGER.error("Failed to load json data for names in {} from the {} data pack", location, res.sourcePackId());
                    return;
                }
                boolean replace = json.get("replace").getAsBoolean();

                if (replace) {
                    SimUKraft.LOGGER.info("Data pack {} has replaced all names in its category.", res.sourcePackId());
                    list.clear();
                }

                JsonArray array = json.getAsJsonArray("names");
                for (JsonElement name : array) {
                    list.add(name.getAsString());
                }

                SimUKraft.LOGGER.info("Loaded {} names from {}", list.size(), location);

            } catch (RuntimeException | IOException ex) {
                SimUKraft.LOGGER.error("Couldn't read names list {} in data pack {}", location, res.sourcePackId(), ex);
            }
        }
    }

    public static String getRandomLastName() {
        return  LAST_NAMES.get(ran.nextInt(LAST_NAMES.size()));
    }

    public static String getRandomFemaleName() {
        return  FEMALE_NAMES.get(ran.nextInt(FEMALE_NAMES.size()));
    }

    public static String getRandomMaleName() {
        return  MALE_NAMES.get(ran.nextInt(MALE_NAMES.size()));
    }
}
