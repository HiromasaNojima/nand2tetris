package nand2tetris.compiler;

public enum TokenType {

	KEYWORD("keyword"), SYMBOL("symbol"), INTEGER_CONSTANT("integerConstant"), STRING_CONSTANT(
			"stringConstant"), IDENTIFIER("identifier");

	private final String name;

	private TokenType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
