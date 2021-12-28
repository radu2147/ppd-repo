package repository;

import domain.Employee;
import jdbcUtils.JdbcUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class EmployeeRepo implements EmployeeRepoInterface {
    private final JdbcUtils dbUtils;

    private static final Logger logger= LogManager.getLogger();
    public EmployeeRepo(Properties props) {
        logger.info("Initializing EmployeeRepo with properties: {}",props);
        dbUtils=new JdbcUtils(props);
    }
    @Override
    public Employee getOne(Long id) {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        Employee employee=null;
        try(PreparedStatement preStmt=con.prepareStatement("select * from employee where id=?")){
            preStmt.setLong(1,id);
            try(ResultSet result=preStmt.executeQuery()){
                while(result.next()){
                    Long i=result.getLong("id");
                    String name=result.getString("name");
                    employee=new Employee(i,name);
                }
            }
        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB"+ex);
        }
        logger.traceExit(employee);
        return employee;
    }

    @Override
    public Employee add(Employee entity) {
        logger.traceEntry("saving task {}",entity);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("insert into employee (name) values(?)")){
            preStmt.setString(1,entity.getName());
            int result=preStmt.executeUpdate();
            logger.trace("Saved {} instances",result);
        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB"+ex);
        }
        logger.traceExit();
        return null;
    }
}
