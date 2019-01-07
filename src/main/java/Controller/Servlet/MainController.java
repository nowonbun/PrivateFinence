package Controller.Servlet;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import Bean.CategoryBean;
import Bean.SelectionBean;
import Common.AbstractServletController;
import Common.FactoryDao;
import Common.JsonConverter;
import Common.Util;
import Dao.ActionroleDao;
import Dao.CategoryDao;
import Dao.GroupDao;
import Dao.LanguageTypeDao;
import Dao.LowCategoryDao;
import Dao.ViewroleDao;
import Model.Actionrole;
import Model.Category;
import Model.Group;
import Model.LanguageType;
import Model.Viewrole;

@Controller
public class MainController extends AbstractServletController {
	@RequestMapping(value = "/main.html")
	public String main(ModelMap modelmap, HttpSession session, HttpServletRequest req, HttpServletResponse res) {
		super.initMenu(modelmap, session);

		return "main";
	}

	@RequestMapping(value = "/category.html")
	public String category(ModelMap modelmap, HttpSession session, HttpServletRequest req, HttpServletResponse res) {
		if (!super.isViewRole(session, FactoryDao.getDao(ViewroleDao.class).getRole("CTGV"))) {
			res.setStatus(403);
			return "";
		}
		super.initMenu(modelmap, session);

		modelmap.addAttribute("income", getCategoryList("i"));
		modelmap.addAttribute("expenditure", getCategoryList("e"));
		modelmap.addAttribute("saving", getCategoryList("s"));

		return "category";
	}

	private List<CategoryBean> getCategoryList(String lowCode) {
		List<CategoryBean> ret = new ArrayList<>();
		for (Category category : FactoryDao.getDao(CategoryDao.class).getCategoryList(FactoryDao.getDao(LowCategoryDao.class).getLowCategory(lowCode))) {
			CategoryBean bean = new CategoryBean();
			bean.setId(category.getIdx());
			bean.setName(category.getName());
			if (category.getIsdeleted()) {
				bean.setDel(2);
			} else {
				bean.setDel(1);
			}
			ret.add(bean);
		}
		return ret;
	}

	@RequestMapping(value = "/saveCategory.html", method = RequestMethod.POST)
	public String saveCategory(ModelMap modelmap, HttpSession session, HttpServletRequest req, HttpServletResponse res) {
		if (!super.isViewRole(session, FactoryDao.getDao(ViewroleDao.class).getRole("CTGV"))) {
			res.setStatus(403);
			return "";
		}
		String income = req.getParameter("income");
		String expenditure = req.getParameter("expenditure");
		String saving = req.getParameter("saving");
		if (income == null || expenditure == null || saving == null) {
			res.setStatus(403);
			return "";
		}
		updateCategory(convertCategoryItem(income), "i");
		updateCategory(convertCategoryItem(expenditure), "e");
		updateCategory(convertCategoryItem(saving), "s");
		
		modelmap.addAttribute("categoryupdated", true);
		return category(modelmap, session, req, res);
	}

	private void updateCategory(List<CategoryBean> list, String code) {
		for (CategoryBean bean : list) {
			Category category;
			if (bean.getId() > 0) {
				category = FactoryDao.getDao(CategoryDao.class).getCategory(bean.getId());
				category.setIsdeleted(bean.getDel() == 2);
			} else {
				category = new Category();
				category.setIsdeleted(false);
			}
			category.setName(bean.getName());
			category.setMstLowCategory(FactoryDao.getDao(LowCategoryDao.class).getLowCategory(code));
			FactoryDao.getDao(CategoryDao.class).update(category);
		}
	}

	private List<CategoryBean> convertCategoryItem(String val) {
		return JsonConverter.parseArray(val, (obj) -> {
			List<CategoryBean> ret = new ArrayList<>();
			for (int i = 0; i < obj.size(); i++) {
				CategoryBean item = JsonConverter.parseObject(obj.get(i).toString(), (obj2) -> {
					try {
						CategoryBean bean = new CategoryBean();
						bean.setId(Integer.parseInt(obj2.getString("id")));
						bean.setName(obj2.getString("name"));
						bean.setDel(Integer.parseInt(obj2.getString("del")));
						return bean;
					} catch (Throwable e) {
						return null;
					}
				});
				if (item != null) {
					ret.add(item);
				}
			}
			return ret;
		});
	}

	@RequestMapping(value = "/admin.html")
	public String admin(ModelMap modelmap, HttpSession session, HttpServletRequest req, HttpServletResponse res) {
		if (!super.isViewRole(session, FactoryDao.getDao(ViewroleDao.class).getRole("ADMN"))) {
			res.setStatus(403);
			return "";
		}
		super.initMenu(modelmap, session);
		List<SelectionBean> countryList = new ArrayList<>();
		for (LanguageType item : FactoryDao.getDao(LanguageTypeDao.class).getData()) {
			SelectionBean bean = new SelectionBean();
			bean.setValue(item.getCode());
			bean.setName(Util.localization(item.getName(), getCurrentUser(session).getLanguaueType().getCode()));
			countryList.add(bean);
		}
		List<SelectionBean> groupList = new ArrayList<>();
		for (Group item : FactoryDao.getDao(GroupDao.class).getData()) {
			SelectionBean bean = new SelectionBean();
			bean.setValue(item.getCode());
			bean.setName(Util.localization(item.getName(), getCurrentUser(session).getLanguaueType().getCode()));
			groupList.add(bean);
		}
		List<SelectionBean> viewrolelist = new ArrayList<>();
		for (Viewrole item : FactoryDao.getDao(ViewroleDao.class).getData()) {
			SelectionBean bean = new SelectionBean();
			bean.setValue(item.getCode());
			bean.setName(Util.localization(item.getName(), getCurrentUser(session).getLanguaueType().getCode()));
			viewrolelist.add(bean);
		}
		List<SelectionBean> actionrolelist = new ArrayList<>();
		for (Actionrole item : FactoryDao.getDao(ActionroleDao.class).getData()) {
			SelectionBean bean = new SelectionBean();
			bean.setValue(item.getCode());
			bean.setName(Util.localization(item.getName(), getCurrentUser(session).getLanguaueType().getCode()));
			actionrolelist.add(bean);
		}
		modelmap.addAttribute("country", countryList);
		modelmap.addAttribute("group", groupList);
		modelmap.addAttribute("viewrole", viewrolelist);
		modelmap.addAttribute("actionrole", actionrolelist);

		return "admin";
	}
}
