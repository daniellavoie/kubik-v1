package com.cspinformatique.kubik.onlinesales.social;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;

public class SimpleConnectionSignUp implements ConnectionSignUp {
	private final AtomicLong userIdSequence = new AtomicLong();

	@Override
	public String execute(Connection<?> connection) {
		return Long.toString(userIdSequence.incrementAndGet());
	}
}
