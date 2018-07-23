package finup.feather.utils.db;

import java.sql.*;

public class MysqlOperation {
    private static String driver;
    private static String url;
    private static String user;
    private static String pass;
    private Connection conn;

    private static MysqlOperation mbp = null;

    private MysqlOperation(String driver, String url, String user, String pass) {
        this.driver = driver;
        this.url = url;
        this.user = user;
        this.pass = pass;
        try {
            // 注册数据库驱动
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            System.err.println("class not found:" + e.getMessage());
        }
        try {
            conn = (Connection) DriverManager.getConnection(url, user, pass);
        } catch (SQLException a) {
            System.err.println("sql exception:" + a.getMessage());
        }
    }

    public static MysqlOperation getInstance(String driver, String url, String user, String pass) {
        if (mbp == null) {
            // 给类加锁 防止线程并发
            synchronized (MysqlOperation.class) {
                if (mbp == null) {
                    mbp = new MysqlOperation(driver, url, user, pass);
                }
            }
        }
        return mbp;
    }

    // 获得连接
    public Connection getConnection() {
        return conn;
    }

    // 关闭连接
    public void closeConnection(ResultSet rs, Statement statement, Connection con) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (con != null) {
                        con.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}