package nand2tetris.compiler;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class JackAnalyzer {

	public static void main(String args[]) throws Exception {
		JackTokenizer tokenizer = new JackTokenizer(args[0]);
		//		Files.write(outputTXmlPath(args[0]), TokensXmlConverter.toXmlString(tokenizer.tokenize()));
		List<String> parsed = new CompilationEngine(tokenizer.tokenize()).compileClass();
		Files.write(outputXmlPath(args[0]), parsed);
	}

	private static Path outputTXmlPath(String inputFileName) {
		return Paths.get(getFileName(inputFileName) + "T.xml");
	}

	private static Path outputXmlPath(String inputFileName) {
		return Paths.get(getFileName(inputFileName) + ".xml");
	}

	private static String getFileName(String fileNameWithFileNameExtension) {
		return fileNameWithFileNameExtension.split("\\.")[0];
	}

}
