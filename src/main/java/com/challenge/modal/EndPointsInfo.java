package com.challenge.modal;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "EndPointsInfo")
public class EndPointsInfo {
	
	private String routeInfo;
	private String requestType;
	private String responseType;
	
	public EndPointsInfo(String routeInfo, String requestType, String responseType) {
		this.routeInfo = routeInfo;
		this.requestType = requestType;
		this.responseType = responseType;
	}
	
	public String getRouteInfo() {
		return routeInfo;
	}

	public void setRouteInfo(String routeInfo) {
		this.routeInfo = routeInfo;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getResponseType() {
		return responseType;
	}

	public void setResponseType(String responseType) {
		this.responseType = responseType;
	}
	

	@Override
	public boolean equals(Object obj) {
		EndPointsInfo endPoint = (EndPointsInfo)obj;
		if (obj == null)
		return false;
		if (this.routeInfo.equals(endPoint.getRouteInfo()) 
				&& this.requestType.equals(endPoint.getRequestType())
				&& this.responseType.equals(endPoint.getResponseType()))
			return true;
		return false;
	}
}
