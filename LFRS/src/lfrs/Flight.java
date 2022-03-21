/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lfrs;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

/**
 *
 * @author NatalieM
 */
public class Flight {

    private String flightNo;
    private String name;
    private String origin;
    private String destination;
    private int duration;
    private int seats;
    private int seatsAvailable;
    private double ticketAmount;
    private static Connection con = ConnectionSingle.getInstance();
    private BookingHelper bookingHelper = new BookingHelper();

    public Flight() {

    }

    public Flight(String flightNo, String name, String origin, String destination, int duration, int seats, int seatsAvailable, double ticketAmount) {
        this.flightNo = flightNo;
        this.name = name;
        this.origin = origin;
        this.destination = destination;
        this.duration = duration;
        this.seats = seats;
        this.seatsAvailable = seatsAvailable;
        this.ticketAmount = ticketAmount;
    }

    public boolean addFlight(Flight flight) {
        String insertNewFlight = "INSERT INTO FLIGHT VALUES ("
                + flight.getFlightNo().toLowerCase() + ", '"
                + flight.getName().toLowerCase() + "', '"
                + flight.getOrigin().toLowerCase() + "', '"
                + flight.getDestination().toLowerCase() + "', '"
                + flight.getDuration() + "',"
                + flight.getSeats() + ","
                + flight.getSeatsAvailable() + ","
                + flight.getTicketAmount() + ")";

        try (Statement statement = con.createStatement()) {
            statement.executeUpdate(insertNewFlight);
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean removeFlight(String flightNo) {
        String removeFlight = "DELETE FROM FLIGHT WHERE FLIGHTNO = '" + flightNo + "'";

        try (Statement statement = con.createStatement()) {
            statement.executeUpdate(removeFlight);
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean updateFlightData(String flightNo, String field, String newValue) {
        String updateRecord = "UPDATE FLIGHT SET " + field.toUpperCase() + " = " + newValue
                + " WHERE FLIGHTNO = " + flightNo;

        try (Statement statement = con.createStatement()) {
            statement.executeUpdate(updateRecord);
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean issueTicket(String flightNo, Client client) throws Exception{
        String checkVailablity = "SELECT AVAILABLE FROM FLIGHT WHERE FLIGHTNO = " + flightNo;
        String decreaseAvailable = "UPDATE FLIGHT SET AVAILABLE =- 1 WHERE FLIGHTNO = " + flightNo;
        try (Statement statement = con.createStatement()) {
            ResultSet rs = statement.executeQuery(checkVailablity);
            if (!rs.next()) {
               throw new Exception("No seats available");
            }
            int available = rs.getInt("AVAILABLE");
            if (available > 0 && bookingHelper.addToReservedFlightsHelper(flightNo, client)) {
                statement.executeUpdate(decreaseAvailable);
                return true;
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public boolean cancelFlight(int ticketNo, int passNo) throws Exception{
        return bookingHelper.cancelHelper(ticketNo, passNo);
    }

    public static Map<String, String> viewBookedFlights() {
        String retrieveReservedFlights = "SELECT * FROM RESERVEDFLIGHTS";
        Map<String, String> reservedFlightsMap = new TreeMap<String, String>();
        try (Statement statement = con.createStatement()) {
            ResultSet rs = statement.executeQuery(retrieveReservedFlights);
            while (rs.next()) {
                int ticketNo = rs.getInt("TICKETNO");
                String flightNo = rs.getString("FLIGHTNO");
                int passNo = rs.getInt("PASSNUM");
                String issueDate = rs.getObject("ISSUEDATE").toString();
                int contactNo = rs.getInt("CONTACT");
                double amount = rs.getDouble("AMOUNT");
                reservedFlightsMap.put(flightNo, "TicketNumber=" + ticketNo + " PassportNumber=" + passNo + " IssueDate=" + issueDate + " ContanceNumber=" + contactNo + " Amount" + amount);
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
            return null;
        }
        return reservedFlightsMap;
    }

    public static Map<String, String> viewBoard() {
        return BookingHelper.viewBoardHelper();
    }

    public String getFlightNo() {
        return flightNo;
    }

    public String getName() {
        return name;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public int getDuration() {
        return duration;
    }

    public int getSeats() {
        return seats;
    }

    public int getSeatsAvailable() {
        return seatsAvailable;
    }

    public double getTicketAmount() {
        return ticketAmount;
    }

    @Override
    public String toString() {
        return "Flight{" + "flightNo=" + flightNo + ", name=" + name + ", origin=" + origin + ", destination=" + destination + ", seats=" + seats + ", seatsAvailable=" + seatsAvailable + ", ticketAmount=" + ticketAmount + '}';
    }

}
