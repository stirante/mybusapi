package com.stirante.mybusapi;


import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;

public class MainController {

    @FXML
    public StackPane stack;
    @FXML
    private WebView webview;

    StackPane getStack() {
        return stack;
    }

    WebView getWebview() {
        return webview;
    }

}