package database;

import databag.Lid;
import database.connect.ConnectionManager;
import datatype.Geslacht;
import datatype.Rijksregisternummer;
import exception.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LidDB implements InterfaceLidDB {

    @Override
    public void toevoegenLid(Lid lid) throws DBException, ApplicationException {
        if (lid != null) {
            String primaryKey = null;

            // connectie tot stand brengen (en automatisch sluiten)
            try (Connection conn = ConnectionManager.getConnection();) {
                // preparedStatement opstellen (en automatisch sluiten)
                try (PreparedStatement stmt = conn.prepareStatement(
                        "insert into lid(rijksregisternummer"
                        + " , voornaam"
                        + " , naam"
                        + " , geslacht"
                        + " , telnr"
                        + " , emailadres"
                        + " , start_lidmaatschap"
                        + " , opmerkingen"
                        + " ) values(?,?,?,?,?,?,?,?)",
                        Statement.RETURN_GENERATED_KEYS);) {
                    if (lid.getRijksregisternummer() != null) {
                        stmt.setString(1, lid.getRijksregisternummer());
                    } else {
                        stmt.setNull(1, java.sql.Types.NULL);
                    }

                    stmt.setString(2, lid.getVoornaam());
                    stmt.setString(3, lid.getNaam());
                    stmt.setString(4, lid.getGeslacht().name());
                    stmt.setString(5, lid.getTelnr());
                    stmt.setString(6, lid.getEmailadres());
                    stmt.setString(7, lid.getStart_lidmaatschap().toString());

                    stmt.setString(8, lid.getOpmerkingen());
                    stmt.execute();

                    ResultSet generatedKeys = stmt.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        Rijksregisternummer r = new Rijksregisternummer(lid.getRijksregisternummer());
                        primaryKey = r.toString();
                    }
                } catch (SQLException sqlEx) {
                    throw new DBException("SQL-exception in toevoegenLid "
                            + "- statement" + sqlEx);
                }
            } catch (SQLException sqlEx) {
                throw new DBException("SQL-exception in toeviegenLid "
                        + "- connection" + sqlEx);
            }
        }

    }

    @Override
    public void wijzigenLid(Lid lid) throws DBException {
        if (lid != null) {
            try (Connection conn = ConnectionManager.getConnection();) {
                try (PreparedStatement stmt = conn.prepareStatement("update Lid" + " set voornaam = ? ," + " naam = ? ," + " telnr = ? ," + " emailadres = ? ," + " opmerkingen = ? " + "where rijksregisternummer = ?");) {

                    stmt.setString(1, lid.getVoornaam());
                    stmt.setString(2, lid.getNaam());
                    stmt.setString(3, lid.getTelnr());
                    stmt.setString(4, lid.getEmailadres());
                    stmt.setString(5, lid.getOpmerkingen());
                    stmt.setString(6, lid.getRijksregisternummer());
                    stmt.execute();

                } catch (SQLException sqlEx) {
                    throw new DBException("SQL-exception in WijzigenLid" + sqlEx);
                }

            } catch (SQLException sqlEx) {
                throw new DBException(
                        "SQL-exception in WijzigenLid - connection" + sqlEx);
            }
        }

    }

    @Override
    public void uitschrijvenLid(String rr) throws DBException {
        if (rr != null) {
            try (Connection conn = ConnectionManager.getConnection();) {
                try (PreparedStatement stmt = conn.prepareStatement(
                        "update Lid"
                        + " set einde_lidmaatschap = ? "
                        + " where rijksregisternummer = ? ");) {
                    System.out.println(Date.valueOf(LocalDate.now()));
                    stmt.setDate(1, Date.valueOf(LocalDate.now()));
                    stmt.setString(2, rr);
                    stmt.execute();

                } catch (SQLException sqlex) {
                    throw new DBException("SQL-exception in uitschrijvenLid " + sqlex);
                }

            } catch (SQLException sqlex) {
                throw new DBException("SQL-exception in uitschrijvenLid " + sqlex);
            }
        }

    }

    @Override
    public Lid zoekLid(String rijksregisternummer) throws DBException, SQLException, ApplicationException {
        if (rijksregisternummer != null) {
            Lid returnLid = null;
            try (Connection conn = ConnectionManager.getConnection();) {
                try (PreparedStatement stmt = conn.prepareStatement(
                        "select rijksregisternummer "
                        + " , naam "
                        + ", voornaam"
                        + ", geslacht"
                        + " , telnr"
                        + ", emailadres "
                        + " , start_lidmaatschap "
                        + " , einde_lidmaatschap "
                        + ", opmerkingen "
                        + "from Lid"
                        + " where rijksregisternummer = ? ");) {
                    stmt.setString(1, rijksregisternummer);
                    stmt.execute();
                    try (ResultSet r = stmt.getResultSet()) {
                        // van de klant uit de database een Klant-object maken
                        Lid l = new Lid();

                        // er werd een klant gevonden
                        if (r.next()) {

                            Rijksregisternummer rr = new Rijksregisternummer(r.getString("rijksregisternummer"));

                            // er werd een klant gevonden
                            l.setRijksregisternummer(rr);
                            l.setNaam(r.getString("naam"));
                            l.setVoornaam(r.getString("voornaam"));
                            l.setGeslacht(Geslacht.valueOf(r.getString("geslacht")));
                            l.setTelnr(r.getString("telnr"));
                            l.setEmailadres(r.getString("emailadres"));
                            l.setStart_lidmaatschap(LocalDate.parse(r.getString("start_lidmaatschap")));
                            if (r.getString("einde_lidmaatschap") != null) {
                                l.setEinde_lidmaatschap(LocalDate.parse(r.getString("einde_lidmaatschap")));
                            }

                            if (r.getString("opmerkingen") != null) {
                                l.setOpmerkingen(r.getString("opmerkingen"));
                            }
                            returnLid = l;
                        }

                        return returnLid;

                    } catch (SQLException sqlEx) {
                        throw new DBException("SQL-exception in zoekLid "
                                + "- resultset" + sqlEx);
                    }

                } catch (SQLException sqlex) {
                    throw new DBException("SQL-exception in zoekLid " + sqlex);
                }
            }
        } else {
            return null;
        }

    }

    @Override
    public ArrayList<Lid> zoekAlleLeden() throws DBException, ApplicationException {
        ArrayList<Lid> Leden = new ArrayList<>();
        // connectie tot stand brengen (en automatisch sluiten)
        try (Connection conn = ConnectionManager.getConnection();) {

            // preparedStatement opstellen (en automatisch sluiten)
            try (PreparedStatement stmt = conn.
                    prepareStatement(
                            "select rijksregisternummer "
                            + " , naam"
                            + " , voornaam"
                            + " , geslacht"
                            + " , telnr"
                            + " , emailadres"
                            + " , start_lidmaatschap "
                            + ", opmerkingen "
                            + " from Lid "
                            + " order by naam "
                            + ", voornaam");) {
                stmt.execute();
                // result opvragen (en automatisch sluiten)
                try (ResultSet r = stmt.getResultSet()) {
                    // van alle klanten uit de database Klant-objecten maken
                    // en in een lijst steken
                    while (r.next()) {
                        Lid l = new Lid();
                        Rijksregisternummer rr = new Rijksregisternummer(r.getString("rijksregisternummer"));
                        // geen controle op null, id moet ingevuld zijn in DB
                        l.setRijksregisternummer(rr);
                        l.setNaam(r.getString("naam"));
                        l.setVoornaam(r.getString("voornaam"));
                        l.setTelnr(r.getString("telnr"));
                        l.setEmailadres(r.getString("emailadres"));
                        l.setStart_lidmaatschap(LocalDate.parse(r.getString("start_lidmaatschap")));

                        if (r.getString("opmerkingen") != null) {
                            l.setOpmerkingen(r.getString("opmerkingen"));
                        }

                        Leden.add(l);
                    }
                    return Leden;
                } catch (SQLException sqlEx) {
                    throw new DBException(
                            "SQL-exception in zoekAlleLeden - resultset" + sqlEx);
                }
            } catch (SQLException sqlEx) {
                throw new DBException(
                        "SQL-exception in zoekAlleLeden - statement" + sqlEx);
            }
        } catch (SQLException sqlEx) {
            throw new DBException(
                    "SQL-exception in zoekAlleLeden - connection" + sqlEx);
        }
    }

    public ArrayList<Lid> zoekAlleBeschikbareLeden() throws DBException {
        ArrayList<Lid> Leden = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();) {
            try (PreparedStatement stmt = conn.prepareStatement("SELECT * "
                    + "FROM lid "
                    + "WHERE einde_lidmaatschap is null AND rijksregisternummer NOT IN ( "
                    + "SELECT lid_rijksregisternummer "
                    + "FROM rit "
                    +" where eindtijd is null"
                    + ")");) {

                stmt.execute();
                try (ResultSet r = stmt.getResultSet()) { //ophalen van gekregen Resultaat en die in een ResultSet object gaan plaatsen
                    while (r.next()) { //fiets object aanmaken en waarde aatribueren aan object, dit gebeurd zolang onze resultset Fiets objecten bevat
                        Lid l = new Lid();
                        Rijksregisternummer rr = new Rijksregisternummer(r.getString("rijksregisternummer"));
                        l.setRijksregisternummer(rr);
                        l.setNaam(r.getString("naam"));
                        l.setVoornaam(r.getString("voornaam"));
                        l.setGeslacht(Geslacht.valueOf(r.getString("geslacht")));
                        l.setTelnr(r.getString("telnr"));
                        l.setEmailadres(r.getString("emailadres"));
                        l.setStart_lidmaatschap(r.getTimestamp("start_lidmaatschap").toLocalDateTime().toLocalDate());
                        l.setOpmerkingen(r.getString("opmerkingen"));
                        System.out.println(l);
                        Leden.add(l);
                    }
                    return Leden;

                } catch (ApplicationException ex) {
                   throw new DBException("er is een fout bij zoekAlleBeschikbareLeden" + ex.getMessage());
                }

            } catch (SQLException e) {
                throw new DBException("er is een fout bij zoekAlleBeschikbareLeden" + e.getMessage());
            }
        } catch (SQLException e) {
            throw new DBException("er is een fout bij zoekAlleBeschikbareLeden" + e.getMessage());
        }

    }
}
