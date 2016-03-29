package tasknote.ui;

import static tasknote.ui.GuiConstant.SPACING_BETWEEN_COMPONENTS;

import java.util.Calendar;
import java.util.Locale;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

public class ClockContainer extends GridPane {
    private final String FORMAT_TIME = "%1$02d:%2$02d:%3$02d %4$s";
    private final String FORMAT_DATE = "%1$3s %2$ 2d";
    private final String FORMAT_DAY = "%1$3s";

    private static final int INTERVAL_SECOND_ANIMATION = 1;

    private static final Calendar _currentTime = Calendar.getInstance();
    private static ClockContainer _timerContainer = null;
    private static Label _monthAndDateLabel = new Label();
    private static Label _hourMinuteAndSecondLabel = new Label();
    private static Label _dayOfWeekLabel = new Label();

    private ClockContainer() {
     // Only one instance of ClockContainer is permitted
    }

    /**
     * getInstance() allows user to get an instance of 
     * ClockContainer.
     * 
     * @return       The one instance of ClockContainer.
     */
    public static ClockContainer getInstance() {
        if (_timerContainer == null) {
            _timerContainer = new ClockContainer();
            _timerContainer.setupClockContainer();
        }

        return _timerContainer;
    }

    /*
     * As per name, set up clock container.
     */
    private void setupClockContainer() {
        setClockContainerPresentation();
        setLabelsPresentation();

        _timerContainer.getChildren().addAll(_monthAndDateLabel, _hourMinuteAndSecondLabel, _dayOfWeekLabel);

        setLabelAnimation();
    }
    
    private void setClockContainerPresentation() {
        this.getStyleClass().add("clock-container");
        this.setHgap(SPACING_BETWEEN_COMPONENTS);
    }

    /*
     * Set up the presentation of the labels in ClockContainer.
     */
    private void setLabelsPresentation() {
         _monthAndDateLabel.getStyleClass().add("clock-label-month-date");
         _hourMinuteAndSecondLabel.getStyleClass().add("clock-label-hour-minute-second");
         _dayOfWeekLabel.getStyleClass().add("clock-label-day-of-week");
         GridPane.setConstraints(_dayOfWeekLabel, 0, 0);
         GridPane.setConstraints(_monthAndDateLabel, 1, 0);
         GridPane.setConstraints(_hourMinuteAndSecondLabel, 1, 1);
         
         setLabelsToCurrentTime();
    }

    /*
     * Set up the labels to update every second.
     */
    private void setLabelAnimation() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(INTERVAL_SECOND_ANIMATION), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        setLabelsToCurrentTime();
                    }
                }), new KeyFrame(Duration.seconds(INTERVAL_SECOND_ANIMATION)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
    
    private void setLabelsToCurrentTime() {
        _currentTime.setTimeInMillis(System.currentTimeMillis());
        
        String monthString = _currentTime.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH).substring(0, 3);
        String dayOfWeek = _currentTime.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH).toUpperCase().substring(0, 3);
        int dayOfMonth = _currentTime.get(Calendar.DAY_OF_MONTH);
        int hours = _currentTime.get(Calendar.HOUR) == 0 ? 12 : _currentTime.get(Calendar.HOUR);
        int minutes = _currentTime.get(Calendar.MINUTE);
        int seconds = _currentTime.get(Calendar.SECOND);
        String ampmString = _currentTime.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM";
        
        _dayOfWeekLabel.setText(String.format(FORMAT_DAY, dayOfWeek));
        _monthAndDateLabel.setText(String.format(FORMAT_DATE, monthString, dayOfMonth));
        _hourMinuteAndSecondLabel.setText(String.format(FORMAT_TIME, hours, minutes, seconds, ampmString));
    }
}
