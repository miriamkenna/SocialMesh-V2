package it.unimib.socialmesh.model;

public class Message {

    private String message;
    private String senderEmail;

    public Message() {}

    public Message(String message, String senderEmail) {
        this.message = message;
        this.senderEmail = senderEmail;
    }

    public String getMessage() {
        return message;
    }

    public String getSenderEmail() {
        return senderEmail;
    }
}
