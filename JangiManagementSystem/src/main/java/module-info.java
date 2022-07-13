module jangi.jangi {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;


    opens jangi.jangi to javafx.fxml;
    exports jangi.jangi;
}