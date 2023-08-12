package com.test;

import java.util.*;

public class test {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int sum = 0;
        int num = in.nextInt();

        String[] qiu,mi;

        for (int i = 0; i < num; i++) {
            int a = in.nextInt();
            qiu = new String[a];
            mi = new String[a];
            boolean flag = true;
            int point = 0;
            for (int j = 0; j < a; j++) {
                qiu[j] = in.next();
            }
            for (int j = 0; j < a; j++) {
                mi[j] = in.next();
                if (qiu[j].equals(mi[j])) {
                    point++;
                } else {
                    point--;
                }
                if(point < 0) flag = false;
            }
            if (flag) sum++;
        }
        System.out.println(sum);
    }

}
