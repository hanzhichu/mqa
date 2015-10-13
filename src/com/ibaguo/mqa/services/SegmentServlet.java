package com.ibaguo.mqa.services;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibaguo.mqa.util.Utils;
import com.ibaguo.nlp.MyNLP;
import com.ibaguo.nlp.seg.common.Term;

public class SegmentServlet extends HttpServlet {
		private static final long serialVersionUID = 1L;

		public SegmentServlet() {
		}

		protected void doGet(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
			String sent = request.getParameter("sent");
			response.setContentType("application/json;charset=utf-8");
			response.setCharacterEncoding("UTF-8");
			response.setStatus(HttpServletResponse.SC_OK);
			List<Term> kwList = MyNLP.segment(sent);
			response.getWriter().println(Utils.toJson(kwList));
			response.getWriter().println("session=" + request.getSession(true).getId());
		}
	}