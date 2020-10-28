package com.company.pollweb.data.dao;

import com.company.pollweb.data.models.Domanda;
import com.company.pollweb.data.proxy.DomandaProxy;
import com.company.pollweb.framework.data.DAO;
import com.company.pollweb.framework.data.DataException;
import com.company.pollweb.framework.data.DataLayer;
import org.json.JSONObject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.company.pollweb.utility.Serializer.StringToJSON;

public class DomandaDao_MySQL extends DAO implements DomandaDao{

    private PreparedStatement inserimento_domanda , domande_by_sondaggioID ,domanda_by_id , domande_ids_by_sondaggoID , modifica_domanda ;

    public DomandaDao_MySQL(DataLayer d) {
        super(d);
    }

    public void init() throws DataException {

        try {
            super.init();
            inserimento_domanda = connection.prepareStatement("INSERT INTO Domanda (sondaggio_id, testo, nota, obbligo, tipologia, vincoli, ordine) VALUES (?, ?, ?, ?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS);
            domande_by_sondaggioID = connection.prepareStatement("SELECT * FROM Domanda WHERE sondaggio_id=? ORDER BY ordine ASC;");
            domanda_by_id= connection.prepareStatement("SELECT * FROM Domanda WHERE ID=?;");
            domande_ids_by_sondaggoID = connection.prepareStatement("SELECT id FROM DOMANDA WHERE sondaggio_id=?;");
            modifica_domanda = connection.prepareStatement("UPDATE Domanda SET sondaggio_id=?,testo=?,nota=?,obbligo=?,tipologia=?,vincoli=?,ordine=? WHERE id=?;");
        } catch (SQLException ex) {
            throw new DataException("Errore durante l'inizializzazione del data layer internship tutor", ex);
        }
    }

    public void destroy() throws DataException {
        try {
            inserimento_domanda.close();
            domande_by_sondaggioID.close();
            domanda_by_id.close();
            domande_ids_by_sondaggoID.close();
            modifica_domanda.close();
        } catch (SQLException ex) {
            Logger.getLogger(SondaggioDao_MySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        super.destroy();
    }

    @Override
    public DomandaProxy creazioneDomanda() {
        return new DomandaProxy(getDataLayer());
    }

    @Override
    public DomandaProxy creazioneDomanda(ResultSet rs) throws DataException {
        try {
            DomandaProxy a = creazioneDomanda();
            if(rs.getInt("id") > 0) {
                a.setId(rs.getInt("id"));
            }
            a.setTesto(rs.getString("testo"));
            a.setNota(rs.getString("nota"));
            a.setObbligo(rs.getInt("obbligo"));
            a.setTipologia(rs.getString("tipologia"));
            a.setVincoli(StringToJSON(rs.getString("vincoli")));
            a.setOrdine(rs.getInt("ordine"));
            return a;
        } catch (SQLException ex) {
            throw new DataException("Incapace di creare domanda dal ResultSet", ex);
        }
    }

    @Override
    public void salvaDomanda(Domanda d) throws DataException {
        int id = d.getId();
        try {
            if (d.getId() > 0) {
                if (d instanceof DomandaProxy && ((DomandaProxy) d).isDirty()) {
                    return;
                }
                modifica_domanda.setInt(1, d.getSondaggio_id());
                modifica_domanda.setString(2, d.getTesto());
                modifica_domanda.setString(3, d.getNota());
                modifica_domanda.setInt(4, d.getObbligo());
                modifica_domanda.setString(5, d.getTipologia());
                String stringToBeInserted = JSONObject.valueToString(d.getVincoli());
                modifica_domanda.setString(6,stringToBeInserted);
                modifica_domanda.setInt(7,d.getOrdine());
                modifica_domanda.setInt(8,d.getId());
                modifica_domanda.executeUpdate();
            } else {
                inserimento_domanda.setInt(1, d.getSondaggio_id());
                inserimento_domanda.setString(2, d.getTesto());
                inserimento_domanda.setString(3, d.getNota());
                inserimento_domanda.setInt(4, d.getObbligo());
                inserimento_domanda.setString(5, d.getTipologia());
                String stringToBeInserted = JSONObject.valueToString(d.getVincoli());
                inserimento_domanda.setString(6,stringToBeInserted);
                inserimento_domanda.setInt(7,d.getOrdine());
                if (inserimento_domanda.executeUpdate() == 1) {
                    try (ResultSet rs = inserimento_domanda.getGeneratedKeys()) {
                        if (rs.next()) {
                            id = rs.getInt(1);
                        }
                    }
                    d.setId(id);
                }
                // inserimento_domanda.close();
            }
           // inserimento_domanda.close();
           // this.destroy();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DataException("Impossibile inserire o modificare la Domanda", ex);
        }

    }

    @Override
    public List<Domanda> getDomandeBySondaggioID(int sondaggioId) throws DataException {
        List<Domanda> domande = new ArrayList();
        try {
            domande_by_sondaggioID.setInt(1,sondaggioId);
            try (ResultSet rs = domande_by_sondaggioID.executeQuery()) {
                while (rs.next()) {
                    domande.add((Domanda) getDomandaByID(rs.getInt("id")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Impossibile caricare le Domande By Sondaggio", ex);
        }
        return domande;
    }

    @Override
    public List<Integer> getDomandeIdsBySondaggioID(int sondaggioId) throws DataException {
        List<Integer> list = new ArrayList();
        try{
            domande_ids_by_sondaggoID.setInt(1,sondaggioId);
            try(ResultSet rs = domande_ids_by_sondaggoID.executeQuery()){
                while(rs.next()) {
                    list.add(rs.getInt("id"));
                }
            }
        }catch (SQLException ex){
            throw new DataException("Impossibile caricare gli id delle Domande by Sondaggio",ex);
        }
        return list;
    }

    @Override
    public Domanda getDomandaByID(int domanda_id) throws DataException {
        try {
            domanda_by_id.setInt(1, domanda_id);
            try (ResultSet rs = domanda_by_id.executeQuery()) {
                if (rs.next()) {
                    return creazioneDomanda(rs);
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Impossibile caricare Domande By ID", ex);
        }
        return null;
    }
}
