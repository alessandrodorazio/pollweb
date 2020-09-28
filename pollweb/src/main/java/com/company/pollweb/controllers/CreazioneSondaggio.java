package com.company.pollweb.controllers;

import com.company.pollweb.data.models.Utente;
import com.company.pollweb.framework.data.DataException;
import com.company.pollweb.framework.result.FailureResult;
import com.company.pollweb.framework.result.SplitSlashesFmkExt;
import com.company.pollweb.framework.result.TemplateManagerException;
import com.company.pollweb.framework.result.TemplateResult;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import static com.company.pollweb.framework.security.SecurityLayer.checkSession;

/**
 *
 * @author gianlucarea
 *
 */
@WebServlet("/sondaggio/nuovo_sondaggio")

public class CreazioneSondaggio extends PoolWebBaseController{

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            HttpSession s = checkSession(request);
            if (s!= null) {
                action_poll(request, response, s);
            } else {
                action_redirect(request, response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void action_poll(HttpServletRequest request, HttpServletResponse response, HttpSession s) throws IOException, ServletException {
        try {
            Utente currentuser = ((com.company.pollweb.data.dao.PollwebDataLayer) request.getAttribute("datalayer")).getUtenteDAO().getUtente((String) s.getAttribute("user_email"));
            if (currentuser.getNomeRuolo().equals("Utente")) {
                request.setAttribute("message", "Non sei autorizzato ad accedere a questa area");
                request.setAttribute("submessage", "Contatta gli admin per diventare collaboratore");
                action_error(request, response);
            } else {
                request.setAttribute("page_title", "Crea Sondaggio");
                TemplateResult res = new TemplateResult(getServletContext());
                request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
                res.activate("/sondaggi/creazione.ftl", request, response);
            }
        } catch (TemplateManagerException | DataException e) {
            e.printStackTrace();
        }
    }

    private void action_redirect(HttpServletRequest request, HttpServletResponse response) throws  IOException {
        try {
            request.setAttribute("urlrequest", request.getRequestURL());
            RequestDispatcher rd = request.getRequestDispatcher("/utenti/login"); //Qui ci va l'url del login!!!
            rd.forward(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }

    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        } else {
            (new FailureResult(getServletContext())).activate((String) request.getAttribute("message"), request, response);
        }
    }

}
