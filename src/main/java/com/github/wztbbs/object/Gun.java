package com.github.wztbbs.object;

/**
 * Created by wztbbs on 2016/1/11.
 */
public class Gun implements Shoutable{

    public Gun() {
        System.out.println("gun....");
    }

    @Override
    public void shout() {
        System.out.println("I am shout.........");
    }
}
