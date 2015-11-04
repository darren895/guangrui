package com.guangrui.action;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.opensymphony.xwork2.ActionSupport;

@Component
@Scope("prototype")
public class BaseAction extends ActionSupport implements SessionAware, ServletRequestAware, ServletResponseAware {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5659620343595818233L;

	protected String SUCCESS = "success";
	
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    private Map<String, Object> session = new HashMap<String, Object>();
    
	public String forward(){
		return SUCCESS;
	}
    
    @Override
    public void setServletRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public void setServletResponse(HttpServletResponse response) {
        this.response = response;
    }

	@Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

}
