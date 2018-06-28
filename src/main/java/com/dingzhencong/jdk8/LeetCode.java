package com.dingzhencong.jdk8;

import java.util.HashMap;

public class LeetCode {

    public static void main(String[] args) {
        LeetCode left = new LeetCode();
        left.fib(3);

//        System.out.println(left.coinChange(new Integer[]{2}, 3));
    }

    int fib(int n){
        if (n < 1)
            return 1;
        int[] dpTable = new int[n + 1];
        int sum = helper(dpTable, n);
        return sum;
    }

    int helper(int[] dpTabale, int n) {
        if (n == 1 || n == 2) {
            return 1;
        }
        if (dpTabale[n] != 0) {
            return dpTabale[n];
        }
        dpTabale[n] = helper(dpTabale,n - 1) + helper(dpTabale,n - 2);
        return dpTabale[n];
    }

    
}
