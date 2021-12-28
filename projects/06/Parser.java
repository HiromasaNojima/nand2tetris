package nand2tetris;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import nand2tetris.command.Command;
import nand2tetris.command.impl.ACommand;
import nand2tetris.command.impl.CCommand;

public class Parser {

	private List<String> lines;

	private Map<String, Integer> symbolTable = new HashMap<>() {
		{
			put("SP", 0);
			put("LCL", 1);
			put("ARG", 2);
			put("THIS", 3);
			put("THAT", 4);
			put("R0", 0);
			put("R1", 1);
			put("R2", 2);
			put("R3", 3);
			put("R4", 4);
			put("R5", 5);
			put("R6", 6);
			put("R7", 7);
			put("R8", 8);
			put("R9", 9);
			put("R10", 10);
			put("R11", 11);
			put("R12", 12);
			put("R13", 13);
			put("R14", 14);
			put("R15", 15);
			put("SCREEN", 16384);
			put("KBD", 24576);
		}
	};

	private int variableAddress = 16;

	public Parser(String filePath) throws IOException {
		try {
			this.lines = Files.readAllLines(Paths.get(filePath));
		} catch (IOException e) {
			System.out.println("Failed to read file.");
			throw e;
		}
	}

	public List<String> parse() {
		List<String> trimedLines = trim();
		this.putSymbols(trimedLines);
		return trimedLines.stream()
				.filter(line -> line.charAt(0) != '(')
				.map(line -> this.judgeCommand(line).toBinaryString())
				.collect(Collectors.toList());
	}

	private void putSymbols(List<String> trimedLines) {
		int address = 0;
		for (String line : trimedLines) {
			if (this.isLabel(line)) {
				symbolTable.put(this.getLabel(line), address);
			} else {
				address++;
			}
		}
	}

	private boolean isLabel(String line) {
		return line.charAt(0) == '(';
	}

	private String getLabel(String line) {
		return line.substring(1, line.length() - 1);
	}

	private List<String> trim() {
		return this.lines.stream().filter(line -> !this.skipLine(line))
				.map(line -> this.trimNeedlessString(line))
				.collect(Collectors.toList());
	}

	private boolean skipLine(String line) {
		if (line.isEmpty()) {
			return true;
		}

		if (line.charAt(0) == '/') {
			return true;
		}

		return false;
	}

	private String trimNeedlessString(String line) {
		StringBuilder result = new StringBuilder();
		line.chars().mapToObj(c -> (char) c).takeWhile(c -> c != '/').filter(c -> c != ' ')
				.forEach(c -> result.append(c));
		return result.toString();
	}

	private Command judgeCommand(String line) {
		if (line.charAt(0) == '@') {
			return new ACommand(toSymbolResolvedLine(line));
		} else {
			return new CCommand(line);
		}
	}

	private String toSymbolResolvedLine(String line) {
		String symbol = line.substring(1, line.length());
		if (symbol.matches("[+-]?\\d*(\\.\\d+)?")) {
			// no need to symbol resolve.
			return line;
		}

		Integer address = null;
		if (symbolTable.containsKey(symbol)) {
			address = symbolTable.get(symbol);
		} else {
			address = variableAddress++;
			symbolTable.put(symbol, address);
		}

		return "@" + address;
	}
}
