package textkit.ocrkit.gui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import textkit.ocrkit.analysis.BlockAnalysis;
import textkit.ocrkit.analysis.ComponentBuilder;
import textkit.ocrkit.analysis.ComponentMerger;
import textkit.ocrkit.analysis.ConnectivityAnalysis;
import textkit.ocrkit.analysis.ImageFilter;
import textkit.ocrkit.analysis.KMeansAnalysis;
import textkit.ocrkit.analysis.XMeansAnalysis;
import textkit.ocrkit.analysis.model.Component;

public class GUIController {
	Shell shell = null;
	Display display = null;
	GUIMenuController menuController = null;
	GUICanvasController canvasController = null;
	GUIImageTreeController imageTreeController = null;
	GUIComponentTreeController componentTreeController = null;
	
	public GUIController() {
		display = new Display();

		shell = new Shell(display);
		shell.setText("Ocr Kit");
		shell.setLayout(new FillLayout());
		menuController = new GUIMenuController(this);
		canvasController = new GUICanvasController(this);
		componentTreeController = new GUIComponentTreeController(this);
		imageTreeController = new GUIImageTreeController(this);
		createContent(shell);
	}

	private void createContent(Shell shell) {
		menuController.createMenu(shell);
		
		SashForm sash = new SashForm(shell, SWT.HORIZONTAL);
		createLeft(sash);
		createRight(sash);
		sash.setWeights(new int[] { 1, 3 });
	}

	public void exit(){
		shell.close();
		display.dispose();
	}

	private void createLeft(SashForm sash) {
		SashForm imagesGroup = new SashForm(sash, SWT.VERTICAL);
		imageTreeController.createTree(imagesGroup);
		componentTreeController.createTree(imagesGroup);
		imagesGroup.setWeights(new int[] { 1, 1 });
	}


	private void createRight(SashForm sash) {
		Group rightGroup = new Group(sash, SWT.NONE| SWT.V_SCROLL  );
		
		
		rightGroup.setLayout(new GridLayout(1, false));

		createRightTop(rightGroup);
		
		canvasController.createCanvas(rightGroup);
	}



	private void createRightTop(Group rightGroup) {
		Group rightTopGroup = new Group(rightGroup, SWT.NONE);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		rightTopGroup.setLayoutData(gridData);

		rightTopGroup.setLayout(new RowLayout(SWT.HORIZONTAL));
		Button connectivityButton = new Button(rightTopGroup, SWT.PUSH);
		connectivityButton.setText("Connectivity Analysis");
		addConnectivityListener(connectivityButton);
		
		Button mergeButton = new Button(rightTopGroup, SWT.PUSH);
		mergeButton.setText("Merge Components");
		addMergeListener(mergeButton);
		
		Button blockButton = new Button(rightTopGroup, SWT.PUSH);
		blockButton.setText("Block Analysis");
		addBlockListener(blockButton);		

		Button kmeansButton = new Button(rightTopGroup, SWT.PUSH);
		kmeansButton.setText("Cluster Analysis");
		addClusterListener(kmeansButton);		

		Button filterButton = new Button(rightTopGroup, SWT.PUSH);
		filterButton.setText("Filter Analysis");
		addFilterListener(filterButton);	
		
		new Button(rightTopGroup, SWT.PUSH).setText("four");
		new Button(rightTopGroup, SWT.PUSH).setText("five");
		new Button(rightTopGroup, SWT.PUSH).setText("six");
	}

	private void addBlockListener(Button blockButton) {
		blockButton.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent event) {
				List<Component> components = componentTreeController.getComponents();
				if(components!=null){
					List<Component> blockComponents = BlockAnalysis.blockAnalysis(components);
					
					MessageBox messageBox = new MessageBox(shell, SWT.ICON_INFORMATION);
					messageBox.setMessage("Done, merge from " + components.size() + " to " + blockComponents.size());
					messageBox.open();
					
					componentTreeController.setComponents(blockComponents);
					componentTreeController.showComponents();	
					
				}else{
					MessageBox messageBox = new MessageBox(shell, SWT.ICON_INFORMATION);
					messageBox.setMessage("Image is not loaded yet");
					messageBox.open();
				}
			}

			public void widgetDefaultSelected(SelectionEvent event) {
			
			}
		});
	}
	

	private void addClusterListener(Button button) {
		button.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent event) {
				Component sourceComp = canvasController.getCurrentComponent();
				if(sourceComp!=null){
					List<Component> components = KMeansAnalysis.analysis(sourceComp);
					MessageBox messageBox = new MessageBox(shell, SWT.ICON_INFORMATION);
					messageBox.setMessage("Done, "+ components.size() + " components found ");
					messageBox.open();
					componentTreeController.setComponents(components);
					componentTreeController.showComponents();						
				}else{
					MessageBox messageBox = new MessageBox(shell, SWT.ICON_INFORMATION);
					messageBox.setMessage("Image is not loaded yet");
					messageBox.open();
				}
			}

			public void widgetDefaultSelected(SelectionEvent event) {
			
			}
		});
	}
	
	private void addFilterListener(Button blockButton) {
		blockButton.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent event) {
				Component sourceComp = canvasController.getCurrentComponent();
				if(sourceComp!=null){
					sourceComp = ImageFilter.sobel(sourceComp);
					List<Component> components = new ArrayList<Component>();
					components.add(sourceComp);
					MessageBox messageBox = new MessageBox(shell, SWT.ICON_INFORMATION);
					messageBox.setMessage("Done, "+ components.size() + " components found ");
					messageBox.open();
					componentTreeController.setComponents(components);
					componentTreeController.showComponents();						
				}else{
					MessageBox messageBox = new MessageBox(shell, SWT.ICON_INFORMATION);
					messageBox.setMessage("Image is not loaded yet");
					messageBox.open();
				}
			}

			public void widgetDefaultSelected(SelectionEvent event) {
			
			}
		});
	}
	
	
	private void addMergeListener(Button mergeButton) {
		mergeButton.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent event) {
				List<Component> components = componentTreeController.getComponents();
				if(components!=null){
					List<Component> mergedComponents = ComponentMerger.merge(components);
					
					MessageBox messageBox = new MessageBox(shell, SWT.ICON_INFORMATION);
					messageBox.setMessage("Done, merge from " + components.size() + " to " + mergedComponents.size());
					messageBox.open();
					
					componentTreeController.setComponents(mergedComponents);
					componentTreeController.showComponents();	
					
				}else{
					MessageBox messageBox = new MessageBox(shell, SWT.ICON_INFORMATION);
					messageBox.setMessage("Image is not loaded yet");
					messageBox.open();
				}
			}

			public void widgetDefaultSelected(SelectionEvent event) {
			
			}
		});
	}

	private void addConnectivityListener(Button connectivityButton) {
		connectivityButton.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent event) {
				Component sourceComp = canvasController.getCurrentComponent();
				if(sourceComp!=null){
					List<Component> components = ConnectivityAnalysis.analysis(sourceComp);
					MessageBox messageBox = new MessageBox(shell, SWT.ICON_INFORMATION);
					messageBox.setMessage("Done, "+ components.size() + " components found ");
					messageBox.open();
					componentTreeController.setComponents(components);
					componentTreeController.showComponents();						
				}else{
					MessageBox messageBox = new MessageBox(shell, SWT.ICON_INFORMATION);
					messageBox.setMessage("Image is not loaded yet");
					messageBox.open();
				}
			}

			public void widgetDefaultSelected(SelectionEvent event) {
			
			}
		});
	}

	public void showGUI() {
		shell.pack();
		shell.open();
		shell.setSize(1200, 800);
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()){
				display.sleep();
			}
		}
		display.dispose();
	}

	public static void main(String[] argv) {
		GUIController controller = new GUIController();
		controller.showGUI();
	}

}
