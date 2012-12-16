package textkit.ocrkit.analysis.model;

import org.eclipse.swt.graphics.RGB;

public class RGBColor {

	public static boolean similarEnoughWith(RGB rgb1, RGB rgb2, int singleThreshold) {
		 double a = rgb1.red-rgb2.red;
		 double b = rgb1.green-rgb2.green;
		 double c = rgb1.blue-rgb2.blue;
		    
		 double distance = Math.sqrt (a*a + b*b + c*c);
		if(distance > singleThreshold){
			return false;
		}
		return true;
	}
	
}
