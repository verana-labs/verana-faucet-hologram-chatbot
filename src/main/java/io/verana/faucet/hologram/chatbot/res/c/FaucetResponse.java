package io.verana.faucet.hologram.chatbot.res.c;

/*{
    "message": "Tokens dispensed successfully.",
    "transactionHash": "0B90A74FB6BEC5A0080AB9FF919B35809D7199A7D30ED31AFBA1C68A1C937EBE"
}
*/

public class FaucetResponse {
	private String message;
	private String transactionHash;
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getTransactionHash() {
		return transactionHash;
	}
	public void setTransactionHash(String transactionHash) {
		this.transactionHash = transactionHash;
	}
	
}
