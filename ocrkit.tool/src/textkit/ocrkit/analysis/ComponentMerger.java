package textkit.ocrkit.analysis;

import java.util.ArrayList;
import java.util.List;

import textkit.ocrkit.analysis.model.Component;
import textkit.ocrkit.analysis.model.RGBColor;

public class ComponentMerger {

	public static List<Component> merge(List<Component> components){
		List<Component> result = new ArrayList<Component>();
		result.addAll(components);
		while(true){
			boolean found = false;
			Component componentx = null;
			Component componenty = null;
			outer:
			for(Component component1 : result){
				for(Component component2 : result){
					if(component1!=component2){
						if(component1.getRectangle().intersects(component2.getRectangle())){
							if(RGBColor.similarEnoughWith(component1.getRGB(), component2.getRGB(), 50)){
								found = true;
								componentx = component1;
								componenty = component2;
								break outer;
							}
						}
					}
				}
			}
			if(found){
				result.remove(componenty);
				componentx.merge(componenty);
			}else{
				break;
			}
		}
		return result;
	}
	
}
