module jangi.user.jangiuser {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;


    opens jangi.user.jangiuser to javafx.fxml;
    exports jangi.user.jangiuser;
}