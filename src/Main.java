import homelet.display.Display;
import homelet.display.Utility;
import homelet.sort.*;

import java.awt.*;
import java.util.Arrays;

public class Main{
	
	public static void main(String[] args){
		double   min     = 0;
		double   max     = 100;
		Double[] doubles = Utility.getData(1000, min, max);
//		ToolBox.printOutArray("->", doubles);
//		Sort.sort(doubles, SortMode.TRIPLE_BUFFERED_QUICK_SORT);
//		System.out.println("=======================");
//		ToolBox.printOutArray("->", doubles);
//		System.out.println("Result is : " + Utility.checkSorted(doubles));
//		for(int i = 0; i < 100; i++){
//			int rand_1 = (int) Utility.getRandomNumber(0, doubles.length);
//			int rand_2 = (int) Utility.getRandomNumber(0, doubles.length);
//			homelet.display.Sort.swap(doubles, rand_1, rand_2);
//		}
		SelectionSortSimulator   selectionSortSimulator   = new SelectionSortSimulator(Arrays.asList(doubles), min, max);
		InsertionSortSimulator   insertionSortSimulator   = new InsertionSortSimulator(Arrays.asList(doubles), min, max);
		MergeSortSimulator       mergeSortSimulator       = new MergeSortSimulator(doubles, min, max);
		QuickSortSimulator       quickSortSimulator       = new QuickSortSimulator(doubles, min, max);
		DoubleQuickSortSimulator doubleQuickSortSimulator = new DoubleQuickSortSimulator(doubles, min, max);
		TripleQuickSortSimulator tripleQuickSortSimulator = new TripleQuickSortSimulator(doubles, min, max);
		EventQueue.invokeLater(()->{
			Display display = new Display("Sort Simulator", 1500, 500);
			display.setLocationRelativeTo(null);
			display.getRenderManager().addRenderTarget(mergeSortSimulator);
			display.getCanvas().getCanvasThread().setFPS(-1);
//			display.getCanvas().getCanvasThread().setPrintNoticeInConsole(true);
			display.getCanvas().startRendering();
			display.setVisible(true);
		});
	}
}
