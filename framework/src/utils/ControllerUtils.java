package utils;

import annotation.UrlMapping;
import dto.ControllerResultDTO;
import dto.UrlMappingDTO;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ControllerUtils {

    public static List<Class<?>> getFiles(String packageName) {
        List<Class<?>> controllerClasses = new ArrayList<>();
        try {
            String packagePath = packageName.replace('.', '/');
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            java.net.URL ressource = classLoader.getResource(packagePath);

            if (ressource == null) {
                System.out.println("Le package " + packageName + " est introuvable (ressource vide).");
                return controllerClasses;
            }
            java.io.File directory = new java.io.File(ressource.toURI());

            if (directory.exists() && directory.isDirectory()) {
                java.io.File[] files = directory.listFiles();
                if (files != null) {
                    for (java.io.File file : files) {
                        if (file.isFile() && file.getName().endsWith(".class")) {
                            String className = file.getName().substring(0, file.getName().length() - 6);
                            String totalClassName = packageName + "." + className;
                            Class<?> clazz = Class.forName(totalClassName);
                            controllerClasses.add(clazz);
                        }
                    }
                }
            } else {
                System.out.println("Le chemin " + packageName + " n'est pas un dossier valide.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors du scan des contrôleurs", e);
        }

        return controllerClasses;
    }

    public static List<Class<?>> getControllerClasses(String packageName) {
        List<Class<?>> controllerClasses = new ArrayList<>();
        List<Class<?>> allClasses = getFiles(packageName);

        for (Class<?> clazz : allClasses) {
            if (clazz.isAnnotationPresent(annotation.Controller.class)
                    || clazz.isAnnotationPresent(annotation.WebApi.class)) {
                controllerClasses.add(clazz);
            }
        }

        return controllerClasses;
    }

    public static boolean isWebApi(Method m) {
        return m.isAnnotationPresent(annotation.WebApi.class);
    }

    // =========================
    // METHODES ANNOTEES @UrlMapping
    // =========================
    public static List<Method> getListMethod(Class<?> classe) {
        List<Method> lsMethods = new ArrayList<>();

        Method[] methods = classe.getDeclaredMethods();

        for (Method m : methods) {
            lsMethods.add(m);
        }

        return lsMethods;
    }

    public static List<Method> getListMethodAnnoteUrl(Class<?> classe) {
        List<Method> lsMethods = getListMethod(classe);
        List<Method> result = new ArrayList<>();

        for (Method method : lsMethods) {
            if (method.isAnnotationPresent(UrlMapping.class)) {
                result.add(method);
            }
        }

        return result;
    }

    public static String getUrlMapping(Method method) {

        if (method.isAnnotationPresent(UrlMapping.class)) {
            UrlMapping annotation = method.getAnnotation(UrlMapping.class);
            return annotation.url();
        }

        return null;
    }

    public static Method getMethodByUrl(Class<?> classe, String url) {

        List<Method> methods = getListMethodAnnoteUrl(classe);

        for (Method method : methods) {
            String currentUrl = getUrlMapping(method);

            if (currentUrl.equals(url)) {
                return method;
            }
        }

        return null;
    }

    // =========================
    // MAP (URL + HTTP METHOD) -> (Classe + Methode)
    // =========================
    public static Map<UrlMappingDTO, ControllerResultDTO> getAllMapUrlMethod(List<Class<?>> lsController) {

        Map<UrlMappingDTO, ControllerResultDTO> result = new HashMap<>();

        for (Class<?> classe : lsController) {

            List<Method> lsMethods = getListMethodAnnoteUrl(classe);

            for (Method method : lsMethods) {

                UrlMapping annotation = method.getAnnotation(UrlMapping.class);

                UrlMappingDTO mapping = new UrlMappingDTO(annotation.url(), annotation.method());

                ControllerResultDTO dto = new ControllerResultDTO();
                dto.setClasse(classe);
                dto.setMethod(method);

                result.put(mapping, dto);
            }
        }

        return result;
    }

}