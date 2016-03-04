package tasknote.ui;

import static javafx.stage.PopupWindow.AnchorLocation.WINDOW_BOTTOM_LEFT;
import static tasknote.ui.GuiConstant.PROPERTY_BACKGROUND_COLOR;
import static tasknote.ui.GuiConstant.PROPERTY_BACKGROUND_RADIUS;
import static tasknote.ui.GuiConstant.PROPERTY_FONT_WEIGHT;
import static tasknote.ui.GuiConstant.SPACING_BETWEEN_COMPONENTS;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Separator;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Notification {
    
    private final static int DURATION_IN_SECOND_NOTIFICATION = 5;
    private final static int MINIMUM_NOTIFICATION_WIDTH = 400;
    
    private Notification() {
        // TODO
    }
    
    public static void newMessage(Stage primaryStage, String message)
    {
        Popup notificationAlert = new Popup();
        VBox notificationContainer = new VBox();
        Text titleMessage = new Text("Notification");
        Text alertMessage = new Text(message);
        Separator separator = new Separator();
        
        notificationContainer.setMinWidth(MINIMUM_NOTIFICATION_WIDTH);
        
        titleMessage.setFill(Color.WHITE);
        titleMessage.setStyle(String.format(PROPERTY_FONT_WEIGHT, "bold"));
        alertMessage.setFill(Color.WHITE);
        alertMessage.setStyle(String.format(PROPERTY_FONT_WEIGHT, "bold"));
        
        notificationContainer.setStyle(String.format(PROPERTY_BACKGROUND_COLOR, "#1e2123")
                + String.format(PROPERTY_BACKGROUND_RADIUS, 10));
        notificationContainer.setSpacing(SPACING_BETWEEN_COMPONENTS);
        notificationContainer.setPadding(new Insets(20, 20, 20, 20));
        notificationContainer.getChildren().addAll(titleMessage, separator,alertMessage);
        
        // TODO Set position
        notificationAlert.setAnchorLocation(WINDOW_BOTTOM_LEFT);
        notificationAlert.setHideOnEscape(false);
        notificationAlert.getContent().add(notificationContainer);
        notificationAlert.show(primaryStage);
        
        fade(notificationAlert, notificationContainer);
    }
    
    private static void fade(Popup popupNotification, Pane pane) {
        Timeline timeline = new Timeline();
        KeyFrame key = new KeyFrame(Duration.seconds(DURATION_IN_SECOND_NOTIFICATION), 
                new KeyValue(popupNotification.opacityProperty(), 0)); 
        timeline.getKeyFrames().add(key);
        timeline.setOnFinished((event) -> popupNotification.hide()); 
        timeline.play();
        
        pane.setOnMouseEntered((new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                timeline.stop();
            }
        }));
        
        pane.setOnMouseExited((new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                timeline.play();
            }
        }));
    }
}
