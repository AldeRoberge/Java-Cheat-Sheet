/**
* StatusUI is a piece of code I use often, if you use WindowBuilder (Eclipse) to preview it you'll understand what it is.
It's basically two columns of JFields, one is the label : and one is the value.
It's taken from FileBro by Andrew Thompson. This code is runneable and tested.
*/


package status;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class Status extends JPanel {

	public JPanel simpleSearch;

	JButton toggleAdvancedSearchBtn;
	private JLabel mainStatusLabel;

	/**
	 * Create the panel.
	 */
	public Status() {

		Font f = new Font("Tahoma", Font.PLAIN, 11);

		// details for a File
		setLayout(new BorderLayout(0, 0));

		JPanel statusPanel = new JPanel();
		add(statusPanel, BorderLayout.CENTER);

		statusPanel.setLayout(new BorderLayout(4, 2));
		statusPanel.setBorder(new EmptyBorder(0, 6, 0, 6));

		JPanel statusLabels = new JPanel(new GridLayout(0, 1, 2, 2));
		statusPanel.add(statusLabels, BorderLayout.WEST);

		JPanel statusValues = new JPanel(new GridLayout(0, 1, 2, 2));
		statusPanel.add(statusValues, BorderLayout.CENTER);

		//

		JLabel statusOneLabel = new JLabel("One :", JLabel.TRAILING);
		statusOneLabel.setFont(f);
		statusLabels.add(statusOneLabel);
		
		JLabel statusOne = new JLabel();
		statusOne.setText("Value");
		statusValues.add(statusOne);

		//

		JLabel statusTwoLabel = new JLabel("Two :", JLabel.TRAILING);
		statusTwoLabel.setFont(f);
		statusLabels.add(statusTwoLabel);
		
		JLabel statusTwo = new JLabel();
		statusTwo.setText("Value");
		statusValues.add(statusTwo);

		//

		JLabel statusThreeLabel = new JLabel("Three :", JLabel.TRAILING);
		statusThreeLabel.setFont(f);
		statusLabels.add(statusThreeLabel);
		
		JLabel statusThree = new JLabel();
		statusThree.setText("Value");
		statusValues.add(statusThree);

		//

		JLabel statusFourLabel = new JLabel("Four :", JLabel.TRAILING);
		statusFourLabel.setFont(f);
		statusLabels.add(statusFourLabel);
		
		JLabel statusFour = new JLabel();
		statusFour.setText("Value");
		statusValues.add(statusFour);

		//

		JLabel statusFiveLabel = new JLabel("Five :", JLabel.TRAILING);
		statusFiveLabel.setFont(f);
		statusLabels.add(statusFiveLabel);
		
		JLabel statusFive = new JLabel();
		statusFive.setText("Value");
		statusValues.add(statusFive);

		//

		JLabel statusSixLabel = new JLabel("Six :", JLabel.TRAILING);
		statusSixLabel.setFont(f);
		statusLabels.add(statusSixLabel);
		
		JLabel statusSix = new JLabel();
		statusSix.setText("Value");
		statusValues.add(statusSix);

		//

		JLabel statusSevenLabel = new JLabel("Seven : ", JLabel.TRAILING);
		statusSevenLabel.setFont(f);
		statusLabels.add(statusSevenLabel);
		
		JLabel statusSeven = new JLabel();
		statusSeven.setText("Value");
		statusValues.add(statusSeven);

		// End

		JPanel mainStatusPanel = new JPanel();
		add(mainStatusPanel, BorderLayout.SOUTH);
		mainStatusPanel.setLayout(new BoxLayout(mainStatusPanel, BoxLayout.Y_AXIS));

		JPanel panel = new JPanel();
		mainStatusPanel.add(panel);
		panel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		mainStatusLabel = new JLabel("Main status");
		mainStatusLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		panel.add(mainStatusLabel);

	}

}
