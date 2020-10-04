/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.company.pollweb.data.impl;

import com.company.pollweb.data.models.Utente;

/**
 *
 * @author alessandrodorazio
 */
public class UtenteImpl implements Utente {
    protected String nome, cognome, email,password;
    protected int ruolo_id;

    public UtenteImpl() {
        this.nome = "";
        this.cognome = "";
        this.email = "";
        this.ruolo_id = 1;
    }
    
    public UtenteImpl(String nome, String cognome, String email, int ruolo_id) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.ruolo_id = ruolo_id;
    }
    
    public String getNome() {
        return this.nome;
    }
    
    public String getCognome() {
        return this.cognome;
    }
    
    public String getNomeCompleto() {
        return this.nome + " " + this.cognome;
    }

    @Override
    public String getPassword() { return this.password; }

    public String getEmail() {
        return this.email;
    }
    
    public int getRuolo() {
        return this.ruolo_id;
    }

    public String getNomeRuolo() {
        if(this.ruolo_id == 3) return "Amministratore";
        if(this.ruolo_id == 2) return "Responsabile";
        return "Utente";
    }

    @Override
    public void setNome(String newNome) {
        this.nome = newNome;
    }

    @Override
    public void setCognome(String newCognome) {
        this.cognome = newCognome;
    }

    @Override
    public void setEmail(String newEmail) {
        this.email = newEmail;
    }

    @Override
    public void setPassword(String newPassword) {
        this.password = newPassword;
    }

    @Override
    public void setRuolo(int newRuoloId) {
        this.ruolo_id = newRuoloId;
    }
}