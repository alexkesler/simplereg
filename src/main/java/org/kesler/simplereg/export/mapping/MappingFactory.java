package org.kesler.simplereg.export.mapping;

import org.kesler.simplereg.export.mapping.support.RosreestrCodeValueMapping;
import org.kesler.simplereg.logic.Reception;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MappingFactory {

    private static MappingFactory instance = new MappingFactory();

    private Reception reception;
    private List<ValueMapping> valueMappings = new ArrayList<ValueMapping>();
    private List<TableMapping> tableMappings = new ArrayList<TableMapping>();

    private MappingFactory() {
    }

    public static synchronized MappingFactory getInstance() {return instance;}

    public MappingFactory initMappings(Reception reception) {
        this.reception = reception;
        valueMappings.clear();
        valueMappings.add(new RosreestrCodeValueMapping(reception));
        return this;
    }

    public Map<String,String> getValueMap() {
        Map<String,String> valueMap = new HashMap<String, String>();
            for (ValueMapping valueMapping:valueMappings) {
                valueMap.put(valueMapping.getName(),valueMapping.getValue());
            }
        return valueMap;
    }

}
