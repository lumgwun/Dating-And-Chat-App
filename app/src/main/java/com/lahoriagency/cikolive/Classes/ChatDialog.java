package com.lahoriagency.cikolive.Classes;

import com.quickblox.chat.model.QBChatDialog;

import java.util.ArrayList;

public class ChatDialog extends QBChatDialog {
    private ArrayList<QBChatMessage> messages;

    public ArrayList<QBChatMessage> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<QBChatMessage> messages) {
        this.messages = messages;
    }
}
