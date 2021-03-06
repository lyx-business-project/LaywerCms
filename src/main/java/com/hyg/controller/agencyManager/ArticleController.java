package com.hyg.controller.agencyManager;

import com.hyg.pojo.extend.ArticleExpand;
import com.hyg.service.ArticleService;
import com.hyg.shiro.PermissionPrefix;
import com.hyg.util.respond.RespondJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ArticleController
{
	@Autowired
	@Qualifier("articleServiceImpl")
	private ArticleService articleService;

	/*==========================================================================*/

	/**
	 * 根据id
	 * 从团队文集表中删除一条数据
	 * @param id
	 * @return
	 */
	@GetMapping("/" + PermissionPrefix.DELETE_DATA + "/article")
	@ResponseBody
	public String deleteOneArticleById(@RequestParam("id") String id)
	{
		if (articleService.deleteOneArticleById(Integer.parseInt(id)))
		{
			return "success";
		}
		else
		{
			return "fail";
		}
	}

	/**
	 * 添加一篇文章
	 * @param articleExpand
	 * @param model
	 * @return
	 */
	@PostMapping("/" + PermissionPrefix.INSERT_DATA + "/article")
	public String insertArticle(ArticleExpand articleExpand, Model model)
	{
		boolean insertFlag = articleService.insertOneArticle(articleExpand);

		if (insertFlag)
		{
			return "base/collectionMgr";
		}
		else
		{
			model.addAttribute("res", "插入数据失败");
			return "base/collectionAdd";
		}
	}

	/**
	 * 编辑一篇文章
	 * @param articleExpand
	 * @param model
	 * @return
	 */
	@PostMapping("/" + PermissionPrefix.UPDATE_DATA + "/article")
	public String editOneArticle(ArticleExpand articleExpand, Model model)
	{
		boolean success = articleService.editOneArticle(articleExpand);

		if (success)
		{
			return "base/collectionMgr";
		}
		else
		{
			model.addAttribute("res", "更新数据失败");
			return "base/editCollection";
		}
	}

	/**
	 * 分页数据
	 * 包括刚进页面的初始数据也是走这个接口
	 * @param author
	 * @return
	 */
	@GetMapping("/" + PermissionPrefix.SELECT_DATA + "/articlePageData")
	@ResponseBody
	public RespondJson<ArticleExpand> articlePageData
	(
		@RequestParam(value = "page", defaultValue = "1") int pageNum,
		@RequestParam(value = "limit", defaultValue = "10") int pageSize,
		@RequestParam(value = "author", defaultValue = "") String author,
		@RequestParam(value = "typeExpand", defaultValue = "") String typeExpand
	)
	{
		if ("请选择".equals(typeExpand))
		{
			typeExpand = "";
		}

		return articleService.listPageData(pageNum, pageSize, author, typeExpand);
	}

	/**
	 * 根据id获取一篇文章
	 * @param id
	 * @return
	 */
	@GetMapping("/" + PermissionPrefix.SELECT_DATA + "/getOneArticleById")
	@ResponseBody
	public RespondJson<ArticleExpand> getOneArticleById(@RequestParam("id") int id)
	{
		return articleService.getOneArticleById(id);
	}
}