package textkit.ocrkit.analysis;

import java.util.ArrayList;
import java.util.List;

import textkit.ocrkit.analysis.model.Component;

public class BlockAnalysis {
	
	public static List<Component> blockAnalysis(List<Component> components){
		List<Component> result = new ArrayList<Component>();
		result.addAll(components);
		for(Component component1 : components){
			for(Component component2 : components){
				if(component1!=component2){
					if(component1.getRectangle().contains(component2.leftMost, component2.topMost)
							&& component1.getRectangle().contains(component2.rightMost, component2.bottomMost)){
						result.remove(component2);
					}
				}
			}
		}
		return result;
	}
	
}
