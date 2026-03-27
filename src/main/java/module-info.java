module com.system.studed_registration_system {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.github.librepdf.openpdf;
    requires org.apache.poi.ooxml;


    opens com.studed_registration_system to javafx.fxml;
    exports com.studed_registration_system;
}