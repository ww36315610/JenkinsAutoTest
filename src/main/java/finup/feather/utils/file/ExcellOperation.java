package finup.feather.utils.file;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * 定义抽象类，save抽象方法，getExcellData()返回excell数据
 * @author cnbjpuhui-5051a
 *
 */
public abstract class ExcellOperation {
	private static Workbook wb;
	private static FileInputStream input;
	private static HSSFSheet hssfSheet;
	private static XSSFSheet xssfSheet;
	private static HSSFRow hssfRow;
	private static XSSFRow xssfRow;
	private static HSSFCell hssfCell;
	private static XSSFCell xssfCell;
	private static int rowNum;
	private static int cellNum;
	private static boolean con = true;

	// 保存数据 【抽象方法、可以保存到mysql、file】
	public abstract void save(String excellPath, String excellSheet);

	/**
	 * 读取Excell 返回List<Map<String, Object>>
	 * 对外只暴露这一个方法，用于生产excell数据，设置为静态，所以在多线程的时候取的文件只是一份，多个线程支队这个list进行操作
	 * run方法中，ExcellOperation a = new ExcellOperation(); Thread t = new
	 * Thread(a);这时候会生产多个对象[类的实例的实例]，但是类实例只有一个【static在类实例中】
	 * 
	 * @param excellPath
	 * @param excellSheet
	 * @return
	 */
	public static List<Map<String, Object>> getExcellData(String excellPath, String excellSheet) {
		readExcell(excellPath, excellSheet);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		getCellNum();
		getRowNum();
		// 遍历行
		for (int r = 1; r < rowNum; r++) {
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			if (con) {
				HSSFRow rowNext = hssfSheet.getRow(r);
				// 遍历列
				for (int i = 0; i < cellNum; i++) {
					hssfCell = rowNext.getCell(i);
					String key = hssfRow.getCell(i).toString();
					String value = rowNext.getCell(i).toString();
					map.put(key, value);
				}
			} else {
				XSSFRow rowNext = xssfSheet.getRow(r);
				// 遍历列
				for (int i = 0; i < cellNum; i++) {
					xssfCell = rowNext.getCell(i);
					String key = xssfRow.getCell(i).toString();
					String value = rowNext.getCell(i).toString();
					map.put(key, value);
				}
			}

			list.add(map);
		}
		return list;
	}

	// 读取Excell || 初始化Workbook、sheet || 返回boolean类型参数
	private static boolean readExcell(String excellPath, String excellSheet) {
		try {
			input = new FileInputStream(excellPath);
			try {
				String fileType = excellPath.substring(excellPath.lastIndexOf(".") + 1, excellPath.length());
				System.out.println(fileType);
				if (fileType.equals("xls")) {
					wb = new HSSFWorkbook(input);
					hssfSheet = (HSSFSheet) wb.getSheet(excellSheet);
				} else if (fileType.equals("xlsx")) {
					wb = new XSSFWorkbook(input);
					xssfSheet = (XSSFSheet) wb.getSheet(excellSheet);
					con = false;
				} else {
					System.out.println("您输入的excel格式不正确");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		return con;
	}

	// 获取sheel的行数
	private static int getRowNum() {
		if (con) {
			rowNum = hssfSheet.getPhysicalNumberOfRows();
		} else {
			rowNum = xssfSheet.getPhysicalNumberOfRows();
		}
		return rowNum;
	}

	// 获取row的列数
	private static int getCellNum() {
		if (con) {
			hssfRow = hssfSheet.getRow(0);
			cellNum = hssfRow.getPhysicalNumberOfCells();
		} else {
			xssfRow = xssfSheet.getRow(0);
			cellNum = xssfRow.getPhysicalNumberOfCells();
		}
		return cellNum;
	}
}