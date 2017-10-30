/** 
* Java has a tendency to start JFrames in an akward position on the screen
*  To put a JFrame right dead smack in the middle of the screen, do this in the initiation
*/

Dimension dimemsion = Toolkit.getDefaultToolkit().getScreenSize();
setLocation(dimemsion.width / 2 - getSize().width / 2, dimemsion.height / 2 - getSize().height / 2);