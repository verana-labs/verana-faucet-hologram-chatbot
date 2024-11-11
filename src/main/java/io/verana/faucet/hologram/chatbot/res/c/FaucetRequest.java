package io.verana.faucet.hologram.chatbot.res.c;

/*{
    "message": "Tokens dispensed successfully.",
    "transactionHash": "0B90A74FB6BEC5A0080AB9FF919B35809D7199A7D30ED31AFBA1C68A1C937EBE"
}
*/

public class FaucetRequest {
	private String address;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	
}
