package database;

import databag.Fiets;
import database.connect.ConnectionManager;
import datatype.Standplaats;
import datatype.Status;
import exception.DBException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class FietsDB implements InterfaceFietsDB {

    @Override
    public Integer toevoegenFiets(Fiets fiets) throws DBException {
        if (fiets != null) {
            Integer primaryKey = null;
            // connectie tot stand brengen (en automatisch sluiten)
            try (Connection conn = ConnectionManager.getConnection();) { //opstellen van connectie met database
                // preparedStatement opstellen (en automatisch sluiten)
                try (PreparedStatement stmt = conn.
                        prepareStatement("insert into fiets("
                                + " registratienummer"
                                + " , status"
                                + " , standplaats"
                                + " , opmerkingen) "
                                + " values(?,?,?,?)",
                                Statement.RETURN_GENERATED_KEYS);) { //opstellen van sql code om een fiets toe te voegen
                                                                     // elke vraagteken word als een insert plaats gezien

                    // vermijdt een NullpointerException, 
                    // maar levert wel een DBException op, 
                    // aangezien registratienummer een verplicht veld is
                    if (fiets.getRegistratienummer() != null) {
                        stmt.setString(1, fiets.getRegistratienummer().toString());
                    } else {
                        stmt.setNull(1, java.sql.Types.NULL);
                    }
                    stmt.setString(2, fiets.getStatus().toString());
                    stmt.setString(3, fiets.getStandplaats().toString());
                    stmt.setString(4, fiets.getOpmerking());
                    stmt.execute();
                    //Waarden uit fiets object in sql Statement plaatsen zodat de fiets doorgegeven wordt naar de database
                    ResultSet generatedKeys = stmt.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        primaryKey = generatedKeys.getInt(1);//automatische sleutel genereren voor onze fiets zodat die een primaire sleutel heeft
                    }

                } catch (SQLException sqlEx) {
                    throw new DBException("SQL-exception in toevoegenFiets" + sqlEx);
                }
            } catch (SQLException sqlEx) {
                throw new DBException(
                        "SQL-exception in toevoegenFiets - connection" + sqlEx);
            }
            return primaryKey;
        } else {
            return null;
        }
    }

    @Override
    public void wijzigenToestandFiets(Integer regnr, Status status) throws DBException {
        if (regnr != null && status != null) {
            // connectie tot stand brengen (en automatisch sluiten)
            try (Connection conn = ConnectionManager.getConnection();) { //connectie aanbrengen met database
                // preparedStatement opstellen (en automatisch sluiten)
                try (PreparedStatement stmt = conn.
                        prepareStatement("update fiets "
                                + " set status =? "
                                + " where registratienummer = ?");) {//perpareStatment is een object met aan klaargemaakte sql statement die een connectie 
                                                                     //string heeft om de database te bereiken. Eens klaargezet kunnen we eventueel waarden gaan toevoen dankzij Preparedstatement object methodes
                    //sql statement klaarmaken 
                    stmt.setString(1, status.toString());
                    stmt.setInt(2, regnr);
                    //waarden in sql Statement gaan plaatsen
                    stmt.execute();//gedefineerde statement gaan uitvoeren
                } catch (SQLException sqlEx) {
                    throw new DBException("SQL-exception in wijzigenToestandFiets" + sqlEx);
                }
            } catch (SQLException sqlEx) {
                throw new DBException(
                        "SQL-exception in wijzigenToestandFiets - connection" + sqlEx);
            }
        }
    }

    @Override
    public void wijzigenOpmerkingFiets(Integer regnr, String opmerking) throws DBException {
        if (regnr != null && opmerking != null) {
            // connectie tot stand brengen (en automatisch sluiten)
            try (Connection conn = ConnectionManager.getConnection();) {
                // preparedStatement opstellen (en automatisch sluiten)
                try (PreparedStatement stmt = conn.//perpareStatment is een object met aan klaargemaakte sql statement die een connectie 
                                                   //string heeft om de database te bereiken. Eens klaargezet kunnen we eventueel waarden gaan toevoen dankzij Preparedstatement object methodes
                        prepareStatement("update fiets "
                                + " set opmerkingen =? "
                                + " where registratienummer = ?");) {
                    stmt.setString(1, opmerking);
                    stmt.setInt(2, regnr);
                    stmt.execute();
                } catch (SQLException sqlEx) {
                    throw new DBException("SQL-exception in wijzigenOpmerkingFiets" + sqlEx);
                }
            } catch (SQLException sqlEx) {
                throw new DBException(
                        "SQL-exception in wijzigenOpmerkingFiets - connection" + sqlEx);
            }
        }
    }

    public Fiets zoekFiets(Integer regnr) throws DBException, SQLException {
        Fiets fiets = null;
        if (regnr != null) {
            try (Connection conn = ConnectionManager.getConnection();) {
                //perpareStatment is een object met aan klaargemaakte sql statement die een connectie 
                //string heeft om de database te bereiken. Eens klaargezet kunnen we eventueel waarden gaan toevoen dankzij Preparedstatement object methodes
                try (PreparedStatement stmt = conn.prepareStatement("select registratienummer "
                        + ",status "
                        + ",opmerkingen "
                        +",standplaats "
                        + "from fiets "
                        + "where registratienummer = ?");) {
                    stmt.setString(1, regnr.toString());
                    stmt.execute();
                    try (ResultSet r = stmt.getResultSet()) {
                        if (r.next()) {
                            Fiets f = new Fiets();
                            f.setRegistratienummer(r.getInt("registratienummer"));

                            f.setStandplaats(Standplaats.valueOf(r.getString("standplaats")));

                            f.setStatus(Status.valueOf(r.getString("status")));

                            if (r.getString("opmerkingen") != null) {
                                f.setOpmerking(r.getString("opmerkingen"));
                            }

                            fiets = f;
                        }
                        return fiets;

                    } catch (SQLException e) {
                        throw new DBException("er is een fout bij zoekFiets" + e);
                    }

                } catch (SQLException e) {
                    throw new DBException("er is een fout bij zoekFiets" + e);
                }
            } catch (SQLException e) {
                throw new DBException("er is een fout bij zoekFiets" + e);
            }
        } else {
            return null;
        }
    }

    @Override
    public ArrayList<Fiets> zoekAlleFietsen() throws DBException {
        ArrayList<Fiets> Fietsen = new ArrayList();

        try (Connection conn = ConnectionManager.getConnection();) {
            try (PreparedStatement stmt = conn.prepareStatement("select registratienummer "//perpareStatment is een object met aan klaargemaakte sql statement die een connectie                                                                    
                    + ",status "                                                           //string heeft om de database te bereiken. Eens klaargezet kunnen we eventueel waarden gaan toevoen dankzij Preparedstatement object methodes
                    +", standplaats "
                    + ",opmerkingen "
                    + "from fiets");) {

                stmt.execute();
                try (ResultSet r = stmt.getResultSet()) { //ophalen van gekregen Resultaat en die in een ResultSet object gaan plaatsen
                    while (r.next()) { //fiets object aanmaken en waarde aatribueren aan object, dit gebeurd zolang onze resultset Fiets objecten bevat
                        Fiets f = new Fiets();
                        f.setRegistratienummer(r.getInt("registratienummer"));

                        f.setStandplaats(Standplaats.valueOf(r.getString("standplaats")));

                        f.setStatus(Status.valueOf(r.getString("status")));
                        
                        f.setOpmerking(r.getString("opmerkingen"));
                        Fietsen.add(f);
                    }
                    return Fietsen;

                } catch (SQLException e) {
                    throw new DBException("er is een fout bij zoekAlleFietsen" +e);
                }

            } catch (SQLException e) {
                throw new DBException("er is een fout bij zoekAlleFietsen" +e);
            }
        } catch (SQLException e) {
            throw new DBException("er is een fout bij zoekAlleFietsen" +e);
        }
    }
    
      public ArrayList<Fiets> zoekAlleBeschikbareFietsen() throws DBException {
        ArrayList<Fiets> Fietsen = new ArrayList();

        try (Connection conn = ConnectionManager.getConnection();) {
            try (PreparedStatement stmt = conn.prepareStatement("SELECT * "
                    + "FROM fiets "
                    + "WHERE status = 'actief' AND registratienummer NOT IN ( "
                    + "SELECT fiets_registratienummer "
                    + "FROM Rit "
                    + "Where eindtijd is null)");) {

                stmt.execute();
                try (ResultSet r = stmt.getResultSet()) { //ophalen van gekregen Resultaat en die in een ResultSet object gaan plaatsen
                    while (r.next()) { //fiets object aanmaken en waarde aatribueren aan object, dit gebeurd zolang onze resultset Fiets objecten bevat
                        Fiets f = new Fiets();
                        f.setRegistratienummer(r.getInt("registratienummer"));

                        f.setStandplaats(Standplaats.valueOf(r.getString("standplaats")));

                        f.setStatus(Status.valueOf(r.getString("status")));

                        f.setOpmerking(r.getString("opmerkingen"));
                        Fietsen.add(f);
                    }
                    return Fietsen;

                } catch (SQLException e) {
                    throw new DBException("er is een fout bij zoekAlleFietsen" + e);
                }

            } catch (SQLException e) {
                throw new DBException("er is een fout bij zoekAlleFietsen" + e);
            }
        } catch (SQLException e) {
            throw new DBException("er is een fout bij zoekAlleFietsen" + e);
        }
    }

}
