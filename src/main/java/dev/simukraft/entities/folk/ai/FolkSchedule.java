package dev.simukraft.entities.folk.ai;

import dev.simukraft.SimUKraft;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.entity.schedule.Schedule;
import net.minecraft.world.entity.schedule.ScheduleBuilder;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

/**
 * This is how we register the folks schedule
 * https://minecraft.fandom.com/wiki/Daylight_cycle
 * Notes:
 * - 1hr in mc is 1000 ticks, which 50s real time
 */
public class FolkSchedule {

    public static DeferredRegister<Schedule> FOLK_SCHEDULES = DeferredRegister.create(ForgeRegistries.SCHEDULES, SimUKraft.MOD_ID);

    // Note: 1500 is 7:30AM
    public static final int WORK_START_TIME = 1500;

    // With the amount of work time, folks will end their shift at 4:30PM (9 hr shift 1 hr break lol)
    public static final int TOTAL_WORK_TIME = 9000;

    public static RegistryObject<Schedule> FOLK_TEST = register("folk_test", () -> new ScheduleBuilder(new Schedule()).changeActivityAt(10, Activity.IDLE).build());
    public static RegistryObject<Schedule> FOLK_DEFAULT = register("folk_default", () -> new ScheduleBuilder(new Schedule()).changeActivityAt(10, Activity.IDLE)
            .changeActivityAt(WORK_START_TIME, Activity.WORK)
            .changeActivityAt(WORK_START_TIME + TOTAL_WORK_TIME, Activity.MEET)
            .changeActivityAt(WORK_START_TIME + TOTAL_WORK_TIME + 1000, Activity.IDLE).
            changeActivityAt(WORK_START_TIME + TOTAL_WORK_TIME + 2000, Activity.REST).build());
    // TODO: Baby folk
    //public static final Schedule VILLAGER_BABY = register("villager_baby").changeActivityAt(10, Activity.IDLE).changeActivityAt(3000, Activity.PLAY).changeActivityAt(6000, Activity.IDLE).changeActivityAt(10000, Activity.PLAY).changeActivityAt(12000, Activity.REST).build();

    public static <T extends Schedule> RegistryObject<T> register(String pKey, Supplier<T> schedule) {
        return FOLK_SCHEDULES.register(pKey, schedule);
    }
}
