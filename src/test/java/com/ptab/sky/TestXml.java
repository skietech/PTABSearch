package com.ptab.sky;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import java.io.File;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public class TestXml {

    public static void main(String[] args) {

        Map<String,String> map = new HashMap<String,String>();
        map.put("name","chris");
        map.put("island","faranga");

        XStream magicApi = new XStream();
        magicApi.registerConverter(new MapEntryConverter());
        magicApi.alias("PatentApplication", Map.class);

       

        Map<String, String> extractedMap = (Map<String, String>) magicApi.fromXML(new File("/Users/raviramani/SkyTechSpace/SkyData/Submission/16260000.xml"));
        assert extractedMap.get("name").equals("chris");
        assert extractedMap.get("island").equals("faranga");

    }

    public static class MapEntryConverter implements Converter {

        public boolean canConvert(Class clazz) {
			return false;}

        public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {

            AbstractMap map = (AbstractMap) value;
            for (Object obj : map.entrySet()) {
                Map.Entry entry = (Map.Entry) obj;
                writer.startNode(entry.getKey().toString());
                Object val = entry.getValue();
                if ( null != val ) {
                    writer.setValue(val.toString());
                }
                writer.endNode();
            }

        }

        public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {

            Map<String, String> map = new HashMap<String, String>();

            while(reader.hasMoreChildren()) {
                reader.moveDown();

                String key = reader.getNodeName(); // nodeName aka element's name
                String value = reader.getValue();
                map.put(key, value);

                reader.moveUp();
            }

            return map;
        }

    }

}