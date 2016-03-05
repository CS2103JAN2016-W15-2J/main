package tasknote.ui;

import static javafx.stage.PopupWindow.AnchorLocation.WINDOW_BOTTOM_LEFT;
import static tasknote.ui.GuiConstant.PROPERTY_BACKGROUND_COLOR;
import static tasknote.ui.GuiConstant.PROPERTY_BACKGROUND_RADIUS;
import static tasknote.ui.GuiConstant.PROPERTY_FONT_SIZE;
import static tasknote.ui.GuiConstant.PROPERTY_FONT_WEIGHT;
import static tasknote.ui.GuiConstant.SPACING_BETWEEN_COMPONENTS;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Separator;
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
        // TODO
    }
    
    // TODO Start here
    public static void setNotification(Stage primaryStage, String title, String message) {
        Popup notificationContainer = new Popup();
        VBox notificationContent = setupNotificationContent(notificationContainer, title, message);
        setupNotificationContainerBehaviour(notificationContainer, notificationContent, primaryStage);
        runFadeAnimation(notificationContainer, notificationContent);
    }
    
    public static void setNotification(Stage primaryStage, String message) {
        setNotification(primaryStage, null, message);
    }
    
    private static void setupNotificationContainerBehaviour(Popup notificationContainer, VBox notificationContent, Stage primaryStage) {
        notificationContainer.setAnchorLocation(WINDOW_BOTTOM_LEFT);
        notificationContainer.setHideOnEscape(false);
        notificationContainer.getContent().add(notificationContent);
        notificationContainer.show(primaryStage);
    }
    
    private static VBox setupNotificationContent(Popup notificationContainer, String title, String message){
        VBox notificationContent = new VBox();
        Text titleMessage = (title == null || title.isEmpty()) ? new Text(DEFAULT_NOTIFICATION_TITLE) : new Text(message) ;
        Text exitButton = new Text("x");
        Text alertMessage = new Text(message);
        Separator separator = new Separator();
        
        HBox titleContent = getNotificationTitle(titleMessage, exitButton);
        
        setupNotificationContentPresentation(notificationContent);
        setupTitleMessagePresentation(titleMessage);
        setupExitButtonPresentation(exitButton);
        setupAlertMessagePresentation(alertMessage);
        
        setupExitButtonBehaviour(exitButton, notificationContainer);
        
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
    
    private static void setupNotificationContentPresentation(VBox notificationContent) {
        notificationContent.setStyle(String.format(PROPERTY_BACKGROUND_COLOR, "#1e2123")
                + String.format(PROPERTY_BACKGROUND_RADIUS, 10));
        notificationContent.setMinWidth(MINIMUM_NOTIFICATION_WIDTH);
        notificationContent.setSpacing(SPACING_BETWEEN_COMPONENTS);
        notificationContent.setPadding(new Insets(20, 20, 20, 20));
    }
    
    private static void setupTitleMessagePresentation(Text titleMessage) {
        titleMessage.setFill(Color.WHITE);
        titleMessage.setStyle(String.format(PROPERTY_FONT_WEIGHT, "bold")
                + String.format(PROPERTY_FONT_SIZE, 18));
    }
    
    private static void setupExitButtonPresentation(Text exitButton) {
        exitButton.setFill(Color.GRAY);
        exitButton.setStyle(String.format(PROPERTY_FONT_SIZE, 15));
    }
    
    private static void setupExitButtonBehaviour(Text exitButton, Popup notificationContainer) {
        exitButton.setOnMouseEntered((new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                exitButton.setFill(Color.WHITE);
                exitButton.setStyle(String.format(PROPERTY_FONT_WEIGHT, "bold")
                        + String.format(PROPERTY_FONT_SIZE, 15));
            }
        }));
        exitButton.setOnMouseExited((new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                exitButton.setFill(Color.GRAY);
                exitButton.setStyle(String.format(PROPERTY_FONT_WEIGHT, "normal")
                        + String.format(PROPERTY_FONT_SIZE, 15));
            }
        }));
        exitButton.setOnMouseClicked((new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                notificationContainer.hide();
            }
        }));
    }
    
    private static void setupAlertMessagePresentation(Text alertMessage) {
        alertMessage.setFill(Color.WHITE);
        alertMessage.setStyle(String.format(PROPERTY_FONT_SIZE, 15));
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
