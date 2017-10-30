/**
* You might want to open a file in the explorer, selected (works cross platform)
*/

try {
	Desktop.getDesktop().open(file.getParentFile());
} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}