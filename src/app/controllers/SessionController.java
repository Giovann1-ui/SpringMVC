package app.controllers;

import java.io.IOException;
import java.util.List;

import annotation.Controller;
import annotation.Injection;
import annotation.Parameter;
import annotation.UrlMapping;
import annotation.WebApi;
import utils.ModelAndView;
import utils.MySession;
import utils.UploadedFile;
import app.model.UserModel;
import app.model.AdresseModel;
import app.model.FichierModel;
import app.repository.UserRepository;
import app.repository.AdresseRepository;
import app.repository.FichierRepository;

import jakarta.servlet.http.*;

@Controller
public class SessionController {

    @Injection
    private UserRepository userRepository;

    @Injection
    private AdresseRepository adresseRepository;

    @Injection
    private FichierRepository fichierRepository;

    @UrlMapping(url = "/compteur", method = "GET")
    public String compteur(MySession session) {
        Integer n = (Integer) session.get("n");
        n = (n == null) ? 1 : n + 1;
        session.add("n", n);
        return "Visite numero " + n;
    }

    @UrlMapping(url = "/compteur/reset", method = "GET")
    public String resetCompteur(MySession session) {
        session.invalidate();
        return "Session reinitialisee";
    }
}
