package repository;

import domain.Account;
import jdbcUtils.JdbcUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class AccountRepo implements AccountRepoInterface{
    private final JdbcUtils dbUtils;

    private static final Logger logger= LogManager.getLogger();

    public AccountRepo(Properties props) {
        logger.info("Initializing AccountRepo with properties: {}",props);
        dbUtils=new JdbcUtils(props);
    }

    @Override
    public Account getOne(String id) {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        Account account=null;
        try(PreparedStatement preStmt=con.prepareStatement("select * from account where username=?")){
            preStmt.setString(1,id);
            try(ResultSet result=preStmt.executeQuery()){
                while(result.next()){
                    String username=result.getString("username");
                    String password=result.getString("password");
                    String name=result.getString("name");
                    account=new Account(username,password);
                }
            }
        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB"+ex);
        }
        logger.traceExit(account);
        return account;
    }

    @Override
    public Iterable<Account> getAll() {
        return null;
    }

    @Override
    public Account add(Account entity) {
        logger.traceEntry("saving task {}",entity);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("insert into account (username,password,name) values(?,?,?)")){
            preStmt.setString(1,entity.getUsername());
            preStmt.setString(2,entity.getPassword());
            preStmt.setString(3,entity.getName());
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
    public Account delete(String s) {
        return null;
    }

    @Override
    public Account update(Account entity) {
        return null;
    }
}
