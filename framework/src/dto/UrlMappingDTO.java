package dto;

public class UrlMappingDTO {

    private String url;
    private String method;

    public UrlMappingDTO(String url, String method) {
        this.url = url;
        this.method = method;
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj)
            return true;

        if (!(obj instanceof UrlMappingDTO))
            return false;

        UrlMappingDTO other = (UrlMappingDTO) obj;

        return url.equals(other.url)
                && method.equalsIgnoreCase(other.method);
    }

    @Override
    public int hashCode() {
        return (url + method.toUpperCase()).hashCode();
    }
}
