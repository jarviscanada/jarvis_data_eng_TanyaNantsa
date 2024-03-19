package ca.jrvs.apps.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCExecutor {

    public static void main(String... args){
        DatabaseConnectionManager dcm = new DatabaseConnectionManager("localhost",
                "hplussport", "postgres", "password");
        try{
            Connection connection = dcm.getConnection();
            CustomerDAO customerDAO = new CustomerDAO(connection);

            //delete implementation
            Customer customer = new Customer();
            customer.setFirstName("John");
            customer.setLastName("Adams");
            customer.setEmail("jadams.wh.gov");
            customer.setAddress("1234 Main St");
            customer.setCity("Arlington");
            customer.setState("VA");
            customer.setPhone("(555) 555-9845");
            customer.setZipCode("01234");
            Customer dbCustomer = customerDAO.create(customer);
            System.out.println(dbCustomer);
            customerDAO.delete(dbCustomer.getId());

            //update implementation
            customer = customerDAO.findById(10000);
            System.out.println(customer.getFirstName() + " " + customer.getLastName() + " " +
                    customer.getEmail());
            customer.setEmail("gwashington@wh.gov");
            customer = customerDAO.update(customer);
            System.out.println(customer.getFirstName() + " " + customer.getLastName() + " " +
                    customer.getEmail());

            //read implementation
            customer = customerDAO.findById(1000);
            System.out.println(customer.getFirstName() + " " + customer.getLastName());

//            //Create implementation
//            Customer customer = new Customer();
//            customer.setFirstName("Lianna");
//            customer.setLastName("Scott");
//            customer.setEmail("liannascott@gmail.ca");
//            customer.setPhone("555-555-4321");
//            customer.setAddress("123 Main St");
//            customer.setCity("New York City");
//            customer.setState("NY");
//            customer.setZipCode("10001");
//            customerDAO.create(customer);

        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
}
