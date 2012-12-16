
package textkit.ocrkit.analysis;

import org.eclipse.swt.graphics.RGB;

import textkit.ocrkit.analysis.model.Component;
import textkit.ocrkit.analysis.model.Pixel;

public class SobelFilter {
	int sobscale = 1;
	short offsetval = 0;
	
	public Component filter(Component input) {
		Component output = new Component();

		int width = input.getWidth();
		int height = input.getHeight();

		for (int y = 0; y < height - 2; y++) {
			for (int x = 0; x < width - 2; x++) {
				Pixel a = input.getPixel(x, y);
				Pixel b = input.getPixel(x + 1, y);
				Pixel c = input.getPixel(x + 2, y);
				Pixel d = input.getPixel(x, y + 1);
				Pixel e = input.getPixel(x + 2, y + 1);
				Pixel f = input.getPixel(x, y + 2);
				Pixel g = input.getPixel(x + 1, y + 2);
				Pixel h = input.getPixel(x + 2, y + 2);

				Pixel o = calculate(x, y, a, b, c, d, e, f, g, h);
				output.add(o);
			}
		}

		// finished
		return output;
	}

	private Pixel calculate(int x, int y, Pixel a, Pixel b, Pixel c, Pixel d, Pixel e, Pixel f, Pixel g, Pixel h) {

		short red = calculate(a, b, c, d, e, f, g, h, new ColorPicker(){
			@Override
			public int pick(Pixel pixel) {
				if(pixel == null){
					System.out.println("");
				}
				return pixel.getRGB().red;
			}
		});
		short green = calculate(a, b, c, d, e, f, g, h, new ColorPicker(){
			@Override
			public int pick(Pixel pixel) {
				return pixel.getRGB().green;
			}
		});
		short blue = calculate(a, b, c, d, e, f, g, h, new ColorPicker(){
			@Override
			public int pick(Pixel pixel) {
				return pixel.getRGB().blue;
			}
		});

		Pixel result = new Pixel(x, y, new RGB(red, green, blue));
		return result;
	}

	private short calculate(Pixel a, Pixel b, Pixel c, Pixel d, Pixel e, Pixel f, Pixel g, Pixel h, ColorPicker picker) {
		int hor = (picker.pick(a) + picker.pick(d) + picker.pick(f))
				- (picker.pick(c) + picker.pick(e) + picker.pick(h));
		if (hor < 0)
			hor = -hor;
		int vert = (picker.pick(a) + picker.pick(b) + picker.pick(c))
				- (picker.pick(f) + picker.pick(g) + picker.pick(h));
		if (vert < 0)
			vert = -vert;
		short gc = (short) (sobscale * (hor + vert));
		gc = (short) (gc + offsetval);
		if (gc > 255)
			gc = 255;
		return gc;
	}

	public interface ColorPicker{
		public int pick(Pixel pixel);
	}
}