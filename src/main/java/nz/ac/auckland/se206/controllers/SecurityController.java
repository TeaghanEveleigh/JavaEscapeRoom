package nz.ac.auckland.se206.controllers;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class SecurityController {
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
    @FXML private Text numbers;
    private int numberOfnumbers=0;
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
    }
    @FXML void hideDoorLabel(){
        doorLabel.setOpacity(0);
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
            
        }else{
            incorrectColor.toFront();
            incorrectTxt.toFront();
            numbers.setOpacity(0);
            numbers.setText("");
        }
        
        
    }
}
