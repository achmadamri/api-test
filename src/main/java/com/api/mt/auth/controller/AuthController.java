package com.api.mt.auth.controller;

import java.util.Date;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.mt.auth.db.entity.TbAuth;
import com.api.mt.auth.db.repository.TbAuthRepository;
import com.api.mt.auth.model.auth.AuthAddRequestModel;
import com.api.mt.auth.model.auth.AuthAddResponseModel;
import com.api.mt.auth.model.auth.AuthCheckRequestModel;
import com.api.mt.auth.model.auth.AuthCheckResponseModel;
import com.api.mt.auth.model.auth.AuthGenerateRequestModel;
import com.api.mt.auth.model.auth.AuthGenerateResponseModel;
import com.api.mt.auth.model.auth.AuthInvalidateRequestModel;
import com.api.mt.auth.model.auth.AuthInvalidateResponseModel;
import com.api.mt.auth.util.MD5;
import com.api.mt.auth.util.SimpleMapper;
import com.api.mt.auth.util.TokenUtil;
import com.api.mt.auth.util.Uid;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;

@CrossOrigin
@RestController
@RequestMapping("/auth")
public class AuthController {

	private Logger log = LoggerFactory.getLogger(AuthController.class);
	
	private TokenUtil tokenUtil = new TokenUtil();
	
	@Autowired
	private Environment env;
	
	@Autowired
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@Autowired
	private TbAuthRepository tbAuthRepository;
	
	@PostMapping("/add")
	@Transactional
	public HttpEntity<?> postAdd(@Valid @RequestBody AuthAddRequestModel requestModel) throws Exception {
		String fid = new Uid().generateString(20);
		log.info("[fid:" + fid + "] requestModel : " + objectMapper.writeValueAsString(requestModel));
		
		SimpleMapper simpleMapper = new SimpleMapper();
		AuthAddResponseModel responseModel = new AuthAddResponseModel(requestModel);
		ResponseEntity<?> responseEntity = null;
		
		TbAuth exampleTbAuth = new TbAuth();
		exampleTbAuth.setTbaEmail(requestModel.getTbaEmail());
		Optional<TbAuth> optTbAuth = tbAuthRepository.findOne(Example.of(exampleTbAuth));
		
		optTbAuth.ifPresentOrElse(tbUser -> {
			responseModel.setStatus("208");
			responseModel.setMessage("Email already exists");
		}, () -> {
			TbAuth tbAuth = new TbAuth();
			tbAuth = (TbAuth) simpleMapper.assign(requestModel, tbAuth);
			
			tbAuth.setTbaCreateDate(new Date());
			tbAuth.setTbaCreateId(0);
			tbAuthRepository.save(tbAuth);
			
			responseModel.setStatus("200");
			responseModel.setMessage("Email added");
		});
		
		responseEntity = new ResponseEntity<>(responseModel, responseModel.getStatus().equals("208") ? HttpStatus.ALREADY_REPORTED : HttpStatus.OK);
		log.info("[fid:" + fid + "] responseEntity : " + objectMapper.writeValueAsString(responseEntity));

		return responseEntity;
	}
	
	@PostMapping("/generate")
	@Transactional
	public HttpEntity<?> postGenerate(@Valid @RequestBody AuthGenerateRequestModel requestModel) throws Exception {
		String fid = new Uid().generateString(20);
		log.info("[fid:" + fid + "] requestModel : " + objectMapper.writeValueAsString(requestModel));
		
		MD5 md5 = new MD5();
		requestModel.setTbaPassword(md5.get(requestModel.getTbaPassword()));
		
		AuthGenerateResponseModel responseModel = new AuthGenerateResponseModel(requestModel);
		ResponseEntity<?> responseEntity = null;
		
		TbAuth exampleTbAuth = new TbAuth();
		exampleTbAuth.setTbaEmail(requestModel.getTbaEmail());
		exampleTbAuth.setTbaPassword(requestModel.getTbaPassword());
		Optional<TbAuth> optTbAuth = tbAuthRepository.findOne(Example.of(exampleTbAuth));
		
		optTbAuth.ifPresentOrElse(tbUser -> {
			String token = tokenUtil.generate(optTbAuth.get().getTbaEmail(), new String[] {
					env.getProperty("services.rest.member.url")
			});
			
			try {
				requestModel.setEmail(optTbAuth.get().getTbaEmail());
				requestModel.setToken(token);
				Claims claims = tokenUtil.claims(requestModel);
				responseModel.setClaims(claims);
				
				responseModel.setToken(token);
				responseModel.setStatus("200");
				responseModel.setMessage("Auth generated");
			} catch (Exception e) {
				responseModel.setStatus("500");
				responseModel.setError(e.getMessage());
			}
		}, () -> {
			responseModel.setStatus("401");
			responseModel.setError("Invalid login");
		});
		
		responseEntity = new ResponseEntity<>(responseModel, responseModel.getStatus().equals("200") ? HttpStatus.OK : (responseModel.getStatus().equals("500") ? HttpStatus.INTERNAL_SERVER_ERROR : HttpStatus.UNAUTHORIZED));
		log.info("[fid:" + fid + "] responseEntity : " + objectMapper.writeValueAsString(responseEntity));

		return responseEntity;
	}
	
	@PostMapping("/check")
	@Transactional
	public HttpEntity<?> postCheck(@Valid @RequestBody AuthCheckRequestModel requestModel) throws Exception {
		String fid = new Uid().generateString(20);
		log.info("[fid:" + fid + "] requestModel : " + objectMapper.writeValueAsString(requestModel));
		
		AuthCheckResponseModel responseModel = new AuthCheckResponseModel(requestModel);
		ResponseEntity<?> responseEntity = null;
		
		try {
			Claims claims = tokenUtil.claims(requestModel);
			responseModel.setClaims(claims);
			
			responseModel.setStatus("200");
			responseModel.setMessage("Auth checked");
		} catch (Exception e) {
			responseModel.setStatus("500");
			responseModel.setError(e.getMessage());
		}
		
		responseEntity = new ResponseEntity<>(responseModel, responseModel.getStatus().equals("200") ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
		log.info("[fid:" + fid + "] responseEntity : " + objectMapper.writeValueAsString(responseEntity));

		return responseEntity;
	}
	
	@GetMapping("/invalidate/{tbaEmail}")
	public HttpEntity<?> getInvalidate(@RequestParam String email, @RequestParam String token, @RequestParam String requestId, @RequestParam String requestDate, @PathVariable String tbaEmail) throws Exception {
		AuthInvalidateRequestModel requestModel = new AuthInvalidateRequestModel();
		requestModel.setEmail(email);
		requestModel.setToken(token);
		requestModel.setRequestId(requestId);
		requestModel.setRequestDate(requestDate);
		
		String fid = new Uid().generateString(20);
		log.info("[fid:" + fid + "] requestModel : " + objectMapper.writeValueAsString(requestModel));
		
		tokenUtil.claims(requestModel);
		
		AuthInvalidateResponseModel responseModel = new AuthInvalidateResponseModel(null);
		ResponseEntity<?> responseEntity = null;
		
		TbAuth exampleTbAuth = new TbAuth();
		exampleTbAuth.setTbaEmail(tbaEmail);
		Optional<TbAuth> optTbAuth = tbAuthRepository.findOne(Example.of(exampleTbAuth));
		
		optTbAuth.ifPresentOrElse(tbAuth -> {
			tokenUtil.invalidate(tbaEmail, new String[] {
					env.getProperty("services.rest.member.url")
			});
			
			responseModel.setStatus("200");
			responseModel.setMessage("Auth invalidated");
		}, () -> {
			responseModel.setStatus("404");
			responseModel.setMessage("Not found");
		});
		
		responseEntity = new ResponseEntity<>(responseModel, responseModel.getStatus().equals("200") ? HttpStatus.OK : HttpStatus.NOT_FOUND);
		log.info("[fid:" + fid + "] responseEntity : " + objectMapper.writeValueAsString(responseEntity));

		return responseEntity;
	}
}
