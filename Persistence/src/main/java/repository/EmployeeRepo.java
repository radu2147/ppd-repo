package repository;

import domain.Vanzare;
import domain.VanzareLocuri;
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
    public VanzareLocuri getOne(Long id) {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        VanzareLocuri vanzareLocuri =null;
        try(PreparedStatement preStmt=con.prepareStatement("select * from vanzare_locuri where id=?")){
            preStmt.setLong(1,id);
            try(ResultSet result=preStmt.executeQuery()){
                while(result.next()){
                    Long i=result.getLong("id");
                    Long vanzareId=result.getLong("vanzare_id");
                    int nrLocuri = result.getInt("seats");
                    vanzareLocuri =new VanzareLocuri(i, new Vanzare(vanzareId, null, null), nrLocuri);
                }
            }
        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB"+ex);
        }
        logger.traceExit(vanzareLocuri);
        return vanzareLocuri;
    }

    @Override
    public VanzareLocuri add(VanzareLocuri entity) {
        logger.traceEntry("saving task {}",entity);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("insert into vanzare_locuri (seats, vanzare_id) values(?, ?)")){
            preStmt.setInt(1,entity.getLoc());
            preStmt.setLong(2,entity.getVanzare().getId());
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
