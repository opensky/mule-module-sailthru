package org.mule.module.sailthru.api;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

public interface MuleSailthruClient
{
    
    
    Map<String,Object> email(String emailAddress, 
            Map<String,Object> vars, 
            Map<String, Integer> lists, 
            String optout)  throws IOException;
    
    Map<String,Object> send(Collection<String> emailAddress, 
            String template, 
            Map<String,Object> vars, 
            Date scheduledTime, 
            Map<String,Object> options) throws IOException;
    
    Map<String,Object> user(String id, String key, 
            Map<String,String> keys, Collection<String> fields, 
            Map<String,Object> vars, Map<String,Integer> lists, 
            String optout, Boolean login, String keysConflict) throws IOException;
}
