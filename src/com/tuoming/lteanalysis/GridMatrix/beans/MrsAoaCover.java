package com.tuoming.lteanalysis.GridMatrix.beans;

import java.util.Arrays;

public class MrsAoaCover {
	public String	date;
	public long	eci;
	public short	inclinationtype;
	public float aoa[] = new float[72];
	public float	avgaoa;
	@Override
	public String toString() {
		
		String line = date + "," + eci + "," + inclinationtype + "," + Arrays.toString(aoa) + "," + avgaoa;
		return line.replace("[", "").replace("]", "");
	}
	
}