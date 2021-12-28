package repository;

import domain.Artist;
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
    public Artist add(Artist entity) {
        logger.traceEntry("saving task {}",entity);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("insert into artist (name,genre) values(?,?)")){
            preStmt.setString(1,entity.getName());
            preStmt.setString(2,entity.getGenre());
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
    public Artist update(Artist entity) {
        logger.traceEntry("update task {}",entity);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("update artist set name=?,genre=? where id=?")){
            preStmt.setString(1,entity.getName());
            preStmt.setString(2,entity.getGenre());
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
    public Iterable<Artist> findArtistByGenre(String genre) {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        List<Artist> artists=new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from artist where genre=?")){
            preStmt.setString(1,genre);
            try(ResultSet result=preStmt.executeQuery()){
                while(result.next()){
                    Long id=result.getLong("id");
                    String name=result.getString("name");
                    String gen=result.getString("genre");
                    Artist artist=new Artist(id,name,gen);
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
    public Artist getOne(Long id) {
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

    @Override
    public Iterable<Artist> getAll() {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        List<Artist> artists=new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from artist")){
            try(ResultSet result=preStmt.executeQuery()){
                while(result.next()){
                    Long id=result.getLong("id");
                    String name=result.getString("name");
                    String gen=result.getString("genre");
                    Artist artist=new Artist(id,name,gen);
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
    public Artist delete(Long id) {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        List<Artist> artists=new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("delete from artist where id=?")){
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
