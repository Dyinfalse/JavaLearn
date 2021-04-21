package com.jgmt.blog.sort;

public class BubbleSort {
    public static void main(String[] args) {
        int[] arr = { 7, 3, 2, 5, 6, 45, 33, 21};

        for(int i = 0; i < arr.length - 1; i++){
            for (int j = 0; j < arr.length - i - 1; j++){
                int temp;
                System.out.println(arr[j] + " " + arr[j + 1]);
                if (arr[j] > arr[j + 1]){
                    temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
            break;
        }
        System.out.println("========res========");
        for (int e : arr) System.out.print(e + " ");
    }
}
