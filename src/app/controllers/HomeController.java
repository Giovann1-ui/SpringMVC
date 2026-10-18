package app.controllers;

import java.io.IOException;

import annotation.Controller;
import annotation.Injection;
import annotation.Parameter;
import annotation.UrlMapping;
import annotation.WebApi;
import utils.ModelAndView;

import app.model.UserModel;
import app.model.AdresseModel;
import app.repository.UserRepository;
import app.repository.AdresseRepository;

import jakarta.servlet.http.*;

@Controller
public class HomeController {

    @Injection
    private UserRepository userRepository;

    @Injection
    private AdresseRepository adresseRepository;

    @UrlMapping(url = "/home", method = "GET")
    public void homes(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.getWriter().println("Bonjour ");
    }

    @UrlMapping(url = "/test")
    public String test() {
        return "Test OK";
    }

    @UrlMapping(url = "/index")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addAttribute("message", "Bonjour depuis le contrôleur !");
        return modelAndView;
    }

    @UrlMapping(url = "/api/hello")
    @WebApi
    public String hello() {
        return "Bonjour depuis l'API";
    }

    @UrlMapping(url = "/api/user")
    @WebApi
    public UserModel getUser() {
        return new UserModel("John Doe", "password123", new AdresseModel("123 Main St", "Cityville", 12345));
    }

    @UrlMapping(url = "/testBase", method = "GET")
    public ModelAndView mamo() {
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addAttribute("message1", userRepository.findById(1L).orElse(null).getUsername());
        return modelAndView;
    }

    @UrlMapping(url = "/addition", method = "GET")
    public ModelAndView formulaire() {
        return new ModelAndView("form");
    }

    @UrlMapping(url = "/addition", method = "POST")
    public ModelAndView calculer(@Parameter("a") int a, @Parameter("b") int b) {
        ModelAndView mv = new ModelAndView("form");
        mv.addAttribute("resultat", a + b);
        return mv;
    }

    @UrlMapping(url = "/personne", method = "GET")
    public ModelAndView formPersonne() {
        return new ModelAndView("form-personne");
    }

    @UrlMapping(url = "/personne", method = "POST")
    public ModelAndView recevoirPersonne(@Parameter("value") UserModel personne) {
        ModelAndView mv = new ModelAndView("form-personne");
        mv.addAttribute("personne", personne);
        if (adresseRepository.existsByStreetAndCityAndPostalCode(personne.getAdresse().getStreet(),
                personne.getAdresse().getCity(), personne.getAdresse().getPostalCode())) {
            AdresseModel adresse = adresseRepository.getByStreetAndCityAndPostalCode(personne.getAdresse().getStreet(),
                    personne.getAdresse().getCity(), personne.getAdresse().getPostalCode());
            UserModel user = new UserModel(personne.getUsername(), personne.getPassword(), adresse);
            userRepository.save(user);
        } else {
            AdresseModel adresse = new AdresseModel(personne.getAdresse().getStreet(), personne.getAdresse().getCity(),
                    personne.getAdresse().getPostalCode());
            adresseRepository.save(adresse);
            UserModel user = new UserModel(personne.getUsername(), personne.getPassword(), adresse);
            userRepository.save(user);
        }

        return mv;
    }

    @UrlMapping(url = "/personne2", method = "POST")
    public String recevoirPersonne2(@Parameter("value") UserModel personne) {
        AdresseModel saisie = personne.getAdresse();
        AdresseModel adresse = null;
        String etatAdresse = "aucune adresse";

        if (saisie != null) {
            adresse = adresseRepository.getByStreetAndCityAndPostalCode(
                    saisie.getStreet(), saisie.getCity(), saisie.getPostalCode());

            if (adresse == null) {
                adresse = adresseRepository.save(new AdresseModel(
                        saisie.getStreet(), saisie.getCity(), saisie.getPostalCode()));
                etatAdresse = "adresse creee";
            } else {
                etatAdresse = "adresse deja existante";
            }
        }

        userRepository.save(new UserModel(personne.getUsername(), personne.getPassword(), adresse));

        return "Utilisateur enregistre : " + personne.getUsername() + " (" + etatAdresse + ")";
    }
}