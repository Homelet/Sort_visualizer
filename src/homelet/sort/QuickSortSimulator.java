package homelet.sort;

import homelet.GH.handlers.GH;
import homelet.GH.utils.IntervalCaller;
import homelet.GH.utils.ToolBox;
import homelet.GH.visual.interfaces.Renderable;
import homelet.display.Sort;
import homelet.display.Utility;

import java.awt.*;
import java.util.LinkedList;

public class QuickSortSimulator implements Renderable{
	
	Double[] data;
	boolean  finnished         = false;
	int      currentLeftBounds = 0, currentRightBounds, currentIndex = 1, currentBoundaries = 0;
	LinkedList<int[][]> boundsList = new LinkedList<>();
	int[]               lastLeft   = null;
	private double maxBounds, minbounds;
	private IntervalCaller caller;
	private Color          middleColor     = new Color(0xFF6600);
	private Color          left_rightColor = new Color(0x3300ff);
	private Color          indexColor      = new Color(0x33FF00);
	private Color          boundaryColor   = new Color(0xFFFF);
	private Color          otherColor      = new Color(0x999999);
	
	public QuickSortSimulator(Double[] doubles, double minBounds, double maxBounds){
		this.data = doubles;
		this.maxBounds = maxBounds;
		this.minbounds = minBounds;
		currentRightBounds = data.length - 1;
		this.caller = new IntervalCaller(this::doingRevolution, 0L);
	}
	
	private void preparingRevolution(){
		int newLeftBounds;
		int newRightBounds;
		if(lastLeft == null){
			int[][] bounds = boundsList.poll();
			if(bounds == null){
				finnished = true;
				System.out.println("Result : " + Utility.checkSorted(data));
				return;
			}
			newLeftBounds = bounds[0][0];
			newRightBounds = bounds[0][1];
			lastLeft = bounds[1];
		}else{
			newLeftBounds = lastLeft[0];
			newRightBounds = lastLeft[1];
			lastLeft = null;
		}
		this.currentLeftBounds = newLeftBounds;
		this.currentRightBounds = newRightBounds;
		this.currentBoundaries = currentLeftBounds;
		Sort.swap(data, currentBoundaries, (int) Utility.getRandomNumber(currentLeftBounds, currentRightBounds));
		this.currentIndex = currentLeftBounds + 1;
		doingRevolution();
	}
	
	private void doingRevolution(){
		if(currentIndex > currentRightBounds){
			finishingRevolution();
			return;
		}
		// if bigger or equals to don't do anything, because it is already in the desired positions
		// if smaller then swap the start of greater with the current
		if(data[currentIndex].compareTo(data[currentLeftBounds]) < 0){
			currentBoundaries++;
			Sort.swap(data, currentBoundaries, currentIndex);
		}
		currentIndex++;
	}
	
	private void finishingRevolution(){
		Sort.swap(data, currentBoundaries, currentLeftBounds);
		if(currentLeftBounds < currentRightBounds){
			boundsList.add(new int[][]{ { currentLeftBounds, currentBoundaries - 1 }, { currentBoundaries + 1, currentRightBounds } });
		}
		preparingRevolution();
	}
	
	@Override
	public void tick(){
		caller.tick();
	}
	
	@Override
	public void render(Graphics2D g){
		Rectangle bounds     = g.getClipBounds();
		double    unitHeight = bounds.getHeight() / (maxBounds - minbounds);
		double    unitWidth  = bounds.getWidth() / data.length;
		for(int index = 0; index < data.length; index++){
			double value = data[index];
			Shape  shape = GH.rectangle(false, index * unitWidth, (maxBounds - value) * unitHeight, unitWidth, value * unitHeight);
			if(!finnished){
				int result = ToolBox.betweenPeaks(index, currentRightBounds, currentLeftBounds);
				switch(result){
					case -1:
					case 1:
						g.setColor(left_rightColor);
						break;
					case 0:
						g.setColor(middleColor);
						break;
					case -2:
					case 2:
						g.setColor(otherColor);
						break;
				}
				if(index == currentIndex){
					g.setColor(indexColor);
				}
				if(index == currentBoundaries){
					g.setColor(boundaryColor);
				}
			}else{
				g.setColor(middleColor);
			}
			g.fill(shape);
			g.setColor(Color.BLACK);
			g.draw(shape);
		}
	}
	
	@Override
	public boolean isTicking(){
		return !finnished;
	}
}
