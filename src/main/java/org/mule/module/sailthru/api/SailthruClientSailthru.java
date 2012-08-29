/**
 * foo.
 */

package org.mule.module.sailthru.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.management.RuntimeErrorException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sailthru.client.SailthruClient;
import com.sailthru.client.exceptions.ApiException;
import com.sailthru.client.handler.response.JsonResponse;
import com.sailthru.client.params.Email;
import com.sailthru.client.params.MultiSend;
import com.sailthru.client.params.Send;
import com.sailthru.client.params.User;

public class SailthruClientSailthru implements MuleSailthruClient
{
    
    private transient final Logger LOG = LoggerFactory.getLogger(this.getClass());
    
    private SailthruClient client;
    private String apiKey;
    private String apiSecret;
    private String apiURL;
    
    
    public SailthruClientSailthru(String apiKey, String apiSecret, String apiURL)
    {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.apiURL = apiURL;
        
    }

    @Override
    public Map<String, Object> email(String emailAddress, Map<String, Object> vars, Map<String, Integer> lists, String optout) throws IOException
    {
        LOG.info("Adding email {}",emailAddress);
        LOG.debug("emailAddress: {}, vars: {}, optout: {}",new Object[]{emailAddress,vars,optout});

        Email email = new Email();
        email.setEmail(emailAddress);
        email.setVars(vars);
        if(null != optout)
        {
            email.setOptout(optout.toString());
        }
        if(MapUtils.isNotEmpty(lists)) email.setLists(lists);
        JsonResponse response = handleErrorResponse(getClient().setEmail(email));
        LOG.debug("Response: {}",response.getResponse());
        return response.getResponse();
    }

    public Map<String, Object> send(Collection<String> emailAddresses, String template,
            Map<String, Object> vars, Date scheduledTime,
            Map<String, Object> options) throws IOException
    {
        LOG.info("Sending email template {} to {}",template,emailAddresses);
        LOG.debug("emailAddress: {}, template:{}, vars: {}, scheduledTime: {}, options: {}",new Object[]{emailAddresses,template,vars,scheduledTime,options});
        MultiSend send = new MultiSend();
        send.setEmails(new ArrayList<String>(emailAddresses));
        send.setTemplate(template);
        if(null != vars && !(vars.isEmpty()))
        {
            send.setVars(vars);
        }
        
        if(null != scheduledTime)
        {
            send.setScheduleTime(scheduledTime);
        }
        
        if(null != options && !(options.isEmpty()))
        {
            send.setOptions(options);
        }
        JsonResponse response = handleErrorResponse(getClient().multiSend(send));
        LOG.debug("Response: {}",response.getResponse());
        
        return response.getResponse();
    }
    
    @Override
    public Map<String, Object> user(String id, String key,
            Map<String, String> keys, Collection<String> fields, Map<String, Object> vars,
            Map<String, Integer> lists, String optout, Boolean login, String keysConflict) throws IOException
    {
        LOG.info("Adding user with id {}({})",id,key);
        LOG.debug("id: {}, key: {},keys: {}, fields: {}, vars: {}, lists: {}, optout: {}, login: {}",new Object[]{id,key,keys,fields,vars,lists,optout,login});
        
        User u = new User(id);
        
        if(CollectionUtils.isNotEmpty(fields))
        {
            Map<String,Object> fields2Use = new HashMap<String, Object>();
            for(String f:fields)
            {
                fields2Use.put(f,1);
            }
            
            u.setFields(fields2Use);
        }
        if(StringUtils.isNotBlank(key)) u.setKey(key);
        if(MapUtils.isNotEmpty(keys)) u.setKeys(keys);
        if(MapUtils.isNotEmpty(lists)) u.setLists(lists);
        if(null != login) u.setLogin(login?1:0);
        if(StringUtils.isNotBlank(optout)) u.setOptoutEmail(optout);
        if(MapUtils.isNotEmpty(vars)) u.setVars(vars);
        if(StringUtils.isNotBlank(keysConflict)) u.setKeysConflict(keysConflict);
        
        JsonResponse response = handleErrorResponse(getClient().saveUser(u));
        LOG.debug("Response: {}",response.getResponse());
        
        return response.getResponse();
    }
    
    protected JsonResponse handleErrorResponse(JsonResponse resp) throws ApiException
    {
        if(!resp.isOK())
        {
           throw new ApiException(200,resp.getResponse().get("errormsg").toString(), resp.getResponse());
        }
        
        return resp;
    }
    

    public SailthruClient getClient()
    {
        if(null == this.client)
        {
            setClient(new SailthruClient(apiKey, apiSecret, apiURL));
        }
        
        return this.client;
    }

    public void setClient(SailthruClient client)
    {
        this.client = client;
    }

    public String getApiKey()
    {
        return apiKey;
    }

    public void setApiKey(String apiKey)
    {
        this.apiKey = apiKey;
    }

    public String getApiSecret()
    {
        return apiSecret;
    }

    public void setApiSecret(String apiSecret)
    {
        this.apiSecret = apiSecret;
    }

    public String getApiURL()
    {
        return apiURL;
    }

    public void setApiURL(String apiURL)
    {
        this.apiURL = apiURL;
    }
}
