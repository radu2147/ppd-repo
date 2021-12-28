package repository;

import domain.Customer;
import jdbcUtils.JdbcUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class CustomerRepo implements CustomerRepoInterface{
    private final JdbcUtils dbUtils;

    private static final Logger logger= LogManager.getLogger();
    public CustomerRepo(Properties props) {
        logger.info("Initializing CustomerRepo with properties: {}",props);
        dbUtils=new JdbcUtils(props);
    }

    @Override
    public Customer add(Customer entity) {
        logger.traceEntry("saving task {}",entity);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("insert into customer (name,address) values(?,?)")){
            preStmt.setString(1,entity.getName());
            preStmt.setString(2,entity.getAddress());
            int result=preStmt.executeUpdate();
            logger.trace("Saved {} instances",result);
        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB"+ex);
        }
        logger.traceExit();
        return null;
    }

    @Override
    public Customer update(Customer entity) {
        logger.traceEntry("update task {}",entity);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("update customer set name=?,address=? where id=?")){
            preStmt.setString(1,entity.getName());
            preStmt.setString(2,entity.getAddress());
            preStmt.setLong(3,entity.getId());
            int result=preStmt.executeUpdate();
            logger.trace("Updated {} instances",result);
        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB"+ex);
        }
        logger.traceExit();
        return null;
    }

    @Override
    public Customer getOne(Long id) {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        Customer customer=null;
        try(PreparedStatement preStmt=con.prepareStatement("select * from customer where id=?")){
            preStmt.setLong(1,id);
            try(ResultSet result=preStmt.executeQuery()){
                while(result.next()){
                    Long i=result.getLong("id");
                    String name=result.getString("name");
                    String address=result.getString("address");
                    customer=new Customer(i,name,address);
                }
            }
        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB"+ex);
        }
        logger.traceExit(customer);
        return customer;
    }

    @Override
    public Iterable<Customer> getAll() {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        List<Customer> customers=new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from customer")){
            try(ResultSet result=preStmt.executeQuery()){
                while(result.next()){
                    Long id=result.getLong("id");
                    String name=result.getString("name");
                    String address=result.getString("address");
                    Customer customer=new Customer(id,name,address);
                    customers.add(customer);
                }
            }
        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB"+ex);
        }
        logger.traceExit(customers);
        return customers;
    }

    @Override
    public Customer delete(Long id) {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        List<Customer> customers=new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("delete from customer where id=?")){
            preStmt.setLong(1,id);
            preStmt.executeUpdate();
        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB"+ex);
        }
        logger.traceExit(customers);
        return null;
    }
}
