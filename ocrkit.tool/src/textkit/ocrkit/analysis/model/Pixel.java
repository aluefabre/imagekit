package textkit.ocrkit.analysis.model;

import org.eclipse.swt.graphics.RGB;

public class Pixel {
	public int x;
	public int y;
	public RGB data;
	public Pixel(int x, int y, RGB rgb) {
		super();
		this.x = x;
		this.y = y;
		this.data = rgb;
	}

	public String asKey(){
		return x+"_"+y;
	}
	
	public String toString(){
		return "("+x+","+y+"):"+data;
	}
	
	public static boolean adjustedTo(Pixel exist, Pixel pixel) {
		if(Math.abs(exist.x-pixel.x)<=1
				&& Math.abs(exist.y-pixel.y)<=1){
			return true;
		}
		return false;
	}

	public RGB getRGB() {
		return data;
	}
	

}
