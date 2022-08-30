package com.itwillbs.action;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// http://localhost:8088/Model2/board ㄴㄴ
// http://localhost:8088/Model2/board2 ㄴㄴ
// http://localhost:8088/Model2/test.bo 

public class BoardFrontController extends HttpServlet{
			// 컨트롤러 == 얘는 서블릿!!!!!!!!!! 
	
	/////////doProcess 시작///////////////////////////////////////////////////
	protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("(from BoardFrontController.doProcess) C: doProcess() = doGet + doPost 실행");

		System.out.println("\n1. 가상 주소 계산 시작-----------");
		// 1. 가상 주소 계산 --------------------------------------------
//		request.getRequestURL(); // 풀 주소,, http부터~~ .bo까지
//		request.getRequestURI(); //  /Model2/test.bo 
								// 프로토콜 X, 포트번호도 X..
								// 같은 프로젝트니까 Model2도 똑같네,, 없애고 /test.bo 이렇게만^^
		// 변수에 담아주기
		String requestURI = request.getRequestURI();
		System.out.println("(from BoardFrontController.doProcess) C: requestURI: " + requestURI);
		
		String ctxPath = request.getContextPath(); // 프로젝트 = 컨텍스트
		System.out.println("(from BoardFrontController.doProcess) C: contextPath: " + ctxPath);
		
		// ctxPath 길이만큼 자르고, 나머지를 가져와랏~~
		String command = requestURI.substring(ctxPath.length());
		System.out.println("(from BoardFrontController.doProcess) C: command: " + command);
		// 1. 가상 주소 계산 끝 -----------------------------------------
		System.out.println("1. 가상 주소 계산 끝-----------\n");

		
		System.out.println("\n2. 가상 주소 매핑 시작-----------");
		// 2. 가상 주소 매핑 -----------------------------------
		// 3)
		ActionForward forward = null; // 미리 선언,, 왜냐면 이 변수 여러 번 쓸거라서
		
		// 1)
		if(command.equals("/BoardWrite.bo")){
			// 이 주소랑 똑같으면 if문 안으로 들어오세용
			// 글쓰기 페이지 보여주기 = 글쓰기 페이지로 이동하기,, 티켓(=forward) 필요!! 
			// (= member CRUD에서 insertForm.. DB 필요 X => view로 보내주면 됨)
			// 2) 잠깐~~~~ ActionForward 만들고 옵시다,,,,

			System.out.println("(from BoardFrontController.doProcess) C: /BoardWrite.bo 주소 호출됨");
			System.out.println("(from BoardFrontController.doProcess) C: DB 정보 필요 없음 -> view 페이지로 이동시킬거임");
			
			// 4) 객체 생성해주기~ = 티켓 만들기~ -> 티켓 안에 정보 저장해주기
			forward = new ActionForward();
//			forward.setPath(path); // 어디로 갈거고
//			forward.setRedirect(isRedirect); // 어떻게 갈건지
			forward.setPath("./board/writeForm.jsp");
				// .jsp?? 찐 주소네? WebContent/board/writeForm.jsp 페이지로 가라~
			forward.setRedirect(false); // Redirect 방식이냐? ㄴㄴ 아니다~ =forward방식으로 갈 것이다~~~
			
			// 이동 완?? ㄴㄴㄴ 아직 안 갔고,, 티켓만 만들었음!!!
			// 이동하러 3단계로 고고
		}// if
		else if(command.equals("/BoardWriteAction.bo")){
			//   /BoardWrite.bo 주소가 호출된 게 아니라~~
			//   /BoardWriteAction.bo (=pro페이지) 얘가 호출됐으면~~~
			System.out.println("(from BoardFrontController.doProcess) C: /BoardWriteAction.bo 호출");
			System.out.println("(from BoardFrontController.doProcess) C: DB 작업 O + 페이지 이동 O");
															//~~Action == pro 페이지니까!!
															// DB에 글 쓴 거 insert 해야 함,,
			// BoardWriteAction() 객체 생성 + 예외 처리,, ㅋ
			BoardWriteAction bwAction = new BoardWriteAction();
			try {
//				bwAction.execute(request, response);
				// 글 쓰는 과정 처리를 위해,, BoardWriteAction 가서 실행하고 온나~~
				
				// ㄴ execute 메서드 실행 결과 -> forward 갖고 옴!!
				forward = bwAction.execute(request, response);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}// else if
		
		// 2. 가상 주소 매핑 끝 -----------------------------------
		System.out.println("2. 가상 주소 매핑 끝-----------\n");

		
		System.out.println("\n3. 가상 주소 이동 시작-----------");
		// 3. 가상 주소 이동 시작 ----------------------------
		if(forward != null){
			// 페이지 이동 정보가 있을 때!! = 통행권 있는 사람들 중에서,
			
			if(forward.isRedirect()){
				// isRedirect가 True일 때, sendRedirect() 방식으로 이동
					// Is Redirect?? = Redirect냐?? 예스!! True!!
				System.out.println("(from BoardFrontController.doProcess) C: isRedirect? true -> " + forward.getPath()+"로 이동, sendRedirect() 방식으로");
				response.sendRedirect(forward.getPath());
				
			} else {
				// isRedirect가 False일 때, forward() 방식으로 이동
					// Is Redirect?? = Redirect냐?? 노!! 거짓!!
				System.out.println("(from BoardFrontController.doProcess) C: isRedirect? false -> " + forward.getPath()+"로 이동, forward() 방식으로");
				RequestDispatcher dis = request.getRequestDispatcher(forward.getPath());
				dis.forward(request, response);
				
			}
		}
		// 3. 가상 주소 이동 끝 ----------------------------
		System.out.println("3. 가상 주소 이동 끝-----------\n\n");
		
	}
	/////////doProcess 끝///////////////////////////////////////////////////
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("(from BoardFrontController.doGet) C: doGet() 실행");
		doProcess(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("(from BoardFrontController.doPost) C: doPost() 실행");
		doProcess(request, response);
	}
	
}