/**
* If you want to stop a program from hanging, use this trick (breaks synchronisation, the code following this will be running at the same time)
*/

new Thread("Thread name") {

	public void run() {
		//This code will run asynchronously
	}
}.start();
