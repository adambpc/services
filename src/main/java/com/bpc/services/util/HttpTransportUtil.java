/**
 * @author Adam Hardie
 */
package com.bpc.services.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class HttpTransportUtil {
	public String sendHttpPost(HttpPost httppost) throws ClientProtocolException, IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		StringBuffer buffer = new StringBuffer();
		String output;
		try {
			CloseableHttpResponse response = httpclient.execute(httppost);
			int statusCode = response.getStatusLine().getStatusCode();
			BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
			while ((output = br.readLine()) != null) {
				//System.out.println(output);
				buffer.append(output);
			}
			if (statusCode != 200) {
				buffer.append("<httpcode>" + statusCode + "</httpcode>");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			httpclient.close();
		}
		//System.out.println("response" + buffer.toString());
		return buffer.toString();
	}
}