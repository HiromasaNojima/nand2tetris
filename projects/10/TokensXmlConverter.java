package nand2tetris.compiler;

import java.util.ArrayList;
import java.util.List;

public class TokensXmlConverter {

	public static List<String> toXmlString(List<Token> tokens) {
		List<String> xml = new ArrayList<>();
		xml.add("<tokens>");
		tokens.forEach(token -> {
			xml.add(XmlConverter.convertTag(token.getTokenType().getName(), token.getTokenXmlValue()));
		});
		xml.add("</tokens>");
		return xml;
	}

}
