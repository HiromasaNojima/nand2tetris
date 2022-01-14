package nand2tetris.compiler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class JackTokenizer {

	private List<String> lines;

	private List<Token> tokens = new ArrayList<>();

	private static final List<String> SYMBOLS = List.of("{", "}", "(", ")", "[", "]", ".",
			",", ";", "+", "-", "*", "/", "&", "|", "<", ">", "=", "~");

	private static final List<Character> SYMBOLS_CHAR = List.of('{', '}', '(', ')', '[', ']', '.',
			',', ';', '+', '-', '*', '/', '&', '|', '<', '>', '=', '~');

	private static final Set<String> KEYWORDS = Set.of("class", "constructor",
			"function", "method", "field", "static", "var", "int", "char",
			"boolean", "void", "true", "false", "null", "this", "let", "do",
			"if", "else", "while", "return");

	public static final String IDENTIFIER = "^[a-zA-Z_]{1}[a-zA-Z0-9_]*";

	public JackTokenizer(String filePath) throws IOException {
		try {
			this.lines = Files.readAllLines(Paths.get(filePath));
		} catch (IOException e) {
			System.out.println("Failed to read file.");
			throw e;
		}
	}

	public List<Token> tokenize() {
		List<String> needLines = this.getNeedLines();
		List<String> commentRemoved = removeComments(needLines);

		for (String line : commentRemoved) {
			List<String> split = null;
			if (line.contains("\"")) {
				split = this.splitLineWithString(line);
			} else {
				split = this.splitBySpace(line);
			}

			for (String word : split) {
				if (word.isBlank()) {
					continue;
				}

				if (word.charAt(0) == '"') {
					tokens.add(new Token(word, TokenType.STRING_CONSTANT));
					continue;
				}

				if (word.matches("\\d+")) {
					tokens.add(new Token(word, TokenType.INTEGER_CONSTANT));
					continue;
				}

				// 複合word
				int start = 0;
				for (int i = 1; i <= word.length(); i++) {
					String substr = word.substring(start, i);
					if (KEYWORDS.contains(substr)) {
						tokens.add(new Token(substr, TokenType.KEYWORD));
						start = i;
						continue;
					} else if (SYMBOLS.contains(substr)) {
						tokens.add(new Token(substr, TokenType.SYMBOL));
						start = i;
						continue;
					}

					// 数字が出てきた場合、数字以外が出てくるまででsubstring, iを進める。
					if (substr.matches(("\\d+"))) {
						boolean noOtherDigit = true;
						for (int end = i; end <= word.length(); end++) {
							if (!Character.isDigit(word.charAt(end))) {
								tokens.add(new Token(word.substring(start, end), TokenType.INTEGER_CONSTANT));
								start = end;
								i = end;
								noOtherDigit = false;
								break;
							}
						}

						// ある地点から最後まで数字しかない。
						if (noOtherDigit) {
							tokens.add(new Token(word.substring(start, word.length()), TokenType.INTEGER_CONSTANT));
						}
						continue;
					}

					// identifier の処理,i - 1までがidentifier
					if (i == word.length()) {
						tokens.add(new Token(substr, TokenType.IDENTIFIER));
					} else if (SYMBOLS_CHAR.contains(word.charAt(i))) {
						tokens.add(new Token(substr, TokenType.IDENTIFIER));
						start = i;
						continue;
					}
				}
			}

		}

		return tokens;
	}

	private List<String> splitLineWithString(String line) {
		String beforeString = line.substring(0, line.indexOf('"'));
		String stringConstant = line.substring(line.indexOf('"'), line.lastIndexOf('"') + 1);
		String afterString = line.substring(line.lastIndexOf('"') + 1, line.length());

		List<String> split = new ArrayList<>();
		split.addAll(this.splitBySpace(beforeString));
		split.add(stringConstant);
		split.addAll(this.splitBySpace(afterString));
		return split;
	}

	private List<String> removeComments(List<String> needLines) {
		// remove block comments.
		List<String> blockcommentRemoved = new ArrayList<>();
		boolean isOnComment = false;
		for (String line : needLines) {
			if (this.isCommentEnd(line)) {
				isOnComment = false;
				continue;
			}

			if (isOnComment) {
				continue;
			}

			if (this.isCommentStart(line)) {
				if (line.contains("*/")) {
					isOnComment = false;
					continue;
				}

				isOnComment = true;
				continue;
			}

			blockcommentRemoved.add(line);
		}

		// remove comment to end line
		List<String> commentRemoved = new ArrayList<>();
		for (String line : blockcommentRemoved) {
			if (line.contains("//")) {
				line = line.substring(0, line.lastIndexOf("//"));
			}
			commentRemoved.add(line);
		}
		return commentRemoved;
	}

	private List<String> splitBySpace(String line) {
		return List.of(line.split(" "));
	}

	private boolean isCommentStart(String word) {
		if (word.length() < 2) {
			return false;
		}

		if (word.charAt(0) != '/' || word.charAt(1) != '*') {
			return false;
		}

		return true;
	}

	private boolean isCommentEnd(String word) {
		if (word.length() < 2) {
			return false;
		}

		if (word.charAt(1) != '/' || word.charAt(0) != '*') {
			return false;
		}

		return true;
	}

	private List<String> getNeedLines() {
		List<String> needLines = new ArrayList<>();
		for (String line : this.lines) {
			String trimedLine = line.trim();
			if (isCommentToEnd(trimedLine)) {
				continue;
			}

			if (trimedLine.isBlank()) {
				continue;
			}

			needLines.add(trimedLine);
		}

		return needLines;
	}

	private boolean isCommentToEnd(String word) {
		if (word.length() < 2) {
			return false;
		}

		if (word.charAt(0) != '/' || word.charAt(1) != '/') {
			return false;
		}

		return true;
	}

}
