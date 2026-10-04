import java.lang.reflect.Method;

public class ControlleurSecurite {
    public static void executerMethode(Object objet, String nomMethode, String roleUtilisateurActuel) throws NoSuchMethodException {
        Method method = objet.getClass().getMethod(nomMethode);
        if(method.isAnnotationPresent(Autorisation.class)){
             Autorisation autorisation = method.getAnnotation(Autorisation.class);
             String role = autorisation.roleRequis();
             if(role.equals(roleUtilisateurActuel)){
                System.out.println("autorisé");
             }
             else{
                System.out.println("acces refusé");
             }
        }
        else{
            System.out.println("acces autorisé");
        }
    }
}
