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

public class sale {
	public static void main(String[] args){
		HashMap<String, String> branchNameMap = new HashMap<String, String>();
		HashMap<String, Long> branchSaleMap = new HashMap<String, Long>();
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
				branchSaleMap.put(items[0], 0L);

			}

			br.close();
		} catch(IOException e){
			System.out.println("支店定義ファイルが存在しませんh");
			return;
		}
		HashMap<String, String> commodityCodeMap = new HashMap<String, String>();
		HashMap<String, Long> commoditySaleMap = new HashMap<String, Long>();
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
				commoditySaleMap.put(items[0], 0l);

			}
			br.close();
			//System.out.println(commoditySaleMap.get("SFT00001"));

		} catch(FileNotFoundException e){
			System.out.println("商品定義ファイルが存在しません");
		} catch(IOException e){
			System.out.println("予期せぬエラーが発生しました");
		}
		File file = new File(args[0]);
		String files[] = file.list();
		ArrayList<String> list = new ArrayList<String>();
		for (String f: files){
			if(f.matches("[0-9]{8}.rcd$")){
				list.add(f);
			}
		//System.out.println(list.size());
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
			//System.out.println(rcdNo);
			//System.out.println(rcdNo2);

		}

		try{

			for(String fileName : list){
				ArrayList<String> rcdList = new ArrayList<String>();
				File rcdName = new File(args[0],fileName);
				FileReader fr = new FileReader(rcdName);
				BufferedReader br = new BufferedReader(fr);
				String s;
				while((s = br.readLine()) != null) {
					rcdList.add(s);
				}
				if(rcdList.size() != 3){
					System.out.println(fileName + "のフォーマットが不正です");
				}


				if (!branchSaleMap.containsKey(rcdList.get(0))){
					System.out.println(fileName + "の支店コードが不正です");
					return;
				}
				long rcdbranchMoney = Long.parseLong(rcdList.get(2));
				rcdbranchMoney = branchSaleMap.get(rcdList.get(0)) + rcdbranchMoney;
				if(rcdbranchMoney > 999999999){
					System.out.println("合計金額が10桁を超えました");
					return;
				}
				branchSaleMap.put(rcdList.get(0),rcdbranchMoney);

				if (!commoditySaleMap.containsKey(rcdList.get(1))){
					System.out.println(fileName + "の商品コードが不正です");
					return;
				}
				long rcdcommodityMoney = Long.parseLong(rcdList.get(2));
				//System.out.println(commoditySaleMap.get(rcdlist.get(1)));
				rcdcommodityMoney = commoditySaleMap.get(rcdList.get(1)) + rcdcommodityMoney;
				if(rcdcommodityMoney > 999999999){
					System.out.println("合計金額が10桁を超えました");
					return;
					}
				commoditySaleMap.put(rcdList.get(1),rcdcommodityMoney);
				br.close();
			}
			File branchOutFile = new File(args[0],"branch.out");
			BufferedWriter bw = new BufferedWriter(new FileWriter(branchOutFile));

			List<Map.Entry<String,Long>> entries =new ArrayList<Map.Entry<String,Long>>(branchSaleMap.entrySet());
		    Collections.sort(entries, new Comparator<Map.Entry<String,Long>>() {
		    	@Override
		    	public int compare(
		        Entry<String,Long> entry1, Entry<String,Long> entry2) {
		    		return ((Long)entry2.getValue()).compareTo((Long)entry1.getValue());
		    	}
		    });
		    for (Entry<String,Long> s : entries) {
	            //System.out.println(s.getKey() + "," + branchNameMap.get(s.getKey())+ ","+ branchSaleMap.get(s.getKey()));
	            bw.write(s.getKey() + "," + branchNameMap.get(s.getKey())+ ","+ branchSaleMap.get(s.getKey()));
	            bw.newLine();
		    }
		    bw.close();



		    File commdityOutFile = new File(args[0],"commodity.out");
			BufferedWriter combw = new BufferedWriter(new FileWriter(commdityOutFile));

			List<Map.Entry<String,Long>> comentries =new ArrayList<Map.Entry<String,Long>>(commoditySaleMap.entrySet());
		    Collections.sort(comentries, new Comparator<Map.Entry<String,Long>>() {
		    	@Override
		    	public int compare(
		        Entry<String,Long> entry1, Entry<String,Long> entry2) {
		    		return ((Long)entry2.getValue()).compareTo((Long)entry1.getValue());
		    	}
		    });
		    for (Entry<String,Long> s : comentries) {
	            //System.out.println(s.getKey() + "," + commodityCodeMap.get(s.getKey())+ ","+ commoditySaleMap.get(s.getKey()));
	            combw.write(s.getKey() + "," + commodityCodeMap.get(s.getKey())+ ","+ commoditySaleMap.get(s.getKey()));
	            combw.newLine();
		    }
		    combw.close();

		}catch(IOException s){
		System.out.println("予期せぬエラーが発生しました。");
		}
		//System.out.println(branchSaleMap.get("001"));
		//System.out.println(commoditySaleMap.get("SFT00001"));

	}
}
