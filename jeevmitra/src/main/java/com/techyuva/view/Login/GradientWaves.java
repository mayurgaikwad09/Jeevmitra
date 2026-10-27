package com.techyuva.view.Login;



import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

public class GradientWaves extends Pane {

    private final Path wave1 = new Path();
    private final Path wave2 = new Path();
    private final Path wave3 = new Path();

    public GradientWaves() {

        setMouseTransparent(true);
        setMinSize(0, 0);

        wave1.setFill(Color.web("#37B875", 0.18));
        wave2.setFill(Color.web("#15945A", 0.14));
        wave3.setFill(Color.web("#83DFA8", 0.28));

        wave1.setStroke(Color.TRANSPARENT);
        wave2.setStroke(Color.TRANSPARENT);
        wave3.setStroke(Color.TRANSPARENT);

        getChildren().addAll(wave1, wave2, wave3);

        // Update wave shapes when the window resizes
        widthProperty().addListener((obs, oldValue, newValue) -> {
            updateWaves();
        });

        heightProperty().addListener((obs, oldValue, newValue) -> {
            updateWaves();
        });

        // Different speeds create a layered motion effect
        animateWave(wave1, 45, 8);
        animateWave(wave2, -55, 11);
        animateWave(wave3, 35, 14);
    }

    private void updateWaves() {

        double w = getWidth();
        double h = getHeight();

        if (w <= 0 || h <= 0) {
            return;
        }

        makeWave(wave1, w, h, 0.58, 0.12);
        makeWave(wave2, w, h, 0.72, 0.20);
        makeWave(wave3, w, h, 0.84, 0.10);
    }

    private void makeWave(
        Path wave,
        double w,
        double h,
        double base,
        double height
    ) {

        double y = h * base;
        double amplitude = h * height;

        wave.getElements().setAll(
            new MoveTo(0, y),

            new CubicCurveTo(
                w * 0.25, y - amplitude,
                w * 0.25, y + amplitude,
                w * 0.50, y
            ),

            new CubicCurveTo(
                w * 0.75, y - amplitude,
                w * 0.75, y + amplitude,
                w, y
            ),

            new javafx.scene.shape.LineTo(w, h),
            new javafx.scene.shape.LineTo(0, h),
            new ClosePath()
        );
    }

    private void animateWave(
        Path wave,
        double distance,
        double seconds
    ) {

        Timeline timeline = new Timeline(
            new KeyFrame(
                Duration.ZERO,
                new KeyValue(
                    wave.translateXProperty(),
                    0,
                    Interpolator.EASE_BOTH
                )
            ),
            new KeyFrame(
                Duration.seconds(seconds),
                new KeyValue(
                    wave.translateXProperty(),
                    distance,
                    Interpolator.EASE_BOTH
                )
            )
        );

        timeline.setAutoReverse(true);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    
}


}
