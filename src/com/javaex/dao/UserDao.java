package com.javaex.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.javaex.vo.UserVo;

public class UserDao {

	// 0. import java.sql.*;
		private Connection conn = null;
		private PreparedStatement pstmt = null;
		private ResultSet rs = null;

		private String driver = "oracle.jdbc.driver.OracleDriver";
		private String url = "jdbc:oracle:thin:@localhost:1521:xe";
		private String id = "webdb";
		private String pw = "webdb";

		private void getConnection() {
			try {
				// 1. JDBC 드라이버 (Oracle) 로딩
				Class.forName(driver);

				// 2. Connection 얻어오기
				conn = DriverManager.getConnection(url, id, pw);

				System.out.println("[접속성공]");
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
		
		public int insert(UserVo userVo) {
			
			int count = 0;
			
			getConnection();
			
			try {
			    // 3. SQL문 준비 / 바인딩 / 실행
				//insert into users
				//values (seq_users_no.nextval, 'aaa', '1234', '최태현', 'male');
			    String query = "";
			    query += " INSERT INTO USERS ";
			    query += " VALUES (seq_users_no.nextval, ";
			    query += " 		  ?, ";
			    query += " 		  ?, ";
			    query += " 		  ?, ";
			    query += " 		  ? ";
			    query += " ) ";
			  
			    pstmt = conn.prepareStatement(query); //쿼리로 만들기
			    pstmt.setString(1, userVo.getId()); // ?(물음표) 중 1번째, 순서중요
			    pstmt.setString(2, userVo.getPassword()); // ?(물음표) 중 2번째, 순서중요
			    pstmt.setString(3, userVo.getName()); // ?(물음표) 중 3번째, 순서중요
			    pstmt.setString(4, userVo.getGender()); // ?(물음표) 중 4번째, 순서중요
				
			    count = pstmt.executeUpdate();
			    
				// 4.결과처리
			    System.out.println("Insert " + count + "건 회원번호 저장");

			} catch (SQLException e) {
			    System.out.println("error:" + e);
			}
			
			
			close();
			
			return count;
		}
		
	
		public UserVo getUser(String id, String pw) {
			
			UserVo userVo = null;
			getConnection();
			
			
			try {
				String query = "";
				query +=" select	no, ";
				query +=" 			name ";
				query +=" from users ";
				query +=" where id = ? ";
				query +=" and password = ? ";
				
				pstmt = conn.prepareStatement(query); //쿼리로 만들기
				pstmt.setString(1, id);
				pstmt.setString(2, pw);
				
				rs = pstmt.executeQuery();
				
				//결과처리
				while(rs.next()) {
					
					int no = rs.getInt("no");
					String name = rs.getString("name");
				
					userVo = new UserVo(no,name);
				}
				
			} catch (SQLException e) {
			    System.out.println("error:" + e);
			}
		
			close();
			return userVo;
		}
		
		
		public UserVo getMdUser(int no) {
			
			UserVo userVo = null;
			getConnection();
			
			try {
				String query = "";
				query +=" select	no, ";
				query +=" 			id, ";
				query +=" 			password, ";
				query +=" 			name, ";
				query +=" 			gender ";
				query +=" from users ";
				query +=" where no = ? ";
				
				pstmt = conn.prepareStatement(query);
				pstmt.setInt(1, no);
				
				rs = pstmt.executeQuery();
			
				// 4. 결과처리
				while(rs.next()) {
					int Userno = rs.getInt("no");
					String id = rs.getString("id");
					String pw = rs.getString("password");
					String name = rs.getString("name");
					String gender = rs.getString("gender");
					
					userVo = new UserVo(Userno, id, pw, name, gender);
		
				}
			} catch (SQLException e) {
			    System.out.println("error:" + e);
			}
		
			close();
			return userVo;
		}
			
		public int userModify(UserVo userVo) {
			int count = 0;
			getConnection();
			
			try {
				String query ="";
				query += " update users ";
				query += " set	  password = ?, ";
				query += " 		  name = ?, ";
				query += " 		  gender = ? ";
				query += " where no = ? ";
				
				pstmt = conn.prepareStatement(query);
				pstmt.setString(1, userVo.getPassword());
				pstmt.setString(2, userVo.getName());
				pstmt.setString(3, userVo.getGender());
						
				count = pstmt.executeUpdate();
				
				//4.결과처리
				System.out.println(count + "건 수정");
			} catch (SQLException e) {
			    System.out.println("error:" + e);
			}
			
			close();
			return count;
		}
		
}
