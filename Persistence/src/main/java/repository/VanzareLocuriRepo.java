package repository;

import domain.Spectacol;
import domain.Vanzare;
import domain.VanzareLocuri;
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

    public List<Integer> getLocuri() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();

        List<Integer> locuri = new ArrayList<>();

        try(PreparedStatement preStmt = con.prepareStatement("select * from vanzari_locuri")){
            try(ResultSet result = preStmt.executeQuery()){
                while(result.next()){
                    Integer loc = result.getInt("loc");
                    locuri.add(loc);
                }

            }
        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }

        logger.traceExit(locuri);
        return locuri;
    }

    public Integer getNrLocuri() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();

        Integer locuri = 0;

        try(PreparedStatement preStmt = con.prepareStatement("select * from sala")){
            try(ResultSet result = preStmt.executeQuery()){
                while(result.next()){
                    locuri = result.getInt("locuri");
                }

            }
        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }

        logger.traceExit(locuri);
        return locuri;
    }

    public Vanzare updateLocuri(Integer locuri) {
        logger.traceEntry("update task {}", locuri);
        Connection con = dbUtils.getConnection();

        try(PreparedStatement preStmt = con.prepareStatement("update sala set locuri=?")){
            preStmt.setInt(1, locuri);
            int result = preStmt.executeUpdate();
            logger.trace("Updated {} instances", result);
        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }

        logger.traceExit();
        return null;
    }
}
