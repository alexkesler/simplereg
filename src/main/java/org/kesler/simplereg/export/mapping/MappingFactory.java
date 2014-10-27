package org.kesler.simplereg.export.mapping;

import org.kesler.simplereg.logic.Reception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MappingFactory {

    private static MappingFactory instance = new MappingFactory();

    private Reception reception;
    private List<ValueMapping> valueMappings;
    private List<TableMapping> tableMappings;

    private MappingFactory() {

    }

    public static synchronized MappingFactory getInstance() {return instance;}

    public void initMappings(Reception reception) {
        this.reception = reception;
    }

    public Map<String,String> getValueMap() {
        Map<String,String> valueMap = new HashMap<String, String>();

        return valueMap;
    }

}
