package com.quodex.matchbox.util;

import org.springframework.stereotype.Component;

@Component
public class SlugUtils {

    public static String generateUniqueSlug(String name, SlugExistenceChecker checker) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or empty for slug generation");
        }

        String baseSlug = name.trim()
                .toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-");

        String slug = baseSlug;
        int counter = 1;

        // Use functional interface to check if slug exists
        while (checker.exists(slug)) {
            slug = baseSlug + "-" + counter++;
        }

        return slug;
    }

    // Functional interface to check if slug already exists
    @FunctionalInterface
    public interface SlugExistenceChecker {
        boolean exists(String slug);
    }
}
