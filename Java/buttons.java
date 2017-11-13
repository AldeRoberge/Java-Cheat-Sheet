JButton button = new JButton("X");
button.setFocusable(false); //Removes the square 'border' when clicked
button.setToolTipText("Delete"); //Hover with mouse shows this text
button.setFont(new Font("Tahoma", Font.BOLD, 13));
button.setForeground(new Color(255, 0, 0)); //Changes the text color
button.setMargin(new Insets(-5, -5, -5, -5)); //allows for the text/icon to display even if theres not enough space (otherwise it will do something like th...)
    button.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
	           //Perform something
        }
    });
button.setBounds(290, 7, 35, 35);
panel.add(button);
