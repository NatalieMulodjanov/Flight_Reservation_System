/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lfrs;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import org.sqlite.SQLiteException;

/**
 *
 * @author NatalieM
 */
public class Controller {

    private Flight flight;
    private FlightSystemViewable view;
    private Client client;
    private final Connection con = ConnectionSingle.getInstance();

    public Controller(Flight model, Client client, FlightSystemViewable view) {
        this.view = view;
        this.client = client;
        this.flight = model;

    }

    public void createTables() {
        String createFlightTable = "CREATE TABLE IF NOT EXISTS FLIGHT "
                + "(FLIGHTNO TEXT PRIMARY KEY, "
                + "NAME TEXT NOT NULL, "
                + "ORIGIN TEXT NOT NULL, "
                + "DEST TEXT NOT NULL, "
                + "DURATION NUMBER NOT NULL, "
                + "SEATS NUMBER NOT NULL, "
                + "AVAILABLE NUMBER NOT NULL, "
                + "AMOUNT DECIMAL(7,2) NOT NULL)";

        String createClientTable = "CREATE TABLE IF NOT EXISTS CLIENTS "
                + "(PASSNUM TEXT PRIMARY KEY, "
                + "FLNAME TEXT NOT NULL, "
                + "CONTACT NUMBER NOT NULL)";

        String createReservedFlightsTable = "CREATE TABLE IF NOT EXISTS RESERVEDFLIGHTS "
                + "(TICKETNO NUMBER PRIMARY KEY, "
                + "FLIGHTNO TEXT, "
                + "PASSNUM NUMBER, "
                + "ISSUEDATE DATE, "
                + "CONTACT NUMBER NOT NULL, "
                + "AMOUNT DECIMAL(7,2) NOT NULL, "
                + "FOREIGN KEY (FLIGHTNO) REFERENCES FLIGHT(FLIGHTNO)"
                + "FOREIGN KEY (PASSNUM) REFERENCES CLIENTS(PASSNUM))";

        try (Statement statement = con.createStatement()) {
            statement.executeUpdate(createFlightTable);
            statement.executeUpdate(createClientTable);
            statement.executeUpdate(createReservedFlightsTable);
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
            System.exit(0);
        }
    }

    public boolean addFlight(Flight flight) {
        if (flight == null) {
            return false;
        }
        return flight.addFlight(flight);
    }

    public boolean removeFlight(String flightNo) {
        if (flight == null) {
            return false;
        }
        return flight.removeFlight(flightNo);
    }

    public boolean updateFlightData(String flightNo, String field, String newValue) {
        if (flight == null) {
            return false;
        }
        return flight.updateFlightData(flightNo, field, newValue);
    }

    public boolean issueTicket(String flightNo, Client client) throws Exception {
        if (flight == null) {
            return false;
        }
        return flight.issueTicket(flightNo, client);
    }

    public boolean bookSeat(String flightNo) {
        if (client == null) {
            return false;
        }
        return client.bookSeat(flightNo);
    }

    public static Map<String, String> viewBookedFlights() {
        return Flight.viewBookedFlights();
    }

    public boolean cancelFlight(int ticketNo, int passNo) throws Exception {
        if (flight == null) {
            return false;
        }
        return flight.cancelFlight(ticketNo, passNo);
    }

    public boolean cancelReservation(int ticketNo) throws Exception {
        if (client == null) {
            return false;
        }
        return client.cancelReservation(ticketNo);
    }

    public List<Flight> searchFlightByDestination(String dest) {
        if (client == null) {
            return null;
        }
        return client.searchFlightByDestination(dest);
    }

    public List<Flight> searchFlightByDuration(int dur) {
        if (client == null) {
            return null;
        }
        return client.searchFlightByDuration(dur);
    }

    public List<Flight> searchFlightByOrigin(String orig) {
        if (client == null) {
            return null;
        }

        return client.searchFlightByOrigin(orig);
    }

    public Map<String, String> viewFlightBoard() {
        if (client == null) {
            return null;
        }

        return client.viewFlightBoard();
    }

    public void updateFlightView() {
        Map<String, String> board = flight.viewBoard();
        view.printBoard(board);
    }

    public void addToClientTable(Client client) throws Exception {
        String addToClientTable = "INSERT INTO CLIENTS VALUES ("
                + client.getPassNum() + ", '"
                + client.getFullName() + "', '"
                + client.getContactNum() + "')";
        try (Statement statement = con.createStatement()) {
            statement.executeUpdate(addToClientTable);
        } catch (SQLiteException e) {
            e.printStackTrace();
            throw new Exception("Failed to add user " + e.getMessage());
        }
    }
    
    public Client getClient(Client client) {
        String getClientQuery = "SELECT * FROM CLIENTS WHERE PASSNUM = " + client.getPassNum();
        try (Statement statement = con.createStatement()) {
            ResultSet rs = statement.executeQuery(getClientQuery);
            if (!rs.next()) {
                return null;
            }
            return new Client(rs.getInt(1), rs.getString(2), rs.getString(3));
        } catch (SQLException ex) {        }
        
        return null;
    }

    public void updateClientView() {
        Map<String, String> board = this.viewFlightBoard();
        view.printBoard(board);
    }

}
