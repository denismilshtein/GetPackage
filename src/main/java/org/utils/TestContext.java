package org.utils;


import org.apache.log4j.Logger;
import org.base.TestBase;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class TestContext {

    private static Properties params;
    public static Logger log = TestBase.log;

    static {
        InputStream is = null;
        try {
            is = TestContext.class.getClassLoader().getResourceAsStream("browser-config.properties");
            params = new Properties();
            params.load(is);
            is.close();


        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }

    }

    public static String getParamValue(String name){
        return params.get(name).toString();
    }

    public static void setParamValue(String name, String value){
        params.put(name, value);
        log.info(name + " updated to: " + value);
    }


}
