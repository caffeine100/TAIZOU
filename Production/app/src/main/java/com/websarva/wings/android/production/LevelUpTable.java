package com.websarva.wings.android.production;

public class LevelUpTable {
    private int[] exTable={0,100,200,300,400,500,600,700,800,1000,1200,1400,1600,1800,2000,2200,2400,2600,2800,3000,3200,3400,3600,
            3800,
            4000,
            4200,
            4400,
            4600,
            4800,
            5000,
            5200,
            5400,
            5600,
            5800,
            6000,
            6200,
            6400,
            6600,
            6800,
            7000,
            7200,
            7400,
            7600,
            7800,
            8000,
            8200,
            8400,
            8600,
            8800,
            9000,
            9200,
            9400,
            9600,
            9800,
            10000,
            10200,
            10400,
            10600,
            10800,
            11000,
            11200,
            11400,
            11600,
            11800,
            12000,
            12200,
            12400,
            12600,
            12800,
            13000,
            13200,
            13400,
            13600,
            13800,
            14000,
            14200,
            14400,
            14600,
            14800,
            15000,
            15200,
            15400,
            15600,
            15800,
            16000,
            16200,
            16400,
            16600,
            16800,
            17000,
            17200,
            17400,
            17600,
            17800,
            18000,
            18200,
            18400,
            18600,
            18800,
            19000};


    public int getExMin(int exPoint){
        int min=levelCheck(exPoint);

        return exTable[min-1];
    }
    public int getExMax(int exPoint){
        int max=levelCheck(exPoint);
        try {
            int s=exTable[max];
        }catch(ArrayIndexOutOfBoundsException e){
            return exTable[max-1];
        }
        return exTable[max];
    }

    public int getLvMax(){
        return exTable[exTable.length-1];
    }



    public int levelCheck(int exPoint){
        int l=0;
        int e=exPoint;

        for(int p:exTable){
            if(e<p){
                break;
            }
            l++;
        }
        return l;
    }



}
