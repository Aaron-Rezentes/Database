package Source;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginController
{
    @FXML
    private AnchorPane LoginMain;

    @FXML
    private Label Title;

    @FXML
    private Label UserIDTitle;

    @FXML
    private TextField UserIDEntry;

    @FXML
    private Label PasswordTitle;

    @FXML
    private TextField PasswordEntry;

    @FXML
    private Button LogInButton;

    @FXML
    private Label LogInError;

    @FXML
    private Label ZoneLabel;

    /**
     * if your computers default language is set to french it displays the menu is french
     */
    public void initialize()
    {
        ZoneLabel.setText(ZoneId.systemDefault().toString());
        ZoneLabel.setVisible(true);
        if(Locale.getDefault().getLanguage().equals("fr"))
        {
            Locale locale = new Locale("fr");
            ResourceBundle logInBundle = ResourceBundle.getBundle("Source.LoginBundle", locale);

            Title.setText(logInBundle.getString("Title"));
            UserIDTitle.setText(logInBundle.getString("UserID"));
            PasswordTitle.setText(logInBundle.getString("Password"));
            LogInButton.setText(logInBundle.getString("LoginButton"));
            LogInError.setText(logInBundle.getString("LoginError"));
        }
    }

    /**
     * Validates the user ID and password, if they are incorrect an error is displayed and it is logged as so and if it succeeds it is logged as a success
     * @throws Exception
     */
    public void LogInButton() throws Exception
    {
        try(Statement query = Main.getConn().createStatement(); FileWriter write = new FileWriter("login_activity.txt", true))
        {
            ResultSet rs = query.executeQuery("SELECT * FROM users WHERE User_Name='" + UserIDEntry.getText() + "' AND Password='" + PasswordEntry.getText() + "'");

            write.write("User ID: " + UserIDEntry.getText() + ", Password: " + PasswordEntry.getText() + ", at: UTC " + LocalDateTime.now(Clock.systemUTC()).withNano(0));
            if(rs.next())
            {
                write.write(", Login successful\n");
                MainAppController.setUserID(rs.getInt(1));

                Parent root = FXMLLoader.load(getClass().getResource("MainApp.fxml"));
                Stage productStage = (Stage)LogInButton.getScene().getWindow();
                Scene scene = new Scene(root);
                productStage.setScene(scene);
                productStage.show();

                try(Statement qry = Main.getConn().createStatement()) {
                    ResultSet rSet = qry.executeQuery("SELECT Appointment_ID, Start FROM appointments");
                    boolean upcoming = false;

                    while (rSet.next()) {
                        if (LocalDateTime.now(Clock.systemUTC()).isBefore(rSet.getObject(2, LocalDateTime.class)) && LocalDateTime.now(Clock.systemUTC()).isAfter(rSet.getObject(2, LocalDateTime.class).minusMinutes(16))) {
                            upcoming = true;
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You have an upcoming appointment with in the next 15 minutes app ID: " + rSet.getInt(1) + ", app start date and time: UTC " + rSet.getObject(2, LocalDateTime.class), ButtonType.OK);
                            alert.showAndWait();
                        }
                    }
                    if (upcoming == false) {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You have no upcoming appointments in the next 15 minutes", ButtonType.OK);
                        alert.showAndWait();
                    }
                }
            }
            else
            {
                write.write(", Login failed\n");
                LogInError.setVisible(true);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}
