package org.example.rpc.protocol;

import java.io.Serializable;

/**
 * @description: 消息
 */
public class RpcProtocol<T> implements Serializable {

    private MsgHeader header;
    private T body;

    public MsgHeader getHeader() {
        return header;
    }

    public void setHeader(MsgHeader header) {
        this.header = header;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }
}
