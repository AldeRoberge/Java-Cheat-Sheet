/** To hide a JFrame on the 'X' button press, put this in the initiation
 * Afterward, just do JPanel.setVisible(true) to unhide
 */

setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
		}
});