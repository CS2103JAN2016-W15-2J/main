package tasknote.ui;

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

    private static final char WHITESPACE = ' ';
    private static final String TIME_DELIMITER = ":";

    private static final String COLOR_HEX_CODE_WHITE = "#ffffff";

    private static final String PROPERTY_FONT_SIZE = "-fx-font-size: %1$dpx;";
    private static final String PROPERTY_TEXT_FILL = "-fx-text-fill: %1$s;";

    private static final int INTERVAL_SECOND_ANIMATION = 1;

    private static int SPACING_BETWEEN_COMPONENTS = 10;
    private static int FONT_SIZE = 20;

    private static final Calendar _currentTime = Calendar.getInstance();
    private static ClockContainer _timerContainer = null;
    private static Label _monthAndDateLabel = new Label();
    private static Label _hourMinuteAndSecondLabel = new Label();
    private static Label _dayOfWeekLabel = new Label();

    private ClockContainer() {
        // Only one instance of ClockContainer is permitted
    }

    public static ClockContainer getInstance() {
    	if (_timerContainer == null) {
    	    _timerContainer = new ClockContainer();
    	    _timerContainer.setupTimer();
    	}
    
    	return _timerContainer;
    }

    private void setupTimer() {
        this.setHgap(SPACING_BETWEEN_COMPONENTS);

        setupLabelStyle();

        GridPane.setConstraints(_dayOfWeekLabel, 0, 0);
        GridPane.setConstraints(_monthAndDateLabel, 1, 0);
        GridPane.setConstraints(_hourMinuteAndSecondLabel, 1, 1);

        _timerContainer.getChildren().addAll(_monthAndDateLabel, _hourMinuteAndSecondLabel, _dayOfWeekLabel);

        _timerContainer.bindToTime();
    }

    private void setupLabelStyle() {
        _monthAndDateLabel.setStyle(String.format(PROPERTY_TEXT_FILL, COLOR_HEX_CODE_WHITE) 
	                               + String.format(PROPERTY_FONT_SIZE, FONT_SIZE));
        _hourMinuteAndSecondLabel.setStyle(String.format(PROPERTY_TEXT_FILL, COLOR_HEX_CODE_WHITE) 
	                                      + String.format(PROPERTY_FONT_SIZE, FONT_SIZE));
        _dayOfWeekLabel.setStyle(String.format(PROPERTY_TEXT_FILL, COLOR_HEX_CODE_WHITE) 
	                            + String.format(PROPERTY_FONT_SIZE, 25) + "-fx-font-weight: bold;");
    }

    private void bindToTime() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(INTERVAL_SECOND_ANIMATION), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        _currentTime.setTimeInMillis(System.currentTimeMillis());
                        String monthString = pad(3, WHITESPACE, _currentTime.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH));
                        String dayOfWeek = pad(3, WHITESPACE, _currentTime.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH).toUpperCase());
                        String dayString = pad(2, WHITESPACE, String.valueOf(_currentTime.get(Calendar.DAY_OF_MONTH)));
                        String hourString = pad(2, '0', _currentTime.get(Calendar.HOUR) == 0 ? "12" : String.valueOf(_currentTime.get(Calendar.HOUR)));
                        String minuteString = pad(2, '0', String.valueOf(_currentTime.get(Calendar.MINUTE)));
                        String secondString = pad(2, '0', String.valueOf(_currentTime.get(Calendar.SECOND)));
                        String ampmString = _currentTime.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM";
                        _dayOfWeekLabel.setText(dayOfWeek);
                        _monthAndDateLabel.setText(monthString + WHITESPACE + dayString);
                        _hourMinuteAndSecondLabel.setText(hourString + TIME_DELIMITER + minuteString + TIME_DELIMITER + secondString + WHITESPACE + ampmString);
                    }
                }), new KeyFrame(Duration.seconds(INTERVAL_SECOND_ANIMATION)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private static String pad(int fieldWidth, char paddingCharacter, String originalString) {
    	int maximumLengthOfString = Math.min(fieldWidth, originalString.length());
    	originalString = originalString.substring(0, maximumLengthOfString);
    	String paddedString = String.format("%" + fieldWidth + "s", originalString).replace(WHITESPACE, paddingCharacter);
    
    	return paddedString;
    }
}
