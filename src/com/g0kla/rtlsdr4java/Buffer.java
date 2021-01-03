package com.g0kla.rtlsdr4java;

public class Buffer
{
	protected float[] mSamples;

	public Buffer( float[] samples )
	{
		mSamples = samples;
	}

	public float[] getSamples()
	{
		return mSamples;
	}

	/**
	 * Cleanup method to nullify all data and references
	 */
	public void dispose()
	{
		mSamples = null;
	}
}
