/**
* If you want to stop a thread from hanging, use this trick
*/

new Thread("Thread name") {

	public void run() {
		//This code will run asynchronously
	}
}.start();
