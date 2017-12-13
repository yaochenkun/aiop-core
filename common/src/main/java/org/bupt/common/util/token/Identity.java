package org.bupt.common.util.token;

/**
 * Token携带信息
 * Created by ken on 2017/6/9.
 */
public class Identity {

	private String token;
	private Integer id; // 对应user_id
	private String issuer;
	private String clientId; //可以是Oauth2.0中的client_id，也可以是一般的username
	private Long duration; // 有效时长，单位毫秒


	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIssuer() {
		return issuer;
	}

	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}
}
