package com.api.jasa.auth.db.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the tb_users database table.
 * 
 */
@Entity
@Table(name="tb_master")
@NamedQuery(name="TbMaster.findAll", query="SELECT t FROM TbMaster t")
public class TbMaster implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="pk")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer pk;

	@Column(name="date")
	private Date date;
	
	@Column(name = "data")
	private String data;

	public Integer getPk() {
		return pk;
	}

	public void setPk(Integer pk) {
		this.pk = pk;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}