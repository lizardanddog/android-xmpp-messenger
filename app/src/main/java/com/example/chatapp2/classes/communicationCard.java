package com.example.chatapp2.classes;

import android.graphics.Bitmap;

public class communicationCard {
    Bitmap bitmapPicture;
    String userName;
    String email;
    String lastMessage;
    boolean ischecked;
    int msg_status;
    /* msg_status:
    0: if message sent to other user;
    1: if message received by other user;
    2: if message read by other user;
    3: if message received from user but not read;
    4: if message received from user and read;
     */


    public communicationCard(Bitmap bitmapPicture, String userName, String email, String lastMessage, int msg_status) {
        this.bitmapPicture = bitmapPicture;
        this.userName = userName;
        this.email = email;
        this.lastMessage=lastMessage;
        this.msg_status=msg_status;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public boolean isIschecked() {
        return ischecked;
    }

    public void setIschecked(boolean ischecked) {
        this.ischecked = ischecked;
    }

    public Bitmap getBitmapPicture() {
        return bitmapPicture;
    }

    public void setBitmapPicture(Bitmap bitmapPicture) {
        this.bitmapPicture = bitmapPicture;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getMsg_status() {
        return msg_status;
    }

    public void setMsg_status(int msg_status) {
        this.msg_status = msg_status;
    }

}
