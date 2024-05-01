package menuitems;

import COVIDbooking.Booking;
import COVIDbooking.BookingFacade;
import COVIDtests.*;
import users.User;
import users.UserFacade;
import views.View;
import java.util.ArrayList;
import java.util.Objects;

/**
 * MenuItem involving a healthcare worker entering details of a user following an interview with them.
 * Recommends a type of test, and creates that for the user's booking.
 */
public class EnterInterviewDetailsMenuItem implements MenuItem {

    /**
     * List of questions to be asked to the user.
     */
    private ArrayList<InterviewQuestion> questions;

    /**
     * This is the percentage of questions that need to be answered true for a PCR test to be recommended.
     */
    private static final double questionThreshold = 0.5;

    private View view;

    /**
     * Constructor.
     */
    public EnterInterviewDetailsMenuItem(View view) {
        this.view = view;

        this.questions = new ArrayList<>();
        this.questions.add(new InterviewQuestion("Does the patient have a cough?"));
        this.questions.add(new InterviewQuestion("Does the patient have a runny nose?"));
        this.questions.add(new InterviewQuestion("Does the patient have a fever?"));
        this.questions.add(new InterviewQuestion("Is the patient having any trouble breathing?"));
        this.questions.add(new InterviewQuestion("Has the patient been in contact with any positive cases in the last 7 days?"));
        this.questions.add(new InterviewQuestion("Has the patient returned from overseas in the last 14 days?"));
    }

    /**
     * Gets the description of the menu item, to be printed out to a menu.
     *
     * @return The description of the MenuItem
     */
    @Override
    public String getMenuDescription() {
        return "Enter interview details";
    }

    /**
     * Executes the MenuItem, passing control flow to it, until it finishes execution.
     *
     * @throws Exception If an error occurs while executing the MenuItem.
     */
    @Override
    public void execute() throws Exception {
        // get information on the patient's booking
        Booking chosenBooking = retrieveChosenBooking();

        // if chosenBooking is null, it means we have chosen to exit early
        if (chosenBooking == null) {
            return;
        }

        // retrieve information about test recommendation and current user
        String recommendation = retrieveTestRecommendation();

        User currentUser = UserFacade.getInstance().getCurrentUser();

        // create a test object
        TestFacade testFacade = TestFacade.getInstance();
        testFacade.createTest(
                recommendation,
                chosenBooking.getCustomerId(),
                chosenBooking.getId(),
                currentUser.getId(),
                ""
        );

        view.displayString("Test created successfully.");
    }

    /**
     * Retrieves a specified booking from the API.
     *
     * @return The booking with the pin specified by the user. Returns null if that booking does not exist
     * or if the user enters an invalid pin and chooses not to try again.
     */
    private Booking retrieveChosenBooking() {
        Booking chosenBooking = null;

        while(chosenBooking == null) {
            try {
                String pin = view.promptStringInput("PIN");
                chosenBooking = BookingFacade.getInstance().getBookingByPin(pin);

            } catch (Exception e) {
                view.displayString(e.getMessage());
                if (!view.promptTryAgain()) {
                    return null;
                }
            }
        }
        return chosenBooking;
    }

    /**
     * Runs the user though a series of interview questions, and then based on the result, recommends a given
     * test type (RAT or PCR) based on the percentage of questions answered with 'true'.
     *
     * @return the recommended test type ("RAT" or "PCR")
     */
    private String retrieveTestRecommendation() {
        float yesCount = 0;

        // loop through the list of questions, print them each to the user, and get a y/n answer
        for (InterviewQuestion question : this.questions) {
            view.displayString(question.questionText + " [y/n] ");
            String choice = view.readAnswer();

            while (!Objects.equals(choice, "y") && !Objects.equals(choice, "n")) {
                view.displayString("Please answer with either 'y' or 'n': ");
                choice = view.readAnswer();
            }

            if (Objects.equals(choice, "y")) {
                yesCount += 1;
            }
        }

        // if the percentage of questions is greater than questionThreshold, recommend a PCR test, otherwise RAT
        if ((yesCount / this.questions.size()) >= questionThreshold) {
            view.displayString("\nRecommended test type: PCR");
            return "PCR";
        } else {
            view.displayString("\nRecommended test type: RAT");
            return "RAT";
        }
    }

    /**
     * Class representing an interview question.
     */
    private static class InterviewQuestion {

        /**
         * Text content of the question.
         */
        private String questionText;

        /**
         * True or false answer of the question.
         */
        private boolean questionAnswer;

        /**
         * Constructor.
         *
         * @param questionText text content of the question.
         */
        public InterviewQuestion(String questionText) {
            this.questionText = questionText;
        }

        /**
         * Gets the question text.
         *
         * @return the question's text.
         */
        public String getQuestionText() {
            return questionText;
        }

        /**
         * Sets the question's answer.
         *
         * @param questionAnswer true or false based on the user response.
         */
        public void setQuestionAnswer(boolean questionAnswer) {
            this.questionAnswer = questionAnswer;
        }

        /**
         * Gets the question's answer.
         *
         * @return the question's true or false answer.
         */
        public boolean getQuestionAnswer() {
            return this.questionAnswer;
        }

    }
}
