package io.github.ThatRobin.ccpacks.Util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class CCPackInfo {

    public String name;
    public String version;
    public String id;
    public String author;
    public String description;
    public String license;

    public CCPackInfo(String name, String version, String id, String author, String description, String license){
        this.name = name;
        this.version = version;
        this.id = id;
        this.author = author;
        this.description = description;
        this.license = license;
    }
}
