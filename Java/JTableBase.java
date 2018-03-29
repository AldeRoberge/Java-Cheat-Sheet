package i18n;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

public class FileManager extends JPanel implements ActionListener {

	/** currently selected File. */
	public ArrayList<File> selectedFiles = new ArrayList<File>();

	/** Directory listing */
	private JTable table;

	/** Table model for File array. */
	private FileTableModel fileTableModel;
	private ListSelectionListener listSelectionListener;
	private boolean cellSizesSet = false;

	/** Popup menu */

	private JPopupMenu popupMenu = new JPopupMenu();

	public FileManager() {

		setLayout(new BorderLayout(3, 3));
		setBorder(new EmptyBorder(5, 5, 5, 5));

		table = new JTable();
		table.setRowSelectionAllowed(true);
		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		table.setAutoCreateRowSorter(true);
		table.setShowVerticalLines(false);
		table.setSelectionBackground(Color.PINK);
		table.setSelectionForeground(Color.WHITE);

		table.setComponentPopupMenu(popupMenu);

		fileTableModel = new FileTableModel();
		table.setModel(fileTableModel);

		//set font bold for column 1 (see CellRenderer at the bottom of this class)
		table.getColumnModel().getColumn(1).setCellRenderer(new CellRenderer());

		listSelectionListener = new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				ListSelectionModel lsm = (ListSelectionModel) e.getSource();

				boolean isAdjusting = e.getValueIsAdjusting();

				if (!lsm.isSelectionEmpty()) {
					// Find out which indexes are selected.
					int minIndex = lsm.getMinSelectionIndex();
					int maxIndex = lsm.getMaxSelectionIndex();

					selectedFiles.clear();

					for (int i = minIndex; i <= maxIndex; i++) {
						if (lsm.isSelectedIndex(i)) {

							//Fixes row being incorrect after sortings
							int row = table.convertRowIndexToModel(i);

							selectedFiles.add(fileTableModel.getFile(row));
						}
					}

					if (!isAdjusting) {
						if (selectedFiles.size() == 1) { //amount of selected files == 1

							File selectedFile = selectedFiles.get(0);

						} else if (selectedFiles.size() > 1) { //more than 1 selected file

						}

					}

				}

			}

		};
		table.getSelectionModel().addListSelectionListener(listSelectionListener);

		JScrollPane tableScroll = new JScrollPane(table);
		Dimension d = tableScroll.getPreferredSize();
		tableScroll.setPreferredSize(new Dimension((int) d.getWidth(), (int) d.getHeight() / 2));
		add(tableScroll, BorderLayout.CENTER);

	}

	/** Update the table on the EDT */
	private void setTableData(List<File> newFiles) {

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if (fileTableModel == null) {
					fileTableModel = new FileTableModel();
					table.setModel(fileTableModel);
				}
				table.getSelectionModel().removeListSelectionListener(listSelectionListener);
				fileTableModel.setFiles(newFiles);
				table.getSelectionModel().addListSelectionListener(listSelectionListener);
				if (!cellSizesSet) {

					table.setRowHeight(40);
					//setColumnWidth(0, -1);
					cellSizesSet = true;

				}
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent event) {

		if (event.getSource() instanceof JMenuItem) {
			JMenuItem menu = (JMenuItem) event.getSource();

		}

	}

	public void addFiles(ArrayList<File> filesToAdd) {
		final List<File> files = fileTableModel.getFiles();

		for (Iterator<File> iterator = filesToAdd.iterator(); iterator.hasNext();) {
			files.add(iterator.next());
		}

		setTableData(files);

	}

	public void removeFiles(ArrayList<File> filesToRemove) {
		final List<File> files = fileTableModel.getFiles();

		for (Iterator<File> iterator = files.iterator(); iterator.hasNext();) {
			File file = iterator.next();

			if (filesToRemove.contains(file)) {
				iterator.remove();
			}
		}

		setTableData(files);

	}

	private void setColumnWidth(int column, int width) {
		TableColumn tableColumn = table.getColumnModel().getColumn(column);
		if (width < 0) {
			// use the preferred width of the header..
			JLabel label = new JLabel((String) tableColumn.getHeaderValue());
			Dimension preferred = label.getPreferredSize();
			// altered 10->14 as per camickr comment.
			width = (int) preferred.getWidth() + 14;
		}
		tableColumn.setPreferredWidth(width);
		tableColumn.setMaxWidth(width);
		tableColumn.setMinWidth(width);
	}

	public void removeSelectedFiles() {
		removeFiles(selectedFiles);
	}

	//UPDATE POPUPMENU END

}

class FileTableModel extends AbstractTableModel {

	private static final String TAG = "FileTableModel";
	private List<File> files;
	private FileSystemView fileSystemView = FileSystemView.getFileSystemView();
	private String[] columns = { "Icon", "File", "Path", "Size", "Last Modified" };

	FileTableModel() {
		files = new ArrayList<File>();
	}

	public Object getValueAt(int row, int column) {
		File file = files.get(row);
		switch (column) {
		case 0:
			return fileSystemView.getSystemIcon(file);
		case 1:
			return fileSystemView.getSystemDisplayName(file);
		case 2:
			return file.getPath();
		case 3:
			return file.length();
		case 4:
			return file.lastModified();
		default:
			System.err.println("Logic Error");
		}
		return "";
	}

	public int getColumnCount() {
		return columns.length;
	}

	public Class<?> getColumnClass(int column) {
		switch (column) {
		case 0:
			return ImageIcon.class;
		case 3:
			return Long.class;
		case 4:
			return Date.class;
		}
		return String.class;
	}

	public String getColumnName(int column) {
		return columns[column];
	}

	public int getRowCount() {
		return files.size();
	}

	public File getFile(int row) {
		return files.get(row);
	}

	public void setFiles(List<File> files) {
		this.files = files;
		fireTableDataChanged();
	}

	public List<File> getFiles() {
		return files;
	}

	public void addFile(File file) {
		files.add(file);
		fireTableDataChanged();
	}

}

class CellRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 1L;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		// if (value>17 value<26) {
		this.setValue(table.getValueAt(row, column));
		this.setFont(this.getFont().deriveFont(Font.ROMAN_BASELINE));
		//}
		return this;
	}
}
