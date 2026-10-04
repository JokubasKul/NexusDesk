package nexusDesk.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

public class ConvertersController {

    private StackPane mainContent;
    public void setMainContent(StackPane mainContent) {
        this.mainContent = mainContent;
    }

    @FXML
    private ComboBox<String> converterType;
    @FXML
    private ComboBox<String> fromUnit;
    @FXML
    private ComboBox<String> toUnit;

    @FXML
    private TextField inputValue;
    @FXML
    private Label result;

    @FXML
    public void initialize() {

        converterType.getItems().addAll(
                "Length",
                "Weight",
                "Temperature",
                "Time",
                "Data Size",
                "Speed"
        );

        converterType.setOnAction(event -> {
            result.setText("0");
            inputValue.clear();

            loadUnits();
        });

        fromUnit.setOnAction(event -> convert());

        toUnit.setOnAction(event -> convert());

        inputValue.textProperty().addListener(
                (observable, oldValue, newValue) -> convert()
        );
    }

    private void loadUnits() {

        fromUnit.getItems().clear();
        toUnit.getItems().clear();

        String converter = converterType.getValue();

        if (converter == null) {
            return;
        }

        switch (converter) {

            case "Length" -> loadLengthUnits();

            case "Weight" -> loadWeightUnits();

            case "Temperature" -> loadTemperatureUnits();

            case "Time" -> loadTimeUnits();

            case "Data Size" -> loadDataSizeUnits();

            case "Speed" -> loadSpeedUnits();
        }

        if (!fromUnit.getItems().isEmpty()) {
            fromUnit.getSelectionModel().selectFirst();
        }

        if (toUnit.getItems().size() > 1) {
            toUnit.getSelectionModel().select(1);
        }
    }

    private void loadLengthUnits() {

        fromUnit.getItems().addAll(
                "Millimeters",
                "Centimeters",
                "Meters",
                "Kilometers",
                "Inches",
                "Feet",
                "Yards",
                "Miles"
        );

        toUnit.getItems().addAll(
                fromUnit.getItems()
        );
    }

    private void loadWeightUnits() {

        fromUnit.getItems().addAll(
                "Milligrams",
                "Grams",
                "Kilograms",
                "Ounces",
                "Pounds"
        );

        toUnit.getItems().addAll(
                fromUnit.getItems()
        );
    }

    private void loadTemperatureUnits() {

        fromUnit.getItems().addAll(
                "Celsius",
                "Fahrenheit",
                "Kelvin"
        );

        toUnit.getItems().addAll(
                fromUnit.getItems()
        );
    }

    private void loadTimeUnits() {

        fromUnit.getItems().addAll(
                "Seconds",
                "Minutes",
                "Hours",
                "Days"
        );

        toUnit.getItems().addAll(
                fromUnit.getItems()
        );
    }

    private void loadDataSizeUnits() {

        fromUnit.getItems().addAll(
                "Bytes",
                "Kilobytes",
                "Megabytes",
                "Gigabytes",
                "Terabytes"
        );

        toUnit.getItems().addAll(
                fromUnit.getItems()
        );
    }

    private void loadSpeedUnits() {

        fromUnit.getItems().addAll(
                "Meters per second",
                "Kilometers per hour",
                "Miles per hour"
        );

        toUnit.getItems().addAll(
                fromUnit.getItems()
        );
    }


    private void convert() {

        String input = inputValue.getText().trim();

        if (input.isEmpty()) {
            result.setText("Enter a value");
            return;
        }

        if (converterType.getValue() == null ||
                fromUnit.getValue() == null ||
                toUnit.getValue() == null) {

            result.setText("Select units");
            return;
        }

        try {

            double value = Double.parseDouble(input);

            String converter = converterType.getValue();
            String from = fromUnit.getValue();
            String to = toUnit.getValue();

            double convertedValue = 0;

            if (converter.equals("Length")) {

                convertedValue = convertLength(value, from, to);

            } else if (converter.equals("Weight")) {

                convertedValue = convertWeight(value, from, to);

            } else if (converter.equals("Temperature")) {

                convertedValue = convertTemperature(value, from, to);

            } else if (converter.equals("Time")) {

                convertedValue = convertTime(value, from, to);

            } else if (converter.equals("Data Size")) {

                convertedValue = convertDataSize(value, from, to);

            } else if (converter.equals("Speed")) {

                convertedValue = convertSpeed(value, from, to);
            }

            result.setText(formatResult(convertedValue));

        } catch (NumberFormatException e) {

            result.setText("Invalid number");
        }
    }

    private String formatResult(double value) {

        if (value == (long) value) {
            return String.valueOf((long) value);
        }

        return String.format("%.6f", value)
                .replaceAll("0+$", "")
                .replaceAll("\\.$", "");
    }

    private double convertLength(double value, String from, String to) {

        double meters;

        switch (from) {

            case "Millimeters" -> meters = value / 1000;

            case "Centimeters" -> meters = value / 100;

            case "Meters" -> meters = value;

            case "Kilometers" -> meters = value * 1000;

            case "Inches" -> meters = value * 0.0254;

            case "Feet" -> meters = value * 0.3048;

            case "Yards" -> meters = value * 0.9144;

            case "Miles" -> meters = value * 1609.344;

            default -> meters = value;
        }

        // Then convert meters to the target unit

        return switch (to) {

            case "Millimeters" -> meters * 1000;

            case "Centimeters" -> meters * 100;

            case "Meters" -> meters;

            case "Kilometers" -> meters / 1000;

            case "Inches" -> meters / 0.0254;

            case "Feet" -> meters / 0.3048;

            case "Yards" -> meters / 0.9144;

            case "Miles" -> meters / 1609.344;

            default -> meters;
        };
    }

    private double convertWeight(double value, String from, String to) {

        double kilograms;

        switch (from) {

            case "Milligrams" -> kilograms = value / 1_000_000;

            case "Grams" -> kilograms = value / 1000;

            case "Kilograms" -> kilograms = value;

            case "Ounces" -> kilograms = value * 0.0283495;

            case "Pounds" -> kilograms = value * 0.453592;

            default -> kilograms = value;
        }

        return switch (to) {

            case "Milligrams" -> kilograms * 1_000_000;

            case "Grams" -> kilograms * 1000;

            case "Kilograms" -> kilograms;

            case "Ounces" -> kilograms / 0.0283495;

            case "Pounds" -> kilograms / 0.453592;

            default -> kilograms;
        };
    }

    private double convertTemperature(double value, String from, String to) {

        double celsius;

        switch (from) {

            case "Celsius" -> celsius = value;

            case "Fahrenheit" -> celsius = (value - 32) * 5 / 9;

            case "Kelvin" -> celsius = value - 273.15;

            default -> celsius = value;
        }

        return switch (to) {

            case "Celsius" -> celsius;

            case "Fahrenheit" -> (celsius * 9 / 5) + 32;

            case "Kelvin" -> celsius + 273.15;

            default -> celsius;
        };
    }

    private double convertTime(double value, String from, String to) {

        double seconds;

        switch (from) {

            case "Seconds" -> seconds = value;

            case "Minutes" -> seconds = value * 60;

            case "Hours" -> seconds = value * 60 * 60;

            case "Days" -> seconds = value * 24 * 60 * 60;

            default -> seconds = value;
        }

        return switch (to) {

            case "Seconds" -> seconds;

            case "Minutes" -> seconds / 60;

            case "Hours" -> seconds / 3600;

            case "Days" -> seconds / 86400;

            default -> seconds;
        };
    }

    private double convertDataSize(double value, String from, String to) {

        // 1 KB = 1024 bytes
        // 1 MB = 1024 KB
        // 1 GB = 1024 MB
        // 1 TB = 1024 GB

        double bytes;

        switch (from) {

            case "Bytes" -> bytes = value;

            case "Kilobytes" -> bytes = value * 1024;

            case "Megabytes" -> bytes = value * 1024 * 1024;

            case "Gigabytes" -> bytes = value * 1024 * 1024 * 1024;

            case "Terabytes" -> bytes = value * 1024 * 1024 * 1024 * 1024;

            default -> bytes = value;
        }

        return switch (to) {

            case "Bytes" -> bytes;

            case "Kilobytes" -> bytes / 1024;

            case "Megabytes" -> bytes / (1024 * 1024);

            case "Gigabytes" -> bytes / (1024 * 1024 * 1024);

            case "Terabytes" -> bytes / (1024 * 1024 * 1024 * 1024);

            default -> bytes;
        };
    }

    private double convertSpeed(double value, String from, String to) {

        double metersPerSecond;

        switch (from) {

            case "Meters per second" -> metersPerSecond = value;

            case "Kilometers per hour" -> metersPerSecond = value / 3.6;

            case "Miles per hour" -> metersPerSecond = value * 0.44704;

            default -> metersPerSecond = value;
        }

        return switch (to) {

            case "Meters per second" -> metersPerSecond;

            case "Kilometers per hour" -> metersPerSecond * 3.6;

            case "Miles per hour" -> metersPerSecond / 0.44704;

            default -> metersPerSecond;
        };
    }
}
