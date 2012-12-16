package textkit.ocrkit.analysis;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGB;

import textkit.ocrkit.analysis.model.Component;
import textkit.ocrkit.analysis.model.Pixel;
import weka.clusterers.ClusterEvaluation;
import weka.clusterers.FarthestFirst;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class ImageFilter {
	
	public static Component smooth(Component source){
		Component result = new Component();
		int width = source.getWidth();
		int height = source.getHeight();
		for(int x=0;x<width;x++){
			for(int y=0;y<height;y++){
				Pixel target = new Pixel(x, y, new RGB(0,0,0));
				List<Pixel> nearbys = new ArrayList<Pixel>();
				for(int i=-2;i<2;i++){
					for(int j=-2;j<2;j++){
						Pixel nearby = source.getPixel(x+i, y+j);
						if(nearby!=null){
							nearbys.add(nearby);
						}						
					}
				}
				int r=0;
				int g=0;
				int b=0;
				for(Pixel nearby : nearbys){
					 r += nearby.data.red;
					 g += nearby.data.green;
					 b += nearby.data.blue;
				}
				target.data.red = r/nearbys.size();
				target.data.green = g/nearbys.size();
				target.data.blue = b/nearbys.size();
				result.add(target);
			}
		}
		return result;
	}

	public static Component sharp(Component source){
		Component result = new Component();
		int width = source.getWidth();
		int height = source.getHeight();
		for(int x=0;x<width;x++){
			for(int y=0;y<height;y++){
				Pixel temp = source.getPixel(x, y);
				Pixel target = new Pixel(x, y, new RGB(0,0,0));
				
				target.data.red = scale(source.avgR, temp.data.red);
				target.data.green = scale(source.avgG, temp.data.green);
				target.data.blue = scale(source.avgB, temp.data.blue);
				result.add(target);
			}
		}
		return result;
	}

	private static int scale(double avg, int source) {
		int target = 0;
		if(source >avg){
			target = (int)(255 - ((double)(255 - source))*0.8); 
		}else{
			target = (int)(((double)(source))*0.8); 
		}
		return target;
	}
	
	
	public static Component sobel(Component source){
		return new SobelFilter().filter(source);
	}
}
