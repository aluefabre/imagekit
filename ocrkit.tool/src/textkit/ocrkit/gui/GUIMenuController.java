package textkit.ocrkit.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class GUIMenuController {
	
	GUIController controller;
	
	public GUIMenuController(GUIController controller){
		this.controller = controller;
	}
	
	public void createMenu(Shell shell) {
		Menu menuBar = new Menu(shell, SWT.BAR);
		MenuItem fileMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
		fileMenuHeader.setText("&File");

		Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
		fileMenuHeader.setMenu(fileMenu);

		MenuItem fileSaveItem = new MenuItem(fileMenu, SWT.PUSH);
		fileSaveItem.setText("&Load");

		MenuItem fileExitItem = new MenuItem(fileMenu, SWT.PUSH);
		fileExitItem.setText("E&xit");

		MenuItem helpMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
		helpMenuHeader.setText("&Help");

		Menu helpMenu = new Menu(shell, SWT.DROP_DOWN);
		helpMenuHeader.setMenu(helpMenu);

		MenuItem helpGetHelpItem = new MenuItem(helpMenu, SWT.PUSH);
		helpGetHelpItem.setText("&Get Help");

		fileExitItem.addSelectionListener(new fileExitItemListener());
		fileSaveItem.addSelectionListener(new fileLoadItemListener());
		helpGetHelpItem.addSelectionListener(new helpGetHelpItemListener());

		shell.setMenuBar(menuBar);
	}

	class fileExitItemListener implements SelectionListener {
		public void widgetSelected(SelectionEvent event) {
			controller.exit();
		}

		public void widgetDefaultSelected(SelectionEvent event) {
		
		}
	}

	class fileLoadItemListener implements SelectionListener {
		public void widgetSelected(SelectionEvent event) {
			controller.imageTreeController.addImage2Tree();
		}

		public void widgetDefaultSelected(SelectionEvent event) {

		}
	}

	class helpGetHelpItemListener implements SelectionListener {
		public void widgetSelected(SelectionEvent event) {
			MessageBox messageBox = new MessageBox(controller.shell, SWT.ICON_INFORMATION);
			messageBox.setMessage("Message");
			messageBox.open();
		}

		public void widgetDefaultSelected(SelectionEvent event) {

		}
	}
}
