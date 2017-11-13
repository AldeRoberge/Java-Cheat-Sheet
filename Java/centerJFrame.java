/** 
* Java has a tendency to start JFrames in an akward position on the screen
* and sometimes (most of the time) setLocationRelativeTo(null); doesn't work, so I use this 
* to put a JFrame right dead smack in the middle of the screen. Add this in the initiation :
*/

Dimension dimemsion = Toolkit.getDefaultToolkit().getScreenSize();
setLocation(dimemsion.width / 2 - getSize().width / 2, dimemsion.height / 2 - getSize().height / 2);
