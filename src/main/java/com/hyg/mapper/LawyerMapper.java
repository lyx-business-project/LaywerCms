package com.hyg.mapper;

import com.hyg.pojo.Lawyer;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Mapper
@Service("lawyerMapper")
public interface LawyerMapper
{

	/**
	 * 根据id获得一个律师的信息
	 * @param lawyerId
	 * @return
	 */
	@Select(" select * from `t_lawyer` where `deleteFlag`!='1' and `lawyerId`=#{lawyerId} ")
	Lawyer getOneLawyerById(int lawyerId);

	/**
	 * 向律师表中添加一条数据
	 * @param lawyer
	 */
	@Insert("insert into `t_lawyer` (`lawyerName`,`lawyerLevel`,`introduction`,`lawyerImg`,`deleteFlag`)" +
				"values (#{lawyerName},#{lawyerLevel},#{introduction},#{lawyerImg},#{deleteFlag});")
	void insertOneLawyer(Lawyer lawyer);

	/**
	 * 根据id删除一个律师 逻辑删除
	 * @param lawyerId
	 */
	@Update(" update `t_lawyer` set `deleteFlag`='1' where `lawyerId`=#{lawyerId} ")
	void deleteOneLawyerById(int lawyerId);

	/**
	 * 编辑律师信息
	 * @param lawyer
	 */
	@Update(" update `t_lawyer` set `lawyerName`=#{lawyerName},`lawyerLevel`=#{lawyerLevel},`introduction`=#{introduction},`lawyerImg`=#{lawyerImg} " +
				" where `lawyerId`=#{lawyerId} ")
	void updateOneLawyerBiId(Lawyer lawyer);

	/**
	 * 根据律师姓名与律师职称搜索律师
	 * @param par 参数 携带律师名与职称
	 * @return
	 */
	@Select(" select * from `t_lawyer` " +
				" where `deleteFlag`!='1' " +
					" and ( `lawyerName` like concat('%',#{lawyerName},'%') and `lawyerLevel` like concat('%',#{lawyerLevel},'%') ) ")
	List<Lawyer> listLawyersByNameAndLevel(Map<String, String> par);
}