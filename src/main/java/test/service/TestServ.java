package test.service;

import java.util.Objects;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;

@Service
public class TestServ {
	
	final static Logger logger  = Logger.getLogger("com.couchbase.client");
	
    /**
     * Create a rec.
     * @return 
     */
    public static ResponseEntity<String> createAndGet(final Bucket bucket, final JsonObject jsonData) {
    	
    	JsonDocument status = null;
    	
       	String uuid = jsonData.getInt("uuid").toString();
       	
        try {
            
	            long startTime = System.currentTimeMillis();
				status = bucket.upsert(JsonDocument.create(uuid, 1800, jsonData));
				long elapsedTimeSet = System.currentTimeMillis() - startTime;
				logger.info(" spookreq SET Request took ### "+elapsedTimeSet+" ### ms , UUID: "+uuid);
				logger.info(" status after SET " + " uuid "+ status.id());
             
                 long startTimeGet = System.currentTimeMillis();
              	 JsonDocument get = bucket.get(uuid);
                 long elapsedTimeGet = System.currentTimeMillis() - startTimeGet;
                 
                 logger.info(" spookreq GET Request took ### "+elapsedTimeGet + " ### ms , UUID:" + uuid);
            
        	     JsonObject content = get == null ? null : get.content();

			if (!Objects.equals(jsonData, content)) {
				 logger.info("Mismatched data!: "+ uuid);
				 return new ResponseEntity<String>(content.toString(), HttpStatus.CONFLICT);
			}
				
			return new ResponseEntity<String>(content.toString(), HttpStatus.OK);
			
        } catch (Exception e) {
        	logger.info("Exception ->" + uuid + "Message ->" + e.getMessage() + "Trace ->" + e.getStackTrace());
            JsonObject responseData = JsonObject.empty().put("success", false).put("failure", "There was an error creating account").put("exception", e.getMessage());
            return new ResponseEntity<String>(responseData.toString(), HttpStatus.NO_CONTENT);
        }
        
    }
    
    /**
     * Create a rec.
     * @return 
     */
    public static ResponseEntity<String> createAndRead(final Bucket bucket, final JsonObject jsonData,String uuid) {
    	
    	JsonDocument status = null;
    	
        try {
            
        	   logger.info("System Env -> "+System.getenv());
        	   logger.info("Bucket -> "+bucket);
        	
	            long startTime = System.currentTimeMillis();
				status = bucket.upsert(JsonDocument.create(uuid, 1800, jsonData));
				long elapsedTimeSet = System.currentTimeMillis() - startTime;
				logger.info(" spookreq SET Request took ### "+elapsedTimeSet+" ### ms , UUID: "+uuid);
				logger.info(" status after SET " + " uuid "+ status.id());
				
				logger.info("System Env -> "+System.getenv());
				logger.info("Bucket -> "+bucket);
             
                 long startTimeGet = System.currentTimeMillis();
              	 JsonDocument get = bucket.get(uuid);
                 long elapsedTimeGet = System.currentTimeMillis() - startTimeGet;
                 
                 logger.info(" spookreq GET Request took ### "+elapsedTimeGet + " ### ms , UUID:" + uuid);
            
        	     JsonObject content = get == null ? null : get.content();

			if (!Objects.equals(jsonData, content)) {
				 logger.info("Mismatched data!: "+ uuid);
				 return new ResponseEntity<String>(content.toString(), HttpStatus.CONFLICT);
			}
				
			return new ResponseEntity<String>(get.id() + content.toString(), HttpStatus.OK);
			
        } catch (Exception e) {
        	logger.info("Exception ->" + uuid + "Message ->" + e.getMessage() + "Trace ->" + e.getStackTrace());
            JsonObject responseData = JsonObject.empty().put("success", false).put("failure", "There was an error creating account").put("exception", e.getMessage());
            return new ResponseEntity<String>(responseData.toString(), HttpStatus.NO_CONTENT);
        }
    }

}
