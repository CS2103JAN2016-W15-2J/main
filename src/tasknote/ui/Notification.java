package tasknote.ui;

import static tasknote.ui.GuiConstant.PROPERTY_BACKGROUND_COLOR;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class Notification {
    
    private Notification() {
        // TODO
    }
    
    public static void newMessage(Stage primaryStage, String message)
    {
        Stage dialog = new Stage();
        GridPane grid = new GridPane();
        Text alertMessage = new Text(message);
        Scene scene = new Scene(grid);
        dialog.initOwner(primaryStage);
        dialog.initStyle(StageStyle.UNDECORATED);
        // TODO Set position
        dialog.setX(primaryStage.getX() + primaryStage.getWidth() - 200);
        dialog.setY(primaryStage.getY() + primaryStage.getHeight() - 150);
        grid.setStyle(String.format(PROPERTY_BACKGROUND_COLOR, "white"));
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.add(alertMessage, 0, 0);
        dialog.setScene(scene);
        dialog.show();
        
        fade(dialog, grid);
    }
    
    private static void fade(Stage stage, Pane pane) {
        Timeline timeline = new Timeline();
        KeyFrame key = new KeyFrame(Duration.seconds(10), new KeyValue (stage.getScene().getWindow().opacityProperty(), 0)); 
        timeline.getKeyFrames().add(key);
        timeline.setOnFinished((event) -> stage.close()); 
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
