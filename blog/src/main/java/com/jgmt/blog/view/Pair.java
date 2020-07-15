package com.jgmt.blog.view;

import java.lang.reflect.ParameterizedType;

public class Pair<T> {
    private T first;
    private T last;
    public Pair(T first, T last) {
        Class clazz = this.getClass();
        System.out.println(clazz.getGenericSuperclass());

        this.first = first;
        this.last = last;
    }
    public T getFirst() { return this.first; }
    public T getLast() { return this.last; }

    // 静态泛型方法应该使用其他类型区分:
    public static <K> Pair<K> create(K first, K last) {
        return new Pair<K>(first, last);
    }
}