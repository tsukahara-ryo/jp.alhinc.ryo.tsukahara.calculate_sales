package jp.alhinc.tsukahara_ryo.calculate_sales;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Sale {
	public static boolean fileRead(String path, String name, String regex, String errermess,
			HashMap<String, String> nameMap, HashMap<String, Long> saleMap){
		BufferedReader br = null;
		FileReader fr = null;
		try{
			File file = new File(path, name);
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			String s;
			while((s = br.readLine()) != null) {
				String str = s;
				String[] items = str.split(",",-1);

				if(!items[0].matches(regex) || (items.length != 2)){
				    System.out.println(errermess+ "定義ファイルのフォーマットが不正です");

				    return false;
				}
				nameMap.put(items[0], items[1]);
				saleMap.put(items[0], 0L);

			}
		}catch(FileNotFoundException e){
			System.out.println(errermess + "定義ファイルが存在しません");
			return false;
		}catch(IOException e){
			System.out.println("予期せぬエラーが発生しました");
			return false;
		}finally{
			try {
				if(br != null){
					br.close();
				}
			} catch (IOException e) {
				System.out.println("予期せぬエラーが発生しました");
				return false;
			}
		}
		return true;

	}

	public static boolean fileOut(HashMap<String, String> nameMap, HashMap<String, Long> saleMap,
			String path, String Name){
		BufferedWriter bw = null;
		try{
		    File outFile = new File(path, Name);
			bw = new BufferedWriter(new FileWriter(outFile));
			List<Map.Entry<String,Long>> comentries =new ArrayList<Map.Entry<String,Long>>(saleMap.entrySet());
		    Collections.sort(comentries, new Comparator<Map.Entry<String,Long>>() {
		    	@Override
		    	public int compare(
		        Entry<String,Long> entry1, Entry<String,Long> entry2) {
		    		return ((Long)entry2.getValue()).compareTo((Long)entry1.getValue());
		    	}
		    });
		    for (Entry<String,Long> s : comentries) {
	            bw.write(s.getKey() + "," + nameMap.get(s.getKey())+ ","+ saleMap.get(s.getKey()));
	            bw.newLine();
		    }
		}catch(IOException e){
			System.out.println("予期せぬエラーが発生しました" );
			return false;
		}finally{
			try {
				if(bw != null){
					bw.close();
				}
			}catch (IOException e) {
				System.out.println("予期せぬエラーが発生しました");
				return false;
			}
		}
		return true;
	}

	public static void main(String[] args){
		if(args.length != 1){
			System.out.println("予期せぬエラーが発生しました");
			return;
		}

		HashMap<String, String> branchNameMap = new HashMap<String, String>();
		HashMap<String, Long> branchSaleMap = new HashMap<String, Long>();
		HashMap<String, String> commodityCodeMap = new HashMap<String, String>();
		HashMap<String, Long> commoditySaleMap = new HashMap<String, Long>();

		if(fileRead(args[0], "branch.lst", "[0-9]{3}$", "支店", branchNameMap, branchSaleMap)){
			return;
		}
		if(fileRead(args[0], "commodity.lst", "\\w{8}$", "商品", commodityCodeMap, commoditySaleMap)){
			return;
		}



		File file = new File(args[0]);
		String files[] = file.list();
		ArrayList<String> list = new ArrayList<String>();

		for (String f: files){
			File fileordr = new File(args[0], f);
			if(f.matches("[0-9]{8}.rcd$")){
				if(fileordr.isFile()){
					list.add(f);
				}
			}
		}
		Collections.sort(list);
		for(int i=1;i<list.size();i++){
			String str1 = new String(list.get(i-1));
			String sitrance1 = str1.substring(0, 8);
			int rcdNo1 = Integer.parseInt(sitrance1);
			String str2 = new String(list.get(i));
			String sitrance2 = str2.substring(0, 8);
			int rcdNo2 = Integer.parseInt(sitrance2);
			int rcdNos = rcdNo1 - rcdNo2;
			if(rcdNos != -1){
				System.out.println("売上ファイル名が連番になっていません");
				return;
			}
		}
		BufferedReader br = null;
		FileReader fr = null;
		try{
			for(String fileName : list){
				ArrayList<String> rcdList = new ArrayList<String>();
				File rcdName = new File(args[0],fileName);
				fr = new FileReader(rcdName);
				br = new BufferedReader(fr);
				String s;
				while((s = br.readLine()) != null) {
					rcdList.add(s);
				}
				if(rcdList.size() != 3){
					System.out.println(fileName + "のフォーマットが不正です");
					return;
				}
				if(!rcdList.get(2).matches("^\\d{1,10}$")){
					System.out.println("予期せぬエラーが発生しました");
					return;
				}


				if (!branchSaleMap.containsKey(rcdList.get(0))){
					System.out.println(fileName + "の支店コードが不正です");
					return;
				}
				long rcdBranchMoney = Long.parseLong(rcdList.get(2));
				rcdBranchMoney = branchSaleMap.get(rcdList.get(0)) + rcdBranchMoney;
				if(rcdBranchMoney > 9999999999L){
					System.out.println("合計金額が10桁を超えました");
					return;
				}
				branchSaleMap.put(rcdList.get(0),rcdBranchMoney);

				if (!commoditySaleMap.containsKey(rcdList.get(1))){
					System.out.println(fileName + "の商品コードが不正です");
					return;
				}
				long rcdcommodityMoney = Long.parseLong(rcdList.get(2));
				rcdcommodityMoney = commoditySaleMap.get(rcdList.get(1)) + rcdcommodityMoney;
				if(rcdcommodityMoney > 9999999999L){
					System.out.println("合計金額が10桁を超えました");
					return;
				}
				commoditySaleMap.put(rcdList.get(1),rcdcommodityMoney);

			}



			if(fileOut(branchNameMap, branchSaleMap, args[0],"branch.out")){
				return;
			}
			if(fileOut(commodityCodeMap, commoditySaleMap, args[0],"commodity.out")){
				return;
			}

		} catch (IOException e) {
			System.out.println("予期せぬエラーが発生しました");

		}finally{
			try {
				if(br != null){
					br.close();
					}
				if(fr != null){
					fr.close();
				}
			} catch (IOException e) {
				System.out.println("予期せぬエラーが発生しました");
				return;
			}
		}

	}
}

