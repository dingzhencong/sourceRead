package com.dingzhencong.util;

import java.util.BitSet;

public class BitMapTest {
    public static void main(String[] args) {
        int n = 100;
        BitSet b = new BitSet(n + 1);
        int i;
        for(i = 2; i <= n; i++){
            b.set(i);
        }

        i = 2;
        while(i * i <= n){
            if(b.get(i)){
                int k = 2 * i;
                while(k <=n){
                    b.clear(k);
                    k += i;
                }
            }
            i++;
        }

        i = 0;
        while(i <= n){
            if(b.get(i)){
                System.out.println(i);
            }
            i++;
        }
    }
}
