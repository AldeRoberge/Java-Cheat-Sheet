/**
* If you want to stop a thread from hanging, use this trick
*/

new Thread("Thread name") {

	public void run() {

			

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		SpamBotLauncher.reconnectSameLootBot(mbc);

		System.out.println(" Reconnected " + SpamBotLauncher.getPreDomainEmail(email) + " to " + server.name() + ".");

	}
}.start();