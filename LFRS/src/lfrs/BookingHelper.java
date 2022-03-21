/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lfrs;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author NatalieM
 */
public class BookingHelper {

    private static Connection con = ConnectionSingle.getInstance();

    public boolean cancelHelper(int ticketNo, int passNo) throws Exception {
        String findMatchingRecord = "SELECT COUNT(TICKETNO) FROM RESERVEDFLIGHTS WHERE TICKETNO = " + ticketNo + " AND PASSNUM = " + passNo;
        String deleteRecord = "DELETE FROM RESERVEDFLIGHTS WHERE TICKETNO = " + ticketNo;
        try (Statement statement = con.createStatement()) {
            ResultSet rs = statement.executeQuery(findMatchingRecord);
            rs.next();
            int countExist = rs.getInt("COUNT(TICKETNO)");
            if (countExist == 1) {
                statement.executeUpdate(deleteRecord);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    public static Map<String, String> viewBoardHelper() {
        String retrieveFlights = "SELECT * FROM FLIGHT";
        Map<String, String> flightsMap = new TreeMap<String, String>();
        try (Statement statement = con.createStatement()){
            ResultSet rs = statement.executeQuery(retrieveFlights);
            while(rs.next()){
                String flightNo = rs.getString("FLIGHTNO");
                String name = rs.getString("NAME");
                String origin = rs.getString("ORIGIN");
                String destination = rs.getString("DEST");
                int duration = rs.getInt("DURATION");
                int seats = rs.getInt("SEATS");
                int available = rs.getInt("AVAILABLE");
                double amount = rs.getDouble("AMOUNT");
                flightsMap.put(flightNo, " Name=" + name + " Origin=" + origin + " Destination=" + destination + " Duration=" + duration + " Seats=" + seats + " AvailableSeats=" + available + " Amount=" + amount);
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
            return null;
        }
        return flightsMap;
    }
    
    public boolean addToReservedFlightsHelper(String flightNo, Client client) {
        String getAmount = "SELECT AMOUNT FROM FLIGHT WHERE FLIGHTNO = " + flightNo;
        double amount = 0;

        int ticketNo = getTicketNoHelper();
        if (ticketNo == -1) {
            return false;
        }
        try (Statement statement = con.createStatement()) {
            try (Statement statement2 = con.createStatement()) {
                ResultSet rs = statement.executeQuery(getAmount);
                rs.next();
                amount = rs.getDouble("AMOUNT");
                statement.close();
                String insertNewReservation = "INSERT INTO RESERVEDFLIGHTS VALUES ("
                        + ticketNo + ", '"
                        + flightNo.toLowerCase() + "' ,"
                        + client.getPassNum() + ","
                        + "CURRENT_DATE,"
                        + client.getContactNum() + ","
                        + amount + ")";
                statement2.executeUpdate(insertNewReservation);
            }

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }

        return true;
    }
    
    private int getTicketNoHelper() {
        int ticketNo = 0;
        String getLastTicketNumber = "SELECT * FROM RESERVEDFLIGHTS WHERE TICKETNO = (SELECT MAX(TICKETNO) FROM RESERVEDFLIGHTS) ";
        try (Statement statement = con.createStatement()) {
            ResultSet rs = statement.executeQuery(getLastTicketNumber);
            rs.next();
            ticketNo = rs.getInt("TICKETNO");
            ticketNo++;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
        }
        return ticketNo;
    }

}
