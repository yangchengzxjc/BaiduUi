# coding=utf-8
__author__ = 'yue'


class DB:
    """
    简单封装DB
    """

    def __init__(self, host, port, user, pwd, db, servertype='mysql'):
        self.host = host
        self.port = port
        self.user = user
        self.pwd = pwd
        self.db = db
        self.servertype = servertype

    def getconnect(self):
        """
        得到连接信息
        返回: conn.cursor()
        """
        if not self.db:
            raise (NameError, "没有设置数据库信息")
        if self.servertype == "mysql":  # 判断需要连接的数据库类型
            import pymysql  # mysql
            self.conn = pymysql.connect(host=self.host, user=self.user, password=self.pwd, database=self.db,
                                        charset="utf8mb4")
        else:
            import pymssql  # sqlserver
            self.conn = pymssql.connect(host=self.host, port=self.port, user=self.user, password=self.pwd, database=self.db,
                                        charset="utf8")
        self.cur = self.conn.cursor()
        if not self.cur:
            raise (NameError, "连接数据库失败")

    def execquery(self, sql):
        """
        执行查询语句
        返回的是一个包含tuple的list，list的元素是记录行，tuple的元素是每行记录的字段
        如：[(第，一，行)(第，二，行)(第，三，行)]
        结果中要取第一行中第二个字段result[0][1]
        """
        self.getconnect()
        self.cur.execute(sql)
        resList = self.cur.fetchall()
        self.close()
        return resList

    def execnonquery(self, sql):
        """
        执行非查询语句
        """
        self.getconnect()
        self.cur.execute(sql)
        self.conn.commit()
        self.close()

    def close(self):
        self.conn.close()