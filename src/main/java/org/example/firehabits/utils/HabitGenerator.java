package org.example.firehabits.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class HabitGenerator {
    private static final List<String> HABIT_NAMES = Arrays.asList(
            "Exercise",
            "Meditation",
            "Reading",
            "Journaling",
            "Healthy Eating",
            "Gratitude Practice",
            "Learning a New Skill",
            "Saving Money",
            "Volunteering",
            "Quality Time with Family"
    );

    private static final List<String> HABIT_COLORS = Arrays.asList(
            "blue",
            "red",
            "yellow"
    );

    private static final Random random = new Random();

    public static String generateRandomHabitName() {
        int index = random.nextInt(HABIT_NAMES.size());
        return HABIT_NAMES.get(index);
    }

    public static String generateRandomHabitColor() {
        int index = random.nextInt(HABIT_COLORS.size());
        return HABIT_COLORS.get(index);
    }
}