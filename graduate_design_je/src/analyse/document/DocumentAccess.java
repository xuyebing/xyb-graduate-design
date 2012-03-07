package analyse.document;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

import analyse.document.pipeFilter.StopFilter;

/**
 * 
 * @author Xu Yebing
 * DocumentAccess 进行文档分析的主类，控制文档分析的过程
 */
public class DocumentAccess {

	// pOutlineLevels的每个元素保存一个文档的大纲级别。 
	public static Vector<Vector<Integer>> pOutlineLevels = new Vector<Vector<Integer>>();
	// tableAtDocs保存文档中表格的所在的段落号,与tableStartIndex配合使用。
	public static Vector<Vector<Integer>> tableAtDocs = new Vector<Vector<Integer>>();
	// contexts中的每个元素保存一个文档的所有段落。
	public static Vector<String []> contexts = new Vector<String[]>();
	// paraBEatDocs 其下标与pOutlineLevels以及contexts的下标保持对应
	public static Vector<Vector<int[]>> paraBEatDocs = new Vector<Vector<int[]>>();
	// tableStartIndex 保存每个文档的表格起始的序号（在WordDocParser的divideResult中）
	public static Vector<Integer> tableStartIndex = new Vector<Integer>();
	// foldersToAnalyze 待分析文档的目录集合，分析顺序是先进先出
	public static List<String> foldersToAnalyze;
	
	public static String resultPath = "D:\\ttmp_1"; // 保存分析结果的路径
	public static String toolPath = "D:\\tool"; // 包含有用的文件（词典、数据词典、停用词文件等）的文件夹（绝对路径）
	
	public static String stopWordFilePath = "D:\\硕士开题\\中文停用词\\stopword1.txt"; // 临时使用
	/**
	 * executeStopFilter 执行去停用词的操作
	 * @return 执行成功返回true，否则返回false
	 * @throws IOException
	 */
	public static boolean executeStopFilter() throws IOException {
		StopFilter stopFilter = new StopFilter();
		stopFilter.initStopWordSet(stopWordFilePath); // 初始化停用词集合
		// 创建保存分词结果的文件夹
		String filteredFolderPath = resultPath + Constant.FILE_SEPARATOR + Constant.FILTERED_DIR;
		File filteredFolder = new File(filteredFolderPath);
		if (!filteredFolder.exists() || !filteredFolder.isDirectory()) {
			if (!filteredFolder.mkdir()) {
				System.out.println("=====>>Error: filteredFolder 没有创建成功 <<=====");
				return false; // 目录没有创建成功，则返回
			}
		}
		String docWordsFolderPath = resultPath + Constant.FILE_SEPARATOR + Constant.SEGMENT_DIR;
		File docWordsFolder = new File(docWordsFolderPath);
		if (docWordsFolder.exists() && docWordsFolder.isDirectory()) {
			// 遍历文件夹，获得其下的所有txt文件（不考虑子文件夹和其他类型的文件）
			File[] docWordsFiles = docWordsFolder.listFiles();
			for (File docWordsFile : docWordsFiles) {
				if (docWordsFile.getName().endsWith(".txt")) {
					BufferedReader br = new BufferedReader(new FileReader(docWordsFile));
					String docWordsContent = "";
					String lineContent;
					while ((lineContent = br.readLine()) != null) {
						docWordsContent += lineContent + "\n"; //获得文档的全部内容
					}
					String filteredContent = stopFilter.filterStopWord(docWordsContent);
					// 将过滤后的内容filteredContent写入新的文件中保存
					// 新文件的绝对路径filteredFilePath
					String filteredFilePath = filteredFolderPath + Constant.FILE_SEPARATOR + docWordsFile.getName();
					File filteredFile = new File(filteredFilePath);
					BufferedWriter bw = new BufferedWriter(new FileWriter(filteredFile));
					bw.write(filteredContent);
					bw.flush();
					bw.close();
				}
			}
		}
		return true;
	}
	
	public static void main(String args[]) {
		WordDocParser wdp = new WordDocParser();
		try {
			// 1. 文档段分割，同时对每个文档段进行分词操作 [本阶段的输出包括：1.划分好的文档段集合。 2.每个文档段对应的词语集合]
			// 目前第一阶段已完成
			wdp.analyze("D:\\硕士开题\\5.31终版v1.0\\测试用例集\\0203\\doc\\需求\\用户需求说明书.doc", resultPath);
			
			// 2. 对分词结果进行不同步骤的处理（包括：去掉停用词、根据数据词典或词典将文档中的英文词翻译成中文词等）
			// 考虑使用类似lucene 标准分析器的“管道过滤器”结构，使得分析过程清晰、明确
			// TODO 文本处理过程
			boolean stopFilterFlag = DocumentAccess.executeStopFilter();
			if (stopFilterFlag)
				System.out.println("=====>> 提取结束! <<=====");
			// TODO 还没考虑处理文档中的英文单词，这需要考虑根据字典（或数据词典）进行翻译 
			2012-3-7 todo
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
