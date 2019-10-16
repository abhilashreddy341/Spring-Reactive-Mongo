package com.challenge.configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;

import com.challenge.modal.EndPointsInfo;
import com.challenge.repository.EndPointsRepository;

/**
 * this class extends the Aware interface {@link ApplicationListener} which listens to the event {@link ContextRefreshedEvent}
 * which implements the onApplicationEvent method to listen to the event {@link ContextRefreshedEvent}
 * @author Abhilash Reddy
 *
 */

@Component
public class EndpointsListener implements ApplicationListener<ContextRefreshedEvent> {
	
	private static final Logger logger = Logger.getLogger(EndpointsListener.class);
	
	@Autowired
	@Qualifier("requestMappingHandlerMapping")
	RequestMappingHandlerMapping requestMappingHandlerMapping;
	
	@Autowired
	EndPointsRepository endPointsRepository;
	
	/**
	 * <p>In this method we are looping through all the handler methods using the java 8 forEach method and uses the regular expression to extract 
	 * the request and response type between the < and > tags and ( and )
	 * <p> all the routes information is saved into the {@link EndPointsInfo} collection
	 */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
    	logger.info("context refresh event handled");
    	ApplicationContext applicationContext = ((ContextRefreshedEvent) event).getApplicationContext();
    	List<EndPointsInfo> endPointsList = new ArrayList<EndPointsInfo>();
    	endPointsRepository.deleteAll().subscribe();
    	requestMappingHandlerMapping.getHandlerMethods().forEach((a,b) -> {
        	 Pattern pattern = Pattern.compile("\\((.*)\\)"); 
        	 String requestType = null;
        	 String responseType = null;
             Matcher m = pattern.matcher(b.toString());
             if (m.find()) {
            	 requestType = m.group(1);
             }
             Pattern pattern2 = Pattern.compile("<(.*)>"); 
             Matcher m2 = pattern2.matcher(b.toString());
             if (m2.find()) {
            	 responseType = m2.group(1);
             }
             endPointsList.add(new EndPointsInfo(a.toString(),requestType, responseType));
             endPointsRepository.insert(new EndPointsInfo(a.toString(),requestType, responseType));
        });
        endPointsRepository.saveAll(endPointsList)
        	.doOnError(error -> {
        	logger.error("exception in saving routes information");
        	})
        .doOnComplete(() -> {
        		logger.info("successfully saved all end points information");
        	})
        	.subscribe();
        
    }
}
