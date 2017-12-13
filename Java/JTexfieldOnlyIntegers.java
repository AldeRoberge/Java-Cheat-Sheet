keyText.addKeyListener(new KeyAdapter() {
    public void keyTyped(KeyEvent e) {
      char c = e.getKeyChar();
      if (!((c >= '0') && (c <= '9') ||
         (c == KeyEvent.VK_BACK_SPACE) ||
         (c == KeyEvent.VK_DELETE))) {
        getToolkit().beep();
        e.consume();
      }
    }
  });
  
  
 //To quote Hovercraft Full Of Eels quoting H.L. Mencken, “For every problem there is a solution which is simple, clean and wrong.”
 //https://stackoverflow.com/questions/11093326/restricting-jtextfield-input-to-integers
