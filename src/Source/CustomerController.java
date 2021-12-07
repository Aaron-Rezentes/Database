package Source;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Clock;
import java.time.LocalDateTime;

public class CustomerController
{

    @FXML
    private Label CustomerLabel;

    @FXML
    private ComboBox<String> CountryField;

    @FXML
    private ComboBox<String> DivisionField;

    @FXML
    private TextField IDField;

    @FXML
    private TextField NameField;

    @FXML
    private TextField AddressField;

    @FXML
    private TextField PostalField;

    @FXML
    private TextField PhoneField;

    @FXML
    private Button AddButton;

    @FXML
    private Button CancelButton;

    /**
     * if updating it loads the fields with their information
     * @throws SQLException
     */
    public void initialize() throws SQLException {

        if(MainAppController.getIsModifyingCust())
        {
            Customer customer = MainAppController.getSelectedCust();
            int countryID = 0;

            CustomerLabel.setText("Update Customer");
            AddButton.setText("Update");
            IDField.setText(String.valueOf(customer.getID()));
            NameField.setText(customer.getName());
            PostalField.setText(String.valueOf(customer.getPostalCode()));
            PhoneField.setText(String.valueOf(customer.getPhoneNumber()));
            AddressField.setText(String.valueOf(customer.getAddress()));

            try(Statement query = Main.getConn().createStatement())
            {
                ResultSet rs = query.executeQuery("SELECT COUNTRY_ID, Division_ID FROM first_level_divisions WHERE Division_ID LIKE '" + customer.getFirstDivisionID() + "'");
                if(rs.next())
                {
                    countryID = rs.getInt(1);
                }
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
            try(Statement query = Main.getConn().createStatement())
            {
                ResultSet rs = query.executeQuery("SELECT Country, COUNTRY_ID FROM countries WHERE COUNTRY_ID LIKE '" + countryID + "'");

                if(rs.next())
                {
                    CountryField.getSelectionModel().select(rs.getString(1));
                }
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
            try(Statement query = Main.getConn().createStatement())
            {
                ResultSet rs = query.executeQuery("SELECT Division, Division_ID FROM first_level_divisions WHERE Division_ID LIKE '" + customer.getFirstDivisionID() + "'");

                if(rs.next())
                {
                    DivisionField.getSelectionModel().select(rs.getString(1));
                }
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            CustomerLabel.setText("Add Customer");
            IDField.setText(String.valueOf(MainAppController.getCustID()));
        }

        try(Statement query = Main.getConn().createStatement())
        {
            ResultSet rs = query.executeQuery("SELECT Country FROM countries");
            ObservableList<String> countries = FXCollections.observableArrayList();

            while (rs.next())
            {
                countries.add(rs.getString(1));
            }
            CountryField.setItems(countries);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        try(Statement query = Main.getConn().createStatement())
        {
            ResultSet rs = query.executeQuery("SELECT Division FROM first_level_divisions");
            ObservableList<String> Divisions = FXCollections.observableArrayList();

            while (rs.next())
            {
                Divisions.add(rs.getString(1));
            }
            DivisionField.setItems(Divisions);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * changes the division list to only be the first level divisions of the selected country
     */
    public void updateDivisions()
    {
        int countryID = 0;
        try(Statement query = Main.getConn().createStatement())
        {
            ResultSet rs = query.executeQuery("SELECT COUNTRY_ID, Country FROM countries WHERE Country LIKE '" + CountryField.getSelectionModel().getSelectedItem() + "'");
            ObservableList<String> Divisions = FXCollections.observableArrayList();

            if(rs.next())
            {
                countryID = rs.getInt(1);
            }
            else
            {
                return;
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        try(Statement query = Main.getConn().createStatement())
        {
            ResultSet rs = query.executeQuery("SELECT Division, COUNTRY_ID FROM first_level_divisions");
            ObservableList<String> Divisions = FXCollections.observableArrayList();

            while (rs.next())
            {
                if(rs.getInt(2) == countryID)
                {
                    Divisions.add(rs.getString(1));
                }
            }
            DivisionField.setItems(Divisions);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * adds the customer to the database
     */
    public void add()
    {
        int userID = MainAppController.getUserID();
        if(MainAppController.getIsModifyingCust())
        {
            try(Statement update = Main.getConn().createStatement())
            {
                update.executeUpdate(
                "UPDATE customers SET" +
                " Customer_Name = '" + NameField.getText() +
                "', Address = '" + AddressField.getText() +
                "', Postal_Code = '" + PostalField.getText() +
                "', Phone = '" + PhoneField.getText() +
                "', Last_Update = '" + LocalDateTime.now(Clock.systemUTC()) +
                "', Last_Updated_By = '" + "UserID: " + userID +
                "' WHERE Customer_ID=" + Integer.valueOf(IDField.getText()));
                showMainApp();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            int divisionID = 0;

            try(Statement query = Main.getConn().createStatement())
            {
                ResultSet rs = query.executeQuery("SELECT Division, Division_ID FROM first_level_divisions WHERE Division LIKE '" + DivisionField.getSelectionModel().getSelectedItem() + "'");

                if(rs.next())
                {
                    divisionID = rs.getInt(2);
                }
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
            try(Statement update = Main.getConn().createStatement())
            {
                update.executeUpdate(
                        "INSERT INTO customers (Customer_ID, Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID)" +
                        " VALUES ('" + IDField.getText() + "','" + NameField.getText() + "','" + AddressField.getText() + "','" + PostalField.getText() + "','" + PhoneField.getText() +
                        "','" + LocalDateTime.now(Clock.systemUTC()) + "','" + "UserID: " + userID + "','" + LocalDateTime.now(Clock.systemUTC()) + "','" + "UserID: " + userID + "','" + divisionID + "')");
                showMainApp();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * returns to the main menu
     */
    public void cancel()
    {
        showMainApp();
    }

    /**
     * shows the main menu
     */
    private void showMainApp()
    {
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("MainApp.fxml"));
            Stage partStage = (Stage)IDField.getScene().getWindow();
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

