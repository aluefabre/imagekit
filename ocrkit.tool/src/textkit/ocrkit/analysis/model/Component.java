package textkit.ocrkit.analysis.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;

public class Component {

	private static final int EDGE_THRESHOLD = 1;
	
	public double avgR = 0;
	public double avgG = 0;
	public double avgB = 0;
	
	public Map<String, Pixel> pixelMap = new HashMap<String, Pixel>();
	
	public int leftMost = Integer.MAX_VALUE;
	public int rightMost = Integer.MIN_VALUE;
	public int topMost = Integer.MAX_VALUE;
	public int bottomMost = Integer.MIN_VALUE;
	
	public Component(){
		
	}
	
	public void add(Pixel pixel) {
		if(pixel.x<leftMost){
			leftMost = pixel.x;
		}
		
		if(pixel.x>rightMost){
			rightMost = pixel.x;
		}
		
		if(pixel.y<topMost){
			topMost = pixel.y;
		}
		
		if(pixel.y>bottomMost){
			bottomMost = pixel.y;
		}
		
		double count = pixelMap.size()+1;
		double sumR = (avgR*pixelMap.size() + pixel.data.red);
		avgR = sumR/count;
		
		double sumG = (avgG*pixelMap.size() + pixel.data.green);
		avgG = sumG/count;
		
		double sumB = (avgB*pixelMap.size() + pixel.data.blue);
		avgB = sumB/count;
		
		pixelMap.put(pixel.asKey(), pixel);
		
	}


	public Rectangle getRectangle(){
		return new Rectangle(this.leftMost, this.topMost,
				this.rightMost-this.leftMost,
				this.bottomMost-this.topMost);
	}

	public RGB getRGB() {
		return new RGB((int)avgR, (int)avgG, (int)avgB);
	}

	public void merge(Component other) {
		if(other.leftMost<leftMost){
			leftMost = other.leftMost;
		}
		
		if(other.rightMost>rightMost){
			rightMost = other.rightMost;
		}
		
		if(other.topMost<topMost){
			topMost = other.topMost;
		}
		
		if(other.bottomMost>bottomMost){
			bottomMost = other.bottomMost;
		}
		
		double count = pixelMap.size()+other.pixelMap.size();
		double sumR = (avgR*pixelMap.size() + other.avgR*other.pixelMap.size());
		avgR = sumR/count;
		
		double sumG = (avgG*pixelMap.size() + other.avgG*other.pixelMap.size());
		avgG = sumG/count;
		
		double sumB = (avgB*pixelMap.size() + other.avgB*other.pixelMap.size());
		avgB = sumB/count;
		
		for(Pixel pixel : other.pixelMap.values()){
			pixelMap.put(pixel.asKey(), pixel);
		}
		
	}

	public boolean isFatEnough() {
//		int border = getRectangle().width + getRectangle().height;
//		if(border*2 > this.pixels.size()){
//			return false;
//		}
		return true;
	}

	public int size() {
		return pixelMap.size();
	}

	public Collection<? extends Pixel> getPixels() {
		return pixelMap.values();
	}

	public Pixel getPixel(int x, int y) {
		return pixelMap.get(x+"_"+y);
	}

	public int getWidth() {
		return getRectangle().width;
	}

	public int getHeight() {
		return getRectangle().height;
	}

}
