package com.mentor.workflow.client;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author: ksipe
 */
public abstract class OMModel implements Serializable {
    private Map<String, Object> model = new HashMap<String, Object>();

    public OMModel(Map<String, Object> model) {
        this.model = model;
    }

    public OMModel() {
    }

    public void put(String key, Object value) {
        model.put(key, value);
    }

    public int size() {
        return model.size();
    }

    public boolean containsKey(Object key) {
        return model.containsKey(key);
    }

    public Object get(String key) {
        return model.get(key);
    }

    public Object remove(Object key) {
        return model.remove(key);
    }

    public void clear() {
        model.clear();
    }

    public Set<String> keySet() {
        return model.keySet();
    }

    public void putAll(Map<String, Object> model) {
        this.model.putAll(model);
    }

    public void putAll(OMModel model) {
        this.model.putAll(model.getModel());
    }

    // these are exposed as a package convenience
    public Map<String, Object> getModel() {
        return model;
    }

    protected void setModel(Map<String, Object> model) {
        this.model = model;
    }

}
