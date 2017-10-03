package com.gizumo.app.web;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gizumo.app.repository.TodoDetail;
import com.gizumo.app.repository.TodoDetailRepository;
import com.gizumo.app.web.TodoDetailForm;

/**
 * Handles requests for the application home page.
 */
@Controller
public class TodoController {

	private static final Logger logger = LoggerFactory.getLogger(TodoController.class);

	@Autowired
	TodoDetailRepository todoDetailRepository;

//	  // Login form
//	  @RequestMapping("/login.html")
//	  public String login() {
//	    return "login.html";
//	  }
//
//	  // Login form with error
//	  @RequestMapping("/login-error.html")
//	  public String loginError(Model model) {
//	    model.addAttribute("loginError", true);
//	    return "login.html";
//	  }
	  
	/**
	 * 初期表示
	 * 
	 * @param model モデル
	 * @return 表示リスト
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(Model model) {
		logger.debug("index");
		List<TodoDetail> list = todoDetailRepository.findBydoFlg(false);
		model.addAttribute("list", list);
		return "index";
	}

	  /**
	   * 新規作成
	   * @param form データ作成用Form
	   * @param model モデル
	   * @return URL
	   */
	  @RequestMapping(value = "/create", method = RequestMethod.GET)
	  public String create(TodoDetailForm form, Model model) {
	    logger.debug("create");
	    return "create";
	  }

	  /**
	   * データ保存
	   * @param form 保存用データForm
	   * @param result 
	   * @param model
	   * @return
	   */
	  @RequestMapping(value = "/save", method = RequestMethod.POST)
	  public String save(@Validated @ModelAttribute TodoDetailForm form, BindingResult result, Model model) {
	    logger.debug("save");
	    TodoDetail todo = convert(form);
	    logger.debug("Todo:{}", todo.toString());
	    todo = todoDetailRepository.saveAndFlush(todo);
	    return "redirect:/" + todo.getId().toString();
	  }

	  /**
	   * 更新
	   * @param id 更新対象ID
	   * @param attributes
	   * @param model
	   * @return リダイレクトURL
	   */
	  @RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
	  public String update(@PathVariable Integer id, RedirectAttributes attributes, Model model) {
	    logger.debug("update");
	    TodoDetail updateTodo = todoDetailRepository.findOne(id);
	    updateTodo.setDoFlg(true);
	    todoDetailRepository.saveAndFlush(updateTodo);
	    attributes.addFlashAttribute("updateMessage", "update ID:" + id);
	    return "redirect:/";
	  }
	  
	  /**
	   * 削除
	   * @param id 削除対象ID
	   * @param attributes 
	   * @param model
	   * @return リダイレクトURL
	   */
	  @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	  public String delete(@PathVariable Integer id, RedirectAttributes attributes, Model model) {
	    logger.debug("Todo + delete");
	    todoDetailRepository.delete(id);
	    attributes.addFlashAttribute("deleteMessage", "delete ID:" + id);
	    return "redirect:/";
	  }

	  /**
	   * 詳細表示
	   * @param id 表示するレコードID
	   * @return 詳細オブジェクト
	   */
	  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
	  public ModelAndView detail(@PathVariable Integer id) {
	    logger.debug("detail");
	    ModelAndView mv = new ModelAndView();
	    mv.setViewName("/detail");
	    TodoDetail detail = todoDetailRepository.findOne(id);
	    mv.addObject("todoDetail", detail);
	    return mv;
	  }

	  /**
	   * フォームをモデルに変換
	   * @param form フォーム
	   * @return model
	   */
	  private TodoDetail convert(TodoDetailForm form) {
	    TodoDetail todo = new TodoDetail();
	    todo.setTitle(form.getTitle());
	    todo.setDoFlg(false);
	    if (StringUtils.isNotEmpty(form.getDetail())) {
	      todo.setDetail(form.getDetail());
	    }
	    todo.setUpdateDate(new Date());
	    return todo;
	  }
}
