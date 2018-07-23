package finup.feather.utils.file;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FileOperation {
	static String fileName = FileOperation.class.getClassLoader().getResource("mongoBason.txt").getPath();
	static String fileA = "C:\\Users\\cnbjpuhui-5051a\\Desktop\\Desktop\\CreditReport.json";

	// 一次读取一行
	public static List<JSONArray> readFileByLines(String fileName) {
		File file = new File(fileName);
		BufferedReader reader = null;
		List<JSONArray> list = new ArrayList<JSONArray>();
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				list.add(JSONArray.parseArray(tempString));
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return list;
	}

	// 一次读取一行 返回List<String>
	public static List<String> readFileByLineString(String fileName) {
		File file = new File(fileName);
		BufferedReader reader = null;
		List<String> list = new ArrayList<String>();
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				list.add(tempString);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return list;
	}

	// 一次读取一行 ，返回JSONObject
	public static JSONObject readFileByLine(String fileName) {
		File file = new File(fileName);
		BufferedReader reader = null;
		JSONObject jsonObject = new JSONObject();
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				jsonObject = JSONObject.parseObject(tempString);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return jsonObject;
	}

	/**
	 * 以字符为单位读取文件，常用于读文本，数字等类型的文件
	 */
	public static JSONArray readFileByChars() {
		List<Object> list = new ArrayList<Object>();
		File file = new File(fileName);
		Reader reader = null;
		String bason = "";
		try {
			System.out.println("以字符为单位读取文件内容，一次读一个字节：");
			// 一次读一个字符
			reader = new InputStreamReader(new FileInputStream(file));
			int tempchar;
			while ((tempchar = reader.read()) != -1) {
				if (((char) tempchar) != '\r') {
					bason += String.valueOf((char) tempchar);
				}
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONArray jsonArray = JSONArray.parseArray(bason);
		return jsonArray;
	}

	// 根据 key值，获取json的value
	public static String getStore(JSONArray jsonArray, String key) {
		JSONObject json = jsonArray.getJSONObject(0);
		if (json.containsKey(key))
			return json.get(key).toString();
		else
			return null;
	}


	// 写文件，追加写入文件
	public static void writeFileTrue(String file, String conent) {
		BufferedWriter out = null;
		try { // true代表追加写入文件
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
			out.write(conent + "\r\n");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	// 写文件，覆盖行
	public static void writeFile(String file, String conent) {
		BufferedWriter out = null;
		try { // true代表追加写入文件
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
			out.write(conent + "\r\n");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		FileOperation fr = new FileOperation();
		List<JSONArray> list = fr.readFileByLines(fileA);
		Iterator iter = list.iterator();
		while (iter.hasNext()) {
			System.out.println(iter.next());
		}
	}
}
