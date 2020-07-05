import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Date;

/**
 * creste by itheima.itcast
 */
public class ExcelTest {
    // @Test
    public void read() throws Exception {

        //读取一个excel
        XSSFWorkbook excel=new XSSFWorkbook(new FileInputStream(new File("d:\\test.xlsx")));
        //拿到sheet
        XSSFSheet sheet = excel.getSheetAt(0);
        //遍历每一行数据
        for (Row row : sheet) {
            for (Cell cell : row) {
                System.out.println(cell.getStringCellValue());
            }
            System.out.println("================================");
        }

        //关闭资源
        excel.close();
    }

    // @Test
    public void read2() throws Exception {

        //读取一个excel
        XSSFWorkbook excel=new XSSFWorkbook(new FileInputStream(new File("d:\\test.xlsx")));
        //拿到sheet
        XSSFSheet sheet = excel.getSheetAt(0);

        //总共多少行 下标 0开始
        int lastRowNum = sheet.getLastRowNum();
        System.out.println("lastRowNum:"+lastRowNum);

        for (int i = 1; i <= lastRowNum; i++) {
            //拿出这一行
            XSSFRow row = sheet.getRow(i);
            //总列数 count 数量 1开始
            short lastCellNum = row.getLastCellNum();
            System.out.println("lastCellNum:"+lastCellNum);
            for (short j = 0; j < lastCellNum; j++) {
                XSSFCell cell = row.getCell(j);
                //判断 数字
                // cell.getCellType()
                // cell.getNumericCellValue()
                System.out.println(cell.getStringCellValue());
            }
            System.out.println("================================");
        }

        //关闭资源
        excel.close();
    }

    // @Test
    public void write() throws Exception {
        //创建一个空的excel
        XSSFWorkbook excel=new XSSFWorkbook();
        //创建 sheet
        XSSFSheet sheet = excel.createSheet("代码完成度day05");
        //插入一行数据
        XSSFRow title = sheet.createRow(0);
        title.createCell(0).setCellValue("姓名");
        title.createCell(1).setCellValue("地址");
        title.createCell(2).setCellValue("年龄");

        //Map<string,map> map<string,string> "姓名"-》"张三" "地址"-》"北京"
        //for  {}
        for (int i = 0; i < 2; i++) {
            XSSFRow dataRow = sheet.createRow(i+1);
            dataRow.createCell(0).setCellValue("张三"+i);
            dataRow.createCell(1).setCellValue("北京"+i);
            dataRow.createCell(2).setCellValue(18+i);
        }

        FileOutputStream fileOutputStream = new FileOutputStream(new File("d:\\hello.xlsx"));
        excel.write(fileOutputStream);
        fileOutputStream.flush();//流 缓存区数据 写到文件里
        excel.close();

    }

    public static void main(String[] args) {
        Date date = new Date();
        System.out.println(date.getDate());
        System.out.println(date.getDay());

    }

}
