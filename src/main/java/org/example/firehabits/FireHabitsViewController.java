package org.example.firehabits;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import org.example.firehabits.utils.ButtonScaleAnimation;

public class FireHabitsViewController implements Initializable {
    private FireHabitsApplication app;

    private ObservableList<Circle> circles;
    private IntegerProperty activeCircleIndex;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TextField habitTitle;

    @FXML
    private Text streakText;

    @FXML
    private Button actionButton;

    @FXML
    private Button prevHabitButton;

    @FXML
    private Button nextHabitButton;

    @FXML
    private ImageView buttonImageView;

    @FXML
    private HBox circleBox;

    @FXML private HBox weekBox;

    public void updateWeekCircles(boolean[] sequence) {
        if (weekBox == null) return;
        ObservableList<Node> children = weekBox.getChildren();
        for (int i = 0; i < children.size(); i++) {
            StackPane stackPane = (StackPane) children.get(i);
            Circle circle = (Circle) stackPane.getChildren().get(0);
            Text text = (Text) stackPane.getChildren().get(1);
            updateWeekCircle(circle, text, sequence[i]);
        }
    }

    @FXML
    private void onNewHabitClick(ActionEvent event) throws IOException {
        this.app.newHabit();
    }

    @FXML
    private void onSwitchRecord(ActionEvent event) throws IOException {
        this.app.switchHabitRecord();
    }

    @FXML
    private void onLeftClick(ActionEvent event) throws  IOException {
        this.app.switchPrevHabit();
    }

    @FXML
    private void onRightClick(ActionEvent event) throws  IOException {
        this.app.switchNextHabit();
    }

    private void redrawCircles() {
        circleBox.getChildren().clear();
        circleBox.getChildren().addAll(circles);
    }

    private Image fireBlueImage;
    private Image fireRedImage;
    private Image fireYellowImage;
    private Image fireOffImage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (this.habitTitle != null) {
            rootPane.setOnMouseClicked(event -> {
                    rootPane.requestFocus();
                    this.app.updateHabitTitle(habitTitle.getText());
            });
        }



        ButtonScaleAnimation.applyScaleAnimation(actionButton);
        ButtonScaleAnimation.applyScaleAnimation(prevHabitButton);
        ButtonScaleAnimation.applyScaleAnimation(nextHabitButton);
        circles = FXCollections.observableArrayList();
        activeCircleIndex = new SimpleIntegerProperty(-1);
        fireOffImage = new Image(getClass().getResourceAsStream("images/fire-off.png"));
        fireRedImage = new Image(getClass().getResourceAsStream("images/fire-red.png"));
        fireYellowImage = new Image(getClass().getResourceAsStream("images/fire-yellow.png"));
        fireBlueImage = new Image(getClass().getResourceAsStream("images/fire-blue.png"));

        redrawCircles();
    }

    private static final String ACTIVE_FILL = "WHITE";
    private static final String ACTIVE_TEXT_FILL = "BLACK";
    private static final String INACTIVE_FILL = "#0000ff00";
    private static final String INACTIVE_TEXT_FILL = "WHITE";

    private void updateWeekCircle(Circle circle, Text text, boolean isActive) {
        if (isActive) {
            circle.setFill(Color.web(ACTIVE_FILL));
            text.setFill(Color.web(ACTIVE_TEXT_FILL));
        } else {
            circle.setFill(Color.web(INACTIVE_FILL));
            text.setFill(Color.web(INACTIVE_TEXT_FILL));
        }
    }

    public void updateStreakText(int streakDays) {
        if (streakDays <= 1) {
            streakText.setText("");
            return;
        }
        streakText.setText("Your current streak is " + streakDays + " days");
    }

    public void updateCircles(int activeIndex) {
        circles.clear();
        for (int i = 0; i < app.getHabits().size(); i++) {
            final int index = i;
            Circle circle = new Circle(6, Color.WHITE);
            circle.setStroke(Color.BLACK);
            circle.setStrokeType(StrokeType.INSIDE);
            circle.opacityProperty().bind(Bindings.createDoubleBinding(() ->
                    activeIndex == index ? 1.0 : 0.4, activeCircleIndex));
            circles.add(circle);
        }
        activeCircleIndex.set(activeIndex);
        redrawCircles();
        ImageView plusIcon = new ImageView(new Image(getClass().getResource("images/plus-icon.png").toExternalForm()));
        plusIcon.setFitWidth(12);
        plusIcon.setFitHeight(12);
        circleBox.getChildren().add(plusIcon);
        if (activeIndex == -1) {
            plusIcon.setOpacity(1);
        } else {
            plusIcon.setOpacity(0.4);
        }
    }

    public void setHabitTitle(String title, String color) {
        this.habitTitle.setText(title);
        if (Objects.equals(color, "blue")) {
            habitTitle.setStyle("-fx-background-color: transparent; -fx-text-fill: #2E7DF9;");
        } else if (Objects.equals(color, "red")) {
            habitTitle.setStyle("-fx-background-color: transparent; -fx-text-fill: #F92E2E;");
        } else if (Objects.equals(color, "yellow")) {
            habitTitle.setStyle("-fx-background-color: transparent; -fx-text-fill: #F09B00;");
        } else {
            habitTitle.setStyle("-fx-background-color: transparent; -fx-text-fill: #F09B00;");
        }
    }

    public void setActive(boolean active, String color) {
            if (active) {
                if (Objects.equals(color, "blue")) {
                    buttonImageView.setImage(fireBlueImage);
                } else if (Objects.equals(color, "red")) {
                    buttonImageView.setImage(fireRedImage);
                } else if (Objects.equals(color, "yellow")) {
                    buttonImageView.setImage(fireYellowImage);
                } else {
                    buttonImageView.setImage(fireYellowImage);
                }

            } else {
                buttonImageView.setImage(fireOffImage);
            }
    }

    public void setApp(FireHabitsApplication app) {
        this.app = app;
    }
}