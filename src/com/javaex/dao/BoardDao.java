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
		
		//게시판 리스트 
		public List<BoardVo> getBoardList() {
			
			List<BoardVo> boardList = new ArrayList<BoardVo>();
			
			getConnection();
			
			try {
				// 3. SQL문 준비 / 바인딩 / 실행
				String query = "";
				query += " SELECT	b.no, ";
				query += " 			b.title, ";
				query += " 		    u.name, ";
				query += " 		    b.hit, ";
				query += " 		    to_char(b.reg_date, 'yyyy-mm-dd) ";
				query += " FROM board b, users u ";
				query += " b.user_no = u.no ";

				pstmt = conn.prepareStatement(query);

				rs = pstmt.executeQuery();

				// 4.결과처리
				while (rs.next()) {

					int no = rs.getInt("no");
					String title = rs.getString("title");
					String name = rs.getString("name");
					int hit = rs.getInt("hit");
					String regDate = rs.getString("reg_date");

					BoardVo bvo = new BoardVo(no, title, hit, regDate, name);

					boardList.add(bvo);

				}

			} catch (SQLException e) {
				System.out.println("error:" + e);
			}

			close();
			return boardList;
		}
			
			
		
		
	
}
