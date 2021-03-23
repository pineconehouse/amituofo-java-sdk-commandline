package com.amituofo.cmdlet.core;

import java.text.DecimalFormat;

public class ConsoleProgressBar {
	private long minimum = 0L;

	private long maximum = 100L;

	private long barLen = 100L;

	private char showChar = '=';

	private DecimalFormat formater = new DecimalFormat("#.##%");

	public ConsoleProgressBar() {
	}

	public ConsoleProgressBar(long minimum, long maximum, long barLen) {
		this(minimum, maximum, barLen, '=');
	}

	public ConsoleProgressBar(long minimum, long maximum, long barLen, char showChar) {
		this.minimum = minimum;
		this.maximum = maximum;
		this.barLen = barLen;
		this.showChar = showChar;
	}

	public void show(long value) {
		if ((value < this.minimum) || (value > this.maximum)) {
			return;
		}

		reset();
		this.minimum = value;
		float rate = (float) (this.minimum * 1.0D / this.maximum);
		long len = (long) (rate * (float) this.barLen);
		draw(len, rate);
		if (this.minimum == this.maximum)
			afterComplete();
	}

	private void draw(long len, float rate) {
		for (int i = 0; i < len; i++) {
			System.out.print(this.showChar);
		}
		System.out.print(' ');
		System.out.print(format(rate));
	}

	private void reset() {
		System.out.print('\r');
	}

	private void afterComplete() {
		System.out.print('\n');
	}

	private String format(float num) {
		return this.formater.format(num);
	}

	public static void main(String[] args) throws InterruptedException {
		ConsoleProgressBar cpb = new ConsoleProgressBar(0L, 200L, 50L, '|');
		for (int i = 1; i <= 100; i++) {
			cpb.show(i);
			Thread.sleep(50L);
		}
	}
}
