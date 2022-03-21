/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lfrs;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

/**
 *
 * @author NatalieM
 */
public class Test {

    static Connection con = ConnectionSingle.getInstance();

    public static void main(String[] args) throws Exception {
        Statement statement = con.createStatement();
        ResultSet rs = statement.executeQuery("select * from CLIENTS");
        while (rs.next()) {
            int ticketNo = rs.getInt(1);
            String x = rs.getString(2);
            String y = rs.getString(3);
            
            System.out.println(ticketNo);
        }

        Flight model = new Flight();
        Flight flight = new Flight("367", "montreal", "MONTREAL", "toronto", 500, 100, 0, 500);
        Flight flight2 = new Flight("8297", "montreal", "montreal", "toronto", 200, 100, 50, 500);

        FlightViewForm view = new FlightViewForm();
        Controller controller = new Controller(model, null, view);
        controller.createTables();
        String deletAll = "DELETE FROM FLIGHT";
        statement.executeUpdate(deletAll);
//        ResultSet rs = statement.executeQuery("select * from RESERVEDFLIGHTS");
        while (rs.next()) {
            int ticketNo = rs.getInt("TICKETNO");
            System.out.println(ticketNo);
        }

        controller.addFlight(flight);
        controller.addFlight(flight2);
        controller.bookSeat("367");
////           Map<String,String> map =   controller.viewFlightBoard();
////           System.out.println(map);
//    controller.addToClientTable(new Client(123, "natakie", "5145028904"));
//        ResultSet rs = statement.executeQuery("SELECT * FROM CLIENT");
//        while (rs.next()){
//            int pass = rs.getInt("PASSNUM");
//            String name = rs.getString("FLNAME");
//            String contact = rs.getString("CONTANCT");
//            Client client = new Client(pass, name, contact);
//            System.out.println("");
//        }
//        boolean ticket = controller.issueTicket("367", new Client("natalie", 1234, 123456));
//         boolean ticket2 = controller.issueTicket("8297", new Client("natalie", 1234, 123456));
//            System.out.println(ticket);
//            System.out.println( ticket2);
//       Map<String,String> map = controller.viewBookedFlights();
//        System.out.println(map);

//        String getFlights = "SELECT * FROM RESERVEDFLIGHTS";
//       
//       
//       ResultSet rs = statement.executeQuery(getFlights);
//
//        while (rs.next()){
//           
//            String flightno = rs.getString("TICKETNO");
//            
//            System.out.println(flightno);
//            //System.out.println(rs.getInt("PASSNUM"));
    }
}
