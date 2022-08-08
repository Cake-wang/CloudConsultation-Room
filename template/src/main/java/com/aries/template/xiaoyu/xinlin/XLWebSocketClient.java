package com.aries.template.xiaoyu.xinlin;

import java.net.URI;

import tech.gusavila92.websocketclient.WebSocketClient;

/**
 * 信令 WebSocketClient
 */
public class XLWebSocketClient extends WebSocketClient {

    public XLWebSocketClient(URI uri) {
        super(uri);
    }

    @Override
    public void onOpen() {

    }

    @Override
    public void onTextReceived(String message) {

    }

    @Override
    public void onBinaryReceived(byte[] data) {

    }

    @Override
    public void onPingReceived(byte[] data) {

    }

    @Override
    public void onPongReceived(byte[] data) {

    }

    @Override
    public void onException(Exception e) {

    }

    @Override
    public void onCloseReceived() {

    }
}
