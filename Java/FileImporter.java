/**
FileImporter is a great way to let your user import files.
Requires a class (here FileImporterAgent) that implements the method importFiles(ArrayList<File> files);
Call "new FileImporter(FileImporterAgent me)"
*/

package ass.file.importer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TooManyListenersException;

import javax.imageio.ImageIO;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import ass.file.FileManager;
import constants.icons.Icons;
import constants.property.Properties;
import file.FileTypes;
import logger.Logger;
import ui.BasicContainer;
import ui.MiddleOfTheScreen;

public class FileImporter extends JFrame {

	private static final String TAG = "FileImporter";

	private ArrayList<File> filesToImport = new ArrayList<File>();
	private DropPane dropPanel;
	private JButton btnImport;

	public static BasicContainer fileImporterParent = new BasicContainer("This is used to pass the icon to the fileChooser", Icons.IMPORT.getImage(), null, false);

	/**
	 * Create the frame.
	 */
	public FileImporter(FileImporterAgent fMan) {

		setIconImage(Icons.IMPORT.getImage());
		setTitle("Import");

		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				filesToImport.clear();
				dropPanel.updateMessage();
				setVisible(false);
			}
		});

		setBounds(100, 100, 553, 262);

		setLocation(MiddleOfTheScreen.getMiddleOfScreenLocationFor(this));

		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel container = new JPanel();
		contentPane.add(container, BorderLayout.CENTER);
		container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));

		dropPanel = new DropPane(this);
		container.add(dropPanel);

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.SOUTH);

		JButton btnOpenFileBrowser = new JButton("Open file browser");
		btnOpenFileBrowser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				Logger.logInfo(TAG, "Selecting a folder...");

				JFileChooser chooser = new JFileChooser();

				// set location on middle of screen

				chooser.setLocation(MiddleOfTheScreen.getMiddleOfScreenLocationFor(chooser));

				chooser.setCurrentDirectory(new File(Properties.LAST_OPENED_LOCATION.getValue()));
				chooser.setDialogTitle("Import folders and samples");
				chooser.setApproveButtonText("Choose");

				// from https://stackoverflow.com/questions/16292502/how-can-i-start-the-jfilechooser-in-the-details-view
				Action details = chooser.getActionMap().get("viewTypeDetails"); // show details view
				details.actionPerformed(null);

				// Format supported by AudioPlayer :
				// WAV, AU, AIFF ,MP3 and Ogg Vorbis files

				chooser.setFileFilter(FileTypes.AUDIO_FILES);

				chooser.setMultiSelectionEnabled(true); // shift + click to select multiple files
				chooser.setPreferredSize(new Dimension(800, 600));
				chooser.setAcceptAllFileFilterUsed(false);
				chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

				if (chooser.showOpenDialog(fileImporterParent) == JFileChooser.APPROVE_OPTION) {
					btnImport.requestFocus();

					String directory = chooser.getCurrentDirectory().toString();

					Properties.LAST_OPENED_LOCATION.setNewValue(directory);

					File[] files = chooser.getSelectedFiles();

					importAll(Arrays.asList(files));

					//
				} else {
					Logger.logInfo(TAG + " (Folder Selector)", "No selection");
				}

			}

		});
		panel.add(btnOpenFileBrowser);

		btnImport = new JButton("Import");
		btnImport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (filesToImport.size() > 0) {

					fMan.importFiles(filesToImport);
					filesToImport.clear();
					setVisible(false);
					dropPanel.updateMessage();

				}
			}
		});
		panel.add(btnImport);

	}

	void importAll(List<File> transferData) {

		for (File file : transferData) { //we do this because the user might choose more than 1 folder

			if (file.isDirectory()) {

				getAllFiles(file.getAbsolutePath(), Properties.INCLUDE_SUBFOLDERS.getValueAsBoolean(), 0);

			} else {
				addFileToImport(file);
			}
		}

	}

	void getAllFiles(String directoryName, boolean includeSubfolders, int totalOfFiles) {

		new Thread("Sound player") {

			public void run() {

				Logger.logInfo(TAG, "Getting all files for directory : " + directoryName + ", including subfolders : " + includeSubfolders);

				File directory = new File(directoryName);

				// get all the files from a directory
				File[] fList = directory.listFiles();

				if (fList != null) {

					for (File file : fList) {

						if (file.isFile()) {

							addFileToImport(file);

						} else if (file.isDirectory() && includeSubfolders) {
							getAllFiles(file.getAbsolutePath(), includeSubfolders, totalOfFiles);
						}
					}

				}

			}
		}.start();

	}

	void addFileToImport(File f) {
		if (!filesToImport.contains(f) && FileTypes.AUDIO_FILES.accept(f)) {
			filesToImport.add(f);

			dropPanel.updateMessage();
		}
	}

	public int getTotalFilesToImport() {
		return filesToImport.size();
	}

}

class DropPane extends JPanel {

	//Thanks to https://stackoverflow.com/questions/13597233/how-to-drag-and-drop-files-from-a-directory-in-java

	private DropTarget dropTarget;
	private DropTargetHandler dropTargetHandler;
	private Point dragPoint;

	private boolean dragOver = false;
	private BufferedImage target;

	private JLabel message;

	private FileImporter fileImporter;

	public DropPane(FileImporter f) {
		this.fileImporter = f;

		try {
			target = ImageIO.read(new File(Icons.CROSS.getImagePath()));
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		setLayout(new GridBagLayout());
		message = new JLabel();
		message.setFont(message.getFont().deriveFont(Font.BOLD, 24));
		add(message);

		updateMessage();

	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(400, 400);
	}

	DropTarget getMyDropTarget() {
		if (dropTarget == null) {
			dropTarget = new DropTarget(this, DnDConstants.ACTION_COPY_OR_MOVE, null);
		}
		return dropTarget;
	}

	DropTargetHandler getDropTargetHandler() {
		if (dropTargetHandler == null) {
			dropTargetHandler = new DropTargetHandler();
		}
		return dropTargetHandler;
	}

	@Override
	public void addNotify() {
		super.addNotify();
		try {
			getMyDropTarget().addDropTargetListener(getDropTargetHandler());
		} catch (TooManyListenersException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void removeNotify() {
		super.removeNotify();
		getMyDropTarget().removeDropTargetListener(getDropTargetHandler());
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (dragOver) {
			Graphics2D g2d = (Graphics2D) g.create();
			g2d.setColor(new Color(0, 255, 0, 64));
			g2d.fill(new Rectangle(getWidth(), getHeight()));
			if (dragPoint != null && target != null) {
				int x = dragPoint.x - 12;
				int y = dragPoint.y - 12;
				g2d.drawImage(target, x, y, this);
			}
			g2d.dispose();
		}
	}

	class DropTargetHandler implements DropTargetListener {

		void processDrag(DropTargetDragEvent dtde) {
			if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
				dtde.acceptDrag(DnDConstants.ACTION_COPY);
			} else {
				dtde.rejectDrag();
			}

			SwingUtilities.invokeLater(new DragUpdate(true, dtde.getLocation()));
			repaint();
		}

		@Override
		public void dragEnter(DropTargetDragEvent dtde) {
			processDrag(dtde);
		}

		@Override
		public void dragOver(DropTargetDragEvent dtde) {
			processDrag(dtde);
		}

		@Override
		public void dropActionChanged(DropTargetDragEvent dtde) {
		}

		@Override
		public void dragExit(DropTargetEvent dte) {
			SwingUtilities.invokeLater(new DragUpdate(false, null));
			repaint();
		}

		@Override
		public void drop(DropTargetDropEvent dtde) {

			SwingUtilities.invokeLater(new DragUpdate(false, null));

			Transferable transferable = dtde.getTransferable();
			if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
				dtde.acceptDrop(dtde.getDropAction());
				try {

					List<File> transferData = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
					if (transferData != null && transferData.size() > 0) {
						fileImporter.importAll(transferData);
						dtde.dropComplete(true);
					}

				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} else {
				dtde.rejectDrop();
			}
		}
	}

	public class DragUpdate implements Runnable {

		private boolean dragOver;
		private Point dragPoint;

		public DragUpdate(boolean dragOver, Point dragPoint) {
			this.dragOver = dragOver;
			this.dragPoint = dragPoint;
		}

		@Override
		public void run() {
			DropPane.this.dragOver = dragOver;
			DropPane.this.dragPoint = dragPoint;
			DropPane.this.repaint();
		}
	}

	public void updateMessage() {
		int nbFiles = fileImporter.getTotalFilesToImport();

		if (nbFiles == 0) {
			message.setText("Drag and drop files here");
		} else if (nbFiles == 1) {
			message.setText("Importing " + nbFiles + " file");
		} else {
			message.setText("Importing " + nbFiles + " files");
		}

	}
}
