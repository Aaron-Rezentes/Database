package Source;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.MissingResourceException;

public class Main extends Application
{
    private static Connection conn;

    /**
     * gets the connection to the database
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        String dbURL = "jdbc:mysql://wgudb.ucertify.com:3306/WJ084DP";
        conn = DriverManager.getConnection(dbURL, "U084DP", "53689205800");
        launch(args);
    }

    /**
     * shows the log in form and closes the databse connection on close
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage)
    {
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>(){public void handle(WindowEvent wEvent) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }});

        try //loads and shows the Login form
        {
            Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        }
        catch(IOException e)
        {
            System.out.println("Error, Login.fxml file not found");
        }
    }

    /**
     * @return the database connection
     */
    public static Connection getConn()
    {
        return conn;
    }
}
