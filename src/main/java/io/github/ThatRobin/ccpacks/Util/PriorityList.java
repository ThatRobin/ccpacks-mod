package io.github.ThatRobin.ccpacks.Util;

import com.google.gson.JsonObject;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.util.thread.TaskQueue;

import java.util.Map;

public class PriorityList {

    private String type;
    private JsonObject jsonObject;

    public PriorityList(String type, JsonObject jsonObject){
        this.type = type;
        this.jsonObject = jsonObject;
    }

    public String getType(){
        return this.type;
    }

    public JsonObject getInstance(){
        return this.jsonObject;
    }
}
