package views;

import COVIDbooking.Booking;
import COVIDtestingsites.FacilityTestingSite;
import utils.Display;
import java.util.ArrayList;

public class CommandLineView implements View {
    private Display display;

    public CommandLineView(){
        this.display = new Display();
    }

    @Override
    public Integer createSubMenu(String[] menuItems) {
        display.printLine("\n" + menuItems[0] + ":");
        for (Integer i = 1; i < menuItems.length; i++){
            display.printLine(i + ". " + menuItems[i]);
        }
        display.print("Selection: ");
        return recordInputSubMenu(menuItems);
    }

    private Integer recordInputSubMenu(String[] menuItems){
        Integer userInput = display.readInteger();

        // input handling
        if (userInput <1 || userInput > (menuItems.length - 1)){
            displayError();
            return createSubMenu(menuItems);
        }
    return userInput;
    }

    @Override
    public String promptStringInput(String value){
        display.print("Enter " + value + ": ");
        return recordStringInput(value);
    }

    private String recordStringInput(String value){
        String userInput = display.readLine();

        // input handling
        if (userInput.length() <= 0){
            displayError();
            return promptStringInput(value);
        }
        return userInput;
    }

    @Override
    public Integer promptIntegerInput(String value){
        display.printLine(value);
        return recordIntegerInput();
    }

    private Integer recordIntegerInput(){
        return display.readInteger();
    }

    @Override
    public void displayError(){
        display.printLine("Invalid input. Try again.");
    }

    @Override
    public void displayString(String message){
        display.printLine(message);
    }

    @Override
    public Boolean promptTryAgain(){
        return display.askTryAgain();
    }

    @Override
    public void displayBookingDetails(Booking booking) {
        display.print("----Booking Details----\nID: " + booking.getId() + "\nSTATUS: " + booking.getAdditionalInfoField("status") + "\nSTART TIME: " + booking.getStartTime() + "\n\n");
    }

    @Override
    public void displayTestingSiteDetails(FacilityTestingSite testingSite) {
        String output = "ID: " + testingSite.getId() + "\n";
        output += "Name: " + testingSite.getName() + "\n";
        output += "Description: " + testingSite.getDescription() + "\n";
        output += "websiteURL: " + testingSite.getWebsiteUrl() + "\n";
        output += "Phone Number: " + testingSite.getPhoneNumber() + "\n";
        output += "Address: " + testingSite.getAddress().toJson().toString() + "\n";
        output += "Additional Info: " + testingSite.getAdditionalInfo().toString() + "\n";
        displayString("---Testing Site---\n" + output);
    }

    @Override
    public void displayListOfTestingSiteDetails(ArrayList<FacilityTestingSite> testingSites) {
        for (FacilityTestingSite testingSite : testingSites) {
            this.displayTestingSiteDetails(testingSite);
        }
    }


    @Override
    public String readAnswer() {
        return display.readLine();
    }

    @Override
    public void waitForEnter(String message) {
        display.print(message);
        display.readLine();
    }
}
