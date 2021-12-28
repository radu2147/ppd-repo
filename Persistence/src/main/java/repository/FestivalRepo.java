package repository;

import domain.Sala;
import domain.Spectacol;
import jdbcUtils.JdbcUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class FestivalRepo implements FestivalRepoInterface {

    @Override
    public Iterable<Spectacol> findByDate(Date date) {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        List<Spectacol> spectacols =new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from festival where date=?")){
            preStmt.setDate(1,date);
            try(ResultSet result=preStmt.executeQuery()){
                while(result.next()){
                    Spectacol spectacol =getEntityFromResultSet(result);
                    spectacols.add(spectacol);
                }

            }
        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB"+ex);
        }
        logger.traceExit(spectacols);
        return spectacols;
    }

    private final JdbcUtils dbUtils;

    private static final Logger logger= LogManager.getLogger();
    public FestivalRepo(Properties props) {
        logger.info("Initializing FestivalRepo with properties: {}",props);
        dbUtils=new JdbcUtils(props);
    }

    private void setPreparedStatement(Spectacol entity, PreparedStatement preStmt) throws SQLException {
        preStmt.setDate(1,entity.getDate());
        preStmt.setString(2,entity.getName());
        preStmt.setLong(3,entity.getSold());
        preStmt.setLong(4,entity.getPriceVanzare());
    }

    @Override
    public Spectacol add(Spectacol entity) {
        logger.traceEntry("saving task {}",entity);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("insert into festival (date,name,sold,priceVanzare) values(?,?,?,?)")){
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
    public Spectacol update(Spectacol entity) {
        logger.traceEntry("update task {}",entity);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("update festival set date=?,name=?,sold=?,priceVanzare=? where id=?")){
            setPreparedStatement(entity,preStmt);
            preStmt.setLong(5,entity.getId());
            int result=preStmt.executeUpdate();
            logger.trace("Updated {} instances",result);
        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB"+ex);
        }
        logger.traceExit();
        return null;
    }
    private Spectacol getEntityFromResultSet(ResultSet result) throws SQLException {
        Long i=result.getLong("id");
        Date date=result.getDate("date");
        String name=result.getString("name");
        Long sold=result.getLong("sold");
        Long priceVanzare = result.getLong("priceVanzare");

        return new Spectacol(i,date,name,sold, priceVanzare, null);
    }

    @Override
    public Spectacol getOne(Long id) {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        Spectacol spectacol =null;
        try(PreparedStatement preStmt=con.prepareStatement("select * from festival where id=?")){
            preStmt.setLong(1,id);
            try(ResultSet result=preStmt.executeQuery()){
                while(result.next()){
                    spectacol =getEntityFromResultSet(result);
                }
            }
        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB"+ex);
        }
        logger.traceExit(spectacol);
        return spectacol;
    }

    @Override
    public Iterable<Spectacol> getAll() {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        List<Spectacol> spectacols =new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from festival")){
            try(ResultSet result=preStmt.executeQuery()){
                while(result.next()){
                    Spectacol spectacol =getEntityFromResultSet(result);
                    spectacols.add(spectacol);
                }

            }
        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB"+ex);
        }
        logger.traceExit(spectacols);
        return spectacols;
    }

    @Override
    public Spectacol delete(Long id) {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        List<Spectacol> spectacols =new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("delete from festival where id=?")){
            preStmt.setLong(1,id);
            preStmt.executeUpdate();
        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB"+ex);
        }
        logger.traceExit(spectacols);
        return null;
    }
}
