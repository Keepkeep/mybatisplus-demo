package com.wpx;

import org.junit.Test;
class T {
	public int add(int a,int b) {
		return a+b;
	}
	
}
class Zi extends T{
	
}

public class TestDemo {
	@Test
	public void test01() {
		T t = new  T();
		T t2 = new  T();
		System.out.println(t);
		System.out.println(t2);
		System.out.println(t.equals(t2));
	}
	
}
