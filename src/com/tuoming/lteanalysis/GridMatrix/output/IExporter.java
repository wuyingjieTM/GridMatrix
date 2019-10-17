package com.tuoming.lteanalysis.GridMatrix.output;

import com.tuoming.lteanalysis.GridMatrix.config.configer;

public interface IExporter {
	int init(configer config);
	void Export(Object data);
	void Export(String data);
	void finish();
}
