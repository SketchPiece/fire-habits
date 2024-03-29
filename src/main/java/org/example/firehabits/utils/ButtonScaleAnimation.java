package org.example.firehabits.utils;

import javafx.animation.ScaleTransition;
import javafx.scene.control.Button;
import javafx.util.Duration;

public class ButtonScaleAnimation {
    private static final double SCALE_FACTOR = 0.9;
    private static final Duration DURATION = Duration.millis(100);

    public static void applyScaleAnimation(Button button) {
        ScaleTransition scaleTransition = new ScaleTransition(DURATION, button);
        scaleTransition.setFromX(1.0);
        scaleTransition.setFromY(1.0);
        scaleTransition.setToX(SCALE_FACTOR);
        scaleTransition.setToY(SCALE_FACTOR);

        button.setOnMousePressed(event -> {
            scaleTransition.setRate(1.0);
            scaleTransition.play();
        });

        button.setOnMouseReleased(event -> {
            scaleTransition.setRate(-1.0);
            scaleTransition.play();
        });
    }
}