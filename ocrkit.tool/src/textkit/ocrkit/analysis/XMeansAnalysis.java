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

public class XMeansAnalysis {


	
	public static List<Component> analysis(Component imageData){
		List<Component> components = new ArrayList<Component>();
		try {
			int width = imageData.getWidth();
			int height = imageData.getHeight();
			
			FastVector attribInfo = new FastVector();
			attribInfo.addElement(new Attribute("R"));
			attribInfo.addElement(new Attribute("G"));
			attribInfo.addElement(new Attribute("B"));
			Instances instances = new Instances("Pixels", attribInfo , 0);
			for(int x=0;x<width;x++){
				for(int y=0;y<height;y++){
					RGB rgb = imageData.getPixel(x, y).getRGB();
					
					Instance instance = new Instance(3);
					instance.setValue(0, rgb.red);
					instance.setValue(1, rgb.green);
					instance.setValue(2, rgb.blue);
					instances.add(instance);
				}
			}

			FarthestFirst cluster = new FarthestFirst();
			cluster.setNumClusters(2);
			cluster.buildClusterer(instances);
			    

		    for(int i=0;i<cluster.numberOfClusters();i++){
		        Component component = new Component();
		        components.add(component);
		    }	    

		    for(int x=0;x<width;x++){
				for(int y=0;y<height;y++){
					RGB rgb = imageData.getPixel(x, y).getRGB();
					
					Instance instance = new Instance(3);
					instance.setValue(0, rgb.red);
					instance.setValue(1, rgb.green);
					instance.setValue(2, rgb.blue);
					int assignment = cluster.clusterInstance(instance);
			    	Component component = components.get(assignment);
			    	Pixel pixel = new Pixel(x, y, rgb);
			    	component.add(pixel);
				}
			}
		    
		    ClusterEvaluation eval = new ClusterEvaluation();
		    eval.setClusterer(cluster);
		    eval.evaluateClusterer(instances);
		    
	        System.out.println("Cluster Evaluation:"+eval.clusterResultsToString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		return components;
	}

	
}
