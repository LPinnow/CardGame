package model;

abstract public class CardCollectionImmutable {

	final private int size;
	
	public CardCollectionImmutable(int size) {
		this.size = size;
	}
	
	public int size() {
		return size;
	}

	
}
