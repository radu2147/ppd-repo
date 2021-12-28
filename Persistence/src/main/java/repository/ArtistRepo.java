package repository;

import domain.Sala;
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

public class ArtistRepo implements ArtistRepoInterface{

    private final JdbcUtils dbUtils;

    private static final Logger logger= LogManager.getLogger();
    public ArtistRepo(Properties props) {
        logger.info("Initializing ArtistRepo with properties: {}",props);
        dbUtils=new JdbcUtils(props);
    }

    @Override
    public Sala add(Sala entity) {
        logger.traceEntry("saving task {}",entity);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("insert into sala (seats) values(?)")){
            preStmt.setInt(1,entity.getSeats());
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
    public Sala update(Sala entity) {
        logger.traceEntry("update task {}",entity);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("update sala set seats=? where id=?")){
            preStmt.setInt(1,entity.getSeats());
            preStmt.setLong(2,entity.getId());
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
    public Sala getOne(Long id) {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        Sala artist=null;
        try(PreparedStatement preStmt=con.prepareStatement("select * from sala where id=?")){
            preStmt.setLong(1,id);
            try(ResultSet result=preStmt.executeQuery()){
                while(result.next()){
                    Long i = result.getLong("id");
                    int seats = result.getInt("seats");
                    artist = new Sala(i,seats);
                }
            }
        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB"+ex);
        }
        logger.traceExit(artist);
        return artist;
    }

    @Override
    public Iterable<Sala> getAll() {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        List<Sala> artists=new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from sala")){
            try(ResultSet result=preStmt.executeQuery()){
                while(result.next()){
                    Long id=result.getLong("id");
                    int seats = result.getInt("seats");
                    Sala artist=new Sala(id,seats);
                    artists.add(artist);
                }
            }
        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB"+ex);
        }
        logger.traceExit(artists);
        return artists;
    }

    @Override
    public Sala delete(Long id) {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        List<Sala> artists=new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("delete from sala where id=?")){
            preStmt.setLong(1,id);
            preStmt.executeUpdate();
        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB"+ex);
        }
        logger.traceExit(artists);
        return null;
    }
}
