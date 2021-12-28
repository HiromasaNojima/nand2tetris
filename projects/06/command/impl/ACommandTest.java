package nand2tetris.command.impl;

import static org.junit.Assert.*;

import org.junit.jupiter.api.Test;

public class ACommandTest {

	@Test
	void test() {
		assertEquals("0000000000000010", new ACommand("@2").toBinaryString());
	}

}
