package org.example.firehabits;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.firehabits.utils.HabitGenerator;

import java.io.*;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;

public class FireHabitsApplication extends Application {
    private static final String HABITS_FILE = "habits.ser";
    private final ArrayList<Habit> habits = new ArrayList<>();
    private int currentHabit = -1;
    private Scene mainScene;

    private final int ANIMATION_DURATION = 200;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("add-habit-view.fxml"));
        this.mainScene = new Scene(fxmlLoader.load());
        this.mainScene.setFill(Color.web("#1C1920"));
        loadHabits();
        FireHabitsViewController fireHabitsViewController = fxmlLoader.getController();
        fireHabitsViewController.setApp(this);

        stage.setTitle("Fire Habits");
        stage.setScene(this.mainScene);
        stage.setResizable(false);
        stage.show();

        if (!this.habits.isEmpty()) setHabitView(0);
    }

    public void setHabitView(int index) throws IOException {
        if (index < 0 || index > this.habits.size()) {
            throw new RuntimeException("Out of habit bounds");
        }
        Habit habit = this.habits.get(index);
        System.out.println("Title: " + habit.getTitle() + ", Streak: " + habit.getOverallStreak());
        FXMLLoader loader = getViewLoader("current-habit-view.fxml");
        Parent newView = loader.load();
        FireHabitsViewController viewController = loader.getController();
        newView.setUserData(viewController);
        viewController.setApp(this);
        viewController.setHabitTitle(habit.getTitle());
        viewController.updateCircles(index);
        viewController.updateWeekCircles(habit.getCurrentWeekSequence());
        viewController.updateStreakText(habit.getOverallStreak());
        this.currentHabit = index;

        // Transition animation
        Node oldView = this.mainScene.getRoot();
        FadeTransition fadeOutTransition = new FadeTransition(Duration.millis(ANIMATION_DURATION), oldView);
        fadeOutTransition.setFromValue(1.0);
        fadeOutTransition.setToValue(0.0);

        FadeTransition fadeInTransition = new FadeTransition(Duration.millis(ANIMATION_DURATION), newView);
        fadeInTransition.setFromValue(0.0);
        fadeInTransition.setToValue(1.0);
        fadeOutTransition.play();

        fadeOutTransition.setOnFinished(event -> {
            this.mainScene.setRoot(newView);
            fadeInTransition.play();
        });
    }

    public void setAddHabitView() throws IOException {
        FXMLLoader loader = getViewLoader("add-habit-view.fxml");
        Parent newView = loader.load();
        FireHabitsViewController viewController = loader.getController();
        newView.setUserData(viewController);
        viewController.setApp(this);
        viewController.updateCircles(-1);
        this.currentHabit = -1;

        // Transition animation
        Node oldView = this.mainScene.getRoot();
        FadeTransition fadeOutTransition = new FadeTransition(Duration.millis(ANIMATION_DURATION), oldView);
        fadeOutTransition.setFromValue(1.0);
        fadeOutTransition.setToValue(0.0);

        FadeTransition fadeInTransition = new FadeTransition(Duration.millis(ANIMATION_DURATION), newView);
        fadeInTransition.setFromValue(0.0);
        fadeInTransition.setToValue(1.0);

        fadeOutTransition.play();

        fadeOutTransition.setOnFinished(event -> {
            this.mainScene.setRoot(newView);
            fadeInTransition.play();
        });
    }

    public FXMLLoader getViewLoader(String viewFileName) {
        URL viewUrl = getClass().getResource(viewFileName);
        if (viewUrl == null) {
            throw new RuntimeException("Scene resource not found");
        }
        return new FXMLLoader(viewUrl);
    }



    public void newHabit() throws IOException {
        Habit newHabit = new Habit(HabitGenerator.generateRandomHabitName(), HabitGenerator.generateRandomHabitColor());
        this.habits.add(newHabit);
        setHabitView(this.habits.size() - 1);
        saveHabits();
    }

    public ArrayList<Habit> getHabits() {
        return habits;
    }

    public void updateHabitTitle(String title) {
        if (this.currentHabit == -1) return;

        Habit habit = this.habits.get(this.currentHabit);
        habit.setTitle(title);
        saveHabits();
    }

    public void switchHabitRecord() {
        Parent rootNode = (Parent) this.mainScene.getRoot();

        FireHabitsViewController viewController = (FireHabitsViewController) rootNode.getUserData();

        Habit habit = this.habits.get(this.currentHabit);
        habit.switchRecord();
        viewController.updateWeekCircles(habit.getCurrentWeekSequence());
        viewController.updateStreakText(habit.getOverallStreak());
        saveHabits();
    }

    public void switchNextHabit() throws IOException {
        System.out.println("Next Current: " + this.currentHabit);
        for (int i = 0; i < this.habits.size(); i++) {
            Habit hab = this.habits.get(i);
            System.out.println(i + " " + hab.getTitle());
        }

        if (this.habits.isEmpty()) return;
        if (this.currentHabit == -1) {
            setHabitView(0);
            return;
        }
        if (this.currentHabit == this.habits.size() - 1) {
            setAddHabitView();
            return;
        }
        setHabitView(this.currentHabit + 1);
    }

    public void switchPrevHabit() throws IOException {
        System.out.println("Next Current: " + this.currentHabit);
        if (this.habits.isEmpty()) return;
        if (this.currentHabit == -1) {
            setHabitView(this.habits.size() - 1);
            return;
        }
        if (this.currentHabit == 0) {
            setAddHabitView();
            return;
        }
        setHabitView(this.currentHabit - 1);
    }

    public void saveHabits() {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(HABITS_FILE))) {
            outputStream.writeObject(habits);
            System.out.println("Habits saved successfully.");
        } catch (IOException e) {
            System.out.println("Failed to save habits: " + e.getMessage());
        }
    }

    public void loadHabits() {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(HABITS_FILE))) {
            habits.clear();
            ArrayList<Habit> loadedHabits = (ArrayList<Habit>) inputStream.readObject();
            habits.addAll(loadedHabits);
            System.out.println("Habits loaded successfully.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Failed to load habits: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch();
    }
}