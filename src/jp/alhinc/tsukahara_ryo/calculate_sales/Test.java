package jp.alhinc.tsukahara_ryo.calculate_sales;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class Test  {
	public static void main(String[] args){
		HashMap<String, String> branchNameMap = new HashMap<String, String>();
		HashMap<String, Integer> branchSaleMap = new HashMap<String, Integer>();
		try{

			File file = new File(args[0],"branch.lst");
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String s;
			while((s = br.readLine()) != null) {
				String str = s;
				String[] items = str.split(",",-1);

				if(!items[0].matches("[0-9]{3}$") && (items.length != 2)){
				    //System.out.print();

				    System.out.println("支店定義ファイルのフォーマットが不正です");
				    return;
				}
				branchNameMap.put(items[0], items[1]);
				branchSaleMap.put(items[0], 0);
			}
			br.close();
			//System.out.println(branchSaleMap.get(001));

		} catch(IOException e){
			System.out.println("支店定義ファイルが存在しません");
			System.out.print(e);
		}
		HashMap<String, String> commodityCodeMap = new HashMap<String, String>();
		HashMap<String, Integer> commodityNameMap = new HashMap<String, Integer>();
		try{

			File file = new File(args[0],"commodity.lst");
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String s;
			while((s = br.readLine()) != null) {
				String str = s;
				String[] items = str.split(",",-1);

				if(!items[0].matches("\\w{8}$") && (items.length != 2)){
				    System.out.println("商品定義ファイルのフォーマットが不正です");
				    return;
				}
				commodityCodeMap.put(items[0], items[1]);
				commodityNameMap.put(items[0], 0);
			}
			br.close();
			//System.out.println(branchSaleMap.get(001));

		} catch(IOException e){
			System.out.println("商品定義ファイルが存在しません");
			System.out.print(e);
		}


		File file = new File(args[0]);   // Fileクラスをインスタンス化
		String files[] = file.list();   // listメソッドでファイルの一覧を配列で取得
		ArrayList<String> list = new ArrayList<String>();
		for (String f: files){
			if(f.matches("[0-9]{8}.rcd$")){   //matchesでフィルタリング
				list.add(f);
			}
			
		}
	}
}

		/*ArrayList<String> getFile(String filename);
		 ArrayList<String> list = new ArrayList<String>();
		 try{
			 File fil = new File(args[0],"000000001.rcd");
		     BufferedReader reader = new BufferedReader(new FileReader(filename));
		     String line;
		     //読み込む行が無くなるまで繰り返す
		     while((line = reader.readLine()) != null){
		    	 	list.add(line); //読み込んだ行を追加していく
		     }
		            reader.close(); //クローズする
		     }catch(FileNotFoundException e){
		      //読み込むファイルが無かったときの処理
		        System.out.println(filename + "が見つかりません。");
		     }catch(IOException e){
		        System.out.println(e);
		     }
		     return;
		}

}*/
