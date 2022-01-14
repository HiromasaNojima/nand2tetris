package nand2tetris.compiler;

public class Token {

	public Token(String token, TokenType tokenType) {
		this.token = token;
		this.tokenType = tokenType;
	}

	private final String token;

	private final TokenType tokenType;

	public String getToken() {
		return token;
	}

	public String getTokenXmlValue() {
		if (this.token.equals("<")) {
			return "&lt;";
		}

		if (this.token.equals(">")) {
			return "&gt;";
		}

		if (this.token.equals("&")) {
			return "&amp;";
		}

		if (this.tokenType == TokenType.STRING_CONSTANT) {
			return this.token.substring(1, this.token.length() - 1);
		}

		return token;
	}

	public TokenType getTokenType() {
		return tokenType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((token == null) ? 0 : token.hashCode());
		result = prime * result + ((tokenType == null) ? 0 : tokenType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Token other = (Token) obj;
		if (token == null) {
			if (other.token != null)
				return false;
		} else if (!token.equals(other.token))
			return false;
		if (tokenType != other.tokenType)
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Token [token=");
		builder.append(token);
		builder.append(", tokenType=");
		builder.append(tokenType);
		builder.append("]");
		return builder.toString();
	}

}
