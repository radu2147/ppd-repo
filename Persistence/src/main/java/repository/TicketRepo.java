package repository;

import domain.Artist;
import domain.Festival;
import domain.Ticket;
import jdbcUtils.JdbcUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class TicketRepo implements TicketRepoInterface {
    private final JdbcUtils dbUtils;

    private static final Logger logger= LogManager.getLogger();
    public TicketRepo(Properties props) {
        logger.info("Initializing TicketRepo with properties: {}",props);
        dbUtils=new JdbcUtils(props);
    }

    private void setPreparedStatement(Ticket entity, PreparedStatement preStmt) throws SQLException {
        preStmt.setLong(1,entity.getFestival().getId());
        preStmt.setDouble(2,entity.getPrice());
        String customer=entity.getClient();

        preStmt.setString(3,customer);
        preStmt.setInt(4,entity.getSeats());
    }

    @Override
    public Ticket add(Ticket entity) {
        logger.traceEntry("saving task {}",entity);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("insert into ticket (festival_id,price,customer,seats) values(?,?,?,?)")){
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
    public Ticket update(Ticket entity) {
        logger.traceEntry("update task {}",entity);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("update ticket set date=?,location=?,name=?,genre=?,seats=?,artist_id=? where id=?")){
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
    private Festival getFestival(Long id){
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        Festival festival=null;
        try(PreparedStatement preStmt=con.prepareStatement("select * from festival where id=?")){
            preStmt.setLong(1,id);
            try(ResultSet result=preStmt.executeQuery()){
                while(result.next()){
                    Long i=result.getLong("id");
                    Date date=result.getDate("date");
                    String location=result.getString("location");
                    String name=result.getString("name");
                    String genre=result.getString("genre");
                    Long seats=result.getLong("seats");
                    Long artist_id=result.getLong("artist_id");

                    festival= new Festival(i,date,location,name,genre,seats,getArtist(artist_id));
                }
            }
        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB"+ex);
        }
        logger.traceExit(festival);
        return festival;
    }
    private Ticket getEntityFromResultSet(ResultSet result) throws SQLException {
        Long i=result.getLong("id");
        Integer seats=result.getInt("seats");
        Long festival_id=result.getLong("festival_id");
        Double price=result.getDouble("price");
        String client=result.getString("customer");

        return new Ticket(i,getFestival(festival_id),price,client,seats);
    }

    @Override
    public Ticket getOne(Long id) {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        Ticket ticket=null;
        try(PreparedStatement preStmt=con.prepareStatement("select * from ticket where id=?")){
            preStmt.setLong(1,id);
            try(ResultSet result=preStmt.executeQuery()){
                while(result.next()){
                    ticket=getEntityFromResultSet(result);
                }
            }
        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB"+ex);
        }
        logger.traceExit(ticket);
        return ticket;
    }

    @Override
    public Iterable<Ticket> getAll() {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        List<Ticket> tickets=new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from ticket")){
            try(ResultSet result=preStmt.executeQuery()){
                while(result.next()){
                    Ticket ticket=getEntityFromResultSet(result);
                    tickets.add(ticket);
                }

            }
        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB"+ex);
        }
        logger.traceExit(tickets);
        return tickets;
    }

    @Override
    public Ticket delete(Long id) {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        List<Ticket> tickets=new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("delete from ticket where id=?")){
            preStmt.setLong(1,id);
            preStmt.executeUpdate();
        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB"+ex);
        }
        logger.traceExit(tickets);
        return null;
    }

    @Override
    public Long getSoldSeats(Long festival_id) {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        long sold= 0L;
        try(PreparedStatement preStmt=con.prepareStatement("select sum(seats) as Sold from ticket where festival_id=?;")){
            preStmt.setLong(1,festival_id);
            try(ResultSet result=preStmt.executeQuery()){
                while(result.next()){
                    sold= result.getLong("Sold");
                }
            }
        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB"+ex);
        }
        logger.traceExit(sold);
        return sold;
    }
}
