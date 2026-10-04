import dto.UrlMappingDTO;
import java.util.HashMap;
import java.util.Map;

public class TestUrlMapping {
    public static void main(String[] args) {
        UrlMappingDTO a = new UrlMappingDTO("/home", "GET");
        UrlMappingDTO b = new UrlMappingDTO("/home", "GET");

        System.out.println("equals : " + a.equals(b));
        System.out.println("hashCode a : " + a.hashCode());
        System.out.println("hashCode b : " + b.hashCode());

        Map<UrlMappingDTO, String> map = new HashMap<>();
        map.put(a, "premier");
        System.out.println("containsKey b : " + map.containsKey(b));
    }
}