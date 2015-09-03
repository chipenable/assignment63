/* 
**
** Copyright 2014, Jules White
**
** 
*/
package org.magnum.videoup.client;

public interface TaskCallback<T> {
    public void taskCallback(int com, T result, Exception e);
    
}
