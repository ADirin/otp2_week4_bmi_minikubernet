package org.example.demo1;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.NodeOrientation;
import javafx.scene.chart.ScatterChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import java.text.DecimalFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javafx.scene.layout.VBox;

public class BMIController {
    @FXML
private Label welcomeText;

    @FXML
    private Label lblWeight;

    @FXML
    private Label lblHeight;

    @FXML
    private TextField tfWeight;


    @FXML
    private TextField tfHeight;

    @FXML
    private Button btnCalculate;

    @FXML
    private Label lblResult;

    @FXML
    private Label lblLocalTime; // New label for showing local time


    @FXML
    private VBox rootVBox;

    @FXML
    private ResourceBundle rb;
    private Locale currentLocale = new Locale("en", "US");
    private Map<String, String> localizedStrings;
    public void initialize() {
        setLanguage(new Locale("en", "US"));

    }

    private void setLanguage(Locale locale) {
        /*lblResult.setText("");
        localizedStrings = LocalizationService.getLocalizedStrings(locale);

        lblWeight.setText(localizedStrings.getOrDefault("weight", "Weight"));
        lblHeight.setText(localizedStrings.getOrDefault("height", "Height"));
        btnCalculate.setText(localizedStrings.getOrDefault("calculate", "Calculate"));

       try {

          rb = ResourceBundle.getBundle("messages", locale);
          lblWeight.setText(rb.getString("weight"));
          lblHeight.setText(rb.getString("height"));
          btnCalculate.setText(rb.getString("calculate"));

         // show the time
          displayLocalTime(locale);
          applyTextDirection(locale);

      }catch(MissingResourceException e) {
          e.printStackTrace();
          lblResult.setText("Error loading resources error");
      }*/

            currentLocale = locale; // store the selected language

            lblResult.setText("");
            localizedStrings = LocalizationService.getLocalizedStrings(locale);

            lblWeight.setText(localizedStrings.getOrDefault("weight", "Weight"));
            lblHeight.setText(localizedStrings.getOrDefault("height", "Height"));
            btnCalculate.setText(localizedStrings.getOrDefault("calculate", "Calculate"));

            displayLocalTime(locale);

            // ✅ Apply RTL/LTR layout after setting language
            applyTextDirection(locale);
        }


    public void onCalculateClick(ActionEvent actionEvent) {
        /*if (tfWeight == null || tfHeight == null) {
            System.err.println("TextFields are empty");
            return;
        }

        try {
            // Parse weight and height values
            double weight = Double.parseDouble(tfWeight.getText());
            double height = Double.parseDouble(tfHeight.getText());

            // Convert height from centimeters to meters
            height = height / 100.0;  // Convert to meters

            System.out.println("Weight: " + weight + " kg, Height: " + height + " m");

            // Calculate BMI using the formula: BMI = weight / (height * height)
            double bmi = weight / (height * height);
            System.out.println("Raw BMI: " + bmi);

            // Format the BMI value to 2 decimal places
            DecimalFormat df = new DecimalFormat("#0.00");
            String bmiString = df.format(bmi);
            System.out.println("Formatted BMI: " + bmiString);

            // Directly concatenate the BMI value to the result message
            lblResult.setText(rb.getString("result") + " " + bmiString);

        } catch (NumberFormatException e) {
            // Handle invalid input
            lblResult.setText(rb.getString("invalid"));
        }*/

        try {
            // ✅ use the active locale
            double weight = Double.parseDouble(tfWeight.getText());
            double height = Double.parseDouble(tfHeight.getText()) / 100.0;
            double bmi = weight / (height * height);
            DecimalFormat df = new DecimalFormat("#0.00");
            lblResult.setText(localizedStrings.getOrDefault("result", "Your BMI is") + " " + df.format(bmi));

            // Save to database
            String language = currentLocale.getLanguage();  // or store current locale
            BMIResultService.saveResult(weight, height, bmi, language);

        } catch (NumberFormatException e) {
            lblResult.setText(localizedStrings.getOrDefault("invalid", "Invalid input"));
        }
    }


    public void onENClick(ActionEvent actionEvent) {
        setLanguage(new Locale("en", "US"));
    }

    public void onFRClick(ActionEvent actionEvent) {
        setLanguage(new Locale("fr", "FR"));

    }

    public void onURClick(ActionEvent actionEvent) {
        setLanguage(new Locale("ur", "PA"));

    }

    public void onVIClick(ActionEvent actionEvent) {
        setLanguage(new Locale("vi", "VI"));

    }

    // Display the time

    private void displayLocalTime(Locale locale) {
        LocalTime currentTime = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss", locale);
        String formattedTime = currentTime.format(formatter);
        lblLocalTime.setText(localizedStrings.getOrDefault("localTime", "Local Time") + " " + formattedTime);
    }



    private void applyTextDirection(Locale locale) {
        String lang = locale.getLanguage();
        boolean isRTL = lang.equals("fa") || lang.equals("ur") || lang.equals("ar") || lang.equals("he");

        Platform.runLater(() -> {
            if (rootVBox != null) {
                // Flip the full layout direction
                rootVBox.setNodeOrientation(
                        isRTL ? NodeOrientation.RIGHT_TO_LEFT : NodeOrientation.LEFT_TO_RIGHT
                );
            }

            // Align text fields
            tfWeight.setStyle(isRTL ? "-fx-text-alignment: right;" : "-fx-text-alignment: left;");
            tfHeight.setStyle(isRTL ? "-fx-text-alignment: right;" : "-fx-text-alignment: left;");
        });
    }


    public void onFAClick(ActionEvent actionEvent) {
        setLanguage(new Locale("fa", "IR"));
    }

}