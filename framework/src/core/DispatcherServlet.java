package core;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dto.ControllerResultDTO;
import dto.UrlMappingDTO;
import utils.ModelAndView;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

public class DispatcherServlet extends HttpServlet {
    List<Class<?>> controllerClasses = new ArrayList<>();
    Map<UrlMappingDTO, ControllerResultDTO> map;

    
    @Override
    @SuppressWarnings("unchecked")
    public void init() throws ServletException {
        map = (Map<UrlMappingDTO, ControllerResultDTO>) getServletContext().getAttribute("urlMap");
        controllerClasses = (List<Class<?>>) getServletContext().getAttribute("controllerClasses");

        if (map == null || controllerClasses == null) {
            throw new ServletException(
                    "urlMap ou controllerClasses non initialisés — AppListener a-t-il bien démarré ?");
        }

        System.out.println("DispatcherServlet initialisé, " + map.size() + " route(s) chargée(s).");
    }

    public void affichage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/plain");

        String path = request.getPathInfo();

        if (path == null || path.isEmpty()) {
            path = request.getServletPath();
        }

        if (path == null || path.isEmpty()) {
            path = "/";
        }

        String httpMethod = request.getMethod();

        UrlMappingDTO key = new UrlMappingDTO(path, httpMethod);

        ControllerResultDTO found = map.get(key);

        // =========================
        // URL CONNUE
        // =========================
        if (found != null) {

            Method method = found.getMethod();
            Class<?> controllerClasse = found.getClasse();

            response.getWriter().println("=== ROUTE TROUVEE ===");
            response.getWriter().println("URL : " + path);
            response.getWriter().println("HTTP : " + httpMethod);
            response.getWriter().println("Controller : " + controllerClasse.getName());
            response.getWriter().println("Méthode Java : " + method.getName());
            response.getWriter().println();

            // Une méthode "simple" (sans paramètre HttpServletRequest/
            // HttpServletResponse) est réellement exécutée par réflexion,
            // et son résultat est affiché.
            if (method.getParameterCount() == 0) {

                try {
                    Object controllerInstance = controllerClasse.getDeclaredConstructor().newInstance();
                    Object result = method.invoke(controllerInstance);

if (result instanceof ModelAndView) {
    ModelAndView mv = (ModelAndView) result;

    // Ne pas forward si la réponse est déjà commitée (println() plus haut)
    if (response.isCommitted()) {
        throw new ServletException("Impossible de forward : la réponse est déjà commitée");
    }
    response.resetBuffer();
    response.setContentType("text/html;charset=UTF-8");

    String nomPackage = (String) getServletContext().getInitParameter("pafSource");
    String nomExtension = (String) getServletContext().getInitParameter("extension");
    String view = "/" + nomPackage + mv.getView() + nomExtension;
    Map<String, Object> attributes = mv.getAttributes();

    if (attributes != null) {
        for (Map.Entry<String, Object> entry : attributes.entrySet()) {
            request.setAttribute(entry.getKey(), entry.getValue());
        }
    }
    request.getRequestDispatcher(view).forward(request, response);
    return;
}

                    response.getWriter().println("=== RESULTAT DE L'EXECUTION ===");

                    if (method.getReturnType().equals(Void.TYPE)) {
                        response.getWriter().println("(méthode void exécutée avec succès)");
                    } else {
                        response.getWriter().println(String.valueOf(result));
                    }

                } catch (Exception e) {
                    response.getWriter().println("=== ERREUR D'EXECUTION ===");
                    response.getWriter().println(e.getCause() != null ? e.getCause().toString() : e.toString());
                }

            } else {
                // Méthode avec paramètres (ex: HttpServletRequest, HttpServletResponse)
                // : exécution non gérée par cette voie.
                response.getWriter().println("(méthode avec paramètres : exécution non gérée par cette voie)");
            }

            return;
        }

        // =========================
        // URL INCONNUE
        // =========================
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        response.getWriter().println("❌ Route introuvable");
        response.getWriter().println("URL : " + path);
        response.getWriter().println("HTTP : " + httpMethod);
        response.getWriter().println();

        response.getWriter().println("=== ROUTES DISPONIBLES ===");
        for (UrlMappingDTO mapping : map.keySet()) {
            ControllerResultDTO r = map.get(mapping);
            response.getWriter().println(
                    mapping.getMethod()
                            + " "
                            + mapping.getUrl()
                            + " -> "
                            + r.getClasse().getSimpleName()
                            + "."
                            + r.getMethod().getName());
        }

        response.getWriter().println();
        response.getWriter().println("=== CONTROLLERS ===");
        for (Class<?> controllerClass : controllerClasses) {
            response.getWriter().println(controllerClass.getName());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        affichage(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        affichage(request, response);
    }
}
