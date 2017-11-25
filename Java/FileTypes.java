


package file;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class ExtensionFilter extends FileFilter {

	//Code from Java2s.com
	//Used by the JFileChooser in RunSS

	private String extensions[];

	private String description;

	public ExtensionFilter(String description, String extension) {
		this(description, new String[] { extension });
	}

	public ExtensionFilter(String description, String extensions[]) {
		this.description = description;
		this.extensions = extensions.clone();
	}

	public boolean accept(File file) {
		if (file.isDirectory()) {
			return true;
		}
		int count = extensions.length;
		String path = file.getAbsolutePath();
		for (String ext : extensions) {
			if (path.endsWith(ext) && (path.charAt(path.length() - ext.length()) == '.')) {
				return true;
			}
		}
		return false;
	}

	public String getDescription() {
		return (description == null ? extensions[0] : description);
	}
}




class FileTypes {
	public static final ExtensionFilter AUDIO_FILES = new ExtensionFilter(
			"Audio Files (*.aiff, *.au, *.mp3, *.ogg, *.mp4, *.wav)",
			new String[] { ".aiff", ".au", ".mp3", ".ogg", ".mp4", ".wav" });

	public static final ExtensionFilter VIDEO_FILES = new ExtensionFilter(
			"Video Files (*.webm, *.mkv, *.flv, *.ogg, *.gif, *.avi, *.mov, *.wmv, *.mp4, *.mpg, *.m4v)",
			new String[] { ".webm", ".mkv", ".flv", ".ogg", ".gif", ".avi", ".mov", ".wmv", ".mp4", ".mpg", ".m4v" });

	public static final ExtensionFilter PICTURE_FILES = new ExtensionFilter(
			"Picture Files (*.jpeg, *.tiff, *.gif, *.bmp, *.png)",
			new String[] { ".jpeg", ".tiff", ".gif", ".bmp", ".png" });

	public static final ExtensionFilter TEXT_FILES = new ExtensionFilter("Text Files (*.text, *.txt)",
			new String[] { ".text", ".txt" });

}
