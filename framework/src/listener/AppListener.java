package listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import annotation.Injection;
import dto.ControllerResultDTO;
import dto.UrlMappingDTO;
import java.lang.reflect.*;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

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

            WebApplicationContext springContext = WebApplicationContextUtils
                    .getRequiredWebApplicationContext(sce.getServletContext());

            Map<Class<?>, Object> controllerInstances = new HashMap<>();

            for (Class<?> c : controllerClasses) {
                System.out.println("  -> " + c.getName());

                // On crée l'instance unique (le Singleton) du contrôleur
                Object controllerInstance = c.getDeclaredConstructor().newInstance();

                // On inspecte les attributs pour l'injection
                Field[] fields = c.getDeclaredFields();
                for (Field field : fields) {
                    if (field.isAnnotationPresent(Injection.class)) {
                        Class<?> fieldType = field.getType();

                        // On récupère le bean Spring
                        Object bean = springContext.getBean(fieldType);

                        // On injecte le bean dans notre INSTANCE de contrôleur
                        field.setAccessible(true);
                        field.set(controllerInstance, bean);
                    }
                }
                controllerInstances.put(c, controllerInstance);
            }
            sce.getServletContext().setAttribute("controllerInstances", controllerInstances);

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
        sce.getServletContext().removeAttribute("controllerInstances");
        System.out.println("=== AppListener : Application arrêtée ===");
    }
}