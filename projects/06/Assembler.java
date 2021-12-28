package nand2tetris;

import static org.junit.Assert.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

public class Assembler {

	public static void main(String args[]) throws Exception {
		Files.write(outputPath(args[0]), new Parser(args[0]).parse());
	}

	private static Path outputPath(String inputFileName) {
		return Paths.get(getFileName(inputFileName) + ".hack");
	}

	private static String getFileName(String fileNameWithFileNameExtension) {
		return fileNameWithFileNameExtension.split("\\.")[0];
	}

	@Test
	void testGetFileName() {
		assertEquals("Prog", getFileName("Prog.asm"));
	}

}
