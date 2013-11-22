/**
 *  ClassName: LoginQuestion.java
 *  created on 2013年11月22日
 *  Copyrights 2011-2012 qjyong All rights reserved.
 *  site: http://blog.csdn.net/qjyong
 *  email: qjyong@gmail.com
 */
package com.snowd.android.jimi.model;

import java.io.Serializable;

/**
 * @author qjyong
 */
public class LoginQuestion implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3808999012363566786L;
	public String id;
	public String value;
	public String toString() {
		return value;
	}
}
