package com.epam.rd.java.basic.topic05.task03;

import java.util.ArrayList;
import java.util.List;

public class Task {
	private int numberOfThreads;
	private int numberOfIterations;
	private int pause;
	private int c1;
	private int c2;
	private final List<Thread> threadList = new ArrayList<>();
	private Runnable runnable;
	private final Object LOCK = new Object();

	public Task(int numberOfThreads, int numberOfIterations, int pause) {
		this.numberOfThreads = numberOfThreads;
		this.numberOfIterations = numberOfIterations;
		this.pause = pause;
	}

	private void startThreads() {
		for(int i = 0; i < numberOfThreads; ++i) {
			threadList.add(new Thread(runnable));
		}
		for (Thread thread : threadList) {
			thread.start();
		}
	}

	private void joinThreads() {
		for (Thread thread : threadList) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void compare() {
		c1 = 0;
		c2 = 0;

		runnable = () -> {
			for (int i = 0; i < numberOfIterations; i++) {
				doComparison();
			}
		};
		startThreads();
		joinThreads();
		threadList.clear();
	}

	public void compareSync() {
		c1 = 0;
		c2 = 0;

		runnable = () -> {
			for (int i = 0; i < numberOfIterations; i++) {
				synchronized (LOCK) { doComparison(); }
			}
		};
		startThreads();
		joinThreads();
		threadList.clear();
	}

	private void doComparison() {
		System.out.printf("%s %d %d%n", (c1 == c2), c1, c2);
		try {
			c1++;
			Thread.sleep(pause);
			c2++;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Task t = new Task(2, 5, 10);
		t.compare();
		System.out.println("~~~");
		t.compareSync();
	}
}
