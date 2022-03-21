/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import lfrs.Client;
import lfrs.ConnectionSingle;
import lfrs.Controller;
import lfrs.Flight;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author NatalieM
 */
public class ControllerTest {

    Controller controller;
    Connection con = ConnectionSingle.getInstance();

    public ControllerTest() {
        this.controller = new Controller(new Flight(), new Client(123, "123", "123"), null);

    }

    @Before
    public void beforeEach() {
        try (Statement statement = con.createStatement()) {
            String deletAll = "DELETE FROM RESERVEDFLIGHTS; DELETE FROM FLIGHT; DELETE FROM CLIENTS";
            statement.executeUpdate(deletAll);
        } catch (SQLException ex) {
            Logger.getLogger(ControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void createTablesTest() {
        controller.createTables();
        // If we can add a flight that means the tables were created
        assertTrue(controller.addFlight(new Flight("123", "123", "123", "123", 123, 123, 123, 123)));
    }

    @Test
    public void addFlightTest() {
        controller.createTables();
        assertTrue(controller.addFlight(new Flight("123", "123", "123", "123", 123, 123, 123, 123)));
    }

    @Test
    public void addFlightTwiceTest() {
        controller.addFlight(new Flight("123", "123", "123", "123", 123, 123, 123, 123));
        assertFalse(controller.addFlight(new Flight("123", "123", "123", "123", 123, 123, 123, 123)));
    }

    @Test
    public void removeFlightTest() {
        controller.addFlight(new Flight("123", "123", "123", "123", 123, 123, 123, 123));
        assertTrue(controller.removeFlight("123"));
    }

//    @Test
//    public void removeFlightDoesntExistTest() {
//        controller.createTables();
//        assertFalse(controller.removeFlight("noFlightLikeThis"));
//    }
    @Test
    public void updateFlightDataTest() {
        controller.createTables();
        controller.addFlight(new Flight("123", "123", "123", "123", 123, 123, 123, 123));
        assertTrue(controller.updateFlightData("123", "flightNo", "321"));
        assertTrue(controller.viewFlightBoard().containsKey("321"));
    }

    @Test
    public void issueTicketTest() {
        try {
            controller.createTables();
            controller.addFlight(new Flight("123", "123", "123", "123", 123, 123, 123, 123));
            assertTrue(controller.issueTicket("123", new Client(123, "123", "123")));
        } catch (Exception ex) {
            fail();
        }
    }

    @Test
    public void issueTicketNoFlightTest() {
        try {
            controller.createTables();
            controller.addFlight(new Flight("123", "123", "123", "123", 123, 123, 123, 123));
            assertFalse(controller.issueTicket("doesntExist", new Client(123, "123", "123")));
        } catch (Exception ex) {
            fail();
        }
    }

    @Test
    public void bookASeatTest() {
        controller.createTables();
        controller.addFlight(new Flight("123", "123", "123", "123", 123, 123, 123, 123));
        assertTrue(controller.bookSeat("123"));
    }

    @Test
    public void bookASeatNoFlightTest() {
        controller.createTables();
        controller.addFlight(new Flight("123", "123", "123", "123", 123, 123, 123, 123));
        assertFalse(controller.bookSeat("doesntExist"));
    }

    @Test
    public void viewBookedFlightsTest() {
        controller.createTables();
        controller.addFlight(new Flight("123", "123", "123", "123", 123, 123, 123, 123));
        assertTrue(controller.viewFlightBoard().containsKey("123"));
    }

    @Test
    public void viewBookedFlightsNoFlightTest() {
        controller.createTables();
        controller.addFlight(new Flight("123", "123", "123", "123", 123, 123, 123, 123));
        assertFalse(controller.viewFlightBoard().containsKey("doesntExist"));
    }

    @Test
    public void cancelFlightTest() {
        try {
            controller.createTables();
            controller.addFlight(new Flight("123", "123", "123", "123", 123, 123, 123, 123));
            controller.cancelFlight(0, 123);
            assertFalse(Controller.viewBookedFlights().containsKey("123"));
        } catch (Exception ex) {
            fail();
        }
    }

    @Test
    public void cancelReservationTest() {
        try {
            controller.createTables();
            controller.addFlight(new Flight("123", "123", "123", "123", 123, 123, 123, 123));
            controller.cancelReservation(0);
            assertFalse(Controller.viewBookedFlights().containsKey("123"));
        } catch (Exception ex) {
            fail();
        }
    }

    @Test
    public void searchFlightByDestinationTest() {
        controller.createTables();
        controller.addFlight(new Flight("123", "123", "123", "123", 123, 123, 123, 123));
        assertEquals(controller.searchFlightByDestination("123").size(), 1);
    }

    @Test
    public void searchFlightByDurationTest() {
        controller.createTables();
        controller.addFlight(new Flight("123", "123", "123", "123", 123, 123, 123, 123));
        assertEquals(controller.searchFlightByDuration(123).size(), 1);
    }

    @Test
    public void searchFlightByOriginTest() {
        controller.createTables();
        controller.addFlight(new Flight("123", "123", "123", "123", 123, 123, 123, 123));
        assertEquals(controller.searchFlightByOrigin("123").size(), 1);
    }

    @Test
    public void viewFlightBoardTest() {
        controller.createTables();
        controller.addFlight(new Flight("123", "123", "123", "123", 123, 123, 123, 123));
        assertTrue(controller.viewFlightBoard().containsKey("123"));
    }

    @Test
    public void addToClientTableTest() {
        try {
            controller.createTables();
            controller.addToClientTable(new Client(1234, "1234", "1234"));
            assertTrue(controller.getClient(new Client(1234, "1234", "1234")) != null);
        } catch (Exception ex) {
            fail();
        }
    }
}
