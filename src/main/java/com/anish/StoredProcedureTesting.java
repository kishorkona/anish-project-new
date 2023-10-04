package com.anish;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.Types;

public class StoredProcedureTesting {
	
	String driver = "org.mariadb.jdbc.Driver";
	String url = "jdbc:mariadb://localhost:3306/anishtests";
    String uid = "kishorkona";
    String pwd = "kishorkona";
    
	public static void main(String[] args) {
		StoredProcedureTesting obj = new StoredProcedureTesting();
		//obj.testProcForInsert();
		//System.out.println(obj.testProcWithInAndOut(1));
		//System.out.println(obj.testProcWithInOut("vijay"));
	}
	
	private void testProcForInsert() {
		Connection conn = null;
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, uid, pwd);
			CallableStatement cs = conn.prepareCall("{call createEmp(?,?)}");
			cs.setInt(1, 1);
			cs.setString(2, "vijay");
			cs.execute();
			System.out.println("procedure executed");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
/*
// Create Proc
DROP PROCEDURE IF EXISTS anishtests.testProcForInsert;
DELIMITER $$
CREATE PROCEDURE testProcForInsert(IN empNo INT, IN empName VARCHAR(1000))
BEGIN     
	INSERT INTO anishtests.Employee(EmployeeNo, EmployeeName) VALUES (empNo,empName);
	commit;
END $$
DELIMITER ;
		
// Run
call testProcForInsert(2,'Kona');
*/
	}
	
	private String testProcWithInAndOut(int empNo) {
		String empName = null;
		Connection conn = null;
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, uid, pwd);
			CallableStatement cs = conn.prepareCall("{call testProcWithInAndOut(?,?)}");
			cs.setInt(1, empNo);
			cs.registerOutParameter(2, Types.VARCHAR);
			cs.execute();
			empName = cs.getString(2);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return empName;
/*
// Create Proc
DROP PROCEDURE IF EXISTS anishtests.testProcWithInAndOut;
DELIMITER $$
CREATE PROCEDURE testProcWithInAndOut(IN empNo INT, OUT empName VARCHAR(1000))
BEGIN     
	select EmployeeName into empName from anishtests.Employee where EmployeeNo=empNo limit 1;
END$$
DELIMITER ;
		
// Run
call testProcWithInAndOut(1, @rslt);
select @rslt as empName;
*/
	}
	
	private String testProcWithInOut(String empNameVal) {
		String empName = null;
		Connection conn = null;
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, uid, pwd);
			CallableStatement cs = conn.prepareCall("{call testProcWithInOut(?)}");
			cs.registerOutParameter(1, Types.VARCHAR);
			cs.setString(1, empNameVal);
			cs.execute();
			empName = cs.getString(1);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return empName;
		
		/*
		// Create Proc
		DROP PROCEDURE IF EXISTS anishtests.testProcWithInAndOut;
		DELIMITER $$
		CREATE PROCEDURE testProcWithInAndOut(IN empNo INT, OUT empName VARCHAR(1000))
		BEGIN     
			select EmployeeName into empName from anishtests.Employee where EmployeeNo=empNo limit 1;
		END$$
		DELIMITER ;
		
		// Run
		call testProcWithInAndOut(1, @rslt);
		select @rslt as empName;
		*/
	}

}

/*
Statement stmt = conn.createStatement();
ResultSet rs = stmt.executeQuery("select * from anishtests.Emp");
while (rs.next()) {
    System.out.print("ID:"+rs.getInt("EmpNo"));
}
*/


