package textkit.ocrkit.gui;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;

import textkit.ocrkit.analysis.ComponentBuilder;
import textkit.ocrkit.analysis.model.Component;
import textkit.ocrkit.analysis.model.Pixel;

public class GUICanvasController {
	Canvas canvas = null;
	Map<String, Image> imagesMap = new HashMap<String, Image>();
	File currentFile = null;
	ScrolledComposite rightMainGroup = null;

	GUIController controller;
	Component currentComponent = null;
	
	public GUICanvasController(GUIController controller){
		this.controller = controller;
	}
	
	public void createCanvas(Group rightGroup) {
		rightMainGroup = new ScrolledComposite(rightGroup, SWT.BORDER
			        | SWT.H_SCROLL | SWT.V_SCROLL);
		
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.verticalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		rightMainGroup.setLayoutData(gridData);

		canvas = new Canvas(rightMainGroup, SWT.NONE);
		canvas.setLayoutData(gridData);
		rightMainGroup.setContent(canvas);
		setImageIntoCanvas();
		
		
	    rightMainGroup.setContent(canvas);
	    rightMainGroup.setExpandHorizontal(true);
	    rightMainGroup.setExpandVertical(true);

	}

	private void setImageIntoCanvas() {
		canvas.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				if(currentFile==null){
					return;
				}
				System.out.println("Painting "+ currentFile.getAbsolutePath());

				Image image = getCurrentImage();
				if(image == null){
					image = new Image(controller.display, currentFile.getAbsolutePath());
					imagesMap.put( currentFile.getAbsolutePath(), image);
				}

				if(currentComponent!=null){
					//e.gc.drawRectangle(currentComponent.getRectangle());
					Display display = Display.getCurrent();
					for(Pixel pixel : currentComponent.getPixels()){
						Color blue = new Color(display, pixel.data);
						e.gc.setForeground(blue);
						e.gc.drawPoint(pixel.x, pixel.y);
					}
				}else{
					e.gc.fillRectangle(canvas.getClientArea());
					e.gc.drawImage(image, 0, 0);
				}

				if(rightMainGroup!=null){
					Rectangle bounds = image.getBounds();
					rightMainGroup.setMinWidth(bounds.width);
				    rightMainGroup.setMinHeight(bounds.height);
				}
				
			}
		});
	}

	public void redraw() {
		canvas.redraw();
	}

	public File getCurrentFile() {
		return currentFile;
	}

	public void setCurrentFile(File currentFile) {
		this.currentFile = currentFile;
		this.currentComponent = null;
	}

	private Image getCurrentImage() {
		if(currentFile!=null){
			Image image = imagesMap.get(currentFile.getAbsolutePath());
			return image;
		}
		return null;
	}

	public void setCurrentComponent(Component component) {
		this.currentComponent = component;
	}

	public Component getCurrentComponent() {
		if(currentComponent!=null){
			return currentComponent;
		}
		Image currentImage = getCurrentImage();
		if(currentImage!=null){
			return ComponentBuilder.build(currentImage);
		}
		return null;
	}

	
}
