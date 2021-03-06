package com.hyg.service.impl;

import com.github.pagehelper.PageHelper;
import com.hyg.mapper.ChargeTypeMapper;
import com.hyg.mapper.ChargeTypeQuestionMapper;
import com.hyg.pojo.ChargeTypeQuestion;
import com.hyg.pojo.extend.ChargeTypeQuestionExtend;
import com.hyg.service.ChargeTypeQuestionService;
import com.hyg.util.respond.RespondJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service("chargeTypeQuestionServiceImpl")
public class ChargeTypeQuestionServiceImpl implements ChargeTypeQuestionService
{
	@Autowired
	@Qualifier("chargeTypeQuestionMapper")
	private ChargeTypeQuestionMapper mapper;

	@Autowired
	@Qualifier("chargeTypeMapper")
	private ChargeTypeMapper chargeTypeMapper;

	/**
	 * 插入一条问答数据
	 * 前端传过来的数据：chargeTypeName  question、answer
	 *
	 * @param question
	 * @return
	 */
	@Override
	public boolean insertOneChargeTypeQuestion(ChargeTypeQuestion question, String chargeTypeName)
	{
		int chargeTypeId;
		try
		{
			chargeTypeId = chargeTypeMapper.getOneChargeTypeByName(chargeTypeName).getId();
		}
		catch (Exception e) // 没有查到数据
		{
			System.out.println("出现异常：" + e.getMessage());
			return false;
		}

		question.setChargeTypeId(chargeTypeId);
		question.setEditDate(new Timestamp(System.currentTimeMillis()));
		question.setCount(0);
		question.setDeleteFlag("0");

		try
		{
			mapper.insertOneChargeTypeQuestion(question);
		}
		catch (Exception e)
		{
			System.out.println("出现异常：" + e.getMessage());
			return false;
		}

		return true;
	}

	/**
	 * 分页数据
	 *
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@Override
	public RespondJson<ChargeTypeQuestionExtend> pageData(int pageNum, int pageSize)
	{
		List<ChargeTypeQuestion> list = mapper.listAllQuestion(); // 分页前查询，为了长度

		PageHelper.startPage(pageNum, pageSize);
		List<ChargeTypeQuestion> questions = mapper.listAllQuestion();

		// 将 ChargeTypeQuestion 对象转换成 ChargeTypeQuestionExtend对象
		List<ChargeTypeQuestionExtend> extendList = new ArrayList<>(questions.size());
		for (ChargeTypeQuestion foo : questions)
		{
			ChargeTypeQuestionExtend temp = new ChargeTypeQuestionExtend(foo, chargeTypeMapper.getOneChargetypenameById(foo.getChargeTypeId()));
			extendList.add(temp);
		}

		return new RespondJson<>(0, null, list.size(), extendList);
	}

	/**
	 * 根据id删除一个问答
	 *
	 * @param id
	 * @return
	 */
	@Override
	public boolean deleteOneQuestionById(int id)
	{
		try
		{
			mapper.deleteOneQuestionById(id);
		}
		catch (Exception e)
		{
			System.out.println("发生异常：" + e.getMessage());
			return false;
		}

		return true;
	}

	/**
	 * 根据id获得一条数据
	 *
	 * @param id
	 * @return
	 */
	@Override
	public RespondJson<ChargeTypeQuestion> getOneQuestionById(int id)
	{
		ChargeTypeQuestion question = mapper.getOneQuestionById(id);

		if (question != null)
		{
			List<ChargeTypeQuestion> list = new ArrayList<>(1);
			list.add(question);

			return new RespondJson<>(0, null, list.size(), list);
		}
		else
		{
			return new RespondJson<>(0, null, 0, new ArrayList<>(0));
		}
	}

	/**
	 * 编辑一个问答
	 * 前端传过来的数据：id、question、answer
	 *
	 * @param question
	 * @return
	 */
	@Override
	public boolean editOneQuestion(ChargeTypeQuestion question)
	{
		question.setEditDate(new Timestamp(System.currentTimeMillis()));

		try
		{
			mapper.updateEditOneQuestion(question);
		}
		catch (Exception e)
		{
			System.out.println("发生了异常：" + e.getMessage());
			return false;
		}

		return true;
	}
}