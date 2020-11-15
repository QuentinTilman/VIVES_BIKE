/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package extra;

import databag.Rit;
import databag.Fiets;
import databag.Lid;
import database.FietsDB;
import database.RitDB;
import database.LidDB;
import database.connect.ConnectionManager;
import exception.ApplicationException;
import exception.DBException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Katrien.Deleu
 */
public class Removals {

    /**
     * Verwijdert alle klanten en hun rekeningen uit de database, zonder enige
     * controle
     *
     * @throws exception.DBException Exception die duidt op een verkeerde
     * installatie van de database of een fout in de query.
     * @throws exception.ApplicationException wordt gegooid wanneer het
     * rekeningnummer ongeldig is
     */
    public static void removeAll() throws DBException, ApplicationException {
        // alle klanten verwijderen

        LidDB ldb = new LidDB();
        RitDB rdb = new RitDB();
        FietsDB fdb = new FietsDB();

        ArrayList<Rit> ritten = rdb.zoekAlleRitten();
        for (Rit r : ritten) {
            removeRit(r.getRitID());
        }
        
        ArrayList<Lid> Leden = ldb.zoekAlleLeden();
        for (Lid l : Leden) {
            // per klant alle rekeningen verwijderen

            removeKlant(String.valueOf(l.getRijksregisternummer()));
        }
        
        ArrayList<Fiets> Fietsen = fdb.zoekAlleFietsen();
        for (Fiets f : Fietsen)
        {
            removeFiets(f.getRegistratienummer());
        }
    }

    /**
     * Verwijdert de opgegeven klant uit de database, zonder enige controle
     *
     * @param id id van de klant die verwijderd moet worden
     * @throws exception.DBException Exception die duidt op een verkeerde
     * installatie van de database of een fout in de query.
     */
    public static void removeKlant(String id) throws DBException {

        // connectie tot stand brengen (en automatisch sluiten)
        try (Connection conn = ConnectionManager.getConnection();) {
            // preparedStatement opstellen (en automtisch sluiten)
            try (PreparedStatement stmt = conn.prepareStatement(
                    "delete from lid where rijksregisternummer = ?");) {
                stmt.setString(1, id);
                // execute voert elke sql-statement uit, executeQuery enkel de select
                stmt.execute();
            } catch (SQLException sqlEx) {
                throw new DBException("SQL-exception in removeLid - statement" + sqlEx);
            }
        } catch (SQLException sqlEx) {
            throw new DBException(
                    "SQL-exception in removeLid - connection" + sqlEx);
        }
    }

    /**
     * Verwijdert de rekening uit de database zonder enige controle
     *
     * @param rekeningnummer rekeningnummer van de rekening die verwijderd moet
     * worden
     * @throws exception.DBException Exception die duidt op een verkeerde
     * installatie van de database of een fout in de query.
     */
    public static void removeRit(int id) throws DBException {

        // connectie tot stand brengen (en automatisch sluiten)
        try (Connection conn = ConnectionManager.getConnection();) {
            // preparedStatement opstellen (en automtisch sluiten)
            try (PreparedStatement stmt = conn.prepareStatement(
                    "delete from rit where id = ?");) {
                stmt.setInt(1, id);
                // execute voert elke sql-statement uit, executeQuery enkel de select
                stmt.execute();
            } catch (SQLException sqlEx) {
                throw new DBException("SQL-exception in removeRit - statement" + sqlEx);
            }
        } catch (SQLException sqlEx) {
            throw new DBException(
                    "SQL-exception in removeRit - connection" + sqlEx);
        }
    }
    
    public static void removeFiets(int id) throws DBException
    {
        // connectie tot stand brengen (en automatisch sluiten)
        try (Connection conn = ConnectionManager.getConnection();) {
            // preparedStatement opstellen (en automtisch sluiten)
            try (PreparedStatement stmt = conn.prepareStatement(
                    "delete from fiets where registratienummer = ?");) {
                stmt.setInt(1, id);
                // execute voert elke sql-statement uit, executeQuery enkel de select
                stmt.execute();
            } catch (SQLException sqlEx) {
                throw new DBException("SQL-exception in removeFiets - statement" + sqlEx);
            }
        } catch (SQLException sqlEx) {
            throw new DBException(
                    "SQL-exception in removeFiets - connection" + sqlEx);
        }
    }

}
