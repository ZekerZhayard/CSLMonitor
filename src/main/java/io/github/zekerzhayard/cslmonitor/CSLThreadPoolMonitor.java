package io.github.zekerzhayard.cslmonitor;

import java.util.AbstractMap;
import java.util.NoSuchElementException;
import java.util.concurrent.CopyOnWriteArrayList;

import customskinloader.config.SkinSiteProfile;

public class CSLThreadPoolMonitor {
    static CopyOnWriteArrayList<AbstractMap.SimpleEntry<Thread, AbstractMap.SimpleEntry<Long, String>>> threadsList = new CopyOnWriteArrayList<>();

    public static SkinSiteProfile put(SkinSiteProfile ssp) {
        try {
            CSLThreadPoolMonitor.threadsList.stream().filter(e -> e.getKey().equals(Thread.currentThread())).findFirst().get().getValue().setValue(ssp.name);
        } catch (NoSuchElementException e) {
            CSLThreadPoolMonitor.threadsList.add(new AbstractMap.SimpleEntry<>(Thread.currentThread(), new AbstractMap.SimpleEntry<>(System.currentTimeMillis(), ssp.name)));
        }
        return ssp;
    }

    public static boolean remove(boolean b) {
        CSLThreadPoolMonitor.threadsList.removeIf(e -> e.getKey().equals(Thread.currentThread()));
        return b;
    }
}