package com.lyj.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.JiebaSegmenter.SegMode;
import com.huaban.analysis.jieba.SegToken;

/**
 * @time:2017年9月7日上午10:05:27
 * @author:longyujia
 * @emial: longyujia@knowlegene.com
 */

public class Synonyms {
	/**
	 * @time:2017年9月7日上午10:05:27
	 * @author:longyujia
	 * @throws Exception
	 * @emial: longyujia@knowlegene.com
	 */

	/*
	 * 全局变量
	 */
	// int minIndex = 3000; // 记录词频较小的词汇，保证基本结果
	// int secMinIndex = 300000;
	// int sampleCount = 99466;// 参数30需要根据数据量适当变化，一般100000的时候取200左右，300000取500左右
	// 关键字频率
	Map<String, Integer> WordDict = new HashMap<String, Integer>();// 字典<词语,频率>

	// /**
	// * 备注：读取csv文件的所有行
	// * @time:2017年10月11日上午9:12:25
	// * @author:longyujia
	// * @param path
	// * @return
	// * @throws Exception
	// */
	// public ArrayList<String> ReadCsv(String path) throws Exception {
	// File csv = new File(path);// csv文件的路径
	// BufferedReader br = null;
	//
	// br = new BufferedReader(new FileReader(csv));
	//
	// String line = "";
	// String everyline = "";
	//
	// ArrayList<String> allString = new ArrayList<String>();
	// while ((line = br.readLine()) != null) {
	// everyline = line;
	//// System.out.println(line);
	// allString.add(everyline);
	// }
	//
	// // System.out.println(allString);
	//
	// br.close();
	//
	// return allString;
	//
	// }

	/**
	 * 备注：读取大csv文件
	 * 
	 * @time:2017年10月11日上午9:33:49
	 * @author:longyujia
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public ArrayList<String> ReadCsv(String path) throws Exception {
		// File csv = new File(path);// csv文件的路径
		// System.out.println("去读大的csv文件");
		// System.out.println("path:" + path);
		// System.out.println("getTotalSpace:"+File);
		ArrayList<String> allString = new ArrayList<String>();
		String line = "";
		FileInputStream fileInputStream = null;
		Scanner sc = null;
		fileInputStream = new FileInputStream(path);
		sc = new Scanner(fileInputStream, "UTF-8");
		// int count = 1;
		while (sc.hasNextLine()) {
			line = sc.nextLine();
			// System.out.println("第" + count + "个，line:" + line);
			// count++;
			allString.add(line);
		}
		// if (sc.ioException() != null) {
		// throw sc.ioException();
		// }
		if (fileInputStream != null) {
			fileInputStream.close();
		}
		if (sc != null) {
			sc.close();
		}

		// System.out.println("大文件读取完成");

		return allString;

	}

	/**
	 * 备注：建立新的字典
	 * 
	 * @time:2017年10月11日上午9:12:42
	 * @author:longyujia
	 * @param path
	 * @throws Exception
	 */
	public void CreateDic(String path) throws Exception {
		// 字典的建立
		// System.out.println("通过csv文件来创建自己的字典");

		// TODO 读取csv文件
		String maxname = null;
		List<String> cut_name = null;
		for (String line : this.ReadCsv(path)) {
			JiebaSegmenter segmenter = new JiebaSegmenter();
			// System.out.println("line.split(',')[1]===" + line.split(",")[1]);
//			maxname = line.split(",")[1];
			maxname = line;
			/*
			 * 2018年1月18日18:36:45
			 */
			maxname = maxname.replaceAll("有限责任公司", "").replaceAll("股份有限公司", "").replaceAll("工程", "").replaceAll("开发", "").replaceAll("股份有限责任公司", "").replaceAll("有限公司", "").replaceAll("有限", "").replaceAll("公司", "").replaceAll("总公司", "").replaceAll("分公司", "");
			maxname = maxname.replaceAll("[\\pP+~$`^=|<>～｀＄＾＋＝｜＜＞￥×]", "");
			cut_name = segmenter.sentenceProcess(maxname);// 名字切分
//			List<SegToken>test_name = segmenter.process(maxname, SegMode.INDEX);
			List<SegToken>test_name = segmenter.process(maxname, SegMode.SEARCH);
			List<String> ind_name = new ArrayList<String>();
			for(SegToken segToken : test_name){
				String name = segToken.toString();
//				System.out.println(name);
				name = name.replaceAll(",", "").replaceAll("[0-9]*", "").replaceAll("\\[|\\]", "").replaceAll(" ", "");
//				System.out.println(name.length());
				if(name.length()<=6){
					ind_name.add(name);
				}
			}
			cut_name = ind_name;
			// System.out.println(cut_name);

			for (String each_word : cut_name) {
				if (WordDict.containsKey(each_word)) {
					int value = WordDict.get(each_word) + 1;
					// System.out.println(each_word+"--"+value);
					WordDict.put(each_word, value);
				} else {
					// System.out.println(each_word);
					WordDict.put(each_word, 1);
				}
			}

		}

		// System.out.println("字典创建完成");

	}

	/**
	 * 备注：千万级数据生成字典
	 * 
	 * @time:2017年10月11日上午10:38:18
	 * @author:longyujia
	 * @param path
	 * @throws IOException
	 */
	public void CreateDic_big(String path) throws IOException {
		// 字典的建立
		// System.out.println("通过csv文件来创建自己的字典");

		// TODO 读取csv文件
		String maxname = null;
		List<String> cut_name = null;
		// ArrayList<String> allString = new ArrayList<String>();
		String line = "";
		FileInputStream fileInputStream = null;
		Scanner sc = null;
		fileInputStream = new FileInputStream(path);
		sc = new Scanner(fileInputStream, "UTF-8");
		// int count = 1;
		while (sc.hasNextLine()) {
			line = sc.nextLine();
			// System.out.println("第" + count + "个，line:" + line);
			// count++;
			JiebaSegmenter segmenter = new JiebaSegmenter();
			// System.out.println("line.split(',')[1]==="+line.split(",")[1]);
//			maxname = line.split(",")[1];
			maxname = line;
			/*
			 * 2018年1月18日18:36:31
			 */
			maxname = maxname.replaceAll("有限责任公司", "").replaceAll("股份有限公司", "").replaceAll("工程", "").replaceAll("开发", "").replaceAll("股份有限责任公司", "").replaceAll("有限公司", "").replaceAll("有限", "").replaceAll("公司", "").replaceAll("总公司", "").replaceAll("分公司", "");
			maxname = maxname.replaceAll("[\\pP+~$`^=|<>～｀＄＾＋＝｜＜＞￥×]", "");
			cut_name = segmenter.sentenceProcess(maxname);// 名字切分
			List<String> ind_name = new ArrayList<String>();
//			List<SegToken>test_name = segmenter.process(maxname, SegMode.INDEX);
			List<SegToken>test_name = segmenter.process(maxname, SegMode.SEARCH);
			for(SegToken segToken : test_name){
				String name = segToken.toString();
//				System.out.println(name);
				name = name.replaceAll(",", "").replaceAll("[0-9]*", "").replaceAll("\\[|\\]", "").replaceAll(" ", "");
//				System.out.println(name.length());
				if(name.length()<=6){
					ind_name.add(name);
				}
			}
			cut_name = ind_name;
			// System.out.println(cut_name);

			for (String each_word : cut_name) {
				if (WordDict.containsKey(each_word)) {
					int value = WordDict.get(each_word) + 1;
					// System.out.println(each_word+"--"+value);
					WordDict.put(each_word, value);
				} else {
					// System.out.println(each_word);
					WordDict.put(each_word, 1);
				}
			}

		}

		if (fileInputStream != null) {
			fileInputStream.close();
		}
		if (sc != null) {
			sc.close();
		}

		// System.out.println("字典创建完成");
	}

	/**
	 * 备注：根据所创建的字典，对每条记录进行简称的获取
	 * 
	 * @time:2017年10月11日上午9:15:40
	 * @author:longyujia
	 * @param csvPath
	 * @param path
	 * @param ShengShipath
	 * @throws Exception
	 */
	public void CreateCsvByDict(String csvPath, String path, String ShengShipath, int minIndex, int sampleCount,
			String TableName, String ColumnName, String IDName) throws Exception {

		// 加载省区市
		List<String> SHQlist = new ArrayList<String>();
		File file = new File(ShengShipath);
		InputStreamReader reader = new InputStreamReader(new FileInputStream(file), "UTF-8");
		BufferedReader br = new BufferedReader(reader);

		String txtline = "";
		while ((txtline = br.readLine()) != null) {
			SHQlist.add(txtline);
		}
//		System.out.println(SHQlist);

		br.close();
		reader.close();
		// System.out.println(SHQlist);

		String maxname = null;
		String yuanshi_name = null;
		List<String> cut_name = null;
		List<SegToken> test_name = null;
		
		for (String line : this.ReadCsv(csvPath)) {
			ArrayList<String> wordlist = new ArrayList<String>();
			JiebaSegmenter segmenter = new JiebaSegmenter();
			// System.out.println("line.split(',')[1]===" + line.split(",")[1]);
//			maxname = line.split(",")[1];
			maxname = line;
			yuanshi_name = maxname;
			System.out.println("原名称："+maxname);
			maxname = maxname.replaceAll("有限责任公司", "").replaceAll("股份有限公司", "").replaceAll("工程", "").replaceAll("开发", "").replaceAll("股份有限责任公司", "").replaceAll("有限公司", "").replaceAll("总公司", "").replaceAll("分公司", "").replaceAll("有限", "").replaceAll("公司", "");
			maxname = maxname.replaceAll("[\\pP+~$`^=|<>～｀＄＾＋＝｜＜＞￥×]", "");//去掉标点符号
			System.out.println("现名称："+maxname);
			cut_name = segmenter.sentenceProcess(maxname);// 名字切分
//			test_name = segmenter.process(maxname, SegMode.INDEX);
			test_name = segmenter.process(maxname, SegMode.SEARCH);
			ArrayList<String> ind_name = new ArrayList<String>();
			for(SegToken segToken : test_name){
				String name = segToken.toString();
//				System.out.println(name);
				name = name.replaceAll(",", "").replaceAll("[0-9]*", "").replaceAll("\\[|\\]", "").replaceAll(" ", "");
//				System.out.println(name.length());
				if(name.length()<=6){
					ind_name.add(name);
				}
			}
			cut_name = ind_name;
			
			/*
			 * 2018年1月30日16:58:30
			 * 添加百度百科爬虫
			 * 
			 * 2018年2月1日15:30:40
			 * 会报错
			 */
			try {
				//TODO 百科爬虫
//				for(String name : cut_name){
					Baikecraw baikecraw = new Baikecraw();
					String baike_jc_name = baikecraw.GetJC(yuanshi_name);
					if(baike_jc_name != null){
						this.AddLineTxt(path, yuanshi_name, baike_jc_name, null);
					}
					this.wait(1000);
//				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			
			

			/*
			 *  清除括号
			 *  2018年1月25日11:44:09
			 */
			 if (cut_name.contains("（")) { 
				 cut_name.remove("（"); 
				 } 
			 if (cut_name.contains("(")) { 
				 cut_name.remove("("); 
				 } 
			 if (cut_name.contains("）")) { 
				 cut_name.remove("）"); 
				 } 
			 if (cut_name.contains(")")) { 
				 cut_name.remove(")"); 
				 }
			 

			for (int i = 0; i < cut_name.size(); i++) {
				// System.out.println(cut_name.get(i));

				// if(cut_name.get(i).equals("\"")){
				// System.out.println("ceshi ''========= " + cut_name.get(i));
				// }

				if (cut_name.get(i).equals("\"") || cut_name.get(i).equals("（") || cut_name.get(i).equals("(")
						|| cut_name.get(i).equals("）") || cut_name.get(i).equals(")")) {
					cut_name.remove(i);
					i--;
				}

			}

			String shengword = "省市区县镇";
			String shiword = "省市区县镇";
			String quword = "省市区县镇";
			String xianword = "省市区县镇";
			String zhenword = "省市区县镇";

			// 清除省市区县
			for (String each_word : cut_name) {
				wordlist.add(each_word);
				if (wordlist.contains("省")) {
					shengword = each_word;
				}
				if (wordlist.contains("市")) {
					shiword = each_word;
				}
				if (wordlist.contains("区")) {
					quword = each_word;
				}
				if (wordlist.contains("县")) {
					xianword = each_word;
				}
				if (wordlist.contains("镇")) {
					zhenword = each_word;
				}
			}
			if (wordlist.contains(shengword))
				wordlist.remove(shengword);
			if (wordlist.contains(shiword))
				wordlist.remove(shiword);
			if (wordlist.contains(quword))
				wordlist.remove(quword);
			if (wordlist.contains(xianword))
				wordlist.remove(xianword);
			if (wordlist.contains(zhenword))
				wordlist.remove(zhenword);

			// 缺少省市区县镇（只包含地名）
			String lessSSQX = "缺少省市区县";

			for (String each_word : wordlist) {
				// System.out.println("each_word="+each_word);

				for (String SHQline : SHQlist) {

					// TODO 即时出现了地名也当做不相等?

					if (each_word.equals(SHQline) || each_word.equals(SHQline + "省")
							|| each_word.equals(SHQline + "市")) {
						lessSSQX = each_word;
						// wordlist.remove(lessSSQX);
						break;
					}

				}
			}
			if (wordlist.contains(lessSSQX)) {
				wordlist.remove(lessSSQX);
			}

			// System.out.println(wordlist);
			// int minIndex=300000 ;
			String minFrequencyWord = null;
			for (String word : wordlist) {
				// System.out.println("wordDict.get("+word+")="+wordDict.get(word));
//				System.out.println("word="+word);
//				System.out.println("WordDict(word)="+WordDict.get(word));
				try {
					if (WordDict.get(word) < minIndex) {
						minIndex = WordDict.get(word);
						minFrequencyWord = word;
					}
				} catch (Exception e) {
				}
			}
			if (wordlist.contains(minFrequencyWord)) {
				wordlist.remove(minFrequencyWord);
			}
			int secMinIndex = 300000;
			String secMinFrequencyWord = null;
			if (wordlist.size() >= 1) {
				for (String each_word : wordlist) {
					try {
						if (WordDict.get(each_word) < secMinIndex) {
							secMinIndex = WordDict.get(each_word);
							secMinFrequencyWord = each_word;
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			} else {
				secMinFrequencyWord = "1234";
			}

			// System.out.println(wordlist);

			// 进行最后的条件筛选得出结果
			cut_name = segmenter.sentenceProcess(maxname);
			test_name = segmenter.process(maxname, SegMode.SEARCH);
			ArrayList<String> name_01 = new ArrayList<String>();
			for(SegToken segToken : test_name){
				String name = segToken.toString();
//				System.out.println(name);
				name = name.replaceAll(",", "").replaceAll("[0-9]*", "").replaceAll("\\[|\\]", "").replaceAll(" ", "");
//				System.out.println(name.length());
				if(name.length()<=6){
					name_01.add(name);
				}
			}
			cut_name = name_01;
			List<String> linklist = new ArrayList<String>();
			// ArrayList<String> Qlinklist = new ArrayList<String>();
			// Qlinklist.add();
			List<String> SQlinklist = new ArrayList<String>();
			SQlinklist.add(lessSSQX);
			// linklist.add(lessSSQX);
			for (String each_word : wordlist) {
				try {
					if ((each_word.length() == 1 && each_word != "（" && each_word != "）") || each_word == minFrequencyWord
							|| each_word == secMinFrequencyWord
							|| (WordDict.get(each_word) < sampleCount && each_word != shengword && each_word != shiword
									&& each_word != xianword && each_word != quword && each_word != zhenword
									&& each_word != lessSSQX)) {
						linklist.add(each_word);
						// System.out.println(each_word);
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			StringBuilder enterword = new StringBuilder();
			// StringBuffer buffer = new StringBuffer();
			for (String each_word : linklist) {
				enterword = enterword.append(each_word + "");
				// buffer=buffer.append(each_word+"");
			}

			// System.out.println("linklist====="+linklist);
			// System.out.println("enterword==="+enterword+"//enterword.length
			// === " + enterword.length());

			if (enterword.length() < 4) {
				// System.out.println("length < 4");
				cut_name = segmenter.sentenceProcess(maxname);
				test_name = segmenter.process(maxname, SegMode.SEARCH);
				ArrayList<String> name_02 = new ArrayList<String>();
				for(SegToken segToken : test_name){
					String name = segToken.toString();
//					System.out.println(name);
					name = name.replaceAll(",", "").replaceAll("[0-9]*", "").replaceAll("\\[|\\]", "").replaceAll(" ", "");
//					System.out.println(name.length());
					if(name.length()<=6){
						name_02.add(name);
					}
				}
				cut_name = name_02;

				// for()

				List<String> bbb = new ArrayList<String>();
				for (int i = 0; i < enterword.length(); i++) {
					// System.out.println("word.substring("+i+","+
					// (i+1)+")="+enterword.substring(i, i+1));
					bbb.add(enterword.substring(i, i + 1));
				}
				// System.err.println("bbb="+bbb);
				int count = 0;
				int ccc = 0;
				// System.out.println("cutname="+cut_name);
				for (int i = 0; i < cut_name.size(); i++) {
					if (cut_name.get(i).equals("\"")) {
						cut_name.remove(i);
						i--;
					}
				}
				for (String word : cut_name) {
					List<String> aaa = new ArrayList<String>();
					for (int i = 0; i < word.length(); i++) {
						// System.out.println("word.substring("+i+","+
						// (i+1)+")="+word.substring(i, i+1));
						aaa.add(word.substring(i, i + 1));
					}
					// System.out.println("aaa="+aaa);
					// System.out.println("word="+word);
					if (word.equals("（")) {
						// System.out.println("ccc-2");
						ccc = -2;
						continue;
					} else {
						// System.out.println("ccc+1");
						ccc += 1;
					}
					// System.out.println("count=="+count);
					if (count == 1 && ccc > 0) {
						// System.out.println("add linklist");
						linklist.add(word);
						break;
					}
					try {
						// System.out.println("aaa.get(aaa.size()-1)"+aaa.get(aaa.size()-1));
						// System.out.println("bbb.get(bbb.size()-1)"+bbb.get(bbb.size()-1));
						if (aaa.get(aaa.size() - 1).equals(bbb.get(bbb.size() - 1))) {
							count += 1;
						}
					} catch (Exception e) {
						break;
					}
				}

				// System.out.println("linklist="+linklist);

			}

//			ArrayList<String> all_list = new ArrayList<String>();
			StringBuffer jc = new StringBuffer();
			StringBuffer qjc = new StringBuffer();
			qjc.append(quword);
			StringBuffer sqjc = new StringBuffer();
			if (!lessSSQX.equals("缺少省市区县")) {
				sqjc.append(lessSSQX);
			}

			for (String word : linklist) {
				jc = jc.append(word);
				qjc = qjc.append(word);
				sqjc = sqjc.append(word);
			}

			/*all_list.add(
					line.split(",")[0] + "," + line.split(",")[1] + ",\"" + jc + "\"," + null + ",\"" + sqjc + "\"");
			System.out.println(all_list);
			// 添加新的数据
			this.AddNewLine(path, line.split(",")[0], line.split(",")[1], jc.toString(), null, sqjc.toString(),
					TableName, ColumnName, IDName);*/
			this.AddLineTxt(path, yuanshi_name, jc.toString(), sqjc.toString());
		}

	}
	
	/*
	 * 2018年1月29日11:27:51
	 */
	public void AddLineTxt(String path , String maxname,String minname,String shi_qu_name) throws IOException{
		File csv = new File(path); // CSV数据文件
		OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(csv, true), "UTF-8");
		BufferedWriter bw = new BufferedWriter(osw); // 附加
		// BufferedWriter bw = new BufferedWriter(new FileWriter(csv, true)); //
		// 附加
		// 添加新的数据行
		// bw.write(id + "," + maxname + ",\"" + minname + "系\"");

		minname = minname.replaceAll("）", "");
		minname = minname.replaceAll("（", "");
		minname = minname.replaceAll("\\s*", "");
		minname = minname.replaceAll("[\\pP+~$`^=|<>～｀＄＾＋＝｜＜＞￥×]", "");
//		bw.write("update " + TableName + " set " + ColumnName + "=" + "\"" + minname + "谱系\"" + " where " + IDName + "="
//				+ id + ";");
//		id = id.replaceAll("\"", "");
		maxname = maxname.replaceAll("\"", "");
		minname = minname.replaceAll("\"", "");
		shi_qu_name = shi_qu_name.replaceAll("\"", "");
				
		/*
		 * 2018年1月15日13:42:22
		 * 输出格式 为 [ID,全称,简称,市区+简称]
		 */
		bw.write("\"" + maxname + "\",\"" + minname + "\",\""+shi_qu_name+"\"");

		bw.newLine();
		bw.close();
	}
	

	/**
	 * 备注：创建csv文件
	 * 
	 * @time:2017年9月7日下午4:07:24
	 * @author:longyujia
	 * @param path
	 * @param datalist
	 * @return
	 */
	public boolean CreateCsvFile(String path, List<String> datalist) {
		// 创建csv文件
		boolean flag = false;
		FileOutputStream out = null;
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;
		File file = new File(path);
		try {
			out = new FileOutputStream(file);
			osw = new OutputStreamWriter(out);
			bw = new BufferedWriter(osw);
			if (datalist != null && !datalist.isEmpty()) {
				for (String fileInfo : datalist) {
					bw.append(fileInfo).append("\r");
				}
			}
			flag = true;
		} catch (Exception e) {
			flag = false;
		} finally {
			if (bw != null) {
				try {
					bw.close();
					bw = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (osw != null) {
				try {
					osw.close();
					osw = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (out != null) {
				try {
					out.close();
					out = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return flag;
	}

	/**
	 * 备注：往csv中添加新记录
	 * 
	 * @time:2017年9月7日下午4:06:42
	 * @author:longyujia
	 * @param path
	 * @param id
	 * @param maxname
	 * @param minname
	 * @throws IOException
	 */
	public void AddNewLine(String path, String id, String maxname, String minname, String qu_name, String shi_qu_name,
			String TableName, String ColumnName, String IDName) throws IOException {
		// 向csv文件中添加一条新的数据
		File csv = new File(path); // CSV数据文件
		OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(csv, true), "UTF-8");
		BufferedWriter bw = new BufferedWriter(osw); // 附加
		// BufferedWriter bw = new BufferedWriter(new FileWriter(csv, true)); //
		// 附加
		// 添加新的数据行
		// bw.write(id + "," + maxname + ",\"" + minname + "系\"");

		minname = minname.replaceAll("）", "");
		minname = minname.replaceAll("（", "");
		minname = minname.replaceAll("\\s*", "");
		minname = minname.replaceAll("[\\pP+~$`^=|<>～｀＄＾＋＝｜＜＞￥×]", "");
//		bw.write("update " + TableName + " set " + ColumnName + "=" + "\"" + minname + "谱系\"" + " where " + IDName + "="
//				+ id + ";");
		id = id.replaceAll("\"", "");
		maxname = maxname.replaceAll("\"", "");
		minname = minname.replaceAll("\"", "");
		shi_qu_name = shi_qu_name.replaceAll("\"", "");
				
		/*
		 * 2018年1月15日13:42:22
		 * 输出格式 为 [ID,全称,简称,市区+简称]
		 */
		System.out.println("\""+id + "\",\"" + maxname + "\",\"" + minname + "\",\""+shi_qu_name+"\"");
		bw.write("\""+id + "\",\"" + maxname + "\",\"" + minname + "\",\""+shi_qu_name+"\"");

		bw.newLine();
		bw.close();
	}

	/**
	 * 获取csv文件的行数
	 * 
	 * @time:2018年1月3日下午3:59:41
	 * @author:longyujia
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public int GetLinesNum(String path) throws Exception {
		int num = 0;

		FileInputStream fileInputStream = null;
		Scanner sc = null;
		fileInputStream = new FileInputStream(path);
		sc = new Scanner(fileInputStream, "UTF-8");

		while (sc.hasNextLine()) {
//			System.out.println("num:" + num);
			sc.nextLine();
			num += 1;
		}
		System.out.println("num:" + num);
		if (fileInputStream != null) {
			fileInputStream.close();
		}
		if (sc != null) {
			sc.close();
		}

		return num;
	}

	/**
	 * 备注：测试
	 * 
	 * @time:2017年9月7日上午10:47:53
	 * @author:longyujia
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		// System.out.println("源csv路径：" + args[0]);
		// System.out.println("生成csv路径：" + args[1]);
		// System.out.println("省市txt路径：" + args[2]);

		Synonyms csvTest = new Synonyms();

//		String csvpath = args[0];//源文件
//		String txtpath = args[1];//目标文件
//		String Shengshipath = args[2];//省市文件

		
		String csvpath = "Z:/data/company_name/company_name_10";
		String txtpath = "Z:/data/company_name/测试/companyname_test.csv";
		String Shengshipath = "Z:/data/company_name/省市.txt";
		
		/*
		 * 2018年1月3日16:27:44
		 * 修改参数 minIndex 为默认个数 300000取3000
		 * 
		 * 2018年1月3日16:06:37
		 * 修改 参数 sampleCount 改为自动 计算
		 */
		//String minIndex = args[3];
		// String sampleCount = args[4];
		//int a = Integer.parseInt(minIndex);
		// int b = Integer.parseInt(sampleCount);
		
		int b = csvTest.GetLinesNum(csvpath);
		int a = b;
		// 100000的时候取200左右，300000取500左右
		// int min = (500/300000)*num;//600取1
		int min = b / 100;
		// int max = (200/100000)*num;//500取1
		int max = b / 100;
		if (min < 1) {
			min = 1;
		}
		if (max < 1) {
			max = 1;
		}
		Random random = new Random();
		System.out.println("min:" + min + "/max:" + max);
		b = random.nextInt(min) + (max - min + 1);
		/*
		 * 2018年1月3日16:26:23
		 * 计算修改完成
		 */

		/*
		 * 2018年1月15日13:59:48
		 * 根据输出格式修改为不需要数据库名称，字段名称，id字段名称
		 */
		/*String tablename = args[3];
		String columnname = args[4];
		String idname = args[5];*/

		/*
		 * String csvpath = "Z:/data/bond/ceshi/test/ks_01.csv"; String txtpath
		 * = "Z:/data/bond/all/all.csv"; String Shengshipath =
		 * "Z:/data/bond/all/省市.txt";
		 */

		// ArrayList<String> tilelist = new ArrayList<String>();
		// tilelist.add("ID,全称,简称,市区+简称");// 输出第一行标题
		// tilelist.add("ID,简称+系");
		File file = new File(txtpath);
		if (!file.exists()) {
			csvTest.CreateCsvFile(txtpath, null);// 创建csv文件
		}

		// TODO 当用户没有输入那两个参数的时候，选择由系统来提供
		/*
		 * 2017年12月28日18:45:45 获取文件的行数,来判断所需要的内容
		 */

		csvTest.CreateDic(csvpath);
//		csvTest.CreateCsvByDict(csvpath, txtpath, Shengshipath, a, b, tablename, columnname, idname);
		/*
		 * 2018年1月15日14:18:31
		 * 修改为无需SQL的输出
		 */
		csvTest.CreateCsvByDict(csvpath, txtpath, Shengshipath, a, b, null, null, null);

	}
}
