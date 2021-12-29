package nand2tetris.vm;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Parser {

	private final List<String> lines;

	private final List<String> parsedLines = new ArrayList<>();

	private int lableNum = 0;

	private final String fileName;

	public Parser(String filePath) throws IOException {
		try {
			this.lines = Files.readAllLines(Paths.get(filePath));
			String[] split = filePath.split("/");
			this.fileName = split[split.length - 1];
		} catch (IOException e) {
			System.out.println("Failed to read file.");
			throw e;
		}
	}

	public List<String> parse() throws Exception {
		List<String> linesNeeded = lines.stream().filter(line -> this.needLine(line))
				.collect(Collectors.toList());

		for (String line : linesNeeded) {
			String[] split = this.splitBySpace(line);
			switch (split[0]) {
			case "push":
				this.translatePush(split);
				break;
			case "pop":
				this.translatePop(split);
				break;
			case "add":
				this.translateAdd();
				break;
			case "sub":
				this.translateSub();
				break;
			case "neg":
				this.translateNeg();
				break;
			case "eq":
				this.translateEq();
				break;
			case "gt":
				this.translateGt();
				break;
			case "lt":
				this.translateLt();
				break;
			case "and":
				this.translateAnd();
				break;
			case "or":
				this.translateOr();
				break;
			case "not":
				this.translateNot();
				break;
			default:
				throw new Exception("command is not identified.");
			}
		}

		return parsedLines;
	}

	private boolean needLine(String line) {
		if (line.isEmpty()) {
			return false;
		}

		if (line.charAt(0) == '/') {
			return false;
		}

		return true;
	}

	private void translatePush(String[] split) {
		if (isSegmentConstant(split[1])) {
			parsedLines.add("@" + split[2]);
			parsedLines.add("D=A");
		} else if (isSegmentLocal(split[1])) {
			parsedLines.add("@LCL");
			parsedLines.add("D=M");
			parsedLines.add("@" + split[2]);
			parsedLines.add("A=D+A");
			parsedLines.add("D=M");
		} else if (isSegmentThat(split[1])) {
			parsedLines.add("@THAT");
			parsedLines.add("D=M");
			parsedLines.add("@" + split[2]);
			parsedLines.add("A=D+A");
			parsedLines.add("D=M");
		} else if (isSegmentArgument(split[1])) {
			parsedLines.add("@ARG");
			parsedLines.add("D=M");
			parsedLines.add("@" + split[2]);
			parsedLines.add("A=D+A");
			parsedLines.add("D=M");
		} else if (isSegmentThis(split[1])) {
			parsedLines.add("@THIS");
			parsedLines.add("D=M");
			parsedLines.add("@" + split[2]);
			parsedLines.add("A=D+A");
			parsedLines.add("D=M");
		} else if (isSegmentTemp(split[1])) {
			parsedLines.add("@R5");
			parsedLines.add("D=A");
			parsedLines.add("@" + split[2]);
			parsedLines.add("A=D+A");
			parsedLines.add("D=M");
		} else if (isSegmentPointer(split[1])) {
			if (split[2].equals("0")) {
				parsedLines.add("@THIS");
			} else {
				parsedLines.add("@THAT");
			}
			parsedLines.add("D=M");
		} else if (isSegmentStatic(split[1])) {
			parsedLines.add("@" + fileName + split[2]);
			parsedLines.add("D=M");
		}

		parsedLines.add("@SP");
		parsedLines.add("A=M");
		parsedLines.add("M=D");

		parsedLines.add("@SP");
		parsedLines.add("M=M+1");
	}

	private void translatePop(String[] split) {
		if (isSegmentLocal(split[1])) {
			parsedLines.add("@LCL");
			parsedLines.add("D=M");
			parsedLines.add("@" + split[2]);
			parsedLines.add("D=D+A");
		} else if (isSegmentArgument(split[1])) {
			parsedLines.add("@ARG");
			parsedLines.add("D=M");
			parsedLines.add("@" + split[2]);
			parsedLines.add("D=D+A");
		} else if (isSegmentThis(split[1])) {
			parsedLines.add("@THIS");
			parsedLines.add("D=M");
			parsedLines.add("@" + split[2]);
			parsedLines.add("D=D+A");
		} else if (isSegmentThat(split[1])) {
			parsedLines.add("@THAT");
			parsedLines.add("D=M");
			parsedLines.add("@" + split[2]);
			parsedLines.add("D=D+A");
		} else if (isSegmentTemp(split[1])) {
			parsedLines.add("@R5");
			parsedLines.add("D=A");
			parsedLines.add("@" + split[2]);
			parsedLines.add("D=D+A");
		} else if (isSegmentPointer(split[1])) {
			if (split[2].equals("0")) {
				parsedLines.add("@THIS");
			} else {
				parsedLines.add("@THAT");
			}
			parsedLines.add("D=A");
		} else if (isSegmentStatic(split[1])) {
			parsedLines.add("@" + fileName + split[2]);
			parsedLines.add("D=A");
		}

		parsedLines.add("@R13");
		parsedLines.add("M=D");
		parsedLines.add("@SP");
		parsedLines.add("AM=M-1");
		parsedLines.add("D=M");
		parsedLines.add("@R13");
		parsedLines.add("A=M");
		parsedLines.add("M=D");
	}

	private void translateAdd() {
		parsedLines.add("@SP");
		parsedLines.add("AM=M-1");
		parsedLines.add("D=M");
		parsedLines.add("A=A-1");
		parsedLines.add("M=M+D");
	}

	private void translateSub() {
		parsedLines.add("@SP");
		parsedLines.add("AM=M-1");
		parsedLines.add("D=M");
		parsedLines.add("A=A-1");
		parsedLines.add("M=M-D");
	}

	private void translateAnd() {
		parsedLines.add("@SP");
		parsedLines.add("AM=M-1");
		parsedLines.add("D=M");
		parsedLines.add("A=A-1");
		parsedLines.add("M=D&M");
	}

	private void translateOr() {
		parsedLines.add("@SP");
		parsedLines.add("AM=M-1");
		parsedLines.add("D=M");
		parsedLines.add("A=A-1");
		parsedLines.add("M=D|M");
	}

	private void translateNot() {
		parsedLines.add("@SP");
		parsedLines.add("A=M-1");
		parsedLines.add("M=!M");
	}

	private void translateNeg() {
		parsedLines.add("@SP");
		parsedLines.add("A=M-1");
		parsedLines.add("M=-M");
	}

	private void translateEq() {
		parsedLines.add("@SP");
		parsedLines.add("AM=M-1");
		parsedLines.add("D=M");
		parsedLines.add("A=A-1");
		parsedLines.add("D=M-D");

		parsedLines.add("@EQTRUE" + lableNum);
		parsedLines.add("D;JEQ");

		parsedLines.add("@SP");
		parsedLines.add("A=M-1");
		parsedLines.add("M=0");

		parsedLines.add("@EQAFTER" + lableNum);
		parsedLines.add("0;JMP");

		parsedLines.add("(EQTRUE" + lableNum + ")");
		parsedLines.add("@SP");
		parsedLines.add("A=M-1");
		parsedLines.add("M=-1");

		parsedLines.add("(EQAFTER" + lableNum + ")");
		lableNum++;
	}

	private void translateLt() {
		parsedLines.add("@SP");
		parsedLines.add("AM=M-1");
		parsedLines.add("D=M");
		parsedLines.add("A=A-1");
		parsedLines.add("D=M-D");

		parsedLines.add("@LTTRUE" + lableNum);
		parsedLines.add("D;JLT");

		parsedLines.add("@SP");
		parsedLines.add("A=M-1");
		parsedLines.add("M=0");

		parsedLines.add("@LTAFTER" + lableNum);
		parsedLines.add("0;JMP");

		parsedLines.add("(LTTRUE" + lableNum + ")");
		parsedLines.add("@SP");
		parsedLines.add("A=M-1");
		parsedLines.add("M=-1");

		parsedLines.add("(LTAFTER" + lableNum + ")");
		lableNum++;
	}

	private void translateGt() {
		parsedLines.add("@SP");
		parsedLines.add("AM=M-1");
		parsedLines.add("D=M");
		parsedLines.add("A=A-1");
		parsedLines.add("D=M-D");

		parsedLines.add("@GTTRUE" + lableNum);
		parsedLines.add("D;JGT");

		parsedLines.add("@SP");
		parsedLines.add("A=M-1");
		parsedLines.add("M=0");

		parsedLines.add("@GTAFTER" + lableNum);
		parsedLines.add("0;JMP");

		parsedLines.add("(GTTRUE" + lableNum + ")");
		parsedLines.add("@SP");
		parsedLines.add("A=M-1");
		parsedLines.add("M=-1");

		parsedLines.add("(GTAFTER" + lableNum + ")");
		lableNum++;
	}

	private String[] splitBySpace(String line) {
		return line.split(" ");
	}

	private boolean isSegmentConstant(String segment) {
		return segment.equals("constant");
	}

	private boolean isSegmentLocal(String segment) {
		return segment.equals("local");
	}

	private boolean isSegmentArgument(String segment) {
		return segment.equals("argument");
	}

	private boolean isSegmentThis(String segment) {
		return segment.equals("this");
	}

	private boolean isSegmentThat(String segment) {
		return segment.equals("that");
	}

	private boolean isSegmentTemp(String segment) {
		return segment.equals("temp");
	}

	private boolean isSegmentPointer(String segment) {
		return segment.equals("pointer");
	}

	private boolean isSegmentStatic(String segment) {
		return segment.equals("static");
	}
}
