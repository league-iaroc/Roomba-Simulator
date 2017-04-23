package org.jointheleague;

public class Path {
	private int row;
	private int col;

	public Path(int row, int col) {
		this.setRow(row);
		this.setColumn(col);
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return col;
	}

	public void setColumn(int col) {
		this.col = col;
	}
}
