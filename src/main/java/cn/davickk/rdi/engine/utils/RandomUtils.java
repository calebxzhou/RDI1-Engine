package cn.davickk.rdi.engine.utils;

import java.util.Random;

public class RandomUtils {
    public static int generateRandomInt(int min, int max) {
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }
    public static boolean randomPercentage(double perc){
        int ranMax=10000;
        int ranPerc=(int)(ranMax*perc);
        int ran=RandomUtils.generateRandomInt(0,ranMax);
        if(ran<ranPerc)
            return true;
        else
            return false;
    }
}
