package org.example.firehabits;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class Habit implements Serializable {
    private String title;
    private String color;
    private final ArrayList<LocalDate> records = new ArrayList<>();

    public Habit(String title, String color) {
        this.title = title;
        this.color = color;
    }

    public String getTitle() {
        return this.title;
    }
    public String getColor() { return this.color; }

    public void setTitle(String title) {
        this.title = title;
    }

    public void generateRandomRecords(int count) {
        Random random = new Random();
        LocalDate today = LocalDate.now();

        for (int i = 0; i < count; i++) {
            int streakLength = random.nextInt(7) + 1; // Generate a random streak length between 1 and 7
            LocalDate startDate = today.minusDays(streakLength - 1);

            for (int j = 0; j < streakLength; j++) {
                LocalDate recordDate = startDate.plusDays(j);
                records.add(recordDate);
            }

            int gapLength = random.nextInt(3) + 1; // Generate a random gap length between 1 and 3
            today = startDate.minusDays(gapLength);
        }
    }
    public void switchRecord() {
        LocalDate today = LocalDate.now();
        if (records.contains(today)) {
            records.remove(today);
        } else {
            records.add(today);
        }
    }

    public boolean isRecordedToday() {
        LocalDate today = LocalDate.now();
        return records.contains(today);
    }

    public ArrayList<LocalDate> getRecords() {
        return records;
    }

    public boolean[] getCurrentWeekSequence() {
        boolean[] currentWeekSequence = new boolean[7];

        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = today.with(DayOfWeek.SUNDAY);

        for (LocalDate date = startOfWeek; !date.isAfter(endOfWeek); date = date.plusDays(1)) {
            int dayIndex = date.getDayOfWeek().getValue() - 1;
            boolean recorded = records.contains(date);
            currentWeekSequence[dayIndex] = recorded;
        }

        return currentWeekSequence;
    }

    public int getOverallStreak() {
        if (records.isEmpty()) {
            return 0;
        }

        int streak = 1;
        LocalDate previousDate = records.get(0);

        for (int i = 1; i < records.size(); i++) {
            LocalDate currentDate = records.get(i);
            long daysBetween = ChronoUnit.DAYS.between(previousDate, currentDate);

            if (daysBetween == 1) {
                streak++;
            } else if (daysBetween > 1) {
                streak = 1;
            }

            previousDate = currentDate;
        }

        return streak;
    }
}
