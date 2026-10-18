package app.controllers;

import java.io.IOException;
import java.net.URLConnection;
import java.util.List;

import annotation.Controller;
import annotation.Injection;
import annotation.Parameter;
import annotation.UrlMapping;
import app.model.FichierModel;
import app.repository.AdresseRepository;
import app.repository.FichierRepository;
import app.repository.UserRepository;
import utils.ModelAndView;
import utils.UploadedFile;

import jakarta.servlet.http.*;

@Controller
public class UploadController {
    @Injection
    private UserRepository userRepository;

    @Injection
    private AdresseRepository adresseRepository;

    @Injection
    private FichierRepository fichierRepository;

    @UrlMapping(url = "/upload", method = "GET")
    public ModelAndView formUpload() {
        return new ModelAndView("upload");
    }

    @UrlMapping(url = "/upload", method = "POST")
    public String upload(@Parameter("fichiers") List<UploadedFile> fichiers) {
        for (UploadedFile f : fichiers) {
            fichierRepository.save(new FichierModel(f.getNom(), f.getExtension(), f.getContenu()));
        }
        return fichiers.size() + " fichier(s) enregistre(s)";
    }

    @UrlMapping(url = "/fichiers", method = "GET")
    public ModelAndView listeFichiers() {
        ModelAndView mv = new ModelAndView("fichiers");
        mv.addAttribute("fichiers", fichierRepository.listerSansContenu());
        return mv;
    }

    @UrlMapping(url = "/fichier", method = "GET")
    public void afficherFichier(@Parameter("id") Long id, HttpServletResponse response)
            throws IOException {

        FichierModel f = (id == null) ? null : fichierRepository.findById(id).orElse(null);
        if (f == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String nomComplet = f.getNom() + "." + f.getExtension();
        String type = URLConnection.guessContentTypeFromName(nomComplet);
        if (type == null) {
            type = "application/octet-stream";
        }

        // affichage direct seulement pour les images et le texte brut, le reste est
        // telecharge
        boolean inline = type.equals("text/plain")
                || (type.startsWith("image/") && !type.contains("svg"));

        response.setContentType(type.equals("text/plain") ? "text/plain;charset=UTF-8" : type);
        response.setHeader("X-Content-Type-Options", "nosniff");
        if (!inline) {
            String nomSur = nomComplet.replaceAll("[^A-Za-z0-9._-]", "_");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + nomSur + "\"");
        }
        response.setContentLength(f.getContenu().length);
        response.getOutputStream().write(f.getContenu());
    }
}
