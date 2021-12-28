package nand2tetris.command;

public abstract class Command {

	protected String line;

	protected Command(String line) {
		this.line = line;
	}

	public abstract String toBinaryString();
}
