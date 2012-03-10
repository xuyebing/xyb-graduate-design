package buaa.sei.xyb.common;

/**
 * @author Xu Yebing
 * 文档描述符类，包含了一个文档对象（文档段或代码段）的所有信息（如：类别、ID、文件名、主题分布信息、分词后的词语集合等）
 */
public class DocumentDescriptor {

	// 唯一确定一个文档的索引：categoryID + ID
	private int ID = -1; // 文档在其类中的序号
	private int matrixIndex = -1; // 文档在整个“词-文档”矩阵中序号（也可以说成行号），用于在LDA计算完毕后找回到对应的文档
	private int categoryID = -1; // 文档所属的类别（如：需求、设计、代码等）
	private String name; // 文档名称
	private String path; // 文档路径
	
	public DocumentDescriptor () {
	}
	public DocumentDescriptor (int ID, int matrixIndex, int categoryID, String name, String path) {
		this.ID = ID;
		this.matrixIndex = matrixIndex;
		this.categoryID = categoryID;
		this.name = name;
		this.path = path;
	}
	
	public int getID () {
		return this.ID;
	}
	public int getMatrixIndex () {
		return this.matrixIndex;
	}
	public int getCategoryID () {
		return this.categoryID;
	}
	public String getName () {
		return this.name;
	}
	public String getPath () {
		return this.path;
	}
}
