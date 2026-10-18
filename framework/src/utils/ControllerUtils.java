package utils;

import annotation.UrlMapping;
import dto.ControllerResultDTO;
import dto.UrlMappingDTO;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.lang.reflect.Field;
import java.io.IOException;

import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;

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
            if (clazz.isAnnotationPresent(annotation.Controller.class)) {
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

    // POUR METHOD AVEC PARAMETRES

    public static Object[] buildArguments(Method method,
            HttpServletRequest request, HttpServletResponse response) {

        Parameter[] params = method.getParameters();
        Object[] args = new Object[params.length];

        for (int i = 0; i < params.length; i++) {
            Parameter p = params[i];
            Class<?> type = p.getType();

            if (type == HttpServletRequest.class) {
                args[i] = request;
                continue;
            }
            if (type == HttpServletResponse.class) {
                args[i] = response;
                continue;
            }

            String name;
            if (p.isAnnotationPresent(annotation.Parameter.class)) {
                name = p.getAnnotation(annotation.Parameter.class).value();
            } else if (p.isNamePresent()) {
                name = p.getName();
            } else {
                throw new IllegalStateException(
                        "Nom de parametre introuvable : utiliser @Parameter ou compiler avec javac -parameters");
            }
            if (type == UploadedFile.class) {
                List<UploadedFile> files = readFiles(request, name);
                args[i] = files.isEmpty() ? null : files.get(0);
                continue;
            }
            if (isUploadedFileList(p)) {
                args[i] = readFiles(request, name);
                continue;
            }

            if (isSimpleType(type)) {
                args[i] = convert(request.getParameter(name), type, name);
            } else {
                args[i] = buildObject(type, name, request);
            }
        }
        return args;
    }

    public static Object convert(String raw, Class<?> type, String name) {
        if (raw == null || raw.isEmpty()) {
            if (type.isPrimitive()) {
                throw new IllegalArgumentException("Parametre manquant : " + name);
            }
            return null;
        }

        if (type == String.class)
            return raw;
        if (type == int.class || type == Integer.class)
            return Integer.parseInt(raw);
        if (type == long.class || type == Long.class)
            return Long.parseLong(raw);
        if (type == double.class || type == Double.class)
            return Double.parseDouble(raw);
        if (type == float.class || type == Float.class)
            return Float.parseFloat(raw);
        if (type == boolean.class || type == Boolean.class)
            return Boolean.parseBoolean(raw);

        throw new IllegalArgumentException("Type non supporte : " + type.getSimpleName());
    }

    public static boolean isSimpleType(Class<?> type) {
        return type.isPrimitive()
                || type == String.class
                || type == Integer.class || type == Long.class
                || type == Double.class || type == Float.class
                || type == Boolean.class;
    }

    public static Object buildObject(Class<?> type, String prefix, HttpServletRequest request) {
        try {
            Object obj = type.getDeclaredConstructor().newInstance();

            for (Field field : type.getDeclaredFields()) {
                String paramName = prefix + "." + field.getName();
                field.setAccessible(true);

                if (isSimpleType(field.getType())) {
                    String raw = request.getParameter(paramName);
                    if (raw == null || raw.isEmpty()) {
                        continue;
                    }
                    field.set(obj, convert(raw, field.getType(), paramName));

                } else if (hasParamWithPrefix(request, paramName)) {
                    // objet imbrique : on se rappelle avec le prefixe allonge
                    field.set(obj, buildObject(field.getType(), paramName, request));
                }
            }
            return obj;

        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Impossible de creer " + type.getSimpleName()
                    + " : il faut un constructeur sans argument", e);
        }
    }

    private static boolean hasParamWithPrefix(HttpServletRequest request, String prefix) {
        for (String key : request.getParameterMap().keySet()) {
            if (key.startsWith(prefix + ".")) {
                return true;
            }
        }
        return false;
    }

    // UPLOAD DE FICHIER

    private static boolean isUploadedFileList(Parameter p) {
        if (p.getType() != List.class) {
            return false;
        }
        java.lang.reflect.Type t = p.getParameterizedType();
        if (!(t instanceof java.lang.reflect.ParameterizedType)) {
            return false;
        }
        java.lang.reflect.Type arg = ((java.lang.reflect.ParameterizedType) t).getActualTypeArguments()[0];
        return arg == UploadedFile.class;
    }

    private static List<UploadedFile> readFiles(HttpServletRequest request, String name) {
        List<UploadedFile> result = new ArrayList<>();

        String contentType = request.getContentType();
        if (contentType == null || !contentType.toLowerCase().startsWith("multipart/")) {
            return result;
        }

        try {
            for (Part part : request.getParts()) {
                String submitted = part.getSubmittedFileName();

                // on ignore les autres champs et le "fichier vide" que le navigateur envoie
                // quand aucun fichier n'est choisi
                if (!name.equals(part.getName()) || submitted == null || submitted.isEmpty()) {
                    continue;
                }
                result.add(new UploadedFile(submitted, part.getContentType(),
                        part.getInputStream().readAllBytes()));
            }
        } catch (IOException | ServletException e) {
            throw new IllegalStateException("Lecture des fichiers impossible", e);
        }
        return result;
    }

}