/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lfrs;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.*;

/**
 *
 * @author NatalieM
 */
public class Client {

    private String fullName;
    private int passNum;
    private String contactNum;
    private static Connection con = ConnectionSingle.getInstance();
    private BookingHelper bookingHelper = new BookingHelper();

    public Client() {
    }

    public Client(int passNum, String fullName, String contactNum) {
        this.passNum = passNum;
        this.fullName = fullName;
        this.contactNum = contactNum;
    }

    public boolean bookSeat(String flightNo) {
        String checkAvailablity = "SELECT AVAILABLE FROM FLIGHT WHERE FLIGHTNO = '" + flightNo + "'";
        String decreaseAvailable = "UPDATE FLIGHT SET AVAILABLE =- 1 WHERE FLIGHTNO = " + flightNo;
        try (Statement statement = con.createStatement()) {
            ResultSet rs = statement.executeQuery(checkAvailablity);
            rs.next();
            int available = rs.getInt("AVAILABLE");
            if (available > 0 && bookingHelper.addToReservedFlightsHelper(flightNo, this)) {
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

    public boolean cancelReservation(int ticketNo) throws Exception{
        return bookingHelper.cancelHelper(ticketNo, passNum);
    }

    public List<Flight> searchFlightByDestination(String dest) {
        String searchByDest = "SELECT * FROM FLIGHT WHERE DEST = '" + dest.toLowerCase()+ "'";
        List<Flight> flightList = new ArrayList<Flight>();
        try (Statement statement = con.createStatement()) {
            ResultSet rs = statement.executeQuery(searchByDest);
            while (rs.next()) {
                String flightNo = rs.getString("FLIGHTNO");
                String name = rs.getString("NAME");
                String origin = rs.getString("ORIGIN");
                String destination = rs.getString("DEST");
                int duration = rs.getInt("DURATION");
                int seats = rs.getInt("SEATS");
                int available = rs.getInt("AVAILABLE");
                double amount = rs.getDouble("AMOUNT");
                flightList.add(new Flight(flightNo, name, origin, destination, duration, seats, available, amount));
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
        }
        return flightList;
    }

    public List<Flight> searchFlightByDuration(int dur) {
        String searchByDuration = "SELECT * FROM FLIGHT WHERE DURATION = " + dur;
        List<Flight> flightList = new ArrayList<Flight>();
        try (Statement statement = con.createStatement()) {
            ResultSet rs = statement.executeQuery(searchByDuration);
            while (rs.next()) {
                String flightNo = rs.getString("FLIGHTNO");
                String name = rs.getString("NAME");
                String origin = rs.getString("ORIGIN");
                String destination = rs.getString("DEST");
                int duration = rs.getInt("DURATION");
                int seats = rs.getInt("SEATS");
                int available = rs.getInt("AVAILABLE");
                double amount = rs.getDouble("AMOUNT");
                flightList.add(new Flight(flightNo, name, origin, destination, duration, seats, available, amount));
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
            return null;
        }
        return flightList;
    }
    
    public List<Flight> searchFlightByOrigin(String orig) {
        String searchByDuration = "SELECT * FROM FLIGHT WHERE ORIGIN = '" + orig.toLowerCase() + "'";
        List<Flight> flightList = new ArrayList<Flight>();
        try (Statement statement = con.createStatement()) {
            ResultSet rs = statement.executeQuery(searchByDuration);
            while (rs.next()) {
                String flightNo = rs.getString("FLIGHTNO");
                String name = rs.getString("NAME");
                String origin = rs.getString("ORIGIN");
                String destination = rs.getString("DEST");
                int duration = rs.getInt("DURATION");
                int seats = rs.getInt("SEATS");
                int available = rs.getInt("AVAILABLE");
                double amount = rs.getDouble("AMOUNT");
                flightList.add(new Flight(flightNo, name, origin, destination, duration, seats, available, amount));
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
            return null;
        }
        return flightList;
    }
    
    public Map<String,String> viewFlightBoard(){
        Map<String,String> flightMap = bookingHelper.viewBoardHelper();
        Map<String,String> flightWithAvailableSeatsMap = new TreeMap();
        for (Map.Entry<String, String> entry : flightMap.entrySet()) {
            if (!entry.getValue().contains("AvailableSeats=0")){
                flightWithAvailableSeatsMap.put(entry.getKey(), entry.getValue());
            }
        }
        return flightWithAvailableSeatsMap;
    }

    public String getFullName() {
        return fullName;
    }

    public int getPassNum() {
        return passNum;
    }

    public String getContactNum() {
        return contactNum;
    }

    @Override
    public String toString() {
        return "Client{" + "fullName=" + fullName + ", passNum=" + passNum + ", contactNum=" + contactNum + '}';
    }

}
