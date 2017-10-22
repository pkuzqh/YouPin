package Community;

public class blogAbstract {
	public static int latestID = -1;
	// 评论数量
	public int commentID = -1;
	// 帖子的抽象摘要
	public int id; // 帖子id
	public String title; // 题目
	public String authorName; // 名字
	public String time; // 时间
	public String filename; // icon图片路径
	public int kind = 1;// 帖子类别

	public blogAbstract() {
		id = -1; //
		filename = null;
	}
}
