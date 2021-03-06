package com.hyg.service.impl;

import com.github.pagehelper.PageHelper;
import com.hyg.config.PicDir;
import com.hyg.mapper.CaseMapper;
import com.hyg.mapper.ChargeMapper;
import com.hyg.mapper.ChargeTypeMapper;
import com.hyg.pojo.Case;
import com.hyg.pojo.extend.CaseExpand;
import com.hyg.pojo.Charge;
import com.hyg.service.CaseService;
import com.hyg.util.FileUtil;
import com.hyg.util.respond.CaseLinkageData;
import com.hyg.util.respond.GetOneCaseJson;
import com.hyg.util.respond.RespondJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service("caseServiceImpl")
public class CaseServiceImpl implements CaseService
{
	@Autowired
	@Qualifier("chargeTypeMapper")
	private ChargeTypeMapper chargeTypeMapper;

	@Autowired
	@Qualifier("chargeMapper")
	private ChargeMapper chargeMapper;

	@Autowired
	@Qualifier("caseMapper")
	private CaseMapper mapper;

	@Value("${cbs.imagesPath}")
	private String picDirSetting; //全局配置文件中设置的图片的路径

	/**
	 * 获得添加案例时的联运数据
	 *
	 * @return
	 */
	@Override
	public RespondJson<CaseLinkageData> getLinkageDate()
	{
		List<CaseLinkageData> data = new ArrayList<>();

		try
		{
			List<String> allTypeName = chargeTypeMapper.listChargeTypeName();
			for (String typeName : allTypeName)
			{
				CaseLinkageData temp = new CaseLinkageData();
				temp.setChargeTypeName(typeName);

				int chargeTypeId = chargeTypeMapper.getOneChargeTypeByName(typeName).getId();
				List chargeNames = chargeMapper.listChargeNameByChargeTypeId(chargeTypeId);
				if (chargeNames.size() > 0)
				{
					temp.setChargeNames(chargeNames);

					data.add(temp);
				}
			}
		}
		catch (Exception e)
		{
			System.out.println("出现了异常：" + e.getMessage());

			data = new ArrayList<>(0);
		}

		return new RespondJson<>(0, null, data.size(), data);
	}

	/**
	 * 添加一个案例
	 * 前端传过来的数据：
	 * chargeName   罪名名称 根据这个获得 chargeId
	 * file			图片文件 根据这人获得picUrl
	 *
	 * title        案例标题
	 * desc         案例介绍
	 * process      办案过程
	 * result	    结果
	 * lessions	    经验心得
	 * successFlag  是否成功
	 *
	 * 后端需要的数据：chargeId、title*、desc*、process*、result*、lessions*、publishDate、successFlag*、editDate、count、deleteFlag、picUrl
	 *
	 * @param oneCase
	 * @return
	 */
	@Override
	public boolean insertOneCase(Case oneCase, String chargeName, MultipartFile file)
	{
		try
		{
			int chargeId = chargeMapper.getIdByChargeName(chargeName);
			oneCase.setChargeId(chargeId);
			oneCase.setPublishDate(new Timestamp(System.currentTimeMillis())); // 发布时间，就是添加数据时的时间
			oneCase.setEditDate(new Timestamp(System.currentTimeMillis()));
			oneCase.setCount(0);
			oneCase.setDeleteFlag("0");

			String allPicDir = picDirSetting.substring(picDirSetting.indexOf(':')+1); //存储图片的总目录
			String casePicDir = allPicDir + PicDir.CASE_DIR;
			String picUrl = FileUtil.savePicToDisk(file, casePicDir, PicDir.CASE_DIR);
			if (picUrl == null)
			{
				return false;
			}
			else
			{
				oneCase.setPicUrl(picUrl);
			}

			mapper.insertOneDate(oneCase);
		}
		catch (Exception e)
		{
			System.out.println("发生异常：" + e.getMessage());
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
	public RespondJson<Case> pageDate(int pageNum, int pageSize)
	{
		List<Case> list = mapper.listAllCase(); // 分页前查询，为了长度

		PageHelper.startPage(pageNum, pageSize);
		List<Case> cases = mapper.listAllCase();

		return new RespondJson<>(0, null, list.size(), cases);
	}

	/**
	 * 根据id获得一个案例
	 *
	 * @param id
	 * @return
	 */
	@Override
	public GetOneCaseJson getOneCaseById(int id)
	{
		try
		{
			Case oneCase = mapper.getOneCaseById(id); // 根据案例id获取一个案例
			Charge charge = chargeMapper.getOneChargeById(oneCase.getChargeId()); // 根据案例的 chargeId 获取它对应的罪名

			System.out.println("++++++++++++++++++++++" + oneCase);
			System.out.println("++++++++++++++++++++++" + oneCase.getChargeId());
			System.out.println("++++++++++++++++++++++" + chargeMapper.getOneChargeById(oneCase.getId()));

			CaseExpand expand = new CaseExpand();
			expand.setId(oneCase.getId());
			expand.setChargeId(oneCase.getChargeId());
			expand.setTitle(oneCase.getTitle());
			expand.setDesc(oneCase.getDesc());
			expand.setProcess(oneCase.getProcess());
			expand.setResult(oneCase.getResult());
			expand.setLessions(oneCase.getLessions());
			expand.setPublishDate(oneCase.getPublishDate());
			expand.setSuccessFlag(oneCase.getSuccessFlag());
			expand.setEditDate(oneCase.getEditDate());
			expand.setCount(oneCase.getCount());
			expand.setDeleteFlag(oneCase.getDeleteFlag());
			expand.setPicUrl(oneCase.getPicUrl());

			expand.setChargeName(charge.getChargeName());
			expand.setChargeTypeName(chargeTypeMapper.getOneChargeTypeById(charge.getChargeTypeId()).getChargeTypeName());
			// 案例数据
			List<CaseExpand> caseList = new ArrayList<>(1);
			caseList.add(expand);

			// 联动数据
			List<CaseLinkageData> linkageData = new ArrayList<>();
			List<String> allTypeName = chargeTypeMapper.listChargeTypeName();
			for (String typeName : allTypeName)
			{
				CaseLinkageData temp = new CaseLinkageData();
				temp.setChargeTypeName(typeName);

				int chargeTypeId = chargeTypeMapper.getOneChargeTypeByName(typeName).getId();
				List chargeNames = chargeMapper.listChargeNameByChargeTypeId(chargeTypeId);
				if (chargeNames.size() > 0)
				{
					temp.setChargeNames(chargeNames);

					linkageData.add(temp);
				}
			}

			return new GetOneCaseJson(0, linkageData, caseList);
		}
		catch (Exception e)
		{
			System.out.println("出现异常：" + e.getMessage());
			e.printStackTrace();
		}

		return new GetOneCaseJson(1, null, null);
	}

	/**
	 * 根据id删除一个案例
	 *
	 * @param id
	 * @return
	 */
	@Override
	public boolean deleteOneCaseById(int id)
	{
		try
		{
			mapper.deleteOneCaseById(id);
		}
		catch (Exception e)
		{
			System.out.println("出现异常：" + e.getMessage());
			return false;
		}

		return true;
	}

	/**
	 * 编辑一个案例
	 * 前端传过来的数据：chargeName file(可能有也可能没有)， id title desc process result lessions successFlag
	 * 后端需要的数据： id* chargeId title* desc* process* result* lessions* successFlag* editDate picUrl
	 * @param oneCase
	 * @param chargeName
	 * @param file
	 * @return
	 */
	@Override
	public boolean editOneCase(Case oneCase, String chargeName, MultipartFile file)
	{
		try
		{
			int chargeId = chargeMapper.getIdByChargeName(chargeName);
			oneCase.setChargeId(chargeId);

			oneCase.setEditDate(new Timestamp(System.currentTimeMillis()));

			if (file == null || file.getOriginalFilename().isEmpty()) // 用户没有选择新的图片
			{
				String oldUrl = mapper.getOneCaseById(oneCase.getId()).getPicUrl(); // 获得要更新的案例的原先的图片url
				oneCase.setPicUrl(oldUrl); // 使用原来的图片url
			}
			else // 用户选择了新的图片
			{
				// 将新的图片存入硬盘 并设置新的url
				String allPicDir = picDirSetting.substring(picDirSetting.indexOf(':')+1); //存储图片的总目录
				String casePicDir = allPicDir + PicDir.CASE_DIR;
				String picUrl = FileUtil.savePicToDisk(file, casePicDir, PicDir.CASE_DIR);
				if (picUrl == null)
				{
					return false;
				}
				else
				{
					oneCase.setPicUrl(picUrl);
				}

				// 删除原先的图片
				String oldUrl = mapper.getOneCaseById(oneCase.getId()).getPicUrl(); // 获得要更新的案例的原先的图片url
				if (oldUrl != null && !oldUrl.isEmpty())
				{
					String allPicDirNext = oldUrl.substring(8);
					File oldPic = new File(allPicDir + allPicDirNext);
					if (oldPic.exists())
					{
						oldPic.delete();
					}
				}
			}

			mapper.updateEditOneCase(oneCase); // 更新数据
		}
		catch (Exception e)
		{
			System.out.println("出现异常：" + e.getMessage());
			e.printStackTrace();
			return false;
		}

		return true;
	}
}