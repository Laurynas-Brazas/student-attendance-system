package com.studed_registration_system;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Main extends Application {

    static {
        // Critical JavaFX initialization for JAR packaging
        System.setProperty("javafx.embed.singleThread", "true");
        System.setProperty("javafx.preloader", "NONE");

        // Workaround for JavaFX 17+ in shaded JAR
        if (System.getProperty("javafx.verbose") == null) {
            System.setProperty("javafx.verbose", "true");
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        // Debug FXML loading
        URL fxmlUrl = getClass().getResource("optionWindow.fxml");
        if (fxmlUrl == null) {
            throw new IOException("Cannot find FXML file. Expected at: " +
                    getClass().getPackage().getName().replace('.', '/') + "/optionWindow.fxml");
        }

        // Load FXML with proper controller factory
        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Parent root = loader.load();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Student Registration System");
        stage.show();
    }

    public static void main(String[] args) {
        try {
            launch(args);
        } catch (Exception e) {
            System.err.println("Application failed to start:");
            e.printStackTrace();
            System.exit(1);
        }
    }
}