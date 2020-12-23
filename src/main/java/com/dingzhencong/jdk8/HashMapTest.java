package com.dingzhencong.jdk8;

public class HashMapTest {
    public static void main(String[] args) {
        String key = "dz111c";
        int hashCode = hash(key);
        System.out.println(hashCode);
        System.out.println(Integer.toBinaryString(key.hashCode()));
        System.out.println(Integer.toBinaryString(key.hashCode() >>> 16));
        System.out.println(Integer.toBinaryString(hashCode));
        System.out.println(Integer.toBinaryString(7));
        System.out.println((8 - 1) & hashCode);
    }

    static int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }
}
