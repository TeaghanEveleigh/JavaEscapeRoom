package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class Room2Controller {
    @FXML Label computerLabel;
    @FXML Button btnHelp;
    @FXML Button btnLogin;
    @FXML Rectangle monitorScreen;
    @FXML Rectangle rectangleText;
    @FXML Text titleComputer;

    @FXML
    private void showComputerLabel (){
        computerLabel.setOpacity(1);
    }
    @FXML void hideComputerLabel(){
        computerLabel.setOpacity(0);
    }
    @FXML void openComputer(){
        monitorScreen.toFront();
        btnHelp.toFront();
        btnLogin.toFront();
        rectangleText.toFront();
        titleComputer.toFront();
    }
}
