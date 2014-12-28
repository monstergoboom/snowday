package com.monstergoboom.snowday.game;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

import java.util.ArrayList;

/**
 * Created by amitrevski on 12/24/14.
 */
public class AtlasBoxing {
    static public class AtlasBox {
        public String name;
        public int index;
        public int x;
        public int y;
        public int width;
        public int height;
        public int paddingX;
        public int paddingY;
    }

    static public class AtlasBoxDef{
        public int x;
        public int y;
        public String bodyType;
        java.util.ArrayList<AtlasBox> boxing;
        public boolean hasError;
        public String message;

        public AtlasBoxDef() {
            x = 0;
            y = 0;
            bodyType = "static";
            boxing = new ArrayList<>();
        }

        public AtlasBox get(String name, int index) {
            AtlasBox found = null;
            for(AtlasBox box : boxing) {
                if(box.name.equalsIgnoreCase(name) && box.index == index) {
                    found = box;
                    break;
                }
            }

            return found;
        }

        public String debug() {
            StringBuffer sb = new StringBuffer();

            sb.append("position-x: " + Integer.toString(x) + ", position-y: " + Integer.toString(y));
            sb.append("body-style: " + bodyType);
            sb.append("boxing array count: " + Integer.toString(boxing.size()));

            if(hasError) {
                sb.append("error loading json, message: " + message);
            }

            return sb.toString();
        }
    }

    public static AtlasBoxDef load(FileHandle fh) {
        Json parser = new Json();
        AtlasBoxDef def = parser.fromJson(AtlasBoxDef.class, fh);
        return def;
    }
}