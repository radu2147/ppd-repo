package repository;

import domain.Sala;
import domain.Spectacol;
import domain.Vanzare;
import jdbcUtils.JdbcUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class VanzareRepo implements VanzareRepoInterface {
    private final JdbcUtils dbUtils;

    private static final Logger logger= LogManager.getLogger();
    public VanzareRepo(Properties props) {
        logger.info("Initializing VanzareRepo with properties: {}",props);
        dbUtils=new JdbcUtils(props);
    }

    private void setPreparedStatement(Vanzare entity, PreparedStatement preStmt) throws SQLException {
        preStmt.setLong(1,entity.getFestival().getId());
        preStmt.setDate(2,entity.getDate());
        preStmt.setLong(3,entity.getSuma());
    }

    @Override
    public Vanzare add(Vanzare entity) {
        logger.traceEntry("saving task {}",entity);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("insert into vanzari (id_spectacol,date, suma) values(?,?, ?)")){
            setPreparedStatement(entity,preStmt);
            int result=preStmt.executeUpdate();
            logger.trace("Saved {} instances",result);
            try (ResultSet generatedKeys = preStmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setId(generatedKeys.getLong(1));
                }
                else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB"+ex);
        }
        logger.traceExit();
        return entity;
    }

    @Override
    public Vanzare update(Vanzare entity) {
        logger.traceEntry("update task {}",entity);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("update Vanzare set date=?,destival_id=? where id=?")){
            setPreparedStatement(entity,preStmt);
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

    private Spectacol getFestival(Long id){
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        Spectacol spectacol =null;
        try(PreparedStatement preStmt=con.prepareStatement("select * from festival where id=?")){
            preStmt.setLong(1,id);
            try(ResultSet result=preStmt.executeQuery()){
                while(result.next()){
                    Long i=result.getLong("id");
                    Date date=result.getDate("date");
                    String name=result.getString("name");
                    Long sold=result.getLong("sold");
                    Long priceVanzare = result.getLong("priceVanzare");
                    Long salaId = result.getLong("sala_id");

                    spectacol = new Spectacol(i,date,name,sold, priceVanzare, new Sala(salaId, 0));
                }
            }
        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB"+ex);
        }
        logger.traceExit(spectacol);
        return spectacol;
    }
    private Vanzare getEntityFromResultSet(ResultSet result) throws SQLException {
        Long i=result.getLong("id");
        Long festival_id=result.getLong("festival_id");
        var date=result.getDate("date");

        return new Vanzare(i,getFestival(festival_id), date);
    }

    @Override
    public Vanzare getOne(Long id) {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        Vanzare vanzare =null;
        try(PreparedStatement preStmt=con.prepareStatement("select * from Vanzare where id=?")){
            preStmt.setLong(1,id);
            try(ResultSet result=preStmt.executeQuery()){
                while(result.next()){
                    vanzare =getEntityFromResultSet(result);
                }
            }
        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB"+ex);
        }
        logger.traceExit(vanzare);
        return vanzare;
    }

    @Override
    public Iterable<Vanzare> getAll() {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        List<Vanzare> vanzares =new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from Vanzare")){
            try(ResultSet result=preStmt.executeQuery()){
                while(result.next()){
                    Vanzare vanzare =getEntityFromResultSet(result);
                    vanzares.add(vanzare);
                }

            }
        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB"+ex);
        }
        logger.traceExit(vanzares);
        return vanzares;
    }

    @Override
    public Vanzare delete(Long id) {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        List<Vanzare> vanzares =new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("delete from Vanzare where id=?")){
            preStmt.setLong(1,id);
            preStmt.executeUpdate();
        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB"+ex);
        }
        logger.traceExit(vanzares);
        return null;
    }

    @Override
    public Long getSoldSeats(Long festival_id) {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        long sold= 0L;
        try(PreparedStatement preStmt=con.prepareStatement("select sum(seats) as Sold from Vanzare where festival_id=?;")){
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

    @Override
    public Iterable<Vanzare> getBySpectacolId(Long id) {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        List<Vanzare> vanzare = new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from Vanzare where id_spectacol=?")){
            preStmt.setLong(1,id);
            try(ResultSet result=preStmt.executeQuery()){
                while(result.next()){
                    vanzare.add(getEntityFromResultSet(result));
                }
            }
        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB"+ex);
        }
        logger.traceExit(vanzare);
        return vanzare;
    }
}
