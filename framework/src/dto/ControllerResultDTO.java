package dto;

import java.lang.reflect.Method;

public class ControllerResultDTO {
    Class<?> classe;
    Method method;

    public ControllerResultDTO(Class<?> classe, Method method) {
        this.classe = classe;
        this.method = method;
    }

    public ControllerResultDTO() {
    }

    public Class<?> getClasse() {
        return classe;
    }

    public Method getMethod() {
        return method;
    }

    public void setClasse(Class<?> classe) {
        this.classe = classe;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

}
