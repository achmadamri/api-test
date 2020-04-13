package com.api.jasa.auth.controller;

import java.util.Date;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.jasa.auth.db.entity.TbDetail;
import com.api.jasa.auth.db.entity.TbMaster;
import com.api.jasa.auth.db.repository.TbDetailRepository;
import com.api.jasa.auth.db.repository.TbMasterRepository;
import com.api.jasa.auth.model.test.TestGenerateRequestModel;
import com.api.jasa.auth.model.test.TestGenerateResponseModel;
import com.api.jasa.auth.util.Uid;
import com.fasterxml.jackson.databind.ObjectMapper;

@CrossOrigin
@RestController
@RequestMapping("/test")
public class TestController {

	private Logger log = LoggerFactory.getLogger(TestController.class);
	
	@Autowired
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@Autowired
	private TbMasterRepository tbMasterRepository;
	
	@Autowired
	private TbDetailRepository tbDetailRepository;
	
	@PostMapping("/generate")
	@Transactional
	public HttpEntity<?> postGenerate(@Valid @RequestBody TestGenerateRequestModel requestModel) throws Exception {
		String fid = new Uid().generateString(20);
		log.info("[fid:" + fid + "] requestModel : " + objectMapper.writeValueAsString(requestModel));
		
		TestGenerateResponseModel responseModel = new TestGenerateResponseModel(requestModel);
		ResponseEntity<?> responseEntity = null;
		
		TbMaster tbMaster = new TbMaster();
		tbMaster.setDate(new Date());
		tbMaster.setData(requestModel.getData());
		tbMasterRepository.save(tbMaster);
		
		TbDetail tbDetail = new TbDetail();
		tbDetail.setDate(new Date());
		tbDetail.setData(requestModel.getData());
		tbDetail.setFk(tbMaster.getPk());
		tbDetailRepository.save(tbDetail);
		
		responseEntity = new ResponseEntity<>(responseModel, HttpStatus.OK);
		log.info("[fid:" + fid + "] responseEntity : " + objectMapper.writeValueAsString(responseEntity));

		return responseEntity;
	}
}
