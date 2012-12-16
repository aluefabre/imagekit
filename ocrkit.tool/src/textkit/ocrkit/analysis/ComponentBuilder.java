package textkit.ocrkit.analysis;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGB;

import textkit.ocrkit.analysis.model.Component;
import textkit.ocrkit.analysis.model.Pixel;

public class ComponentBuilder {

	public static Component build(Image currentImage) {
		Component result = new Component();
		ImageData imageData = currentImage.getImageData();
		int width = imageData.width;
		int height = imageData.height;
		for(int x=0;x<width;x++){
			for(int y=0;y<height;y++){
				RGB rgb = imageData.palette.getRGB(imageData.getPixel(x, y));
				Pixel pixel = new Pixel(x, y, rgb);
				result.add(pixel);
			}
		}
		return result;
	}
	
}
