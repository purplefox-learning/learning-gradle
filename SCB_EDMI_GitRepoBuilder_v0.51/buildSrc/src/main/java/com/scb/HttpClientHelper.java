package com.scb;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.ResponseHandler;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.auth.UsernamePasswordCredentials;



public class HttpClientHelper {

	private CloseableHttpClient httpclient;
	private HttpUriRequest requestBase;	//could be an instance of HttpGet or HttpPost;
	
	public static final String GET = "GET";
	public static final String POST = "POST";
	
	private static String remoteStash = "https://bitbucket.global.standardchartered.com/rest/api/1.0/users/1551052/repos";	//post
	private static String queryCatalogMetadata = "http://10.20.175.64:9000/rest/model-service-compound?packageName=";	//get
	private static String queryFsPackages = "http://10.20.175.165:5502/rest/CCMConfigurationManagement.getFSPackagesInDomain?domain=";	//get
	private static String queryFsConfigXml = "http://10.20.175.165:5502/rest/CCMConfigurationManagement.createConfigXML?FSPackageName=";	//get
	private static String queryAssociatedPackages = "http://10.20.175.165:5502/rest/CCMConfigurationManagement.getAssociatedPackagesForFS";	//post
	
	//--- Main Entry Points ---
	public static String createRemoteRepo(String gitJsonReq) throws Exception {
		HttpClientHelper hc = new HttpClientHelper(remoteStash,POST);
		//System.out.println("createRemoteRepo url is "+remoteStash);
		//System.out.println("gitJsonReq is "+gitJsonReq);
		//hc.setBasicAuthentication("1551052", "whateverpassword");
		hc.setHeader("authorization","Basic MTU1MTA1MjpQdXJwbGVmb3g4MTA5MzkwMg==");	//this is chenming's bank id and pwd
		hc.setContentType("application/json");
		hc.setContentRaw(gitJsonReq);
		String s = hc.executeSimple();
		hc.closeHttpClient();
		return s;
	}
	public static String getCatalogMetadata(String name) throws Exception {
		String catalogMetadata = queryCatalogMetadata + name;	//name = scbCoreBankingCustomer etc
		//System.out.println("getCatalogMetadata url is "+catalogMetadata);
		HttpClientHelper hc = new HttpClientHelper(catalogMetadata,GET);
		String s = hc.executeSimple();
		hc.closeHttpClient();
		return s;
	}
	public static String getFsPackages(String domain) throws Exception {
		String fspacks = queryFsPackages + domain;	//domain = Cash etc
		//System.out.println("getFsPackages url is "+fspacks);
		HttpClientHelper hc = new HttpClientHelper(fspacks,GET);
		hc.setBasicAuthentication("Administrator", "manage");
		String s = hc.executeSimple();
		hc.closeHttpClient();
		return s;
	}
	public static String getPackageConfigXml(String packageName) throws Exception {
		String fsConfig = queryFsConfigXml + packageName;	//packageName = scbCoreBankingCustomer etc
		//System.out.println("getPackageConfigXml url is "+fsConfig);
		HttpClientHelper hc = new HttpClientHelper(fsConfig,GET);
		hc.setBasicAuthentication("Administrator", "manage");
		String s = hc.executeSimple();
		hc.closeHttpClient();
		return s;
	}
	public static String getAssociatedPackagesForFS(String xml) throws Exception {
		HttpClientHelper hc = new HttpClientHelper(queryAssociatedPackages,POST);
		//System.out.println("getAssociatedPackagesForFS url is "+queryAssociatedPackages);
		hc.setBasicAuthentication("Administrator", "manage");
		hc.setContentType("text/xml");
		//hc.setContentRaw(getContentFromFile("c:/a/temp-edmiFunctionServiceConfig-scbCoreBankingCustomer.xml"));
		hc.setContentRaw(xml);
		String s = hc.executeSimple();
		hc.closeHttpClient();
		return s;
	}
	//--- End of Main Entry Points ---
	
	public HttpClientHelper(String url, String httpMethod) {
		httpclient = HttpClients.createDefault();
		if (httpMethod.equals(GET)) requestBase = new HttpGet(url);
		else requestBase = new HttpPost(url);
	}
	
	public void setBasicAuthentication(String user, String pwd) throws Exception {
		UsernamePasswordCredentials creds = new UsernamePasswordCredentials(user, pwd);
		requestBase.addHeader(new BasicScheme().authenticate(creds,requestBase,null));
	}
	
	public void setContentType(String type) throws Exception {
		//type can be "application/json" or "text/xml" etc
		setHeader("Content-type",type);
	}
	
	public void setHeader(String key, String value) throws Exception {
		requestBase.setHeader(key,value);
	}
	
	public void setContentRaw(String content) throws Exception {
		HttpPost httpPost = (HttpPost) requestBase;
		StringEntity entity = new StringEntity(content);
		httpPost.setEntity(entity);
	}
	
	public void setContentPaired(Map<String, String> map) throws Exception {
		HttpPost httpPost = (HttpPost) requestBase;
        List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		
		//nvps.add(new BasicNameValuePair("username", "vip"));
		//nvps.add(new BasicNameValuePair("password", "pwd"));
		for (Map.Entry<String, String> entry : map.entrySet())
		{
			//System.out.println(entry.getKey() + "/" + entry.getValue());
			nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
        
        httpPost.setEntity(new UrlEncodedFormEntity(nvps));
	}
	
	public String executeSimple() throws Exception {
		ResponseHandler<String> responseHandler=new BasicResponseHandler();
		String responseBody = httpclient.execute(requestBase, responseHandler);
		//System.out.println(responseBody);
        //JSONObject response=new JSONObject(responseBody);
		return responseBody;
	}
	
	public String executeFlexible() throws Exception {
		
		String responseBody;
		CloseableHttpResponse resp = null;
		
		try {
			
			resp = httpclient.execute(requestBase);
			
			//status
			System.out.println(resp.getStatusLine().getStatusCode());
			
			//content
			HttpEntity entity1 = resp.getEntity();
			responseBody = EntityUtils.toString(entity1, "UTF-8");			
			System.out.println(responseBody);
			
		} finally {
			//close() has to be done in a finally block
			resp.close();
		}
		
		return responseBody;
	}
	
	public void closeHttpClient() throws Exception {
		httpclient.close();
	}

	public static String getContentFromFile(String absPath) throws Exception {
		//String absPath = "c:/a/step2response.xml";
		
		System.out.println("reading file "+absPath);
		byte[] encoded  = java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(absPath));
		
		//String s = new String(encoded, java.nio.charset.StandardCharsets.UTF_8);
		String s = new String(encoded);
		
		//System.out.println(s);
		return s;
	}

}

