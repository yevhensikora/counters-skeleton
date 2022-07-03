package com.epam.rd.java.basic.topic05.task03;

public class Task {

	private int numberOfThreads;

	private int numberOfIterations;

	private int pause;

	private int c1;

	private int c2;

	public Task(int numberOfThreads, int numberOfIterations, int pause) {
		
	}

	public void compare() {
		
	}

	public void compareSync() {

	}
	
	public static void main(String[] args) {
		Task t = new Task(2, 5, 10);
		t.compare();
		System.out.println("~~~");
		t.compareSync();
	}

}
