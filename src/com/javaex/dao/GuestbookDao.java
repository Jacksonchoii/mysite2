package com.javaex.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import com.javaex.vo.GuestbookVo;

public class GuestbookDao {

	// 필드

	// 0. import java.sql.*;
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private String id = "webdb";
	private String pw = "webdb";

	// 생성자
	// 메소드g/s
	// 메소드일반

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

	// insert
	public int guestInsert(GuestbookVo guestbookVo) {

		int count = 0;

		getConnection();

		try {
			// 3. SQL문 준비 / 바인딩 / 실행

			String query = "";
			query += " INSERT INTO GUESTBOOK ";
			query += " VALUES (seq_no.nextval, ?, ?, ?, sysdate)";

			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, guestbookVo.getName());
			pstmt.setString(2, guestbookVo.getPassword());
			pstmt.setString(3, guestbookVo.getContent());

			count = pstmt.executeUpdate();

			// 4.결과처리
			System.out.println(count + "건이 저장되었습니다.");

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		close();
		return count;
	}

	public List<GuestbookVo> getGuestList() {

		List<GuestbookVo> guestList = new ArrayList<GuestbookVo>();

		getConnection();

		try {
			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "";
			query += " SELECT no, ";
			query += " 		  name, ";
			query += " 		  password, ";
			query += " 		  content, ";
			query += " 		  reg_date ";
			query += " FROM guestbook ";

			pstmt = conn.prepareStatement(query);

			rs = pstmt.executeQuery();

			// 4.결과처리
			while (rs.next()) {

				int no = rs.getInt("no");
				String name = rs.getString("name");
				String password = rs.getNString("password");
				String content = rs.getNString("content");
				String regDate = rs.getNString("reg_date");

				GuestbookVo gvo = new GuestbookVo(no, name, password, content, regDate);

				guestList.add(gvo);

			}

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		close();
		return guestList;
	}

	public GuestbookVo getGuest(int nO) {
		GuestbookVo guestVo = null;

		getConnection();
		try {
			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "";
			query += " select no, ";
			query += " 		  name, ";
			query += " 		  password, ";
			query += " 		  content, ";
			query += " 		  reg_date ";
			query += " from guestbook ";
			query += " where no = ? ";

			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, nO);

			rs = pstmt.executeQuery();

			// 4. 결과 처리
			while (rs.next()) {
				int no = rs.getInt("no");
				String name = rs.getString("name");
				String password = rs.getString("password");
				String content = rs.getString("content");
				String regDate = rs.getString("reg_date");

				guestVo = new GuestbookVo(no, name, password, content, regDate);
			}

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		close();
		return guestVo;
	}
	
	public int guestDelete(GuestbookVo guestVo) {

		int count = 0;

		getConnection();

		try {
			// 3. SQL문 준비 / 바인딩 / 실행

			String query = "";
			query += " delete from guestbook ";
			query += " where no = ? ";
			query += " and password = ? ";

			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, guestVo.getNo());
			pstmt.setString(2, guestVo.getPassword());

			count = pstmt.executeUpdate();

			// 4.결과처리
			System.out.println(count + "건이 삭제되었습니다.");

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		close();
		return count;
	}
	
	
}
