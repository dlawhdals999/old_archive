package org.board.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 
 * @author 	:	Zaccoding
 * @date 	: 	2017. 4. 8.
 */
@Controller
@RequestMapping("/articles/*")
public class BoardController {
	
	private static final Logger logger = LoggerFactory.getLogger(BoardController.class);
	
	@RequestMapping("/home")
	public String testHome(Model model) {		
		logger.info("BoardController...testHome()");		
		model.addAttribute("msg","HOME!");
		return "home";
	}
	
	/**
	 * 게시글 작성 폼 요청 처리 메소드
	 *  
	 * @return 게시글 작성 폼
	 */
	@RequestMapping(value="/create",method=RequestMethod.GET)
	public String registerGET() {				
		return "/board/register";				
	}
	
	/**
	 * 게시글 작성 POST 요청 처리 메소드
	 * 
	 * @return 게시글 리스트 페이지
	 */
	@RequestMapping(value="/create",method=RequestMethod.POST)
	public String registerPOST() {
		
		return null;
	}
	
	

}
