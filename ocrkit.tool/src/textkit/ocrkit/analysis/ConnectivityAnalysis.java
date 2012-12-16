package textkit.ocrkit.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGB;

import textkit.ocrkit.analysis.model.Component;
import textkit.ocrkit.analysis.model.Pixel;
import textkit.ocrkit.analysis.model.RGBColor;

public class ConnectivityAnalysis {

	public static List<Component> analysis(Component imageData){
		List<Component> components = new ArrayList<Component>();
		
		int width = imageData.getWidth();
		int height = imageData.getHeight();
		
		Map<String, Pixel> unprocessedPixels = new HashMap<String, Pixel>();
		for(int x=0;x<width;x++){
			for(int y=0;y<height;y++){
				RGB rgb = imageData.getPixel(x, y).getRGB();
				Pixel pixel = new Pixel(x, y, rgb);
				unprocessedPixels.put(pixel.asKey(), pixel);
			}
		}
		
		while(unprocessedPixels.size()>0){
			Entry<String, Pixel> randomEntry = unprocessedPixels.entrySet().iterator().next();
			Pixel randomPixel = unprocessedPixels.remove(randomEntry.getKey());
			Component component = new Component();
			component.add(randomPixel);
			growComponent(component, unprocessedPixels);
			if(component.size()>10){
				if(component.isFatEnough()){
					components.add(component);
				}
			}
		}
		
		return components;
	}

	private static void growComponent(Component component, Map<String, Pixel> unprocessedPixels) {
		List<Pixel> newPixels = new ArrayList<Pixel>();
		newPixels.addAll(component.getPixels());
		while(newPixels.size()>0){
			List<Pixel> edgePixels = new ArrayList<Pixel>();
			for(Pixel pixel : newPixels){
				List<Pixel> adjustedPixels = adjustedPixels(pixel, unprocessedPixels, component);
				edgePixels.addAll(adjustedPixels);
			}
			newPixels.clear();
			for(Pixel edgePixel : edgePixels){
				if(unprocessedPixels.containsKey(edgePixel.asKey())){
					component.add(edgePixel);
					unprocessedPixels.remove(edgePixel.asKey());
					newPixels.add(edgePixel);
				}
			}
		}	
	}
	
	private static List<Pixel> adjustedPixels(Pixel point, Map<String, Pixel> unprocessedPixels, Component component) {
		List<Pixel> result = new ArrayList<Pixel>();
		String[] asKeys = new String[]{
				asKey(point.x+1, point.y),
				asKey(point.x-1, point.y),
				asKey(point.x+1, point.y+1),
				asKey(point.x-1, point.y+1),
				asKey(point.x+1, point.y-1),
				asKey(point.x-1, point.y-1),
				asKey(point.x, point.y+1),
				asKey(point.x, point.y-1),
				
		};
		for(String asKey : asKeys){
			if(unprocessedPixels.containsKey(asKey)){
				Pixel edgePixel = unprocessedPixels.get(asKey);
				if(RGBColor.similarEnoughWith(edgePixel.data, component.getRGB(), 50)){
					result.add(edgePixel);
				}
			}
		}
		
		return result;
	}

	private static String asKey(int x, int y) {
		return x+"_"+y;
	}
	
}
