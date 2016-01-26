package rpcdemo.rpc.common;

public class Entry<R, C> {
	
	private R row;
	private C colum;
	
	public R getRow() {
		return row;
	}
	public void setRow(R row) {
		this.row = row;
	}
	public C getColum() {
		return colum;
	}
	public void setColum(C colum) {
		this.colum = colum;
	}
	
	
}
