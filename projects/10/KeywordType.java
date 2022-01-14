package nand2tetris.compiler;

import java.util.List;

public enum KeywordType {

	CLASS_VAR_DEC, SUBROUTINE_DEC, VAR_DEC, CONSTANT, OP;

	private final static List<String> OP_LIST = List.of("+", "-", "*", "/", "&", "|", "<", ">", "=");

	public static KeywordType of(String keyword) {
		if (keyword.equals("field") || keyword.equals("static")) {
			return CLASS_VAR_DEC;
		} else if (keyword.equals("constructor") || keyword.equals("function") || keyword.equals("method")) {
			return SUBROUTINE_DEC;
		} else if (keyword.equals("var")) {
			return VAR_DEC;
		} else if (keyword.equals("true") || keyword.equals("false") || keyword.equals("null")
				|| keyword.equals("this")) {
			return CONSTANT;
		} else if (OP_LIST.contains(keyword)) {
			return OP;
		} else {
			return null;
		}

	}

}
