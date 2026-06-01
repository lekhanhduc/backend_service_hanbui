package com.javabuilder.backendservice.utils;

public final class PageResponseUtils {

    private PageResponseUtils() {
    }

    public static int normalizePage(int page) {
        return Math.max(1, page);
    }

    public static int normalizeSize(int size) {
        return Math.clamp(size, 1, 10);
    }
}
