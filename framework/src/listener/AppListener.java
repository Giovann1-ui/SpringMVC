package listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import java.util.List;
import java.util.Map;
import dto.ControllerResultDTO;
import dto.UrlMappingDTO;

public class AppListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String controllersPackage = sce.getServletContext().getInitParameter("controller");
        try {
            List<Class<?>> controllerClasses = utils.ControllerUtils.getControllerClasses(controllersPackage);
            Map<UrlMappingDTO, ControllerResultDTO> map = utils.ControllerUtils.getAllMapUrlMethod(controllerClasses);

            sce.getServletContext().setAttribute("urlMap", map);
            sce.getServletContext().setAttribute("controllerClasses", controllerClasses);

            System.out.println("=== AppListener : Application démarrée ===");
            System.out.println("Package scanné : " + controllersPackage);
            System.out.println("Controllers trouvés : " + controllerClasses.size());
            for (Class<?> c : controllerClasses) {
                System.out.println("  -> " + c.getName());
            }
            System.out.println("Routes enregistrées : " + map.size());
            for (UrlMappingDTO key : map.keySet()) {
                ControllerResultDTO val = map.get(key);
                System.out.println("  [" + key.getMethod() + "] " + key.getUrl()
                        + " -> " + val.getClasse().getSimpleName() + "." + val.getMethod().getName() + "()");
            }
        } catch (RuntimeException e) {
            System.out.println("Erreur AppListener : " + e.getMessage());
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'initialisation de l'application", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        sce.getServletContext().removeAttribute("urlMap");
        sce.getServletContext().removeAttribute("controllerClasses");
        System.out.println("=== AppListener : Application arrêtée ===");
    }
}