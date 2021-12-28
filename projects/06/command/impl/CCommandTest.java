package nand2tetris.command.impl;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

public class CCommandTest {

	CCommand command = new CCommand("AMD=1;JMP");

	@Test
	void testToBiniaryString() {
		assertEquals("1110111111111111", new CCommand("AMD=1;JMP").toBinaryString());
	}

	@Test
	void testJump() throws Exception {

		Method method = CCommand.class.getDeclaredMethod("jump", String.class);
		method.setAccessible(true);

		assertEquals("000", method.invoke(command, "01"));
		assertEquals("111", method.invoke(command, ";JMP"));
	}

	@Test
	void testDest() throws Exception {
		Method method = CCommand.class.getDeclaredMethod("dest", String.class);
		method.setAccessible(true);

		assertEquals("000", method.invoke(command, "01"));
		assertEquals("101", method.invoke(command, "AM="));
	}

	@Test
	private void testComp() throws Exception {
		Method method = CCommand.class.getDeclaredMethod("comp", String.class);
		method.setAccessible(true);

		assertEquals("1110000", method.invoke(command, "D=M"));
	}

	@Test
	void compFirstIndexTest() throws Exception {
		Method method = CCommand.class.getDeclaredMethod("compFirstIndex", String.class);
		method.setAccessible(true);

		assertEquals(0, method.invoke(command, "M"));
		assertEquals(2, method.invoke(command, "D=1"));
	}

	@Test
	void compLastIndexTest() throws Exception {
		Method method = CCommand.class.getDeclaredMethod("compLastIndex", String.class);
		method.setAccessible(true);

		assertEquals(1, method.invoke(command, "0;JMP"));
		assertEquals(3, method.invoke(command, "D=M"));
	}

}
