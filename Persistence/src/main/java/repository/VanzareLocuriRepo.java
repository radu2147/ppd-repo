package repository;

import domain.Spectacol;
import domain.VanzareLocuri;
import jdbcUtils.JdbcUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public class VanzareLocuriRepo implements VanzareLocuriRepoInterface{


    private final JdbcUtils dbUtils;

    private static final Logger logger= LogManager.getLogger();
    public VanzareLocuriRepo(Properties props) {
        logger.info("Initializing FestivalRepo with properties: {}",props);
        dbUtils=new JdbcUtils(props);
    }

    private void setPreparedStatement(VanzareLocuri entity, PreparedStatement preStmt) throws SQLException {
        preStmt.setLong(1,entity.getVanzare().getId());
        preStmt.setInt(2,entity.getLoc());
    }

    @Override
    public VanzareLocuri add(VanzareLocuri entity) {
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("insert into vanzari_locuri (id_vanzare, loc) values(?,?)")){
            setPreparedStatement(entity,preStmt);
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
