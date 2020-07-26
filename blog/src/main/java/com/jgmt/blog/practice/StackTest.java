package com.jgmt.blog.practice;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class StackTest {

    public static void main(String[] args) {

        String hex = toHex(1234567);
        if (hex.equalsIgnoreCase("12D687")) {
            System.out.println("测试通过");
        } else {
            System.out.println("测试失败");
        }
    }

    /** RES STACK */
    static Deque<String> dq = new LinkedList<>();

    static String[] codeList = new String[]{"F","E","D","C","B","A"};

    static String toHex(int n) {

        setStack(n);

        return getAllStack();

        /**
         * 简化 利用系统栈写法
         */
//        String alpha = "0123456789ABCDEF";
//        if (n < 16){
//            return String.valueOf(alpha.charAt(n));
//        }
//        return toHex(n/16)+String.valueOf(alpha.charAt(n%16));
    }

    static String getAllStack(){
        String res = "";
        for(String s: dq){
            res += s;
        }
        return res;
    }

    static void setStack(int n){
        // 商不为0, 因为使用int类型接参，所以省略小数点后面的
        System.out.println(n);
        if(n > 0){
            dq.push(to16String(n % 16));
//            System.out.println(dq);
            setStack(n / 16);
        }
    }

    static String to16String (int n) {
        if(n < 10) return Integer.toString(n);

        return codeList[15 - n];
    }
}
