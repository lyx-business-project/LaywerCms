package com.hyg.mapper;

import com.hyg.pojo.Case;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository("caseMapper")
public interface CaseMapper
{
	/**
	 * 添加一个案例
	 * @param oneCase
	 */
	@Insert(" insert into `t_case` (`chargeId`,`title`,`desc`,`process`,`result`,`lessions`,`publishDate`,`successFlag`,`editDate`,`count`,`deleteFlag`,`picUrl`) " +
				" values (#{chargeId},#{title},#{desc},#{process},#{result},#{lessions},#{publishDate},#{successFlag},#{editDate},#{count},#{deleteFlag},#{picUrl}) ")
	void insertOneDate(Case oneCase);

	/**
	 * 查询所有数据
	 * @return
	 */
	@Select(" select * from `t_case` ")
	List<Case> listAllCase();

	/**
	 * 根据id获得一个案例
	 * @param id
	 * @return
	 */
	@Select(" select * from `t_case` where `id`=#{id} ")
	Case getOneCaseById(int id);

	/**
	 * 根据id删除一个案例
	 * @param id
	 */
	@Update(" delete from `t_case` where `id`=#{id} ")
	void deleteOneCaseById(int id);

	/**
	 * 根据id 编辑一个案例
	 * 更新的字段：chargeId title desc process result lessions successFlag editDate
	 * @param oneCase
	 */
	@Update(" update `t_case` set `chargeId`=#{chargeId},`title`=#{title},`desc`=#{desc},`process`=#{process},`result`=#{result},`lessions`=#{lessions},`successFlag`=#{successFlag},`editDate`=#{editDate},`picUrl`=#{picUrl} " +
				" where `id`=#{id} ")
	void updateEditOneCase(Case oneCase);
}