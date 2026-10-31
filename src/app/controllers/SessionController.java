package app.controllers;

import java.io.IOException;
import java.util.List;

import annotation.Auth;
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

    @Auth
    @UrlMapping(url = "/secret", method = "GET")
    public String secret(MySession session) {
        return "Page reservee, connecte en tant que : " + session.get("user");
    }

    @UrlMapping(url = "/inscription", method = "GET")
    public ModelAndView formInscription() {
        return new ModelAndView("inscription");
    }

    @UrlMapping(url = "/inscription", method = "POST")
    public ModelAndView inscription(@Parameter("username") String username,
            @Parameter("password") String password,
            @Parameter("confirmation") String confirmation) {

        String erreur = null;
        if (username == null || username.trim().isEmpty() || password == null || password.isEmpty()) {
            erreur = "Tous les champs sont obligatoires";
        } else if (!password.equals(confirmation)) {
            erreur = "Les mots de passe ne correspondent pas";
        } else if (userRepository.findFirstByUsername(username.trim()) != null) {
            erreur = "Ce nom d'utilisateur existe deja";
        }

        if (erreur != null) {
            ModelAndView mv = new ModelAndView("inscription");
            mv.addAttribute("erreur", erreur);
            return mv;
        }

        userRepository.save(new UserModel(username.trim(), password, null));

        ModelAndView mv = new ModelAndView("login");
        mv.addAttribute("message", "Compte cree, tu peux te connecter");
        return mv;
    }

    @UrlMapping(url = "/login", method = "GET")
    public ModelAndView formLogin() {
        return new ModelAndView("login");
    }

    @UrlMapping(url = "/login", method = "POST")
    public ModelAndView login(@Parameter("username") String username,
            @Parameter("password") String password,
            MySession session) {

        UserModel user = (username == null) ? null : userRepository.findFirstByUsername(username);

        if (user == null || password == null || !user.getPassword().equals(password)) {
            ModelAndView mv = new ModelAndView("login");
            mv.addAttribute("erreur", "Identifiants incorrects");
            return mv;
        }

        session.invalidate(); // nouvel identifiant de session a chaque connexion
        session.add("user", user.getUsername());

        ModelAndView mv = new ModelAndView("profil");
        mv.addAttribute("username", user.getUsername());
        return mv;
    }

    @UrlMapping(url = "/logout", method = "GET")
    public ModelAndView logout(MySession session) {
        session.invalidate();
        return new ModelAndView("login");
    }

    @UrlMapping(url = "/profil", method = "GET")
    public ModelAndView profil(MySession session) {
        ModelAndView mv = new ModelAndView("profil");
        mv.addAttribute("username", session.get("user")); // null si personne n'est connecte
        return mv;
    }
}
