package utils;

import java.util.Map;
import java.util.HashMap;

public class ModelAndView {
    private String view;
    private Map<String, Object> attributes;

public ModelAndView() {
    this.attributes = new HashMap<>();
}

    public ModelAndView(String view) {
        this.view = view;
        this.attributes = new HashMap<>();
    }

    public String getView() {
        return this.view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

public void addAttribute(String key, Object value) {
    if (this.attributes == null) {
        this.attributes = new HashMap<>();
    }
    this.attributes.put(key, value);
}
