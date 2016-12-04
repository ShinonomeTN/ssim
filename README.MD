#青果网课表数据抓取工具

此工具专门用于抓取青果网的“课程课表”数据，并整理成原始课程数据导入到数据库

##执行命令

总共有三种启动方式

	java -jar SimpleSubjectImporter.jar 学期代号
	java -jar SimpleSubjectImporter.jar 学期代号 缓存文件夹
	java -jar SimpleSubjectImporter.jar 学期代号 缓存文件夹 --continue

其中学期代号格式是年份+学期，例如20140，2014是年份，0为第一学期（1就是第二学期）。缓存文件夹是制定爬虫把网页缓存在哪里。--continue 选项是直接跳过抓取，仅丛缓存文件夹导入。

##配置文件

配置文件有两个： login.properties 和 connection.properties

###login.properties 说明

此配置文件是关于爬虫的配置文件。

模板如下：

	username=
	password=
	role=
	charset=GB2312
	tableFormat=2
	delay=600
	userAgent=Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.90 Safari/537.36undefined5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.90 Safari/537.36 SN:NULL

username 填登录帐号。 password 填登录密码。 role 指登录的类型，学生的类型是 STU ，其他类型请自行查看青果网教务登录框的其他值。charset 是页面编码，默认 GB2312  不要改，除非青果网更改页面编码。 tableFormat 是爬取的课表格式，不用改。 delay 是抓取下一个页面前的等待时间，单位毫秒，建议不要低于400，青果网容易卡住。 userAgent 是要伪装的浏览器，如果你有特殊偏好的话可以改成其他浏览器，这里默认是 Linux 下的 Chrome。

###connection.properties 说明

此配置文件是关于数据库的配置文件。

模板如下：

	hostname=localhost:3306
	username=
	password=
	database=ClassSheet

 hostname 填 mysql 服务器的地址，必须带上端口号。 username 和 password 是数据库用户和对应的密码。 database 指目标数据库。

##使用注意

- 只能使用 MySQL/MariaDB 数据库
- 缓存文件夹要先创建，如果不存在将会报错
- 每次导入请先删除指定数据库中 Lessons 和 Subjects 两张表
- 目标数据库必须预先存在
- 爬虫抓取页面的时间间隔建议不要低于400
- 每一次抓取前，都要事先清除掉缓存目录和数据库表
