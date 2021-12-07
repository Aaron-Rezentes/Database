package Source;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.function.IntBinaryOperator;

public class MainAppController
{

    @FXML
    private TableView<Customer> CustomerTable;

    @FXML
    private TableColumn<Customer, Integer> CustID;

    @FXML
    private TableColumn<Customer, String> CustName;

    @FXML
    private TableColumn<Customer, Integer> CustPostal;

    @FXML
    private TableColumn<Customer, Integer> CustPhone;

    @FXML
    private TableColumn<Customer, String> CustDivision;

    @FXML
    private Button AddCustomer;

    @FXML
    private TableView<Appointment> AppointmentTable;

    @FXML
    private TableColumn<Appointment, Integer> AppID;

    @FXML
    private TableColumn<Appointment, String> AppTitle;

    @FXML
    private TableColumn<Appointment, LocalDateTime> AppStart;

    @FXML
    private TableColumn<Appointment, LocalDateTime> AppEnd;

    @FXML
    private TableColumn<Appointment, Integer> AppCustID;

    @FXML
    private TableColumn<Appointment, Integer> AppContactID;

    @FXML
    private TableColumn<Appointment, String> AppDescription;

    @FXML
    private TableColumn<Appointment, String> AppLocation;

    @FXML
    private TableColumn<Appointment, String> AppType;

    @FXML
    private TableView<Appointment> AppointmentTable1;

    @FXML
    private TableColumn<Appointment, Integer> AppID1;

    @FXML
    private TableColumn<Appointment, String> AppTitle1;

    @FXML
    private TableColumn<Appointment, LocalDateTime> AppStart1;

    @FXML
    private TableColumn<Appointment, LocalDateTime> AppEnd1;

    @FXML
    private TableColumn<Appointment, Integer> AppCustID1;

    @FXML
    private TableColumn<Appointment, Integer> AppContactID1;

    @FXML
    private TableColumn<Appointment, String> AppDescription1;

    @FXML
    private TableColumn<Appointment, String> AppLocation1;

    @FXML
    private TableColumn<Appointment, String> AppType1;

    @FXML
    private TableView<Appointment> AppointmentTable11;

    @FXML
    private TableColumn<Appointment, Integer> AppID11;

    @FXML
    private TableColumn<Appointment, String> AppTitle11;

    @FXML
    private TableColumn<Appointment, LocalDateTime> AppStart11;

    @FXML
    private TableColumn<Appointment, LocalDateTime> AppEnd11;

    @FXML
    private TableColumn<Appointment, Integer> AppCustID11;

    @FXML
    private TableColumn<Appointment, Integer> AppContactID11;

    @FXML
    private TableColumn<Appointment, String> AppDescription11;

    @FXML
    private TableColumn<Appointment, String> AppLocation11;

    @FXML
    private TableColumn<Appointment, String> AppType11;

    @FXML
    private TableView<Appointment> AppointmentTable12;

    @FXML
    private TableColumn<Appointment, Integer> AppID12;

    @FXML
    private TableColumn<Appointment, String> AppTitle12;

    @FXML
    private TableColumn<Appointment, LocalDateTime> AppStart12;

    @FXML
    private TableColumn<Appointment, LocalDateTime> AppEnd12;

    @FXML
    private TableColumn<Appointment, Integer> AppCustID12;

    @FXML
    private TableColumn<Appointment, Integer> AppContactID12;

    @FXML
    private TableColumn<Appointment, String> AppDescription12;

    @FXML
    private TableColumn<Appointment, String> AppLocation12;

    @FXML
    private TableColumn<Appointment, String> AppType12;

    @FXML
    private Button AddAppointment;

    @FXML
    private ToggleGroup Filter;

    @FXML
    private RadioButton RadioNone;

    @FXML
    private RadioButton RadioWeek;

    @FXML
    private RadioButton RadioMonth;

    @FXML
    private RadioButton AppTypes;

    @FXML
    private ToggleGroup Reports;

    @FXML
    private RadioButton ContactSchedule;

    @FXML
    private RadioButton ExpiredApps;

    private static boolean isModifyingCust;

    private static Customer selectedCust;

    private static boolean isModifyingApp;

    private static Appointment selectedApp;

    private static int custID = 1;

    private static int appID = 1;

    private static int userID = 0;

    /**
     * Loads and fills the Customer and Appointment table views
     * @throws SQLException
     */
    public void initialize() throws SQLException
    {
        try(Statement query = Main.getConn().createStatement()) //Loads the Customer table with the customer records from the database
        {
            ResultSet rs = query.executeQuery("SELECT * FROM customers");
            ObservableList<Customer> custArray = FXCollections.observableArrayList();

            while (rs.next())
            {
                Customer cust = new Customer(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(10));
                custArray.add(cust);
                if(rs.getInt(1) >= custID)
                {
                    custID= rs.getInt(1) + 1;
                }
            }
            CustID.setCellValueFactory(new PropertyValueFactory<>("ID"));
            CustName.setCellValueFactory(new PropertyValueFactory<>("name"));
            CustPostal.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
            CustPhone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
            CustDivision.setCellValueFactory(new PropertyValueFactory<>("firstDivisionID"));
            CustomerTable.setItems(custArray);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        try(Statement query = Main.getConn().createStatement()) //Loads the Appointment table with the appointment records from the database
        {
            ResultSet rs = query.executeQuery("SELECT * FROM appointments");
            ObservableList<Appointment> appointmentArray = FXCollections.observableArrayList();

            while (rs.next())
            {
                Appointment appointment = new Appointment(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getObject(6, LocalDateTime.class),
                rs.getObject(7, LocalDateTime.class), rs.getInt(12), rs.getInt(13), rs.getInt(14));
                appointmentArray.add(appointment);
                if(rs.getInt(1) >= appID)
                {
                    appID= rs.getInt(1) + 1;
                }
            }
            AppID.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
            AppTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
            AppStart.setCellValueFactory(new PropertyValueFactory<>("start"));
            AppEnd.setCellValueFactory(new PropertyValueFactory<>("end"));
            AppCustID.setCellValueFactory(new PropertyValueFactory<>("custID"));
            AppContactID.setCellValueFactory(new PropertyValueFactory<>("contactID"));
            AppDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
            AppLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
            AppType.setCellValueFactory(new PropertyValueFactory<>("type"));
            AppointmentTable.setItems(appointmentArray);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Loads the add customer frame
     * @throws IOException
     */
    public void addCust() throws IOException
    {
        isModifyingCust = false;
        Parent root = FXMLLoader.load(getClass().getResource("Customer.fxml"));
        Stage productStage = (Stage)AddCustomer.getScene().getWindow();
        Scene scene = new Scene(root);
        productStage.setScene(scene);
        productStage.show();
    }

    /**
     * Loads the update customer frame
     * @throws IOException
     */
    public void updateCust() throws IOException
    {
        isModifyingCust = true;
        selectedCust = CustomerTable.getSelectionModel().getSelectedItem();
        if (selectedCust == null)
        {
            return;
        }
        Parent root = FXMLLoader.load(getClass().getResource("Customer.fxml"));
        Stage productStage = (Stage)AddCustomer.getScene().getWindow();
        Scene scene = new Scene(root);
        productStage.setScene(scene);
        productStage.show();
    }

    /**
     * If there are no associated appointments it deletes the customer otherwise it displays an error message
     */
    public void deleteCust()
    {
        Customer selection = CustomerTable.getSelectionModel().getSelectedItem();

        if (selection == null)
        {
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete the record for " + selection.getName() + "?", ButtonType.YES, ButtonType.CANCEL);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES)
        {
            try(Statement update = Main.getConn().createStatement())
            {
                update.executeUpdate("DELETE FROM appointments WHERE Customer_ID=" + selection.getID());

            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
            try(Statement update = Main.getConn().createStatement())
            {
                update.executeUpdate("DELETE FROM customers WHERE Customer_ID=" + selection.getID());
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }

            try(Statement query = Main.getConn().createStatement()) //Reloads the Customer table with the customer records from the database
            {
                ResultSet rs = query.executeQuery("SELECT * FROM customers");
                ObservableList<Customer> custArray = FXCollections.observableArrayList();

                while (rs.next())
                {
                    Customer cust = new Customer(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(10));
                    custArray.add(cust);
                }
                CustID.setCellValueFactory(new PropertyValueFactory<>("ID"));
                CustName.setCellValueFactory(new PropertyValueFactory<>("name"));
                CustPostal.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
                CustPhone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
                CustDivision.setCellValueFactory(new PropertyValueFactory<>("firstDivisionID"));
                CustomerTable.setItems(custArray);
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
            try(Statement query = Main.getConn().createStatement()) //Loads the Appointment table with the appointment records from the database
            {
                ResultSet rs = query.executeQuery("SELECT * FROM appointments");
                ObservableList<Appointment> appointmentArray = FXCollections.observableArrayList();

                while (rs.next())
                {
                    Appointment appointment = new Appointment(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getObject(6, LocalDateTime.class),
                            rs.getObject(7, LocalDateTime.class), rs.getInt(12), rs.getInt(13), rs.getInt(14));
                    appointmentArray.add(appointment);
                }

                AppID.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
                AppTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
                AppStart.setCellValueFactory(new PropertyValueFactory<>("start"));
                AppEnd.setCellValueFactory(new PropertyValueFactory<>("end"));
                AppCustID.setCellValueFactory(new PropertyValueFactory<>("custID"));
                AppContactID.setCellValueFactory(new PropertyValueFactory<>("contactID"));
                AppDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
                AppLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
                AppType.setCellValueFactory(new PropertyValueFactory<>("type"));
                AppointmentTable.setItems(appointmentArray);
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * Loads the add appointment frame
     * @throws IOException
     */
    public void addApp() throws IOException
    {
        isModifyingApp = false;
        Parent root = FXMLLoader.load(getClass().getResource("Appointment.fxml"));
        Stage productStage = (Stage)AddAppointment.getScene().getWindow();
        Scene scene = new Scene(root);
        productStage.setScene(scene);
        productStage.show();
    }

    /**
     * Loads the update appointment frame
     * @throws IOException
     */
    public void updateApp() throws IOException
    {
        isModifyingApp = true;
        selectedApp = AppointmentTable.getSelectionModel().getSelectedItem();
        if (selectedApp == null)
        {
            return;
        }
        Parent root = FXMLLoader.load(getClass().getResource("Appointment.fxml"));
        Stage productStage = (Stage)AddAppointment.getScene().getWindow();
        Scene scene = new Scene(root);
        productStage.setScene(scene);
        productStage.show();
    }

    /**
     * Deletes the selected appointment
     */
    public void deleteApp()
    {
        Appointment selection = AppointmentTable.getSelectionModel().getSelectedItem();

        if (selection == null)
        {
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete the Appointment of type " + selection.getType() + " and ID " + selection.getAppointmentID() + "?", ButtonType.YES, ButtonType.CANCEL);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES)
        {
            try(Statement update = Main.getConn().createStatement())
            {
                update.executeUpdate("DELETE FROM appointments WHERE Appointment_ID=" + selection.getAppointmentID());
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }

            try(Statement query = Main.getConn().createStatement()) //Loads the Appointment table with the appointment records from the database
            {
                ResultSet rs = query.executeQuery("SELECT * FROM appointments");
                ObservableList<Appointment> appointmentArray = FXCollections.observableArrayList();

                while (rs.next())
                {
                    Appointment appointment = new Appointment(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getObject(6, LocalDateTime.class),
                            rs.getObject(7, LocalDateTime.class), rs.getInt(12), rs.getInt(13), rs.getInt(14));
                    appointmentArray.add(appointment);
                }

                AppID.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
                AppTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
                AppStart.setCellValueFactory(new PropertyValueFactory<>("start"));
                AppEnd.setCellValueFactory(new PropertyValueFactory<>("end"));
                AppCustID.setCellValueFactory(new PropertyValueFactory<>("custID"));
                AppContactID.setCellValueFactory(new PropertyValueFactory<>("contactID"));
                AppDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
                AppLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
                AppType.setCellValueFactory(new PropertyValueFactory<>("type"));
                AppointmentTable.setItems(appointmentArray);
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * Lambda: here I used a Lambda expression to shorten my code so I didn't have to write the same subtraction over and over again.
     * checks for and displays several error messages and if it doesn't find any it adds the customer to the database
     * Filters the appointments by week or month
     */
    public void filter()
    {
        if (RadioNone.isSelected())
        {
            try(Statement query = Main.getConn().createStatement())
            {
                ResultSet rs = query.executeQuery("SELECT * FROM appointments");
                ObservableList<Appointment> appointmentArray = FXCollections.observableArrayList();

                while (rs.next())
                {
                    Appointment appointment = new Appointment(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getObject(6, LocalDateTime.class),
                            rs.getObject(7, LocalDateTime.class), rs.getInt(12), rs.getInt(13), rs.getInt(14));
                    appointmentArray.add(appointment);
                }

                AppID.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
                AppTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
                AppStart.setCellValueFactory(new PropertyValueFactory<>("start"));
                AppEnd.setCellValueFactory(new PropertyValueFactory<>("end"));
                AppCustID.setCellValueFactory(new PropertyValueFactory<>("custID"));
                AppContactID.setCellValueFactory(new PropertyValueFactory<>("contactID"));
                AppDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
                AppLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
                AppType.setCellValueFactory(new PropertyValueFactory<>("type"));
                AppointmentTable.setItems(appointmentArray);
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        else if (RadioWeek.isSelected())
        {
            try(Statement query = Main.getConn().createStatement()) //Filters the Appointment table by week
            {
                ResultSet rs = query.executeQuery("SELECT * FROM appointments WHERE YEAR(Start)=YEAR(CURDATE()) AND WEEKOFYEAR(Start)=WEEKOFYEAR(CURDATE())");
                ObservableList<Appointment> appointmentArray = FXCollections.observableArrayList();

                while (rs.next())
                {
                    Appointment appointment = new Appointment(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getObject(6, LocalDateTime.class),
                            rs.getObject(7, LocalDateTime.class), rs.getInt(12), rs.getInt(13), rs.getInt(14));
                    appointmentArray.add(appointment);
                }

                AppID.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
                AppTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
                AppStart.setCellValueFactory(new PropertyValueFactory<>("start"));
                AppEnd.setCellValueFactory(new PropertyValueFactory<>("end"));
                AppCustID.setCellValueFactory(new PropertyValueFactory<>("custID"));
                AppContactID.setCellValueFactory(new PropertyValueFactory<>("contactID"));
                AppDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
                AppLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
                AppType.setCellValueFactory(new PropertyValueFactory<>("type"));
                AppointmentTable.setItems(appointmentArray);
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        else if (RadioMonth.isSelected())
        {
            try(Statement query = Main.getConn().createStatement()) //Filters the Appointment table by month
            {

                LocalDateTime nowUTC = LocalDateTime.now(Clock.systemUTC());
                LocalDateTime nowLocal = LocalDateTime.now(Clock.systemDefaultZone());

                int offSet = 0;
                String offSetStr;
                IntBinaryOperator diff = (n1, n2) -> n1 - n2;

                if(nowUTC.isAfter(nowLocal) && diff.applyAsInt(nowUTC.getHour(), nowLocal.getHour()) > 0)//this if else structure determines the timezone offset from UTC
                {
                    offSet = 0 - (diff.applyAsInt(nowUTC.getHour(), nowLocal.getHour()));
                }
                else if (nowUTC.isAfter(nowLocal) && diff.applyAsInt(nowUTC.getHour(), nowLocal.getHour()) < 0)
                {
                    offSet = 0 - (24 + (diff.applyAsInt(nowUTC.getHour(), nowLocal.getHour())));
                }
                else if (nowLocal.isAfter(nowUTC) && diff.applyAsInt(nowUTC.getHour(), nowLocal.getHour()) > 0)
                {
                    offSet = 0 - (diff.applyAsInt(nowUTC.getHour(), nowLocal.getHour()));
                }
                else if (nowLocal.isAfter(nowUTC) && diff.applyAsInt(nowUTC.getHour(), nowLocal.getHour()) < 0)
                {
                    offSet = 24 - (diff.applyAsInt(nowUTC.getHour(), nowLocal.getHour()));
                }
                offSetStr = offSet + ":00:00";
                ResultSet rs = query.executeQuery("SELECT * FROM appointments WHERE YEAR(Start)=YEAR(CURDATE()) AND MONTH(ADDTIME(Start, \"" + offSetStr + "\"))=MONTH(CURDATE())");
                ObservableList<Appointment> appointmentArray = FXCollections.observableArrayList();
                while (rs.next())
                {
                    Appointment appointment = new Appointment(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getObject(6, LocalDateTime.class),
                            rs.getObject(7, LocalDateTime.class), rs.getInt(12), rs.getInt(13), rs.getInt(14));
                    appointmentArray.add(appointment);
                }

                AppID.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
                AppTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
                AppStart.setCellValueFactory(new PropertyValueFactory<>("start"));
                AppEnd.setCellValueFactory(new PropertyValueFactory<>("end"));
                AppCustID.setCellValueFactory(new PropertyValueFactory<>("custID"));
                AppContactID.setCellValueFactory(new PropertyValueFactory<>("contactID"));
                AppDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
                AppLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
                AppType.setCellValueFactory(new PropertyValueFactory<>("type"));
                AppointmentTable.setItems(appointmentArray);
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * Generates 1 of 3 different reports based on user selection
     */
    public void genReport()
    {
        if (ExpiredApps.isSelected())
        {
            AppointmentTable1.setVisible(true);
            AppointmentTable11.setVisible(false);
            AppointmentTable12.setVisible(false);
            try(Statement query = Main.getConn().createStatement())
            {
                ResultSet rs = query.executeQuery("SELECT * FROM appointments WHERE Start < '" + LocalDateTime.now(Clock.systemUTC()) + "'");
                ObservableList<Appointment> appointmentArray = FXCollections.observableArrayList();

                while (rs.next())
                {
                    Appointment appointment = new Appointment(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getObject(6, LocalDateTime.class),
                            rs.getObject(7, LocalDateTime.class), rs.getInt(12), rs.getInt(13), rs.getInt(14));
                    appointmentArray.add(appointment);
                }

                AppID1.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
                AppTitle1.setCellValueFactory(new PropertyValueFactory<>("title"));
                AppStart1.setCellValueFactory(new PropertyValueFactory<>("start"));
                AppEnd1.setCellValueFactory(new PropertyValueFactory<>("end"));
                AppCustID1.setCellValueFactory(new PropertyValueFactory<>("custID"));
                AppContactID1.setCellValueFactory(new PropertyValueFactory<>("contactID"));
                AppDescription1.setCellValueFactory(new PropertyValueFactory<>("description"));
                AppLocation1.setCellValueFactory(new PropertyValueFactory<>("location"));
                AppType1.setCellValueFactory(new PropertyValueFactory<>("type"));
                AppointmentTable1.setItems(appointmentArray);
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        else if (ContactSchedule.isSelected())
        {
            AppointmentTable1.setVisible(false);
            AppointmentTable11.setVisible(true);
            AppointmentTable12.setVisible(false);
            try(Statement query = Main.getConn().createStatement())
            {
                ResultSet rs = query.executeQuery("SELECT * FROM appointments");
                ObservableList<Appointment> appointmentArray = FXCollections.observableArrayList();

                while (rs.next())
                {
                    Appointment appointment = new Appointment(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getObject(6, LocalDateTime.class),
                            rs.getObject(7, LocalDateTime.class), rs.getInt(12), rs.getInt(13), rs.getInt(14));
                    appointmentArray.add(appointment);
                }

                AppID11.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
                AppTitle11.setCellValueFactory(new PropertyValueFactory<>("title"));
                AppStart11.setCellValueFactory(new PropertyValueFactory<>("start"));
                AppEnd11.setCellValueFactory(new PropertyValueFactory<>("end"));
                AppCustID11.setCellValueFactory(new PropertyValueFactory<>("custID"));
                AppContactID11.setCellValueFactory(new PropertyValueFactory<>("contactID"));
                AppDescription11.setCellValueFactory(new PropertyValueFactory<>("description"));
                AppLocation11.setCellValueFactory(new PropertyValueFactory<>("location"));
                AppType11.setCellValueFactory(new PropertyValueFactory<>("type"));
                AppointmentTable11.setItems(appointmentArray);
                AppointmentTable11.getSortOrder().add(AppCustID11);
                AppointmentTable11.sort();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        else if (AppTypes.isSelected())
        {
            AppointmentTable1.setVisible(false);
            AppointmentTable11.setVisible(false);
            AppointmentTable12.setVisible(true);
            try(Statement query = Main.getConn().createStatement())
            {
                ResultSet rs = query.executeQuery("SELECT * FROM appointments WHERE YEAR(Start)=YEAR(CURDATE()) AND MONTH(Start)=MONTH(CURDATE())");
                ObservableList<Appointment> appointmentArray = FXCollections.observableArrayList();

                while (rs.next())
                {
                    Appointment appointment = new Appointment(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getObject(6, LocalDateTime.class),
                            rs.getObject(7, LocalDateTime.class), rs.getInt(12), rs.getInt(13), rs.getInt(14));
                    appointmentArray.add(appointment);
                }

                AppID12.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
                AppTitle12.setCellValueFactory(new PropertyValueFactory<>("title"));
                AppStart12.setCellValueFactory(new PropertyValueFactory<>("start"));
                AppEnd12.setCellValueFactory(new PropertyValueFactory<>("end"));
                AppCustID12.setCellValueFactory(new PropertyValueFactory<>("custID"));
                AppContactID12.setCellValueFactory(new PropertyValueFactory<>("contactID"));
                AppDescription12.setCellValueFactory(new PropertyValueFactory<>("description"));
                AppLocation12.setCellValueFactory(new PropertyValueFactory<>("location"));
                AppType12.setCellValueFactory(new PropertyValueFactory<>("type"));
                AppointmentTable12.setItems(appointmentArray);
                AppointmentTable12.getSortOrder().add(AppType12);
                AppointmentTable12.sort();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void close()
    {
        System.exit(0);
    }

    /**
     * @return whether a Customer is being added or updated
     */
    public static  boolean getIsModifyingCust()
    {
        return isModifyingCust;
    }

    /**
     * @return the selected customer
     */
    public static Customer getSelectedCust()
    {
        return selectedCust;
    }

    /**
     * @return A new customer ID
     */
    public static int getCustID()
    {
        return custID;
    }

    /**
     * @return whether an appointment is being added or updated
     */
    public static boolean getIsModifyingApp() {
        return isModifyingApp;
    }

    /**
     * @return the selected appointment
     */
    public static Appointment getSelectedApp() {
        return selectedApp;
    }

    /**
     * @return a new appointment ID
     */
    public static int getAppID() {
        return appID;
    }

    /**
     * @return the current logged in user's ID
     */
    public static int getUserID() {
        return userID;
    }

    /**
     * @param userID sets the current logged in users ID
     */
    public static void setUserID(int userID) {
        MainAppController.userID = userID;
    }
}

