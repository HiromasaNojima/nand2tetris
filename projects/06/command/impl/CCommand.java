package nand2tetris.command.impl;

import java.util.HashMap;
import java.util.Map;

import nand2tetris.command.Command;

public class CCommand extends Command {

	public CCommand(String line) {
		super(line);
	}

	private static final String NULL = "000";

	@Override
	public String toBinaryString() {
		return new StringBuilder("111").append(this.comp(this.line)).append(this.dest(this.line))
				.append(this.jump(this.line))
				.toString();
	}

	private String jump(String str) {
		int jumpCharIndex = str.indexOf(';');
		if (jumpCharIndex == -1) {
			return NULL;
		}

		return JMP.valueOf(str.substring(jumpCharIndex + 1)).binaryString;
	}

	enum JMP {

		JGT("001"), JEQ("010"), JGE("011"), JLT("100"), JNE("101"), JLE("110"), JMP("111");

		private final String binaryString;

		private JMP(String binaryString) {
			this.binaryString = binaryString;
		}
	}

	private String dest(String str) {
		int destCharIndex = str.indexOf('=');
		if (destCharIndex == -1) {
			return NULL;
		}

		return DEST.valueOf(str.substring(0, destCharIndex)).binaryString;
	}

	enum DEST {
		M("001"), D("010"), MD("011"), A("100"), AM("101"), AD("110"), AMD("111");

		private final String binaryString;

		private DEST(String binaryString) {
			this.binaryString = binaryString;
		}
	}

	private String comp(String str) {
		String comp = str.substring(compFirstIndex(str), compLastIndex(str));
		return Comp.of(comp).binaryValue;
	}

	private int compFirstIndex(String str) {
		if (str.contains("=")) {
			return str.indexOf('=') + 1;
		} else {
			return 0;
		}
	}

	private int compLastIndex(String str) {
		if (str.contains(";")) {
			return str.indexOf(';');
		} else {
			return str.length();
		}
	}

	enum Comp {
		//@formatter:off
		ZERO("0", "0101010"), ONE("1", "0111111"), MINUS_ONE("-1", "0111010"), D("D", "0001100"), A("A", "0110000"),
		NEGATIVE_D("!D", "0001101"), NEGATIVE_A("!A", "0110001"), MINUS_D("-D","0001111"), MINUS_A("-A", "0110011"),
		D_PLUS_ONE("D+1", "0011111"), A_PLUS_ONE("A+1", "0110111"), D_MINUS_ONE("D-1", "0001110"), A_MINUS_ONE("A-1", "0110010"),
		D_PLUS_A("D+A", "0000010"), D_MINUS_A("D-A", "0010011"), A_MINUS_D("A-D", "0000111"), D_AND_A("D&A", "0000000"),
		D_OR_A("D|A", "0010101"), M("M", "1110000"), NEGATIVE_M("!M", "1110001"), MINUS_M("-M", "1110011"),
		M_PLUS_ONE("M+1", "1110111"), M_MINUS_ONE("M-1", "1110010"), D_PLUS_M("D+M", "1000010"), D_MINUS_M("D-M", "1010011"),
		M_MINUS_D("M-D", "1000111"), D_AND_M("D&M", "1000000"), D_OR_M("D|M", "1010101");
		//@formatter:on

		private static final Map<String, Comp> valueMap = createMap();

		private static Map<String, Comp> createMap() {
			Map<String, Comp> map = new HashMap<String, Comp>();
			Comp[] values = Comp.values();
			for (Comp comp : values) {
				map.put(comp.value, comp);
			}
			return map;
		}

		private final String value;
		private final String binaryValue;

		private Comp(String value, String binaryValue) {
			this.value = value;
			this.binaryValue = binaryValue;
		}

		public static Comp of(String value) {
			return valueMap.get(value);
		}
	}
}
