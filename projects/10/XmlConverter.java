package nand2tetris.compiler;

public class XmlConverter {

	public static String convertTag(String tagName, String value) {
		StringBuilder sb = new StringBuilder();
		sb.append("<");
		sb.append(tagName);
		sb.append(">");
		sb.append(value);
		sb.append("</");
		sb.append(tagName);
		sb.append(">");
		return sb.toString();
	}

}
