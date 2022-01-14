package nand2tetris.compiler;

public enum Statement {

	LET, IF, WHILE, DO, RETURN;

	public static Statement of(String keyword) {
		if (keyword.equals("let")) {
			return LET;
		} else if (keyword.equals("if")) {
			return IF;
		} else if (keyword.equals("while")) {
			return WHILE;
		} else if (keyword.equals("do")) {
			return DO;
		} else if (keyword.equals("return")) {
			return RETURN;
		} else {
			return null;
		}
	}

}
