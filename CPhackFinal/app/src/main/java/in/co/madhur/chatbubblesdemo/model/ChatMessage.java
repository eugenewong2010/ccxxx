package in.co.madhur.chatbubblesdemo.model;

/**
 * Created by madhur on 17/01/15.
 */
public class ChatMessage {

    private MessageType messageType;
    private long messageTime;
    private String messageText;
    private int drawableResId;
    private UserType userType;
    private Status messageStatus;

    public ChatMessage() {
    }

    public ChatMessage(MessageType messageType, String messageText) {
        this.messageType = messageType;
        this.messageText = messageText;
    }

    public ChatMessage(MessageType messageType, int drawableResId) {
        this.messageType = messageType;
        this.drawableResId = drawableResId;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public void setMessageStatus(Status messageStatus) {
        this.messageStatus = messageStatus;
    }

    public String getMessageText() {
        return messageText;
    }

    public int getDrawableResId(){
        return drawableResId;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public UserType getUserType() {
        return userType;
    }

    public Status getMessageStatus() {
        return messageStatus;
    }
}
