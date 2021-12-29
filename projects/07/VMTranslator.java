package nand2tetris.vm;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class VMTranslator {

	public static void main(String args[]) throws Exception {
		Files.write(outputPath(args[0]), new Parser(args[0]).parse());
	}

	private static Path outputPath(String inputFileName) {
		return Paths.get(getFileName(inputFileName) + ".asm");
	}

	private static String getFileName(String fileNameWithFileNameExtension) {
		return fileNameWithFileNameExtension.split("\\.")[0];
	}
}
