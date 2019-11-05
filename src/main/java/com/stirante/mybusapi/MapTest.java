package com.stirante.mybusapi;

import com.stirante.mybusapi.model.Vehicle;
import com.sun.javafx.webkit.WebConsoleListener;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class MapTest extends Application {

    public static MainController controller;
    private List<Vehicle> vehicles;
    private static MyBusAPI api;

    public static void main(String[] args) {
        api = new MyBusAPI(City.LUBLIN);
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(MapTest.class.getResource("/MainWindow.fxml"));
        BorderPane node = fxmlLoader.load();
        controller = fxmlLoader.getController();
        final WebEngine engine = controller.getWebview().getEngine();
        WebConsoleListener.setDefaultListener((webView, message, lineNumber, sourceId) -> System.out.println("Console: [" + sourceId + ":" + lineNumber + "] " + message));
        engine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.SUCCEEDED) {
                new Thread(() -> {
                    Platform.runLater(() -> {
                        engine.executeScript("document.setLoc([51.248773, 22.566046])");
                    });
                    while (true) {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        try {
                            vehicles = api.getVehicles("", "");
                        } catch (Exception e) {
                            e.printStackTrace();
                            vehicles = new ArrayList<>();
                        }
                        Platform.runLater(() -> {
                            engine.executeScript("document.clearBuses()");
                            for (Vehicle v : vehicles) {
                                engine.executeScript("document.addBus([" + v.getLat() + ", " + v.getLon() + "])");
                            }
//                            engine.executeScript("document.setLoc([" + lat + ", " + lon + "])");
                        });
                    }
                }).start();
            }
        });
        engine.load(MapTest.class.getResource("/map.html").toString());
        stage.setTitle("Buses");
        Scene scene = new Scene(node, 1000, 700, Color.web("#666970"));
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(event -> System.exit(0));
    }

}
