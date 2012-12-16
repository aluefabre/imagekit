package textkit.ocrkit.gui;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class GUIImageTreeController {
	
	GUIController controller;

	Tree componentsTree = null;

	Tree imagesTree = null;
	
	public GUIImageTreeController(GUIController controller){
		this.controller = controller;
	}
	
	private void addImageTreeListener() {
		imagesTree.addListener(SWT.MouseDoubleClick, new Listener() {
			public void handleEvent(Event event) {
				Point point = new Point(event.x, event.y);
		        TreeItem item = imagesTree.getItem(point);
				if (item == null){
					return;
				}
				controller.canvasController.setCurrentFile( (File) item.getData() );
				controller.canvasController.redraw();
			}
		});
	}

	public void createTree(Composite imagesGroup) {
		imagesTree = new Tree(imagesGroup, SWT.BORDER);
		addImageTreeListener();
	}

	public void addImage2Tree() {
		FileDialog fd = new FileDialog(controller.shell, SWT.OPEN);
		fd.setText("Load Image");
		String[] filterExt = {"*.*",  "*.png", "*.jpg", ".bmp" };
		fd.setFilterExtensions(filterExt);
		String selected = fd.open();
		File file = new File(selected);
		TreeItem child1 = new TreeItem(imagesTree, SWT.NONE, 0);
		child1.setText(file.getAbsolutePath());
		child1.setData(file);
	}
}
