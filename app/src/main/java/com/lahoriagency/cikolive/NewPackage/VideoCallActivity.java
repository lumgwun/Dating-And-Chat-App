package com.lahoriagency.cikolive.NewPackage;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.lahoriagency.cikolive.R;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBSignaling;
import com.quickblox.chat.QBWebRTCSignaling;
import com.quickblox.chat.listeners.QBVideoChatSignalingManagerListener;
import com.quickblox.videochat.webrtc.QBRTCClient;
import com.quickblox.videochat.webrtc.QBRTCSession;
import com.quickblox.videochat.webrtc.callbacks.QBRTCClientSessionCallbacks;

import java.util.Map;

public class VideoCallActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_video_call);
        QBChatService.getInstance().getVideoChatWebRTCSignalingManager()
                .addSignalingManagerListener(new QBVideoChatSignalingManagerListener() {
                    @Override
                    public void signalingCreated(QBSignaling qbSignaling, boolean createdLocally) {
                        if (!createdLocally) {
                            QBRTCClient.getInstance(getApplicationContext()).addSignaling((QBWebRTCSignaling) qbSignaling);
                        }
                    }
                });
        QBRTCClient.getInstance(getApplicationContext()).addSessionCallbacksListener(new QBRTCClientSessionCallbacks() {
            @Override
            public void onReceiveNewSession(QBRTCSession qbrtcSession) {

            }

            @Override
            public void onUserNoActions(QBRTCSession qbrtcSession, Integer integer) {

            }

            @Override
            public void onSessionStartClose(QBRTCSession qbrtcSession) {

            }

            @Override
            public void onUserNotAnswer(QBRTCSession qbrtcSession, Integer integer) {

            }

            @Override
            public void onCallRejectByUser(QBRTCSession qbrtcSession, Integer integer, Map<String, String> map) {

            }

            @Override
            public void onCallAcceptByUser(QBRTCSession qbrtcSession, Integer integer, Map<String, String> map) {

            }

            @Override
            public void onReceiveHangUpFromUser(QBRTCSession qbrtcSession, Integer integer, Map<String, String> map) {

            }

            @Override
            public void onSessionClosed(QBRTCSession qbrtcSession) {

            }
        });

    }


}