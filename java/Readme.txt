代码采用UTF-8编码

代码行数约为2800行

运行方式：
项目使用ide eclipse构建
在本地进行调试的时候 需要先用eclipse打开两个项目 先运行Server项目 再运行Client项目，需要先注册账号然后登陆。
如果选择和服务器进行沟通，需要现将connection里的ip地址更改，再运行Client即可。 

newServer —— 服务器代码、数据
	src —— 代码及数据存放文件夹
		data —— 保存已发帖子的内容
			1 —— 第一类帖子的内容 
				no —— no代表帖子的编号，代表第几个帖子
					abstract.txt —— 帖子的概要
					icon.jpg —— 显示帖子概要时显示的图片
					text —— 帖子的具体内容
						no.jpg	—— 第no个图片
						blog.html —— 帖子的主体内容	
			2 —— 第二类帖子的内容
			3 —— 第三类帖子的内容
			4 —— 第四类帖子的内容
			5 —— 第五类帖子的内容
			6 —— 第六类帖子的内容

	bin —— 编译出来的文件

YoPin —— 用户端代码、数据
	src —— 代码、数据
		blog_icon —— 缓存标题图片
		Client —— 客户端的代码
		Community —— 发帖、显示帖子界面的代码
		image —— 背景图片
		Info —— UserInfo —— 用户信息的类
		MainPae —— MainFace —— 主界面的类
		usr_image —— 用户头像的本地缓存
		usr_info —— 记住用户的密码
	YoPin —— 保存数据
		Pictures —— 存放显示界面所需要的图片
		tmp —— 发帖和详细读帖时的本地缓存
		
		