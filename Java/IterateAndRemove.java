/**
* In Java, removing an object from an array while iterating causes a java.util.ConcurrentModificationException
* To remediate to this, we use an iterator and call iterator.remove(); instead
*/

try {
	for (Iterator<SoundPanel> iterator = allPanels.iterator(); iterator.hasNext();) {
		SoundPanel editPropertyPanel = iterator.next();
		
		//do something else

		iterator.remove();
	}

	//do something else

} catch (Exception e1) {
	e1.printStackTrace();
}