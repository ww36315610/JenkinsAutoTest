package finup.feather.utils.http;

import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.testng.collections.Maps;

import java.util.Map;

public class Oauth {

	public static OAuth2AccessToken getToken(String url,String clientID,String clientSecret) {
		ResourceOwnerPasswordAccessTokenProvider provider = new ResourceOwnerPasswordAccessTokenProvider();
		ResourceOwnerPasswordResourceDetails resource = new ResourceOwnerPasswordResourceDetails();
		resource.setClientAuthenticationScheme(AuthenticationScheme.form);
		resource.setAccessTokenUri(url);
		resource.setClientId(clientID);
		resource.setClientSecret(clientSecret);
		resource.setGrantType("client_credentials");
		OAuth2AccessToken accessToken = provider.obtainAccessToken(resource, new DefaultAccessTokenRequest());
		return accessToken;
	}


	//	根据jdbc.properties文件给获取相应的oauth
	public static Map<String, Object> headerPut(OAuth2AccessToken token) {
		Map<String, Object> header = com.google.common.collect.Maps.newHashMap();
		header.put("Accept", "*/*");
		header.put("Content-Type", "application/json;charset=UTF-8");
		header.put("Authorization", String.format("%s %s", token.getTokenType(), token.getValue()));
		return header;
	}
}
