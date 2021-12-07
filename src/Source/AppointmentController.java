package Source;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.function.IntBinaryOperator;

public class AppointmentController 
{
    @FXML
    private ComboBox<String> AppEndTime;

    @FXML
    private TextField AppIDField;

    @FXML
    private TextField AppTitleField;

    @FXML
    private TextField AppLocationField;

    @FXML
    private TextField AppTypeField;

    @FXML
    private Button AddButton;

    @FXML
    private Label AppointmentLabel;

    @FXML
    private DatePicker AppStartDate;

    @FXML
    private ComboBox<String> AppStartTime;

    @FXML
    private TextArea AppDescriptionArea;

    @FXML
    private DatePicker AppEndDate;

    @FXML
    private ComboBox<String> AppContact;

    @FXML
    private TextField UserIDField;

    @FXML
    private ComboBox<String> AppCustomer;

    @FXML
    private Label BuisinessHours;

    @FXML
    private Label Overlap;

    @FXML
    private Label Backwards;

    /**
     * If updating it fills the fields with their information
     * @throws SQLException
     */
    public void initialize() throws SQLException {

        ObservableList<String> times = FXCollections.observableArrayList();
        LocalTime time = LocalTime.of(0, 0);

        for(int i = 0; i < 48; i++)
        {
            times.add(time.toString());
            time = time.plusMinutes(30);
        }
        AppStartTime.setItems(times);
        AppEndTime.setItems(times);

        if(MainAppController.getIsModifyingApp())
        {
            Appointment appointment = MainAppController.getSelectedApp();

            AppointmentLabel.setText("Update Appointment");
            AddButton.setText("Update");
            AppIDField.setText(String.valueOf(appointment.getAppointmentID()));
            UserIDField.setText(String.valueOf(MainAppController.getUserID()));
            AppTitleField.setText(appointment.getTitle());
            AppLocationField.setText(String.valueOf(appointment.getLocation()));
            AppTypeField.setText(appointment.getType());
            AppDescriptionArea.setText(appointment.getDescription());
            AppStartDate.setValue(appointment.getEnd().toLocalDate());
            AppEndDate.setValue(appointment.getStart().toLocalDate());
            AppStartTime.getSelectionModel().select(String.valueOf(appointment.getStart().toLocalTime()));
            AppEndTime.getSelectionModel().select(String.valueOf(appointment.getEnd().toLocalTime()));
            AppContact.getSelectionModel().select(String.valueOf(appointment.getContactID()));
            AppCustomer.getSelectionModel().select(String.valueOf(appointment.getCustID()));

        }
        else
        {
            AppointmentLabel.setText("Add Appointment");
            AppIDField.setText(String.valueOf(MainAppController.getAppID()));
            UserIDField.setText(String.valueOf(MainAppController.getUserID()));
        }

        try(Statement query = Main.getConn().createStatement())
        {
            ResultSet rs = query.executeQuery("SELECT Contact_ID FROM contacts");
            ObservableList<String> contacts = FXCollections.observableArrayList();

            while (rs.next())
            {
                contacts.add(String.valueOf(rs.getInt(1)));
            }
            AppContact.setItems(contacts);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        try(Statement query = Main.getConn().createStatement())
        {
            ResultSet rs = query.executeQuery("SELECT Customer_ID FROM customers");
            ObservableList<String> customers = FXCollections.observableArrayList();

            while (rs.next())
            {
                customers.add(String.valueOf(rs.getInt(1)));
            }
            AppCustomer.setItems(customers);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

    }

    /**
     * Lambda: here I used a Lambda expression to shorten my code so I didn't have to write the same subtraction over and over again.
     * checks for and displays several error messages and if it doesn't find any it adds the customer to the database
     */
    public void add()
    {
        LocalDateTime nowUTC = LocalDateTime.now(Clock.systemUTC());
        LocalDateTime nowLocal = LocalDateTime.now(Clock.systemDefaultZone());
        Boolean valid = true;

        int offSet = 0;
        IntBinaryOperator diff = (n1, n2) -> n1 - n2;

        if(nowUTC.isAfter(nowLocal) && diff.applyAsInt(nowUTC.getHour(), nowLocal.getHour()) > 0)//this if else structure determines the timezone offset from UTC
        {
            offSet = (diff.applyAsInt(nowUTC.getHour(), nowLocal.getHour()));
        }
        else if (nowUTC.isAfter(nowLocal) && diff.applyAsInt(nowUTC.getHour(), nowLocal.getHour()) < 0)
        {
            offSet = (24 + (diff.applyAsInt(nowUTC.getHour(), nowLocal.getHour())));
        }
        else if (nowLocal.isAfter(nowUTC) && diff.applyAsInt(nowUTC.getHour(), nowLocal.getHour()) > 0)
        {
            offSet = (diff.applyAsInt(nowUTC.getHour(), nowLocal.getHour()));
        }
        else if (nowLocal.isAfter(nowUTC) && diff.applyAsInt(nowUTC.getHour(), nowLocal.getHour()) < 0)
        {
            offSet = 0 - (24 - (diff.applyAsInt(nowUTC.getHour(), nowLocal.getHour())));
        }

        try(Statement query = Main.getConn().createStatement())
        {
            ResultSet rs = query.executeQuery("SELECT Customer_ID, Start, End FROM appointments");
            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            while(rs.next())
            {
                if(rs.getInt(1) == Integer.valueOf(AppCustomer.getSelectionModel().getSelectedItem()) &&
                   (LocalDateTime.of(AppEndDate.getValue(), LocalTime.parse(AppEndTime.getSelectionModel().getSelectedItem())).plusHours(offSet)
                   .isAfter(LocalDateTime.parse(rs.getString(2), format)) &&
                   (LocalDateTime.of(AppEndDate.getValue(), LocalTime.parse(AppEndTime.getSelectionModel().getSelectedItem())).plusHours(offSet)
                   .isBefore(LocalDateTime.parse(rs.getString(3), format)) || LocalDateTime.of(AppEndDate.getValue(), LocalTime.parse(AppEndTime.getSelectionModel().getSelectedItem())).plusHours(offSet)
                   .isEqual(LocalDateTime.parse(rs.getString(3), format)))
                   || (LocalDateTime.of(AppStartDate.getValue(), LocalTime.parse(AppStartTime.getSelectionModel().getSelectedItem()).plusHours(offSet))
                   .isBefore(LocalDateTime.parse(rs.getString(3), format)) &&
                   LocalDateTime.of(AppStartDate.getValue(), LocalTime.parse(AppStartTime.getSelectionModel().getSelectedItem())).plusHours(offSet)
                   .isAfter(LocalDateTime.parse(rs.getString(2), format)) || LocalDateTime.of(AppEndDate.getValue(), LocalTime.parse(AppEndTime.getSelectionModel().getSelectedItem())).plusHours(offSet)
                   .isEqual(LocalDateTime.parse(rs.getString(3), format)))))
                {
                    Overlap.setVisible(true);
                    valid = false;
                }
                else
                {
                    Overlap.setVisible(false);
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        if(ZonedDateTime.of(
                LocalDateTime.of(
                        AppStartDate.getValue(), LocalTime.parse(
                                AppStartTime.getSelectionModel().getSelectedItem())),
                ZoneId.systemDefault()).isBefore(ZonedDateTime.of(
                LocalDateTime.of(
                        AppStartDate.getValue(), LocalTime.of(8, 0)
                ), ZoneId.of("America/New_York"))) ||
                ZonedDateTime.of(
                        LocalDateTime.of(
                                AppEndDate.getValue(), LocalTime.parse(
                                        AppEndTime.getSelectionModel().getSelectedItem())),
                        ZoneId.systemDefault()).isAfter(ZonedDateTime.of(
                        LocalDateTime.of(
                                AppStartDate.getValue(), LocalTime.of(22, 0)
                        ), ZoneId.of("America/New_York"))) || LocalDateTime.of(
                AppStartDate.getValue(), LocalTime.parse(
                        AppStartTime.getSelectionModel().getSelectedItem())).isBefore(LocalDateTime.now()))
        {
            BuisinessHours.setVisible(true);
            valid = false;
        }
        else
        {
            BuisinessHours.setVisible(false);
        }
        if(LocalDateTime.of(AppStartDate.getValue(), LocalTime.parse(AppStartTime.getSelectionModel().getSelectedItem()))
           .isAfter(LocalDateTime.of(AppEndDate.getValue(), LocalTime.parse(AppEndTime.getSelectionModel().getSelectedItem()))))
        {
            Backwards.setVisible(true);
            valid = false;
        }
        else
        {
            Backwards.setVisible(false);
        }
        if(valid == false)
        {
            return;
        }
        if(MainAppController.getIsModifyingApp())
        {
            try(Statement update = Main.getConn().createStatement())
            {
                update.executeUpdate(
                        "UPDATE appointments SET" +
                        " Title = '" + AppTitleField.getText() +
                        "', Description = '" + AppDescriptionArea.getText() +
                        "', Location = '" + AppLocationField.getText() +
                        "', Type = '" + AppTypeField.getText() +
                        "', Start = '" + LocalDateTime.of(AppStartDate.getValue(), LocalTime.parse(AppStartTime.getSelectionModel().getSelectedItem())).plusHours(offSet) +
                        "', End = '" + LocalDateTime.of(AppEndDate.getValue(), LocalTime.parse(AppEndTime.getSelectionModel().getSelectedItem())).plusHours(offSet) +
                        "', Last_Update = '" + LocalDateTime.now(Clock.systemUTC()) +
                        "', Last_Updated_By = '" + "UserID: " + UserIDField.getText() +
                        "', Customer_ID = '" + Integer.valueOf(AppCustomer.getSelectionModel().getSelectedItem()) +
                        "', Contact_ID = '" + Integer.valueOf(AppContact.getSelectionModel().getSelectedItem()) +
                        "' WHERE Appointment_ID=" + Integer.valueOf(AppIDField.getText()));
                showMainApp();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            try(Statement update = Main.getConn().createStatement())
            {
                update.executeUpdate(
                        "INSERT INTO appointments (Appointment_ID, Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID)" +
                        " VALUES ('" + Integer.valueOf(AppIDField.getText()) + "','" + AppTitleField.getText() + "','" + AppDescriptionArea.getText() + "','" + AppLocationField.getText() + "','" +
                        AppTypeField.getText() + "','" + LocalDateTime.of(AppStartDate.getValue(), LocalTime.parse(AppStartTime.getSelectionModel().getSelectedItem())).plusHours(offSet) + "','" +
                        LocalDateTime.of(AppEndDate.getValue(), LocalTime.parse(AppEndTime.getSelectionModel().getSelectedItem())).plusHours(offSet) + "','" + LocalDateTime.now(Clock.systemUTC()) +
                        "','" + "UserID: " + UserIDField.getText() + "','" + LocalDateTime.now(Clock.systemUTC()) + "','" + "UserID: " + UserIDField.getText() + "','" +
                        Integer.valueOf(AppCustomer.getSelectionModel().getSelectedItem()) + "','" + UserIDField.getText() + "','" + AppContact.getSelectionModel().getSelectedItem() + "')");
                showMainApp();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * returns to main menu
     */
    public void cancel()
    {
        showMainApp();
    }

    /**
     * show the main menu
     */
    private void showMainApp()
    {
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("MainApp.fxml"));
            Stage partStage = (Stage)AppIDField.getScene().getWindow();
            Scene scene = new Scene(root);
            partStage.setScene(scene);
            partStage.show();
        }
        catch(Exception e)
        {
            System.out.println("Error, fxml file not found");
        }
    }
}
