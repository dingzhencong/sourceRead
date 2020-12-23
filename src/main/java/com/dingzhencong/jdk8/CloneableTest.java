package com.dingzhencong.jdk8;

public class CloneableTest implements Cloneable {
    private String str;
    private Integer[] integers;

    public Integer[] getIntegers() {
        return integers;
    }

    public void setIntegers(Integer[] integers) {
        this.integers = integers;
    }

    public CloneableTest() {
        System.out.println("CloneableTest constructor()");
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    /*public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }*/

    public static void main(String[] args) throws CloneNotSupportedException {
        CloneableTest so0 = new CloneableTest();
        so0.setStr("111");
        so0.setIntegers(new Integer[]{1, 2, 3, 4});
        CloneableTest so1 = (CloneableTest)so0.clone();

        System.out.println("so0 == so1?" + (so0 == so1));
        System.out.println("so0.getClass() == so1.getClass()?" + (so0.getClass() == so1.getClass()));
        System.out.println("so0.equals(so1)?" + (so0.equals(so1)));

        System.out.println("so1.getStr()£º" + so1.getStr());
        System.out.println("so1.getIntegers()£º" + so1.getIntegers().length);
        so1.setStr("222");
        System.out.println("so0.getStr()£º" + so0.getStr());
        System.out.println("so1.getStr()£º" + so1.getStr());
    }
}
