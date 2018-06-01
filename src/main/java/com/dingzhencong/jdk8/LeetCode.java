package com.dingzhencong.jdk8;

import java.util.HashMap;

public class LeetCode {

    public static void main(String[] args) {
        LeetCode left = new LeetCode();
        left.fib(3);

        System.out.println(left.coinChange(new Integer[]{2}, 3));
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


    public int coinChange(Integer[] coins, int amount) {
//        Arrays.sort(coins, Collections.reverseOrder());
        int count = 0;
        while (amount != 0) {
            for (int i = coins.length - 1; i >= 0; i--) {
                if (amount >= coins[i]) {
                    count += amount / coins[i];
                    amount %= coins[i];
                }
            }
        }
        return count;
    }

    public int coinChange(int[] coins, int amount) {
        int count = 0;
        HashMap<Integer, Integer> hash = new HashMap<Integer, Integer> ();

        return count;
    }

    int dp(HashMap<Integer, Integer> hash, int[] coins,int amount) {
        if (hash.containsKey(amount)) {
            return hash.get(amount);
        }
        if (amount == 0) {
            return 0;
        }
        if (amount < 0) {
            return -1;
        }
        for (int coin : coins) {
            int subProblem = dp(hash, coins, amount - coin);
            if (subProblem == -1) {
                continue;
            }
            
        }
        return 0;
    }
}
