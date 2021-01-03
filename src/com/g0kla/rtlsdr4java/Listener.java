package com.g0kla.rtlsdr4java;

public interface Listener<T>
{
	public void receive( T t );
}