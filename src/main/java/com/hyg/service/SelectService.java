package com.hyg.service;

import com.hyg.pojo.*;
import com.hyg.util.RespondJson;

/**
 * 这里边的所有服务都是返回前端需要的表的json数据 的java对象
 * 返回的是所有表的 所有数据 的所有字段
 */
public interface SelectService
{
	/**
	 * 团队文集表
	 * @return
	 */
	RespondJson<ArticleExpand> getArticleData();

	/**
	 * 轮播图表
	 * @return
	 */
	RespondJson<Carousel> getCarouselData();

	/**
	 * 留言表
	 * @return
	 */
	RespondJson<MsgBoard> getMsgBoardData();

	/**
	 * 用户表
	 * @return
	 */
	RespondJson<User> getUserData();

	/**
	 * 事务所表，只有一条数据
	 * @return
	 */
	RespondJson<Agency> getAgencyData();

	/**
	 * 律师表
	 * @return
	 */
	RespondJson<Lawyer> getLawyerData();

	RespondJson<Menu> getMenuData();
}