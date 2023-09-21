package nz.ac.auckland.se206.controllers;


import javafx.util.Duration;


import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class SecurityController {
    @FXML private Line cameraLine1;
    @FXML private Line cameraLine2;
    @FXML private Ellipse cameraBase;
    @FXML private ImageView cameraTriangle;
    @FXML private Text interractHint;
    @FXML Label wireLabel;
    @FXML Label doorLabel;
    @FXML Rectangle correctColor;
    @FXML Rectangle incorrectColor;
    @FXML Text incorrectTxt;
    @FXML Text correctTxt;
    @FXML private Rectangle one;
    @FXML private Rectangle two;
    @FXML private Rectangle three;
    @FXML private Rectangle four;
    @FXML private Rectangle five;
    @FXML private Rectangle six;
    @FXML private Rectangle seven;
    @FXML private Rectangle eight;
    @FXML private Rectangle nine;
    @FXML private Rectangle zero1;
    @FXML private Rectangle clear;
    @FXML private Rectangle enter;
    @FXML private ImageView keypad;
    @FXML private Rectangle numberRectangle;
    @FXML private Text numbers;
    @FXML private Rectangle keypadRectangle;
    private int numberOfnumbers=0;
    @FXML private Text number1;
    @FXML private Text number2;
    @FXML private Text number3;
    @FXML private Text number4;
    @FXML private Text number5;
    @FXML private Text number6;
    @FXML private Text number7;
    @FXML private Text number8;
    @FXML private Text number9;
    @FXML private Line securityLine1;
    @FXML private Line securityLine2;
    @FXML private Line securityLine3;
    @FXML private Text securityText;

    // @FXML private Text number0;
    @FXML 
    private void handleClearEnter(MouseEvent event){
        Rectangle clickRectangle = (Rectangle) event.getSource();
        if(clickRectangle == clear){
            numbers.setText("");
            numberOfnumbers=0;
        }
        else if(clickRectangle==enter){
            checkPin();
        }
        
    }
    @FXML 
    private void showDoorLabel(){
        doorLabel.setOpacity(1);
        interractHint.setOpacity(1);
        
    }
    @FXML void hideDoorLabel(){
        doorLabel.setOpacity(0);
        interractHint.setOpacity(0);
    }
    @FXML
    private void showSecurity() {
        // Change the stroke width of all three security lines to 3


        // Change the fill of all three security lines to white
        securityLine1.setStroke(Color.WHITE);
        securityLine2.setStroke(Color.WHITE);
        securityLine3.setStroke(Color.WHITE);

        // Change the opacity of the security text to 1 (fully visible)
        securityText.setOpacity(1);
        interractHint.setOpacity(1);
    
        
    }

    @FXML
    private void hideSecurity() {
        // Reset the stroke width of all three security lines (assuming it was originally 1, adjust as necessary)
        securityLine1.setStrokeWidth(3);
        securityLine2.setStrokeWidth(3);
        securityLine3.setStrokeWidth(3);

        // Reset the fill of all three security lines (assuming it was originally null, adjust as necessary)
        securityLine1.setStroke(Color.TRANSPARENT);
        securityLine2.setStroke(Color.TRANSPARENT);
        securityLine3.setStroke(Color.TRANSPARENT);

        // Change the opacity of the security text to 0 (completely hidden)
        securityText.setOpacity(0);
        interractHint.setOpacity(0);
    }
    @FXML
    private void handleMouseClicked(MouseEvent event) {
        // Get the source rectangle that was clicked
        Rectangle clickedRectangle = (Rectangle) event.getSource();
        
        // Determine which rectangle was clicked and append the appropriate number
        if(numberOfnumbers<6){
        if (clickedRectangle == one) {
            enterValue(" 1");
        } else if (clickedRectangle == two) {
            enterValue(" 2");
        } else if (clickedRectangle == three) {
            enterValue(" 3");
        } else if (clickedRectangle == four) {
            enterValue(" 4");
        } else if (clickedRectangle == five) {
            enterValue(" 5");
        } else if (clickedRectangle == six) {
            enterValue(" 6");
        } else if (clickedRectangle == seven) {
            enterValue(" 7");
        } else if (clickedRectangle == eight) {
            enterValue(" 8");
        } else if (clickedRectangle == nine) {
            enterValue(" 9");
        }
        else if(clickedRectangle==zero1){
            enterValue(" 0");
        }
    }
}
    
    
    @FXML private void disableCamera(){
        cameraLine1.toBack();
        cameraLine2.toBack();
        cameraBase.toBack();
        cameraTriangle.toBack();
        //what you can do here is also remove obstacle preventing the player from moving into the camera area
     }

    private void enterValue(String value) {
        numbers.setText(numbers.getText() + value);
        numberOfnumbers++;
    }
    private void checkPin(){
        String pin = " 1 3 4 6 9 8";
        System.out.println(numbers.getText());
        if(pin.equals(numbers.getText())){
            correctColor.toFront();
            correctTxt.toFront();
            numbers.setOpacity(0);
    
            // Wait for 0.5 seconds then hide the keypad
            PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
            pause.setOnFinished(event -> hideKeyPad());
            pause.play();
    
        } else {
            incorrectColor.toFront();
            incorrectTxt.toFront();
            numbers.setOpacity(0);
            numbers.setText("");
    
            // Wait for 0.5 seconds then reset the numbers
            PauseTransition pause = new PauseTransition(Duration.seconds(1));
            pause.setOnFinished(event -> resetNumbers());
            pause.play();
        }
    }
    
    private void resetNumbers() {
        numbers.setText("");
        numberOfnumbers = 0;
        incorrectColor.toBack();
        incorrectTxt.toBack();
        numbers.setOpacity(1);
    }
    @FXML 
    private void showKeyPad() {
        keypadRectangle.toFront();
        
        keypad.toFront();
    
        numberRectangle.toFront();
        numbers.toFront();
        // number0.toFront();
        number1.toFront();
        number2.toFront();
        number3.toFront();
        number4.toFront();
        number5.toFront();
        number6.toFront();
        number7.toFront();
        number8.toFront();
        number9.toFront(); // Making sure the keypad image is in front of its background
        one.toFront();
        two.toFront();
        three.toFront();
        four.toFront();
        five.toFront();
        six.toFront();
        seven.toFront();
        eight.toFront();
        nine.toFront();
        zero1.toFront();
        clear.toFront();
        enter.toFront(); // This makes sure the numbers text is visible on the top
    }

    @FXML
    private void hideKeyPad() {
        keypadRectangle.toBack();
        numberRectangle.toBack();
        number1.toBack();
        number2.toBack();
        number3.toBack();
        number4.toBack();
        number5.toBack();
        number6.toBack();
        number7.toBack();
        number8.toBack();
        number9.toBack();
    
        keypad.toBack();
        one.toBack();
        two.toBack();
        three.toBack();
        four.toBack();
        five.toBack();
        six.toBack();
        seven.toBack();
        eight.toBack();
        nine.toBack();
        zero1.toBack();
        clear.toBack();
        enter.toBack();
        numbers.setOpacity(0);
        correctColor.setOpacity(0);
        incorrectColor.setOpacity(0);
        correctTxt.setOpacity(0);
        incorrectTxt.setOpacity(0);
    }
    @FXML private void showWireLabel(){
        wireLabel.setOpacity(1);
        interractHint.setOpacity(1);
    }
    @FXML private void hideWireLabel(){
        wireLabel.setOpacity(0);
        interractHint.setOpacity(0);
    }
}
