package database;

import databag.Fiets;
import databag.Rit;
import database.connect.ConnectionManager;
import datatype.Rijksregisternummer;
import datatype.Standplaats;
import datatype.Status;
import exception.ApplicationException;
import exception.DBException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.converter.LocalDateTimeStringConverter;

public class RitDB implements InterfaceRitDB {

    @Override
    public Integer toevoegenRit(Rit rit) throws DBException, SQLException {
        if (rit != null) {
            Integer primaryKey = null;

            try (Connection conn = ConnectionManager.getConnection()) {
                try (PreparedStatement stmt = conn.prepareStatement("insert into rit ("
                        + " id"
                        + " , starttijd"
                        + " , lid_rijksregisternummer"
                        + " , fiets_registratienummer)"
                        + "values (?,?,?,? )",
                        Statement.RETURN_GENERATED_KEYS);) {
                    int id = zoekAlleRitten().size()+1;
                    stmt.setInt(1, id );
                    stmt.setString(2, LocalDateTime.now().toString());
                    stmt.setString(3, rit.getLidRijksregisternummer());
                    stmt.setInt(4, rit.getFietsRegistratienummer());
                    stmt.execute();
                    primaryKey = id;

                  
                } catch (SQLException sqlEx) {
                    throw new DBException("SQL-exception in toevoegenRit" + sqlEx);
                } catch (ApplicationException ex) {
                    Logger.getLogger(RitDB.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (SQLException sqlEx) {
                throw new DBException(
                        "SQL-exception in toevoegenRit - connection" + sqlEx);
            }
            return primaryKey;
        } else {
            return null;
        }
    }

    @Override
    public void afsluitenRit(Rit rit) throws DBException {
        if (rit != null) {
            Integer primaryKey = rit.getRitID();

            try (Connection conn = ConnectionManager.getConnection()) {
                try (PreparedStatement stmt = conn.prepareStatement("Update rit "
                        + " set eindtijd = ? "
                        + " where id = ? ");) {

                    stmt.setString(1, LocalDateTime.now().toString());
                    stmt.setInt(2, primaryKey);
                    stmt.execute();

                } catch (SQLException sqlEx) {
                    throw new DBException("SQL-exception in afsluitenRit " + sqlEx);
                }
            } catch (SQLException sqlEx) {
                throw new DBException(
                        "SQL-exception in afsluitenRit - connection" + sqlEx);
            }
        }
    }
    

    @Override
    public ArrayList zoekAlleRitten() throws DBException, ApplicationException {
        ArrayList<Rit> ritten = new ArrayList();

        try (Connection conn = ConnectionManager.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement("select *"
                    + " from Rit");) {
                stmt.execute();

                try (ResultSet r = stmt.getResultSet()) {
                    while (r.next()) {
                        Rit rit = new Rit();
                        rit.setRitID(r.getInt("id"));
                        rit.setStarttijd(r.getTimestamp("starttijd").toLocalDateTime());
                        if (r.getTimestamp("eindtijd") != null) {
                            rit.setEindtijd(r.getTimestamp("eindtijd").toLocalDateTime());
                        }
                        Rijksregisternummer rr = new Rijksregisternummer(r.getString("lid_rijksregisternummer"));
                        rit.setLidRijksregisternummer(rr);
                        rit.setFietsRegistratienummer(r.getInt("fiets_registratienummer"));

                        ritten.add(rit);
                    }
                    return ritten;

                } catch (SQLException sqlEx) {
                    throw new DBException(
                            "SQL-exception in zoekAlleLeden - resultset" + sqlEx);
                }

            } catch (SQLException sqlEx) {
                throw new DBException("SQL-exception in statement" + sqlEx);
            }
        } catch (SQLException sqlEx) {
            throw new DBException(
                    "SQL-exception in zoekalleritten - connection" + sqlEx);
        }
    }

    @Override
    public Rit zoekRit(Integer ritID) throws DBException, ApplicationException {
        Rit rit = null;

        if (ritID != null) {
            try (Connection conn = ConnectionManager.getConnection()) {
                try (PreparedStatement stmt = conn.prepareStatement(" select *"
                        + " from Rit"
                        + " where id = ?");) {
                    stmt.setString(1, ritID.toString());
                    stmt.execute();

                    try (ResultSet r = stmt.getResultSet()) {
                        if (r.next()) {
                            rit = new Rit();
                            rit.setRitID(r.getInt("id"));
                            rit.setStarttijd(r.getTimestamp("starttijd").toLocalDateTime());
                            if (r.getTimestamp("eindtijd") != null) {
                                rit.setEindtijd(r.getTimestamp("eindtijd").toLocalDateTime());
                            }

                            Rijksregisternummer rr = new Rijksregisternummer(r.getString("lid_rijksregisternummer"));
                            rit.setLidRijksregisternummer(rr);
                            rit.setFietsRegistratienummer(r.getInt("fiets_registratienummer"));
                        }
                        return rit;
                    } catch (SQLException sqlEx) {
                        throw new DBException("SQL-exception in Resultset" + sqlEx);

                    }
                } catch (SQLException sqlEx) {
                    throw new DBException("SQL-exception in statement" + sqlEx);
                }
            } catch (SQLException sqlEx) {
                throw new DBException(
                        "SQL-exception in zoekrit - connection" + sqlEx);
            }
        } else {
            return null;
        }

    }

    @Override
    public int zoekEersteRitVanLid(String rr) throws DBException, ApplicationException {
        if (rr != null) {
            Rit rit = null;
            try (Connection conn = ConnectionManager.getConnection()) {
                try (PreparedStatement stmt = conn.prepareStatement("select * "
                        + " from rit "
                        + " where lid_rijksregisternummer = ? "
                        + " AND starttijd = ( select min(starttijd) from rit ) ");) {
                    stmt.setString(1, rr);
                    stmt.execute();

                    try (ResultSet r = stmt.getResultSet()) {
                        if (r.next()) {
                            rit = new Rit();
                            rit.setRitID(r.getInt("id"));
//                            rit.setPrijs(r.getBigDecimal(1));
                            rit.setStarttijd(r.getTimestamp("starttijd").toLocalDateTime());
                            if (r.getString("eindtijd") != null) {

                                rit.setEindtijd(r.getTimestamp("eindtijd").toLocalDateTime());
                            }

                            rit.setLidRijksregisternummer(new Rijksregisternummer(r.getString("lid_rijksregisternummer")));
                            rit.setFietsRegistratienummer(r.getInt("fiets_registratienummer"));
                        }

                        return rit.getRitID();

                    } catch (SQLException sqlEx) {
                        throw new DBException("SQL-exception in zoekEersteRitVanLid-rit object aanmaken" + sqlEx);
                    }

                } catch (SQLException sqlEx) {
                    throw new DBException("SQL-exception in zoekEersteRitVanLid-statement" + sqlEx);
                }
            } catch (SQLException sqlEx) {
                throw new DBException(
                        "SQL-exception in zoekEersteRitVanLid - connection3" + sqlEx);
            }
        } else {
            return 0;
        }

    }

    @Override
    public ArrayList zoekActieveRittenVanLid(String rr) throws Exception {
        ArrayList<Rit> ritten = new ArrayList();

        try (Connection conn = ConnectionManager.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(" select * "
                    + "from Rit "
                    + " where lid_rijksregisternummer = ? "
                    + " AND eindtijd is null ");) {
                stmt.setString(1, rr);
                stmt.execute();

                try (ResultSet r = stmt.getResultSet()) {
                    while (r.next()) {
                        Rit rit = new Rit();
                        rit.setRitID(r.getInt("id"));
                        rit.setStarttijd(r.getTimestamp("starttijd").toLocalDateTime());
                        Rijksregisternummer rn = new Rijksregisternummer(r.getString("lid_rijksregisternummer"));
                        rit.setLidRijksregisternummer(rn);
                        rit.setFietsRegistratienummer(r.getInt("fiets_registratienummer"));

                        ritten.add(rit);
                    }
                } catch (SQLException sqlEx) {
                    throw new DBException("SQL-exception in zoekActieveRittenVanLid" + sqlEx);

                }
                return ritten;

            } catch (SQLException sqlEx) {
                throw new DBException("SQL-exception in zoekActieveRittenVanLid" + sqlEx);
            }
        } catch (SQLException sqlEx) {
            throw new DBException(
                    "SQL-exception in zoekActieveRittenVanLid - connection" + sqlEx);
        }
    }

    @Override
    public ArrayList zoekActieveRittenVanFiets(Integer regnr) throws Exception {
        ArrayList ritten = new ArrayList();

        if (regnr != null) {
            try (Connection conn = ConnectionManager.getConnection()) {
                try (PreparedStatement stmt = conn.prepareStatement("select * "
                        + "from rit "
                        + " where fiets_registratienummer = ?"
                        + " AND eindtijd is null")) {
                    stmt.setString(1, regnr.toString());
                    stmt.execute();
                    try (ResultSet r = stmt.getResultSet()) {
                        while (r.next()) {
                            Rit rit = new Rit();
                            rit.setRitID(r.getInt("id"));
                            rit.setPrijs(r.getBigDecimal("prijs"));
                            rit.setStarttijd(r.getTimestamp("starttijd").toLocalDateTime());
                            rit.setEindtijd(null);
                            Rijksregisternummer rn = new Rijksregisternummer(r.getString("lid_rijksregisternummer"));
                            rit.setLidRijksregisternummer(rn);
                            rit.setFietsRegistratienummer(r.getInt("fiets_registratienummer"));

                            ritten.add(rit);

                        }
                        return ritten;
                    } catch (SQLException e) {
                        throw new DBException("fout opgelopen bij zoekActieveRitten" + e);
                    }
                } catch (SQLException e) {
                    throw new DBException("fout opgelopen bij zoekActieveRitten" + e);
                }

            } catch (SQLException e) {
                throw new DBException("fout opgelopen bij zoekActieveRitten" + e);
            }
        } else {
            return null;
        }

    }
}
