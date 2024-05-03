package dev.simukraft.data.sided;

public class ClientRuntime {

    private static String name;
    private static double money;
    private static int sims;

    private static boolean hasInfo = false;
    private static boolean hasRequestedInfo = false;
    private static long lastRequestAt = 0L;
    private static boolean allowRequest = true;

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        ClientRuntime.name = name;
    }

    public static double getMoney() {
        return money;
    }

    public static void setMoney(double money) {
        ClientRuntime.money = money;
    }

    public static int getSims() {
        return sims;
    }

    public static void setSims(int sims) {
        ClientRuntime.sims = sims;
    }

    public static boolean hasInfo() {
        return hasInfo;
    }

    public static void setHasInfo(boolean hasInfo) {
        ClientRuntime.hasInfo = hasInfo;
    }

    public static void setHasRequestedInfo(boolean hasRequstedInfo) {
        ClientRuntime.hasRequestedInfo = hasRequstedInfo;
    }

    public static boolean hasRequestedInfo() {
        return hasRequestedInfo;
    }

    public static long getLastRequestAt() {
        return lastRequestAt;
    }

    public static void setLastRequestAt(long lastRequestAt) {
        ClientRuntime.lastRequestAt = lastRequestAt;
    }

    public static boolean isAllowRequest() {
        return allowRequest;
    }

    public static void setAllowRequest(boolean allowRequest) {
        ClientRuntime.allowRequest = allowRequest;
    }
}
