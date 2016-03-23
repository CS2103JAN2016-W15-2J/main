package tasknote.ui;

import static tasknote.ui.GuiConstant.SPACING_BETWEEN_COMPONENTS;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Separator;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Notification {
    private final static int DURATION_IN_SECOND_NOTIFICATION = 5;
    private final static int MINIMUM_NOTIFICATION_WIDTH = 400;
    private final static String DEFAULT_NOTIFICATION_TITLE = "Notification";

    private Notification() {
        // Prevent instantiation of Notification
    }
    
    /**
     * Create a notification.
     * 
     * @param primaryStage      Enter the primary state used - that is, the "owner" of the notification.
     * @param title             Set the title of the notification.
     * @param message           Set the content of the notification.
     */
    public static void setupNotification(Stage primaryStage, String title, String message) {
        Popup notificationContainer = new Popup();
        VBox notificationContent = setupNotificationContent(notificationContainer, title, message);
        setNotificationContainerBehaviour(notificationContainer, notificationContent, primaryStage);
        runFadeAnimation(notificationContainer, notificationContent);
    }
    
    /**
     * Create a notification. 
     * However, in this case, the title is omitted, which will result in "Notification" being used as title.
     * 
     * @param primaryStage      Enter the primary state used - that is, the "owner" of the notification.
     * @param message           Set the content of the notification.
     */
    public static void setupNotification(Stage primaryStage, String message) {
        setupNotification(primaryStage, null, message);
    }
    
    private static void setNotificationContainerBehaviour(Popup notificationContainer, VBox notificationContent, Stage primaryStage) {
        notificationContainer.setHideOnEscape(false);
        notificationContainer.getContent().add(notificationContent);
        notificationContainer.setAutoFix(true);
        notificationContainer.centerOnScreen();
        notificationContainer.show(primaryStage);
    }
    
    private static VBox setupNotificationContent(Popup notificationContainer, String title, String message){
        VBox notificationContent = new VBox();
        Text titleMessage = (title == null || title.isEmpty()) ? new Text(DEFAULT_NOTIFICATION_TITLE) : new Text(message) ;
        Text exitButton = new Text("x");
        Text alertMessage = new Text(message);
        Separator separator = new Separator();
        
        HBox titleContent = getNotificationTitle(titleMessage, exitButton);
        
        setNotificationContentPresentation(notificationContent);
        setTitleMessagePresentation(titleMessage);
        setExitButtonPresentation(exitButton);
        setAlertMessagePresentation(alertMessage);
        
        setExitButtonBehaviour(exitButton, notificationContainer);
        
        notificationContent.getChildren().addAll(titleContent, separator, alertMessage);
        
        return notificationContent;
    }
    
    private static HBox getNotificationTitle(Text titleMessage, Text exitButton) {
        HBox titleContent = new HBox();
        
        HBox leftCell = new HBox();
        leftCell.setAlignment( Pos.CENTER_LEFT );
        HBox.setHgrow( leftCell, Priority.ALWAYS );
        
        HBox rightCell = new HBox();
        rightCell.setAlignment( Pos.CENTER_RIGHT );
        HBox.setHgrow( rightCell, Priority.ALWAYS );
        
        leftCell.getChildren().add(titleMessage);
        rightCell.getChildren().add(exitButton);
        
        titleContent.getChildren().addAll(leftCell, rightCell);
        
        return titleContent;
    }
    
    private static void setNotificationContentPresentation(VBox notificationContent) {
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(5.0);
        dropShadow.setOffsetX(3.0);
        dropShadow.setOffsetY(3.0);
        dropShadow.setColor(Color.rgb(18,19,21));
        
        notificationContent.setEffect(dropShadow);
        
        notificationContent.getStyleClass().add("notification-content");
        notificationContent.setMinWidth(MINIMUM_NOTIFICATION_WIDTH);
        notificationContent.setSpacing(SPACING_BETWEEN_COMPONENTS);
    }
    
    private static void setTitleMessagePresentation(Text titleMessage) {
        titleMessage.getStyleClass().add("notification-title");
    }
    
    private static void setExitButtonPresentation(Text exitButton) {
        exitButton.getStyleClass().addAll("notification-exit-button");
    }
    
    private static void setExitButtonBehaviour(Text exitButton, Popup notificationContainer) {
        exitButton.setOnMouseClicked((new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                notificationContainer.hide();
            }
        }));
    }
    
    private static void setAlertMessagePresentation(Text alertMessage) {
        alertMessage.getStyleClass().add("notification-alert-message");
    }
    
    private static void runFadeAnimation(Popup popupNotification, Pane pane) {
        Timeline timeline = new Timeline();
        KeyFrame key = new KeyFrame(Duration.seconds(DURATION_IN_SECOND_NOTIFICATION), 
                new KeyValue(popupNotification.opacityProperty(), 0)); 
        timeline.getKeyFrames().add(key);
        timeline.setOnFinished((event) -> popupNotification.hide()); 
        timeline.play();
        
        pane.setOnMouseEntered((new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                //TODO Make it replay animation upon hover
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
