/*******************************************************************************
 * @author Copyright (C) 2019 ICreated, Sergey Polyarus
 *  @date 2019
 *  This program is free software; you can redistribute it and/or modify it
 *  under the terms version 2 of the GNU General Public License as published
 *  by the Free Software Foundation. This program is distributed in the hope
 *  that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 *  warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *  See the GNU General Public License for more details.
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc., 
 *  59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.
 ******************************************************************************/
package org.icreated.wstore.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

import org.icreated.wstore.bean.SessionUser;

public class ServiceFactory {

	
	
    public static <T extends AService> T create(Properties ctx, Class<T> type) {
    	
    	Class[] cArg = new Class[1];
    	cArg[0] = Properties.class;
//    	cArg[1] = SessionUser.class;
	
     try {
		return (T)type.getDeclaredConstructor(cArg).newInstance(ctx);
		
	} catch (IllegalArgumentException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (InvocationTargetException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (NoSuchMethodException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (SecurityException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (InstantiationException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IllegalAccessException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
     return null;
}
    
    
    public static <T extends AService> T create(Properties ctx, SessionUser sessionUser, Class<T> type) {
    	
    	Class[] cArg = new Class[2];
    	cArg[0] = Properties.class;
    	cArg[1] = SessionUser.class;
	
     try {
		return (T)type.getDeclaredConstructor(cArg).newInstance(ctx, sessionUser);
		
	} catch (IllegalArgumentException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (InvocationTargetException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (NoSuchMethodException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (SecurityException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (InstantiationException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IllegalAccessException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
     return null;
}    

}
