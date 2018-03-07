package com.example;

public class Runner {
	public static void main(String[] args) {
		Calculator c = new Calculator();
        double result = c.sum(3.5,6.5);
		System.out.println("3.5+6.5=" + result);
	}
	
}