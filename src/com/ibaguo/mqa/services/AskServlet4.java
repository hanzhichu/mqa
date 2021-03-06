package com.ibaguo.mqa.services;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibaguo.mqa.intefaces.QuestionToAnswer;
import com.ibaguo.mqa.json.AskResult;
import com.ibaguo.mqa.json.Doc;
import com.ibaguo.mqa.json.JsonAskResult;
import com.ibaguo.mqa.json.Status;
import com.ibaguo.mqa.pack.impl.IBaguoAsk4;
import com.ibaguo.mqa.util.Utils;

public class AskServlet4 extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public AskServlet4() {
	}
 
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String sent = request.getParameter("sent");
		int size;
		try{
			size = Integer.valueOf(request.getParameter("num"));
		}catch(Exception e){
			size = 1;
		}
		response.setContentType("application/json;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		response.setStatus(HttpServletResponse.SC_OK);
		QuestionToAnswer qa = new IBaguoAsk4();
		List<AskResult> askResult = qa.makeQa(sent,size);
		String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		Status status = new Status(1, "Success", now);
		response.getWriter().println(Utils.toJson(new JsonAskResult(status, askResult)));
	}
}
