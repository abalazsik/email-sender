package org.balazad;

import java.io.InputStreamReader;
import java.util.Properties;

/**
 *
 * @author ador
 */
public class Configuration extends Properties {

    public static final Configuration INSTANCE = new Configuration();

    private Configuration() {
        try ( InputStreamReader isr = new InputStreamReader(Configuration.class.getResourceAsStream("/application.properties"), "UTF-8")) {
            this.load(isr);
        } catch (Exception e) {
            throw new RuntimeException("Failed to read application.properties", e);
        }
    }
}
