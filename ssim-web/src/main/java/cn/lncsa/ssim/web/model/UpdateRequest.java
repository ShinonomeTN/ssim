package cn.lncsa.ssim.web.model;

import javax.persistence.*;

/**
 * Created by cattenlinger on 2017/3/9.
 */
@Entity
@Table(name = "update_request", indexes = {
        @Index(name = "index_ur_senderId",columnList = "senderId"),
        @Index(name = "index_ur_request",columnList = "requestFor")
})
public class UpdateRequest {
    private Long id;
    private String senderId;
    private String senderName;
    private String requestFor;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getRequestFor() {
        return requestFor;
    }

    public void setRequestFor(String requestFor) {
        this.requestFor = requestFor;
    }
}
