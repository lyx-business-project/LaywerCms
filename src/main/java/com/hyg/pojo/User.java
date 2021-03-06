package com.hyg.pojo;

import lombok.Data;

/**
 * 登录用户表
 */
@Data
public class User
{
	private Integer id;
	private String password;
	private String loginName;
	private String realName;
	private String gender;
	private String userTel;
	private String email;
	private String position;
	private String deleteFlag;
	private String role;

	public static class GenderType
	{
		public static final String MAN = "男";
		public static final String WOMAN = "女";
	}
}