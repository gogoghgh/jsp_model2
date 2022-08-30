package com.itwillbs.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BoardDAO {
	// DAO (Data Access Object) 데이터 처리 객체
	// DB 쓰는 작업들 여기서 다,,,
	
	// 공통 변수 선언 (인스턴스 변수)
	private Connection con = null; 			// DB 연결 정보 저장하는 객체
	private PreparedStatement pstmt = null; // DB에 sql 실행 처리해주는 객체
	private ResultSet rs = null; 			// select 실행 결과를 저장하는 객체
	private String sql = ""; 				// sql 쿼리 구문 저장하는 객체
	
	// 기본 생성자
	public BoardDAO() {
		System.out.println("(from BoardDAO) DB 연결에 관한 모든 준비 완^^ ");
	}
	
	
	// DB 연결 메서드
	private Connection getConnect() throws Exception{
		// 디비 연결정보
		String DRIVER = "com.mysql.cj.jdbc.Driver";
		String DBURL = "jdbc:mysql://localhost:3306/jspdb";
		String DBID = "root";
		String DBPW = "1234";
		
		// 1. 드라이버로드
		Class.forName(DRIVER);
		System.out.println("(from BoardDAO_getConnect) 드라이버로드 성공 ");
		
		// 2. 디비연결
		con = DriverManager.getConnection(DBURL, DBID, DBPW);
		System.out.println("(from BoardDAO_getConnect) 디비연결 성공 ");
		System.out.println("(from BoardDAO_getConnect) con : " + con);
		
		return con;
	}
	// 디비 연결 끝///////////////////////////////////////////////////////////////
	
	
	// 자원 해제
	public void closeDB(){
		//con -> pstmt -> rs 순으로 만들어지니까 해제는 역순으로
		// rs -> pstmt -> con !!!!!!
		try {
			if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(con != null) con.close();
			System.out.println("(from BoardDAO_closeDB) 자원 해제 완 ㅂ2ㅂ2");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	// 자원 해제 끝//////////////////////////////////////////////////////////////
	
	// 글쓰기 메서드 boardWrite()
	public void boardWrite(BoardDTO dto){
		// dto에 글쓴 정보 저장되어 있으니까,, 그 dto를 매개변수로 받아오기
		int bno = 0; // 글 번호 저장할 변수 준비
		try {
			// 1+2. 드라이버로드 + DB 연결 + 6. 자원 해제까지,,
			con = getConnect();
			
			// 3. sql & pstmt & ?
			//		근데,, dto에서 모든 컬럼들 다 받아온 게 아닌데,???? 우야지? 
			//      nn 안 걸린 애들은 ㄱㅊ한데,, pk인 bno도 없음.. 클났네~~
			// 		bno를 여기서 만들고 가실게요~~~~~~~ 
			// 게시판 글 번호(bno) 계산 (= 작성된 가장 마지막 번호 + 1)
					// 기존에 있는 글 개수 + 1 = 새로 작성하려는 글 번호넴
					// 방법 2가지 == 1. 직접 계산해서 번호 매김 / 2. 자동으로~~
			sql = "select max(bno) from itwill_board"; // bno 컬럼의 최댓값을 갖고 와봐~~ 
				// count(bno)는?? 될 수도 안 될 수도,, 왜냐면 중간에 글 삭제한 거 있으면,, 중복이 생겨버림~ pk인데 중복되면 안돼~~~
			pstmt = con.prepareStatement(sql);
			
			// 4. sql 실행
			rs = pstmt.executeQuery(); // select 구문이니까 rs에 저장
			
			// 5. 데이터 처리 (글 번호 계산)
			// rs에 있는 데이터,, 처리
			if(rs.next()){
				// max(bno)가 있을 때~~ +1 하면 끝
//				bno = rs.getInt("bno") + 1; // 땡~~~~~~ X 
//				bno = rs.getInt("max(bno)") + 1; // 딩동댕~~~ O
				bno = rs.getInt(1) + 1; // 딩동댕~~~ OOO 1번 컬럼=인덱스에 있는 값!!! 
			} /* else {
				// bno가 없을 때,, = 아무것도 없을 때는~~
				// bno = 1;
				// 근데 else 구문 필요 없삼~~ getInt + f2 눌러서,, 메서드 소개글에 보면,,,
				// Returns: the column value; if the value is SQL NULL, the value returned is 0
				// null일 때 -> 0 리턴!!!!!!
				
				// 그럼 else 언제 쓰냐,,,? 굳이 안 써도 됨,,,,, 하하하,,
				// max(bno)         =/=     bno  달라요~~~~~~~~~~~
				//   ▶                       ●
				// 커서가 있고              없고..
				// rs.next() == true         rs.next() == false
				// 
				
			} */
			
			System.out.println("(from BoardDAO_boardWrite) 글번호 bno: " + bno);
			// 글 번호 계산 끝났고,,
			// 게시판 글쓰기 실행
			// 다시 3단계. sql 작성 & pstmt & ?
			sql = "insert into itwill_board(bno, name, pass, subject, content,"
					+ "readcount, re_ref, re_lev, re_seq, date, ip, file) "
					+ "values(?, ?, ?, ?, ?, ?, ?, ?, ?, now(), ?, ?)";
													// date는 직접 지정 안 하고 now로 할게요,,,,,^^,, 
			
			pstmt = con.prepareStatement(sql);
			
			pstmt.setInt(1, bno);
			pstmt.setString(2, dto.getName());
			pstmt.setString(3, dto.getPass());
			pstmt.setString(4, dto.getSubject());
			pstmt.setString(5, dto.getContent());
			pstmt.setInt(6, 0); // 게시판에 글 처음 딱 쓰면 조회수는 항상 0
			pstmt.setInt(7, bno); // 답글 그룹 번호 == 글 번호 (일반글)
			pstmt.setInt(8, 0); // 답글 레벨 0 (일반글)
			pstmt.setInt(9, 0); // 답글 순서 0 (일반글)
						// 10번째 컬럼 date는 now()로 채웠으니까
			pstmt.setString(10, dto.getIp());
			pstmt.setString(11, dto.getFile());
			
			// 4. sql 실행
			pstmt.executeUpdate();
			System.out.println("(from BoardDAO_boardWrite) 글 작성 완  bno: " + bno);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 자원 해제
			closeDB();
		}
		
	}
	// 글쓰기 메서드 boardWrite() 끝///////////////////////////////////////////////////
	
	
	
	
	
}// BoardDAO class
