package com.api.jasa.auth.model.test;

import com.api.jasa.auth.db.entity.TbMaster;
import com.api.jasa.auth.model.ResponseModel;

public class TestGenerateResponseModel extends ResponseModel {
	
	public TestGenerateResponseModel(TestGenerateRequestModel requestModel) {
		super(requestModel);
	}
	
	private TbMaster tbMaster;

	public TbMaster getTbMaster() {
		return tbMaster;
	}

	public void setTbMaster(TbMaster tbMaster) {
		this.tbMaster = tbMaster;
	}
}
