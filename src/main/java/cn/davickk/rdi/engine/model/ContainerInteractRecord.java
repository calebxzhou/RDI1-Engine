package cn.davickk.rdi.engine.model;

import com.google.gson.Gson;

import java.io.Serializable;
import java.sql.Timestamp;

public class ContainerInteractRecord implements Serializable {
    private String playerName,clickedItemName,clickTyping,clickedItemNbt,blockName;
    private int posX,posY,posZ,itemAmount;
    private Timestamp time;

    public ContainerInteractRecord(String playerName, String clickedItemName, String clickTyping, String clickedItemNbt, String blockName, int itemAmount,int posX, int posY, int posZ, Timestamp time) {
        this.playerName = playerName;
        this.clickedItemName = clickedItemName;
        this.clickTyping = clickTyping;
        this.clickedItemNbt = clickedItemNbt;
        this.blockName = blockName;
        this.itemAmount=itemAmount;
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.time = time;
    }

    @Override
    public String toString(){
        return new Gson().toJson(this);
    }
}
