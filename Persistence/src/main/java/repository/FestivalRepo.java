package repository;

import domain.Artist;
import domain.Festival;
import jdbcUtils.JdbcUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class FestivalRepo implements FestivalRepoInterface {

    @Override
    public Iterable<Festival> findByDate(Date date) {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        List<Festival> festivals=new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from festival where date=?")){
            preStmt.setDate(1,date);
            try(ResultSet result=preStmt.executeQuery()){
                while(result.next()){
                    Festival festival=getEntityFromResultSet(result);
                    festivals.add(festival);
                }

            }
        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB"+ex);
        }
        logger.traceExit(festivals);
        return festivals;
    }

    private final JdbcUtils dbUtils;

    private static final Logger logger= LogManager.getLogger();
    public FestivalRepo(Properties props) {
        logger.info("Initializing FestivalRepo with properties: {}",props);
        dbUtils=new JdbcUtils(props);
    }

    private void setPreparedStatement(Festival entity,PreparedStatement preStmt) throws SQLException {
        preStmt.setDate(1,entity.getDate());
        preStmt.setString(2,entity.getLocation());
        preStmt.setString(3,entity.getName());
        preStmt.setString(4,entity.getGenre());
        preStmt.setLong(5,entity.getSeats());
        Artist artist=entity.getArtist();
        if(artist!=null)
            preStmt.setLong(6,artist.getId());
        else
            preStmt.setLong(6,-1);
    }

    @Override
    public Festival add(Festival entity) {
        logger.traceEntry("saving task {}",entity);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("insert into festival (date,location,name,genre,seats,artist_id) values(?,?,?,?,?,?)")){
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

    @Override
    public Festival update(Festival entity) {
        logger.traceEntry("update task {}",entity);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("update festival set date=?,location=?,name=?,genre=?,seats=?,artist_id=? where id=?")){
            setPreparedStatement(entity,preStmt);
            preStmt.setLong(7,entity.getId());
            int result=preStmt.executeUpdate();
            logger.trace("Updated {} instances",result);
        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB"+ex);
        }
        logger.traceExit();
        return null;
    }

    private Artist getArtist(Long id){
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        Artist artist=null;
        try(PreparedStatement preStmt=con.prepareStatement("select * from artist where id=?")){
            preStmt.setLong(1,id);
            try(ResultSet result=preStmt.executeQuery()){
                while(result.next()){
                    Long i=result.getLong("id");
                    String name=result.getString("name");
                    String gen=result.getString("genre");
                    artist=new Artist(i,name,gen);
                }
            }
        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB"+ex);
        }
        logger.traceExit(artist);
        return artist;
    }
    private Festival getEntityFromResultSet(ResultSet result) throws SQLException {
        Long i=result.getLong("id");
        Date date=result.getDate("date");
        String location=result.getString("location");
        String name=result.getString("name");
        String genre=result.getString("genre");
        Long seats=result.getLong("seats");
        Long artist_id=result.getLong("artist_id");

        return new Festival(i,date,location,name,genre,seats,getArtist(artist_id));
    }

    @Override
    public Festival getOne(Long id) {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        Festival festival=null;
        try(PreparedStatement preStmt=con.prepareStatement("select * from festival where id=?")){
            preStmt.setLong(1,id);
            try(ResultSet result=preStmt.executeQuery()){
                while(result.next()){
                    festival=getEntityFromResultSet(result);
                }
            }
        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB"+ex);
        }
        logger.traceExit(festival);
        return festival;
    }

    @Override
    public Iterable<Festival> getAll() {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        List<Festival> festivals=new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from festival")){
            try(ResultSet result=preStmt.executeQuery()){
                while(result.next()){
                    Festival festival=getEntityFromResultSet(result);
                    festivals.add(festival);
                }

            }
        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB"+ex);
        }
        logger.traceExit(festivals);
        return festivals;
    }

    @Override
    public Festival delete(Long id) {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        List<Festival> festivals=new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("delete from festival where id=?")){
            preStmt.setLong(1,id);
            preStmt.executeUpdate();
        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB"+ex);
        }
        logger.traceExit(festivals);
        return null;
    }
}
