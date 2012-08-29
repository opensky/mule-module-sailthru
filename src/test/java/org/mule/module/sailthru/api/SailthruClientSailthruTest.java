/**
 * foo.
 */

package org.mule.module.sailthru.api;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.sailthru.client.handler.response.JsonResponse;
import com.sailthru.client.params.Email;
import com.sailthru.client.params.MultiSend;
import com.sailthru.client.params.Send;

public class SailthruClientSailthruTest
{
    
    Properties props;
    
    SailthruClientSailthru client;
    com.sailthru.client.SailthruClient mockClient;
    
    @Before
    public void setup() throws Exception
    {
        props = new Properties();
        props.load(this.getClass().getResourceAsStream("/test.properties"));
        
        props.putAll(System.getProperties());
        
        client = new SailthruClientSailthru(props.getProperty("api_key"), props.getProperty("shared_secret"), props.getProperty("base_api"));
        mockClient = mock(com.sailthru.client.SailthruClient.class);
        
        client.setClient(mockClient);
    }

    @Test
    public void testSailthruClientSailthru()
    {
        MuleSailthruClient client2 = new SailthruClientSailthru(props.getProperty("api_key"), props.getProperty("shared_secret"), props.getProperty("base_api"));
        assertNotNull(client2);
    }

    @Test
    public void testEmail() throws Exception
    {
        Email s = new Email();
        s.setEmail("test@example.com");
        
        Map<String,Object> resp2 = new HashMap<String,Object>();
        resp2.put("email","example@example.com");
        
        JsonResponse resp = new JsonResponse(resp2);
        
        when(mockClient.setEmail(any(Email.class))).thenReturn(resp);
        
        client.email("test@example.com",null,null,null);
        ArgumentCaptor<Email> args = ArgumentCaptor.forClass(Email.class);
        verify(mockClient).setEmail(args.capture());
        
        assertEquals("test@example.com",args.getValue().toHashMap().get("email"));
    }
    
    @Test
    public void testEmailWithOptout() throws Exception
    {
        Email s = new Email();
        s.setEmail("test@example.com");
        
        Map<String,Object> resp2 = new HashMap<String,Object>();
        resp2.put("email","example@example.com");
        resp2.put("optout","blast");
        
        JsonResponse resp = new JsonResponse(resp2);
        
        when(mockClient.setEmail(any(Email.class))).thenReturn(resp);
        
        client.email("test@example.com",null,null,"blast");
        ArgumentCaptor<Email> args = ArgumentCaptor.forClass(Email.class);
        verify(mockClient).setEmail(args.capture());
        
        assertEquals("test@example.com",args.getValue().toHashMap().get("email"));
    }

    @Test
    public void testSend() throws Exception
    {
        
        Map<String,Object> resp2 = new HashMap<String,Object>();
        resp2.put("send_id","testME");
        resp2.put("email","example@example.com");
        resp2.put("template","welcome");
        
        JsonResponse resp = new JsonResponse(resp2);
        
        when(mockClient.multiSend(any(MultiSend.class))).thenReturn(resp);
        
        client.send(Collections.singleton("test@example.com"), "welcome", null, null,null);
        ArgumentCaptor<MultiSend> args = ArgumentCaptor.forClass(MultiSend.class);
        verify(mockClient).multiSend(args.capture());
        
        assertEquals("test@example.com",args.getValue().toHashMap().get("email"));
        assertEquals("welcome",args.getValue().toHashMap().get("template"));
        assertNull(args.getValue().toHashMap().get("vars"));
    }

    @Test
    public void testGetClient()
    {
        assertTrue(client.getClient() instanceof com.sailthru.client.SailthruClient);
    }

    @Test
    public void testSetClient()
    {
        assertEquals(mockClient,client.getClient());
    }

    @Test
    public void testGetApiKey()
    {
        assertEquals(props.getProperty("api_key"),client.getApiKey());
    }

    @Test
    public void testSetApiKey()
    {
        client.setApiKey("A");
        assertEquals("A",client.getApiKey());
    }

    @Test
    public void testGetApiSecret()
    {
        assertEquals(props.getProperty("shared_secret"),client.getApiSecret());
    }

    @Test
    public void testSetApiSecret()
    {
        client.setApiSecret("B");
        assertEquals("B",client.getApiSecret());
    }

    @Test
    public void testGetApiURL()
    {
       assertEquals(props.getProperty("base_api"),client.getApiURL());
    }

    @Test
    public void testSetApiURL()
    {
        client.setApiURL("http://www.sailthru.com");
        assertEquals("http://www.sailthru.com",client.getApiURL());
    }

}
