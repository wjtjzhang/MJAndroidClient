package com.lele.entity;

public enum CartType {
	W(1), T(2), S(3);

	private int sequence;
	
	CartType(int sequence) {
		this.sequence = sequence;
	}

	public int getSequence() {
		return sequence;
	}
}
