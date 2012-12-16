package textkit.ocrkit.gui;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import textkit.ocrkit.analysis.model.Component;

public class GUIComponentTreeController {
	
	GUIController controller;

	Tree componentsTree = null;
	
	List<Component> components;
	
	public GUIComponentTreeController(GUIController controller){
		this.controller = controller;
	}
	
	private void addComponentsTreeListener() {
		componentsTree.addListener(SWT.MouseDoubleClick, new Listener() {
			public void handleEvent(Event event) {
				Point point = new Point(event.x, event.y);
		        TreeItem item = componentsTree.getItem(point);
				if (item == null){
					return;
				}
				controller.canvasController.setCurrentComponent( (Component) item.getData() );
				controller.canvasController.redraw();
			}
		});
	}

	public void createTree(Composite imagesGroup) {
		componentsTree = new Tree(imagesGroup, SWT.BORDER);
		addComponentsTreeListener();
	}

	public void showComponents() {
		componentsTree.removeAll();
		for(Component component:components){
			TreeItem child1 = new TreeItem(componentsTree, SWT.NONE, 0);
			child1.setText(component.getRGB()+":"+component.size());
			child1.setData(component);
		}
	}

	public void setComponents(List<Component> components) {
		this.components = components;
	}

	public List<Component> getComponents() {
		return components;
	}
}
