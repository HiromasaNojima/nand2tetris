package nand2tetris.command.impl;

import static org.junit.Assert.*;

import org.junit.jupiter.api.Test;

import nand2tetris.command.Command;

public class ACommand extends Command {

	public ACommand(String line) {
		super(line);
	}

	@Override
	public String toBinaryString() {
		String addressBinary = Integer.toBinaryString(Integer.valueOf(this.line.substring(1)));
		return toACommand(addressBinary);
	}

	private String toACommand(String addressBinary) {
		// A Command[15] is 0;
		StringBuilder aCommand = new StringBuilder("0");

		int paddingLength = 15 - addressBinary.length();
		for (int i = 0; i < paddingLength; i++) {
			aCommand.append("0");
		}

		aCommand.append(addressBinary);
		return aCommand.toString();
	}

	@Test
	void test() {
		assertEquals("0000000000000010", new ACommand("@2").toBinaryString());
	}

}
