package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class Room2Controller {
    @FXML private Label safeLabel;
    @FXML private Rectangle monitorStand;
    @FXML private Line entranceLine1;
    @FXML private Line entranceLine2;
    @FXML private Line entranceLine3;
    @FXML private Text entranceLabel;

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
        monitorStand.toFront();
    }
    @FXML private void showEntranceLabel(){
        entranceLine1.setOpacity(1);
        entranceLine2.setOpacity(1);
        entranceLine3.setOpacity(1);
        entranceLabel.setOpacity(1);
    }
    @FXML private void hideEntranceLabel(){
        entranceLine1.setOpacity(0);
        entranceLine2.setOpacity(0);
        entranceLine3.setOpacity(0);
        entranceLabel.setOpacity(0);
    }
    @FXML private void hideSafeLabel(){
        safeLabel.setOpacity(0);
    }
    @FXML private void showSafeLabel(){
        safeLabel.setOpacity(1);
    }
}
