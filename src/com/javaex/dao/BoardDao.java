package com.javaex.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.javaex.vo.BoardVo;

public class BoardDao {

	// 필드

	// 0. import java.sql.*;
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private String id = "webdb";
	private String pw = "webdb";

	// db접속
	private void getConnection() {
		try {
			// 1. JDBC 드라이버 (Oracle) 로딩
			Class.forName(driver);

			// 2. Connection 얻어오기
			conn = DriverManager.getConnection(url, id, pw);

		} catch (ClassNotFoundException e) {
			System.out.println("error: 드라이버 로딩 실패 - " + e);
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
	}

	// 5. 자원정리
	private void close() {

		try {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

	}

	// 게시판 리스트
	public List<BoardVo> getBoardList() {

		List<BoardVo> boardList = new ArrayList<BoardVo>();

		getConnection();

		try {
			// SQL문 준비 / 바인딩 / 실행 
			String query = "";
			query += " SELECT  b.no, ";
			query += "         title, ";
			query += "         content, ";
			query += "         hit, ";
			query += "         to_char(reg_date, 'YYYY-MM-DD') reg_date, ";
			query += "         user_no, ";
			query += "         id, ";
			query += "         password, ";
			query += "         name, ";
			query += "         gender ";
			query += " FROM board b, users u ";
			query += " where b.user_no = u.no ";
			query += " order by b.no desc ";

			pstmt = conn.prepareStatement(query);

			rs = pstmt.executeQuery();

			// 결과 처리
			while (rs.next()) {
				int no = rs.getInt("no");
				String title = rs.getString("title");
				String name = rs.getString("name");
				int hit = rs.getInt("hit");
				String regDate = rs.getString("reg_date");
				int userNo = rs.getInt("user_no");

				BoardVo bVo = new BoardVo(no, title, name, hit, regDate, userNo);
				boardList.add(bVo);
			}

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		close();
		return boardList;
	}

	// 게시글쓰기
	public int boardWrite(BoardVo boardVo) {

		int count = 0;
		getConnection();

		try {
			// 3. SQL문 준비 / 바인딩 / 실행

			String query = "";
			query += " INSERT INTO board ";
			query += " VALUES (seq_board_no.nextval, ?, ?, default, sysdate, ?)";

			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, boardVo.getTitle());
			pstmt.setString(2, boardVo.getContent());
			pstmt.setInt(3, boardVo.getUser_no());

			count = pstmt.executeUpdate();

			// 4.결과처리

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		close();
		return count;

	}

	// 게시글 삭제
	public int boardDelete(int no) {

		int count = 0;
		getConnection();

		try {
			// 3. SQL문 준비 / 바인딩 / 실행

			String query = "";
			query += " delete from board ";
			query += " where no = ? ";

			pstmt = conn.prepareStatement(query);

			pstmt.setInt(1, no);

			count = pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		close();
		return count;

	}

	// 게시글 보기
	public BoardVo boardRead(int no) {

		getConnection();

		BoardVo boardVo = null;

		try {
			// SQL문 준비 / 바인딩 / 실행 
			String query = "";
			query += " SELECT  b.no, ";
			query += "         name, ";
			query += "         hit, ";
			query += "         to_char(reg_date, 'YYYY-MM-DD') reg_date, ";
			query += "         title, ";
			query += "         content, ";
			query += "         user_no ";
			query += " FROM board b, users u ";
			query += " where  b.user_no = u.no ";
			query += " and b.no = ? ";

			pstmt = conn.prepareStatement(query);

			pstmt.setInt(1, no);
			
			rs = pstmt.executeQuery();

			// 결과 처리
			while(rs.next()) {
				int num = rs.getInt("no");
				String name = rs.getString("name");
				int hit = rs.getInt("hit");
				String regDate = rs.getString("reg_date");
				String title = rs.getString("title");
				String content = rs.getString("content");
				int userNo = rs.getInt("user_no");
				
				boardVo = new BoardVo(num, name, hit, regDate, title, content, userNo);
			}

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		
		close();
		return boardVo;
	}

	//조회수 증가
	public int hitAdd(int no) {
		int count = 0;
		getConnection();
		
		try {
			// 3. SQL문 준비 / 바인딩 / 실행

			String query = "";
			query += " update board ";
			query += " set hit = hit + 1 ";
			query += " where no = ? ";

			pstmt = conn.prepareStatement(query);

			pstmt.setInt(1, no);

			count = pstmt.executeUpdate();
		
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		
		close();
		return count;
	}
	
	//게시글 수정
	public int boardModify(BoardVo boardVo) {
		int count = 0;
		getConnection();
		
		try {
			// 3. SQL문 준비 / 바인딩 / 실행

			String query = "";
			query += " update board ";
			query += " set title = ?, ";
			query += " 	   content = ? ";
			query += " where no = ? ";

			pstmt = conn.prepareStatement(query);

			pstmt.setString(1, boardVo.getTitle());
			pstmt.setString(2, boardVo.getContent());
			pstmt.setInt(3, boardVo.getNo());

			count = pstmt.executeUpdate();
		
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		
		close();
		return count;
	}
	
}
