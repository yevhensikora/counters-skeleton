package com.epam.rd.java.basic.topic05.task03;

import java.io.*;
import java.util.*;

import org.junit.jupiter.api.*;

/**
 * @author D. Kolesnikov
 */
public class TaskTest {

	private static final PrintStream STD_OUT = System.out;

	private final ByteArrayOutputStream baos = new ByteArrayOutputStream();

	private final PrintStream out = new PrintStream(baos);
	
	@Test
	void shouldCountersBeEqualedForEveryIterationWhenSync() {
		System.setOut(out);
		Task t = new Task(5, 5, 10);

		t.compareSync();

		out.flush();
		String actual = baos.toString();
		System.setOut(STD_OUT);

		for (String line : actual.split(System.lineSeparator())) {
			if ("false".equals(line.substring(0, line.indexOf(' ')))) {
				Assertions.fail(() -> "Not synchronized code detected: " + line);
			}
		}
	}

	@Test
	void gshouldCountersBeNotEqualedForSomeIterationWhenNotSync() {
		System.setOut(out);
		Task t = new Task(5, 5, 10);

		t.compare();

		out.flush();
		String actual = baos.toString();
		System.setOut(STD_OUT);

		for (String line : actual.split(System.lineSeparator())) {
			if ("false".equals(line.substring(0, line.indexOf(' ')))) {
				return;
			}
		}
		Assertions.fail(() -> "Synchronized code detected:\n" + actual);
	}

	@Test
	void shouldCountersBeReseted() {
		Task t = new Task(5, 5, 10);

		t.compare();
		System.setOut(out);
		t.compareSync();

		out.flush();
		String actual = baos.toString();
		System.setOut(STD_OUT);

		for (String line : actual.split(System.lineSeparator())) {
			if ("false".equals(line.substring(0, line.indexOf(' ')))) {
				Assertions.fail(() -> "It looks like counters not reseted");
			}
		}
	}

	@Test
	void shouldBeAtLeast5WorkingThreadsWhenCompare() {
		List<String> threadList = new ArrayList<>();
		Task t = new Task(5, 5, 10);

		new Thread() {
			public void run() {
				setName("task-runner");
				try {
					Thread.sleep(25);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				Thread.getAllStackTraces().keySet().stream()
					.map(t -> t.getName())
					.filter(name -> name.startsWith("Thread-"))
					.forEach(threadList::add);
			}
		}.start();

		t.compare();

		long threadsCount = threadList.size();
		Assertions.assertTrue(threadsCount >= 5, 
				() -> "'compare' must use at least 5 threads, detected threads: " + threadList);
	}

	@Test
	void shouldBeAtLeast5WorkingThreadsWhenCompareSync() {
		List<String> threadList = new ArrayList<>();
		Task t = new Task(5, 5, 10);

		new Thread() {
			public void run() {
				setName("task-runner");
				try {
					Thread.sleep(25);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				Thread.getAllStackTraces().keySet().stream()
					.map(t -> t.getName())
					.filter(name -> name.startsWith("Thread-"))
					.forEach(threadList::add);
			}
		}.start();

		t.compareSync();

		long threadsCount = threadList.size();
		Assertions.assertTrue(threadsCount >= 5, 
				() -> "'compareSync' must use at least 5 threads, detected threads: " + threadList);
	}
	
}
