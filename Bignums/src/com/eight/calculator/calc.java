package com.eight.calculator;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public final class calc extends Application {

    private boolean numberInputting;

    @FXML
    private TextField display;

    public calc() {

        this.numberInputting = false;
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Calculator");
        stage.setOnCloseRequest(x -> {
            Platform.exit();
        });
        stage.setResizable(false);
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("main.fxml"))));
        stage.show();
    }

    @FXML
    protected void handleOnAnyButtonClicked(ActionEvent evt) {
        Button button = (Button)evt.getSource();
        final String buttonText = button.getText();
        if (buttonText.equals("C")) {
            numberInputting = false;
            display.setText("0");;
            return;
        }
        if (buttonText.matches("[0-9\\.]")) {
            if (display.getText().equals("0"))
                display.clear();
            if (!numberInputting) {
                numberInputting = true;

            }
            display.appendText(buttonText);
            return;
        }
        if (buttonText.matches("[＋－×/^]")) {

                switch (buttonText){
                    case "^":
                        display.appendText("^");
                        break;
                    case "×":
                        display.appendText("*");
                        break;
                    case "/":
                        display.appendText("/");
                        break;
                    case "＋":
                        display.appendText("+");
                        break;
                    case "－":
                        display.appendText("-");
                        break;
                }


            numberInputting = false;
            return;
        }
        if (buttonText.equals("=")) {

            display.setText(calculator.gui(display.getText()));
            numberInputting = false;
            return;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}